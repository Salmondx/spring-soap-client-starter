package com.salmondx.cxf.client.core.handler;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.salmondx.cxf.client.core.metadata.MethodMetadata;
import rx.Observable;
import rx.Single;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salmondx on 07/09/16.
 */
public class HystrixInvocationHandler implements InvocationHandler {
    private Object proxyService;
    private Map<String, MethodMetadata> methodsToInvoke;
    private Map<String, HystrixCommand.Setter> setters = new HashMap<>();

    public HystrixInvocationHandler(Object proxyService, Map<String, MethodMetadata> methodsToInvoke) {
        this.proxyService = proxyService;
        this.methodsToInvoke = methodsToInvoke;
        for (String methodName : methodsToInvoke.keySet()) {
            setters.put(methodName, HystrixCommand.Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey(methodName)
            ));
        }
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        String methodKey = method.getName() + ":" + method.getReturnType().getCanonicalName();
        if ("equals".equals(method.getName())) {
            return equals(args[0]);
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }

        if (!methodsToInvoke.containsKey(methodKey)) {
            throw new RuntimeException("Undefined method called: " + method.getName());
        }
        HystrixCommand<Object> hystrixCommand = new HystrixCommand<Object>(setters.get(methodKey)) {
            @Override
            protected Object run() throws Exception {
                try {
                    MethodMetadata invokableMethod = methodsToInvoke.get(methodKey);
                    return invokableMethod.invoke(proxyService, args);
                } catch (Exception e) {
                    throw e;
                } catch (Throwable t) {
                    throw (Error) t;
                }
            }
        };
        if (isReturnsHystrixCommand(method)) {
            return hystrixCommand;
        } else if (isReturnsObservable(method)) {
            return hystrixCommand.toObservable();
        } else if (isReturnsSingle(method)) {
            return hystrixCommand.toObservable().toSingle();
        } else {
            return hystrixCommand.execute();
        }
    }

    private boolean isReturnsHystrixCommand(Method method) {
        return HystrixCommand.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsSingle(Method method) {
        return Single.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsObservable(Method method) {
        return Observable.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public int hashCode() {
        return proxyService.hashCode();
    }

    @Override
    public String toString() {
        return proxyService.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return proxyService.equals(obj);
    }
}

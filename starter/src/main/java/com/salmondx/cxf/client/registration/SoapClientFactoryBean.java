package com.salmondx.cxf.client.registration;

import com.salmondx.cxf.client.registration.model.SoapMethodData;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import com.salmondx.cxf.client.core.handler.HystrixInvocationHandler;
import com.salmondx.cxf.client.core.metadata.MethodMetadata;
import com.salmondx.cxf.client.core.metadata.MethodMetadataImpl;
import com.salmondx.cxf.client.core.metadata.request.RequestMetadataFactory;
import com.salmondx.cxf.client.core.metadata.response.ResponseMetadataFactory;

import javax.jws.WebMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Salmondx on 01/09/16.
 */
@Data
public class SoapClientFactoryBean implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware{

    private Class<?> type;
    private Class<?> service;
    private Map<String, List<SoapMethodData>> soapMethods;
    private Map<String, MethodMetadata> methodsToInvoke = new HashMap<>();
    private ApplicationContext applicationContext;
    private RequestMetadataFactory requestMetadataFactory;
    private ResponseMetadataFactory responseMetadataFactory;
    private Object proxyService;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(type.getClassLoader(),
                new Class<?>[]{type}, new HystrixInvocationHandler(proxyService, methodsToInvoke));
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Method[] serviceMethods = service.getMethods();
        for (Method serviceMethod : serviceMethods) {
            Annotation webMethod = serviceMethod.getAnnotation(WebMethod.class);
            Assert.notNull(webMethod, "Provided cxf portType doesn't contains WebMethod annotation");
            Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(webMethod);
            String webMethodName = (String) attributes.get("operationName");
            String webMethodAlias = (String) attributes.get("action");
            if (soapMethods.containsKey(webMethodName) || soapMethods.containsKey(soapMethodAlias(webMethodAlias))) {
                if (StringUtils.isEmpty(webMethodName)) {
                    webMethodName = serviceMethod.getName();
                }
                List<SoapMethodData> soapMethodDataList = getProxyMethodData(webMethodName, webMethodAlias);
                for (SoapMethodData soapMethodData : soapMethodDataList) {
                    List<Object> autowiredFields = new ArrayList<>();
                    for (int i = 0; i < soapMethodData.getAutowiredFields().length; i++) {
                        Object autowiredField = applicationContext.getBean(soapMethodData.getAutowiredFields()[i]);
                        autowiredFields.add(autowiredField);
                    }
                    Class<?>[] parameterTypes = serviceMethod.getParameterTypes();
                    // error with same method for different parameters
                    Method proxyMethod = findProxiedMethod(soapMethodData);
                    MethodMetadataImpl methodMetadata = new MethodMetadataImpl();
                    methodMetadata.setInvokableMethod(findMethodInSoapService(proxyService, webMethodName));
                    methodMetadata.setRequestMetadata(requestMetadataFactory.create(parameterTypes, proxyMethod, autowiredFields));
                    methodMetadata.setResponseMetadata(responseMetadataFactory.create(proxyMethod, serviceMethod));
                    methodsToInvoke.put(soapMethodData.getMethodName() + ":" + soapMethodData.getReturnTypeName(), methodMetadata);
                }
            }
        }
    }

    private String soapMethodAlias(String fullName) {
        if (StringUtils.contains(fullName, "#")) {
            return StringUtils.substringAfterLast(fullName, "#");
        } else {
            return fullName;
        }
    }

    private List<SoapMethodData> getProxyMethodData(String soapMethodName, String soapMethodAlias) {
        if (soapMethods.containsKey(soapMethodName)) {
            return soapMethods.get(soapMethodName);
        } else {
            return soapMethods.get(soapMethodAlias(soapMethodAlias));
        }
    }

    private Method findMethodInSoapService(Object proxy, String webMethodName) {
        Method[] methods = proxy.getClass().getMethods();
        for (Method method : methods) {
            if (StringUtils.equals(webMethodName.toLowerCase(), method.getName().toLowerCase())) {
                return method;
            }
        }
        throw new BeanCreationException("Cxf proxy doesn't have method with name " + webMethodName);
    }

    private Method findProxiedMethod(SoapMethodData soapMethodData) {
        for (Method method : type.getDeclaredMethods()) {
            if (StringUtils.equals(method.getName(), soapMethodData.getMethodName())
                    && StringUtils.equals(method.getReturnType().getCanonicalName(), soapMethodData.getReturnTypeName())) {
                return method;
            }
        }
        throw new BeanCreationException("Method " + soapMethodData.getMethodName() + " not found in proxied @SoapClient interface");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.proxyService = applicationContext.getBean(service);
    }

}

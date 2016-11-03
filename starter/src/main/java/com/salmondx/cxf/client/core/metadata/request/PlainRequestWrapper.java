package com.salmondx.cxf.client.core.metadata.request;

import lombok.AllArgsConstructor;
import com.salmondx.cxf.client.core.TypeConverter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Salmondx on 07/11/16.
 */
@AllArgsConstructor
public class PlainRequestWrapper implements RequestWrapper {
    private List<Method> setters;
    private Class<?> requestType;
    private TypeConverter converter;

    @Override
    public Object createRequestObject(Object[] args) {
        try {
            Object inputWrapper = requestType.newInstance();
            // set argument values to input object
            for (int i = 0; i < args.length; i++) {
                Method setter = setters.get(i);
                setter.invoke(inputWrapper, converter.convertIfDifferentTypes(
                        args[i],
                        setter.getParameterTypes()[0]
                ));
            }
            return inputWrapper;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create request object", e);
        }
    }
}

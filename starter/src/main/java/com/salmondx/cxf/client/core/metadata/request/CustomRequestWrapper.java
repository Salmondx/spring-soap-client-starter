package com.salmondx.cxf.client.core.metadata.request;

import com.salmondx.cxf.client.core.TypeConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Salmondx on 07/11/16.
 */
@AllArgsConstructor
public class CustomRequestWrapper implements RequestWrapper {
    private Map<Field, Field> fieldsMapping;
    private TypeConverter converter;
    private Class<?> requestType;

    @Override
    public Object createRequestObject(Object[] args) {
        BeanWrapper requestWrapper = new BeanWrapperImpl(requestType);
        BeanWrapper argumentWrapper = new BeanWrapperImpl(args[0]);

        for (Map.Entry<Field, Field> fieldEntry : fieldsMapping.entrySet()) {
            Object valueFromResponse = argumentWrapper.getPropertyValue(fieldEntry.getKey().getName());
            Object resultProperty = converter.convertIfDifferentTypes(valueFromResponse, fieldEntry.getValue().getType());
            requestWrapper.setPropertyValue(fieldEntry.getValue().getName(), resultProperty);
        }
        return requestWrapper.getWrappedInstance();
    }
}

package com.salmondx.cxf.client.core.metadata.response;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;
import com.salmondx.cxf.client.core.TypeConverter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Salmondx on 05/09/16.
 */
public class SimpleResponseMetadata extends ResponseMetadata {

    public SimpleResponseMetadata(TypeConverter converter, Class<?> proxyType, String pathPrefix, Map<Field, Field> fieldsMapping) {
        super(converter, proxyType, createPathPrefix(pathPrefix), fieldsMapping);
    }

    public Object convertResponse(Object response) {
        BeanWrapper responseWrapper = new BeanWrapperImpl(response);
        BeanWrapper resultWrapper = new BeanWrapperImpl(proxyType);

        for (Map.Entry<Field, Field> fieldEntry : fieldsMapping.entrySet()) {
            Object valueFromResponse = responseWrapper.getPropertyValue(pathPrefix + fieldEntry.getValue().getName());
            Object resultProperty = converter.convertIfDifferentTypes(valueFromResponse, fieldEntry.getKey().getType());
            resultWrapper.setPropertyValue(fieldEntry.getKey().getName(), resultProperty);
        }
        return resultWrapper.getWrappedInstance();
    }

    private static String createPathPrefix(String pathPrefix) {
        if (StringUtils.isEmpty(pathPrefix)) {
            return pathPrefix;
        } else {
            return pathPrefix + ".";
        }
    }
}

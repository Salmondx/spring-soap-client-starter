package com.salmondx.cxf.client.core.metadata.response;

import com.salmondx.cxf.client.core.TypeConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Salmondx on 05/09/16.
 */
public class CollectionResponseMetadata extends ResponseMetadata {
    public CollectionResponseMetadata(TypeConverter converter, Class<?> proxyType, String pathPrefix, Map<Field, Field> fieldsMapping) {
        super(converter, proxyType, pathPrefix, fieldsMapping);
    }

    public Object convertResponse(Object response) {
        BeanWrapper responseWrapper = new BeanWrapperImpl(response);
        List<Object> resultList = new ArrayList<>();
        Collection<?> responseCollection = (Collection<?>) responseWrapper.getPropertyValue(pathPrefix);
        for (Object responseItem : responseCollection) {
            resultList.add(rowFromResponse(responseItem));
        }
        return resultList;
    }

    private Object rowFromResponse(Object responseItem) {
        BeanWrapper responseWrapper = new BeanWrapperImpl(responseItem);
        BeanWrapper resultWrapper = new BeanWrapperImpl(proxyType);
        for (Map.Entry<Field, Field> fieldEntry : fieldsMapping.entrySet()) {
            Object valueFromResponse = responseWrapper.getPropertyValue(fieldEntry.getValue().getName());
            Object resultProperty = converter.convertIfDifferentTypes(valueFromResponse, fieldEntry.getKey().getType());
            resultWrapper.setPropertyValue(fieldEntry.getKey().getName(), resultProperty);
        }
        return resultWrapper.getWrappedInstance();
    }
}

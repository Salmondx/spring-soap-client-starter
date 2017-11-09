package com.salmondx.cxf.client.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salmondx on 08/09/16.
 */
public class PathUtils {
    public static Pair<String, Class<?>> findPathPrefix(Class<?> responseType, String pathFromAnnotation) throws IntrospectionException {
        Pair<String, Class<?>> pathWithType = findPath(responseType, pathFromAnnotation, "");
        Assert.hasText(pathWithType.getLeft(), "Cannot find property " + pathFromAnnotation + " from @Response annotation");
        return pathWithType;
    }

    private static Pair<String, Class<?>> findPath(Class<?> propertyClass, String pathFromAnnotation, String fullPath) {
        if (propertyClass.equals(Class.class)) {
            return Pair.of("", Class.class);
        }
        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = Introspector.getBeanInfo(propertyClass).getPropertyDescriptors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, PropertyDescriptor> properties = new HashMap<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            properties.put(propertyDescriptor.getName(), propertyDescriptor);
        }
        if (properties.containsKey(pathFromAnnotation)) {
            PropertyDescriptor propertyDescriptor = properties.get(pathFromAnnotation);
            Class<?> type = propertyDescriptor.getPropertyType();
            if (Collection.class.isAssignableFrom(type)) {
                ParameterizedType parameterizedType = (ParameterizedType) propertyDescriptor.getReadMethod().getGenericReturnType();
                type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            }
            return Pair.of(getFullPath(fullPath, pathFromAnnotation), type);
        } else {
            return properties.values()
                    .stream()
                    .map(propertyDescriptor -> findPath(propertyDescriptor.getPropertyType(), pathFromAnnotation, getFullPath(fullPath, propertyDescriptor.getName())))
                    .filter(pair -> StringUtils.isNotEmpty(pair.getLeft()))
                    .findFirst()
                    .orElse(Pair.of("", Class.class));
        }
    }

    private static String getFullPath(String previousPath, String newPart) {
        if (StringUtils.isEmpty(previousPath)) {
            return newPart;
        } else {
            return previousPath + "." + newPart;
        }
    }
}

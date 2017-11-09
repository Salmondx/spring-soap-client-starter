package com.salmondx.cxf.client.core.utils;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salmondx on 08/09/16.
 */
public class Utilities {
    public static Map<Field, Field> serializationMappings(Class<?> genericResponseType, Class<?> actualServiceResponseType) {
        Map<Field, Field> fieldsMapping = new HashMap<>();
        for (Field field : genericResponseType.getDeclaredFields()) {
            Annotation annotation = field.getAnnotation(com.salmondx.cxf.client.annotation.Field.class);
            Field fieldFromActualResponse;
            if (annotation != null) {
                String fieldName = (String) AnnotationUtils.getValue(annotation);
                fieldFromActualResponse = ReflectionUtils.findField(actualServiceResponseType, fieldName);
                Assert.notNull(fieldFromActualResponse, "Field with name " + fieldName + " not found in type " + actualServiceResponseType);
            } else {
                fieldFromActualResponse = ReflectionUtils.findField(actualServiceResponseType, field.getName());
                Assert.notNull(fieldFromActualResponse, "Field with name " + field.getName() + " not found in type " + actualServiceResponseType);
            }
            fieldsMapping.put(field, fieldFromActualResponse);
        }
        return fieldsMapping;
    }
}

package com.salmondx.cxf.client.core.metadata.request;

import com.salmondx.cxf.client.core.TypeConverter;
import com.salmondx.cxf.client.core.utils.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import com.salmondx.cxf.client.annotation.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static com.salmondx.cxf.client.core.utils.NamingUtilities.removeSetPrefix;

/**
 * Created by Salmondx on 08/09/16.
 */
public abstract class RequestMetadataFactory {
    TypeConverter converter;

    public RequestMetadataFactory(TypeConverter converter) {
        this.converter = converter;
    }

    public abstract RequestMetadata create(Class<?>[] parameterTypes, Method proxyMethod, List<Object> autowiredFields);

    public static final class Default extends RequestMetadataFactory {

        public Default(TypeConverter converter) {
            super(converter);
        }

        @Override
        public RequestMetadata create(Class<?>[] parameterTypes, Method proxyMethod, List<Object> autowiredFields) {
            if (autowiredFields != null && autowiredFields.size() > parameterTypes.length) {
                throw new BeanCreationException(String.format(
                        "Size of autowired parameters (%s) is greater than method's input parameters (%s)",
                        autowiredFields.size(), parameterTypes.length));
            }
            Map<Class<?>, Object> autowiredFieldsClasses;
            if (autowiredFields != null) {
                autowiredFieldsClasses = autowiredFields
                        .stream()
                        .collect(Collectors.toMap(Object::getClass, it -> it));
            } else {
                autowiredFieldsClasses = new HashMap<>();
            }
            Parameter[] proxyInputParameters = proxyMethod.getParameters();

            List<RequestWrapper> requestWrappers = new LinkedList<>();
            for (Class<?> parameterType : parameterTypes) {
                if (autowiredFieldsClasses.containsKey(parameterType)) {
                    requestWrappers.add(new AutowiredRequestWrapper(
                            autowiredFieldsClasses.get(parameterType)
                    ));
                } else {
                    if (isPrimitiveTypes(proxyInputParameters)) {
                        for (int i = 0; i < proxyInputParameters.length; i++) {
                            requestWrappers.add(
                                    new PrimitiveRequestWrapper(i)
                            );
                        }
                    } else if (isCustomObject(proxyInputParameters)) {
                        requestWrappers.add(
                                createForCustomObject(proxyMethod.getParameterTypes()[0], parameterType, converter)
                        );
                    } else {
                        requestWrappers.add(
                                createForPlainObjects(parameterType, proxyInputParameters)
                        );
                    }
                }
            }
            return new RequestMetadata(requestWrappers);
        }

        private boolean isPrimitiveTypes(Parameter[] proxyInputParameters) {
            for (Parameter proxyInputParameter : proxyInputParameters) {
                if (proxyInputParameter.getAnnotation(Param.class) == null) {
                    return BeanUtils.isSimpleProperty(proxyInputParameter.getType());
                }
            }
            return false;
        }

        private boolean isCustomObject(Parameter[] proxyInputParameters) {
            return proxyInputParameters.length == 1 && proxyInputParameters[0].getAnnotation(Param.class) == null;
        }

        private RequestWrapper createForCustomObject(Class<?> proxyInputArg, Class<?> actualRequestType, TypeConverter converter) {
            Map<Field, Field> fieldMapping = Utilities.serializationMappings(proxyInputArg, actualRequestType);
            return new CustomRequestWrapper(fieldMapping, converter, actualRequestType);
        }

        private RequestWrapper createForPlainObjects(Class<?> inputArg, Parameter[] proxyInputParameters) {
            Method[] inputMethods = inputArg.getDeclaredMethods();
            List<Method> inputSetterList = new LinkedList<>();
            for (Parameter inputParameter: proxyInputParameters) {
                Annotation paramAnnotation = inputParameter.getAnnotation(Param.class);
                Assert.notNull(paramAnnotation, "Parameters in methods should be with @Param annotation");
                String fieldName = (String) AnnotationUtils.getValue(paramAnnotation);
                for (Method inputMethod: inputMethods) {
                    if (!StringUtils.contains(inputMethod.getName(), "get")) {
                        if (StringUtils.equals(StringUtils.uncapitalize(removeSetPrefix(inputMethod.getName())), fieldName)) {
                            inputSetterList.add(inputMethod);
                        }
                    }
                }
            }
            if (inputSetterList.size() == 0) {
                throw new IllegalArgumentException("Can not map " + inputArg + " to proxy input parameters. Wrong mapping in @Param annotation.");
            }
            return new PlainRequestWrapper(inputSetterList, inputArg, converter);
        }

    }
}

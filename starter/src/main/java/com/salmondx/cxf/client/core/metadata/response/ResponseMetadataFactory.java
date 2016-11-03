package com.salmondx.cxf.client.core.metadata.response;

import com.netflix.hystrix.HystrixCommand;
import com.salmondx.cxf.client.annotation.Response;
import com.salmondx.cxf.client.core.TypeConverter;
import com.salmondx.cxf.client.core.utils.PathUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.annotation.AnnotationUtils;
import rx.Observable;
import rx.Single;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

import static com.salmondx.cxf.client.core.utils.Utilities.serializationMappings;

/**
 * Created by Salmondx on 08/09/16.
 */
public abstract class ResponseMetadataFactory {
    TypeConverter converter;

    public ResponseMetadataFactory(TypeConverter converter) {
        this.converter = converter;
    }

    public abstract ResponseMetadata create(Method proxyMethod, Method serviceMethod);

    public static final class Default extends ResponseMetadataFactory {

        public Default(TypeConverter converter) {
            super(converter);
        }

        @Override
        public ResponseMetadata create(Method proxyMethod, Method serviceMethod) {
            Class<?> proxyResponseType = proxyMethod.getReturnType();
            Class<?> actualResponseType = serviceMethod.getReturnType();
            try {
                if (Observable.class.isAssignableFrom(proxyResponseType) ||
                        HystrixCommand.class.isAssignableFrom(proxyResponseType) ||
                        Single.class.isAssignableFrom(proxyResponseType)) {

                    ParameterizedType type = (ParameterizedType) proxyMethod.getGenericReturnType();
                    if (type.getActualTypeArguments()[0] instanceof ParameterizedType) {
                        ParameterizedType secondLevelType = (ParameterizedType) type.getActualTypeArguments()[0];
                        Class<?> genericResponseType = (Class<?>) secondLevelType.getRawType();
                        if (Collection.class.isAssignableFrom(genericResponseType)) {
                            Class<?> collectionGenericType = (Class<?>) secondLevelType.getActualTypeArguments()[0];
                            return createCollectionMetadata(collectionGenericType, actualResponseType);
                        } else {
                            return createResponseMetadata(genericResponseType, actualResponseType);
                        }

                    } else {
                        Class<?> genericResponseType = (Class<?>) type.getActualTypeArguments()[0];
                        return createResponseMetadata(genericResponseType, actualResponseType);
                    }
                }

                if (Collection.class.isAssignableFrom(proxyResponseType)) {
                    ParameterizedType type = (ParameterizedType) proxyMethod.getGenericReturnType();
                    Class<?> genericResponseType = (Class<?>) type.getActualTypeArguments()[0];
                    return createCollectionMetadata(genericResponseType, actualResponseType);
                } else if (Void.class.isAssignableFrom(proxyResponseType)){
                    return new VoidResponseMetadata();
                }
                else {
                    return createResponseMetadata(proxyResponseType, actualResponseType);
                }
            } catch (Exception e) {
                throw new BeanCreationException("Cannot create response metadata. Probably wrong response type provided or @Response value points on wrong location");
            }
        }

        private ResponseMetadata createResponseMetadata(Class<?> proxyResponseType, Class<?> actualResponseType) throws IntrospectionException {
            Annotation responseAnnotation = proxyResponseType.getAnnotation(Response.class);
            String prefix;
            Map<Field, Field> namingMappings;
            if (responseAnnotation == null) {
                namingMappings = serializationMappings(proxyResponseType, actualResponseType);
                prefix = "";
            } else {
                String innerProperty = (String) AnnotationUtils.getValue(responseAnnotation);
                Pair<String, Class<?>> pathWithType = PathUtils.findPathPrefix(actualResponseType, innerProperty);
                namingMappings = serializationMappings(proxyResponseType, pathWithType.getRight());
                prefix = pathWithType.getLeft();
            }
            return new SimpleResponseMetadata(converter, proxyResponseType, prefix, namingMappings);
        }

        private ResponseMetadata createCollectionMetadata(Class<?> proxyResponseType, Class<?> actualResponseType) throws IntrospectionException {
            Annotation responseAnnotation = proxyResponseType.getAnnotation(Response.class);
            String innerProperty = (String) AnnotationUtils.getValue(responseAnnotation);
            Pair<String, Class<?>> pathWithType = PathUtils.findPathPrefix(actualResponseType, innerProperty);

            Map<Field, Field> namingMappings = serializationMappings(proxyResponseType, pathWithType.getRight());


            return new CollectionResponseMetadata(converter, proxyResponseType, pathWithType.getLeft(), namingMappings);
        }
    }
}

package com.salmondx.cxf.client.core.metadata.response;

import com.salmondx.cxf.client.core.TypeConverter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Salmondx on 05/09/16.
 */
public abstract class ResponseMetadata {
    TypeConverter converter;
    Class<?> proxyType;
    String pathPrefix;
    Map<Field, Field> fieldsMapping;

    public ResponseMetadata(TypeConverter converter, Class<?> proxyType, String pathPrefix, Map<Field, Field> fieldsMapping) {
        this.converter = converter;
        this.proxyType = proxyType;
        this.pathPrefix = pathPrefix;
        this.fieldsMapping = fieldsMapping;
    }

    public ResponseMetadata() {}

    public abstract Object convertResponse(Object response);
}

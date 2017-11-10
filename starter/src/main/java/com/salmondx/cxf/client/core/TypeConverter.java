package com.salmondx.cxf.client.core;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Salmondx on 09/09/16.
 */

/**
 * TypeConverter interface that is used during serialization and deserialization phases.
 * By default, a Spring ConversionService is used.
 */
public interface TypeConverter {
    /**
     * Check can convert a types
     * @param sourceType
     * @param targetType
     * @return
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    /**
     * Convert source object to target type
     * @param source
     * @param targetType
     * @return
     */
    Object convert(Object source, Class<?> targetType);

    /**
     * Convert a source object to target type. If can't, then return type as-is without modification.
     * @param source
     * @param target
     * @return
     */
    Object convertIfDifferentTypes(Object source, Class<?> target);
}

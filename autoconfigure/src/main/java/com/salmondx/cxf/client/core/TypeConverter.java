package com.salmondx.cxf.client.core;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Salmondx on 09/09/16.
 */
public interface TypeConverter {
    boolean canConvert(Class<?> sourceType, Class<?> targetType);
    Object convert(Object source, Class<?> targetType);
    Object convertIfDifferentTypes(Object source, Class<?> target);
}

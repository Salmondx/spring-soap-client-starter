package com.salmondx.cxf.client.core.metadata;

/**
 * Created by Salmondx on 05/09/16.
 */

/**
 * Accepts proxy service and proxy arguments, serialize it to the required type
 * and deserialize a response object to the required type.
 */
public interface MethodMetadata {
    Object invoke(Object proxyService, Object... arguments) throws Exception;
}

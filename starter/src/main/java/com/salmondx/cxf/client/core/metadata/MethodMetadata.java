package com.salmondx.cxf.client.core.metadata;

/**
 * Created by Salmondx on 05/09/16.
 */
public interface MethodMetadata {
    Object invoke(Object proxyService, Object... arguments) throws Exception;
}

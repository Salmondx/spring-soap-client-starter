package com.salmondx.cxf.client.core.metadata.request;

/**
 * Created by Salmondx on 07/11/16.
 */

/**
 * Used to serialize proxy parameters to the original type
 */
public interface RequestWrapper {
    Object createRequestObject(Object[] args);
}

package com.salmondx.cxf.client.core.metadata.request;

import lombok.AllArgsConstructor;

/**
 * Created by Salmondx on 07/11/16.
 */
@AllArgsConstructor
public class AutowiredRequestWrapper implements RequestWrapper {
    private Object autowiredField;

    @Override
    public Object createRequestObject(Object[] args) {
        return autowiredField;
    }
}

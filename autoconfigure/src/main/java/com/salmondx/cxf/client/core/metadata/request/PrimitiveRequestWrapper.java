package com.salmondx.cxf.client.core.metadata.request;

/**
 * Created by Salmondx on 08/11/16.
 */
public class PrimitiveRequestWrapper implements RequestWrapper {
    private int elementOrder;

    public PrimitiveRequestWrapper(int elementOrder) {
        this.elementOrder = elementOrder;
    }
    @Override
    public Object createRequestObject(Object[] args) {
        return args[elementOrder];
    }
}

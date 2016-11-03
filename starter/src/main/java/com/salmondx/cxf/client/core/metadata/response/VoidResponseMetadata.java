package com.salmondx.cxf.client.core.metadata.response;

/**
 * Created by Salmondx on 08/11/16.
 */
public class VoidResponseMetadata extends ResponseMetadata {

    public VoidResponseMetadata() {}

    @Override
    public Object convertResponse(Object response) {
        return null;
    }
}

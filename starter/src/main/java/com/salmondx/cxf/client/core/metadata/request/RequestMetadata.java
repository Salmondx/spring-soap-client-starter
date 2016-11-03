package com.salmondx.cxf.client.core.metadata.request;

import java.util.List;

/**
 * Created by Salmondx on 08/09/16.
 */
public class RequestMetadata {
    private List<RequestWrapper> inputParameters;

    public RequestMetadata(List<RequestWrapper> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public Object[] createInputArgument(Object[] args) throws Exception {
        return inputParameters
                .stream()
                .map(it -> it.createRequestObject(args))
                .toArray();
    };

}

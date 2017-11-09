package com.salmondx.cxf.client.core.metadata;

import com.salmondx.cxf.client.core.metadata.response.ResponseMetadata;
import lombok.Data;
import com.salmondx.cxf.client.core.metadata.request.RequestMetadata;

import java.lang.reflect.Method;

/**
 * Created by Salmondx on 02/09/16.
 */
@Data
public class MethodMetadataImpl implements MethodMetadata {
    private Method invokableMethod;
    private RequestMetadata requestMetadata;
    private ResponseMetadata responseMetadata;

    public Object invoke(Object proxyService, Object... args) throws Exception {
        Object response = invokableMethod.invoke(proxyService, requestMetadata.createInputArgument(args));
        if (response == null) {
            return null;
        }
        return responseMetadata.convertResponse(response);
    }
}

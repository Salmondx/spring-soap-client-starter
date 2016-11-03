package com.salmondx.cxf.client.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Created by Salmondx on 03/11/16.
 */
@Data
@AllArgsConstructor
public class SoapMethodData {
    private String methodName;
    private String returnTypeName;
    private Class<?>[] autowiredFields;
}

package com.salmondx.cxf.client.data;

import com.salmondx.cxf.client.annotation.Field;

/**
 * Created by Salmondx on 12/09/16.
 */
public class FailedProxyClass {
    private String ean;
    private String ccy;
    @Field("amtn")
    private Long amount;
}

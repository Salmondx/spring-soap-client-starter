package com.salmondx.cxf.client.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Salmondx on 12/09/16.
 */
@Data
public class ProxyClass {
    private String ean;
    private String ccy;
    private BigDecimal amt;
}

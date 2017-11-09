package com.salmondx.cxf.client.data;

import com.salmondx.cxf.client.annotation.Response;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Salmondx on 12/09/16.
 */
@Data
@Response("actualClasses")
public class ActualClass {
    private String ean;
    private String ccy;
    private BigDecimal amt;
    private List<ActualClass> actualClasses;
}

package com.salmondx.cxf.client.data;

import com.salmondx.cxf.client.annotation.Param;
import com.salmondx.cxf.client.annotation.SoapMethod;

import java.util.List;

/**
 * Created by Salmondx on 12/09/16.
 */
public interface TestInterface {
    @SoapMethod("Get")
    List<ActualClass> collection(ProxyClass requestTest);

    @SoapMethod("Get")
    ActualClass customObject(ProxyClass requestTest);

    @SoapMethod("Get")
    ActualClass plainObject(@Param("ccy") String ccy);
}

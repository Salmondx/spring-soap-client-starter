package com.salmondx.cxf.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Salmondx on 01/09/16.
 */

/**
 * Constructs a proxy service around original soap service
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoapClient {
    Class<?> service();
}

package com.salmondx.cxf.client.annotation;

import org.springframework.context.annotation.Import;
import com.salmondx.cxf.client.registration.SoapClientRegistrar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Salmondx on 01/09/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SoapClientRegistrar.class)
public @interface EnableSoapClients {
}

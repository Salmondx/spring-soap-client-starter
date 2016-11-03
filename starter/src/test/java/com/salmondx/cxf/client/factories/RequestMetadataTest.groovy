package com.salmondx.cxf.client.factories

import com.salmondx.cxf.client.core.TypeConverter
import com.salmondx.cxf.client.core.metadata.request.CustomRequestWrapper
import com.salmondx.cxf.client.core.metadata.request.PlainRequestWrapper
import com.salmondx.cxf.client.core.metadata.request.RequestMetadata
import com.salmondx.cxf.client.core.metadata.request.RequestMetadataFactory
import com.salmondx.cxf.client.core.utils.Utilities
import com.salmondx.cxf.client.data.ActualClass
import com.salmondx.cxf.client.data.ProxyClass
import com.salmondx.cxf.client.data.TestInterface
import spock.lang.Specification

import java.lang.reflect.Method

/**
 * Created by Salmondx on 12/09/16.
 */
class RequestMetadataTest extends Specification {

    def "it should create request from custom object"() {
        given:
        def requestMetadata = new CustomRequestWrapper(Utilities.serializationMappings(ProxyClass, ActualClass), typeConverter, ActualClass)
        def testClass = new ProxyClass()
        testClass.amt = BigDecimal.ONE
        testClass.ccy = "RUR"
        testClass.ean = "4012413213"
        when:
        ActualClass actual = requestMetadata.createRequestObject(testClass)
        then:
        actual.ean == testClass.ean
        actual.amt == testClass.amt
        actual.ccy == testClass.ccy
    }

    def "it should create request from plain parameters"() {
        given:
        List<Method> list = new LinkedList<>()
        list.add(ActualClass.getMethod("setEan", String))
        list.add(ActualClass.getMethod("setCcy", String))
        list.add(ActualClass.getMethod("setAmt", BigDecimal))
        def requestMetadata = new PlainRequestWrapper(list, ActualClass, typeConverter)
        when:
        ActualClass actual = requestMetadata.createRequestObject("4102311", "RUR", BigDecimal.ONE)
        then:
        actual.ccy == "RUR"
        actual.ean == "4102311"
        actual.amt == BigDecimal.ONE
    }

    def "it should create CustomObjectRequestMetadata"() {
        given:
        RequestMetadataFactory requestMetadataFactory = new RequestMetadataFactory.Default(typeConverter)
        def methodWithCustomObject = TestInterface.getMethod("customObject", ProxyClass)
        when:
        def result = requestMetadataFactory.create(methodWithCustomObject.getParameterTypes(), methodWithCustomObject, null)
        then:
        result instanceof RequestMetadata
    }

    def "it should thrown Exception because no underlying class found"() {
        given:
        RequestMetadataFactory requestMetadataFactory = new RequestMetadataFactory.Default(typeConverter)
        def methodWithPlainObjects = TestInterface.getMethod("plainObject", String)
        when:
        def result = requestMetadataFactory.create(methodWithPlainObjects.getParameterTypes(), methodWithPlainObjects, null)
        then:
        thrown(IllegalArgumentException)
    }

    def typeConverter = new TypeConverter() {
        @Override
        boolean canConvert(Class<?> sourceType, Class<?> targetType) {
            return true
        }

        @Override
        Object convert(Object source, Class<?> targetType) {
            return source
        }

        @Override
        Object convertIfDifferentTypes(Object source, Class<?> target) {
            return source
        }
    }
}

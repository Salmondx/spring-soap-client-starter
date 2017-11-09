package com.salmondx.cxf.client.factories

import com.salmondx.cxf.client.core.TypeConverter
import com.salmondx.cxf.client.core.metadata.response.CollectionResponseMetadata
import com.salmondx.cxf.client.core.metadata.response.ResponseMetadata
import com.salmondx.cxf.client.core.metadata.response.ResponseMetadataFactory
import com.salmondx.cxf.client.core.metadata.response.SimpleResponseMetadata
import com.salmondx.cxf.client.core.utils.Utilities
import com.salmondx.cxf.client.data.ActualClass
import com.salmondx.cxf.client.data.ProxyClass
import com.salmondx.cxf.client.data.TestInterface
import spock.lang.Specification

/**
 * Created by Salmondx on 12/09/16.
 */
class ResponseMetadataTest extends Specification {
    def "it should create response from SimpleResponseMetadata"() {
        given:
        SimpleResponseMetadata simpleResponseMetadata = new SimpleResponseMetadata(typeConverter, ActualClass, "", Utilities.serializationMappings(ActualClass, ActualClass))
        ActualClass response = new ActualClass()
        response.amt = BigDecimal.ONE
        response.ccy = "RUR"
        response.ean = "411413213"
        when:
        ActualClass responseFromMetadata = simpleResponseMetadata.convertResponse(response)
        then:
        responseFromMetadata.ean == response.ean
        responseFromMetadata.ccy == response.ccy
        responseFromMetadata.amt == response.amt
    }

    def "it should create response from CollectionResponseMetadata"() {
        given:
        CollectionResponseMetadata collectionResponseMetadata = new CollectionResponseMetadata(typeConverter, ActualClass, "actualClasses", Utilities.serializationMappings(ActualClass, ActualClass))
        ActualClass response = new ActualClass()
        response.amt = BigDecimal.ONE
        response.ccy = "RUR"
        response.ean = "411413213"
        ActualClass innerResponse = new ActualClass()
        response.actualClasses = [innerResponse, innerResponse, innerResponse]
        when:
        List<ActualClass> responseFromMetadata = collectionResponseMetadata.convertResponse(response)
        then:
        responseFromMetadata.size() == 3
    }

    def "it should create CollectionResponseMetadata"() {
        given:
        ResponseMetadataFactory responseMetadataFactory = new ResponseMetadataFactory.Default(typeConverter)
        def method = TestInterface.getMethod("collection", ProxyClass)
        def methodWithPlainResponse = TestInterface.getMethod("customObject", ProxyClass)
        when:
        ResponseMetadata responseMetadata = responseMetadataFactory.create(method, methodWithPlainResponse)
        then:
        responseMetadata instanceof CollectionResponseMetadata
    }

    def "it should create SimpleResponseMetadata"() {
        given:
        ResponseMetadataFactory responseMetadataFactory = new ResponseMetadataFactory.Default(typeConverter)
        def methodWithPlainResponse = TestInterface.getMethod("customObject", ProxyClass)
        when:
        ResponseMetadata responseMetadata = responseMetadataFactory.create(methodWithPlainResponse, methodWithPlainResponse)
        then:
        responseMetadata instanceof SimpleResponseMetadata
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

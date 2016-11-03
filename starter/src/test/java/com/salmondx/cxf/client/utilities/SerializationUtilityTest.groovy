package com.salmondx.cxf.client.utilities

import com.salmondx.cxf.client.data.ActualClass
import com.salmondx.cxf.client.core.utils.Utilities
import com.salmondx.cxf.client.data.FailedProxyClass
import com.salmondx.cxf.client.data.ProxyClass
import spock.lang.Specification

import java.lang.reflect.Field

/**
 * Created by Salmondx on 12/09/16.
 */
class SerializationUtilityTest extends Specification {

    def "it should found all field from actual class"() {
        when:
        Map<Field, Field> mappings = Utilities.serializationMappings(ProxyClass, ActualClass)
        def list = mappings.entrySet().collect { it.value.name }
        then:
        mappings.size() == 3
        list.containsAll("ean", "amt", "ccy")
    }

    def "it should throw IllegalArgumentException if field not found"() {
        when:
        Utilities.serializationMappings(FailedProxyClass, ActualClass)
        then:
        thrown(IllegalArgumentException)
    }

}

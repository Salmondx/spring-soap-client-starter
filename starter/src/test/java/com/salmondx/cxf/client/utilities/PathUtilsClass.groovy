package com.salmondx.cxf.client.utilities

import org.apache.commons.lang3.tuple.Pair
import com.salmondx.cxf.client.core.utils.PathUtils
import com.salmondx.cxf.client.data.ActualClass
import com.salmondx.cxf.client.data.TestClass
import spock.lang.Specification

/**
 * Created by Salmondx on 12/09/16.
 */
class PathUtilsClass extends Specification {

    def "it should find ThirdInnerClass with full path"() {
        when:
        Pair<String, Class<?>> pathWithClass = PathUtils.findPathPrefix(TestClass, "third")
        then:
        pathWithClass.getRight() == TestClass.ThirdInnerClass
        pathWithClass.getLeft() == "second.third"
    }

    def "it should find inner collection with full path"() {
        when:
        Pair<String, Class<?>> pathWithClass = PathUtils.findPathPrefix(ActualClass, "actualClasses")
        then:
        pathWithClass.getRight() == ActualClass
        pathWithClass.getLeft() == "actualClasses"
    }

    def "it should throw IllegalArgumentException if path not found"() {
        when:
        PathUtils.findPathPrefix(TestClass, "notFound")
        then:
        thrown(IllegalArgumentException)
    }
}

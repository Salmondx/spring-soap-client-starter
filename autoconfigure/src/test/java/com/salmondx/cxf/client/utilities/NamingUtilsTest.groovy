package com.salmondx.cxf.client.utilities

import com.salmondx.cxf.client.core.utils.NamingUtilities
import spock.lang.Specification

/**
 * Created by Salmondx on 12/09/16.
 */
class NamingUtilsTest extends Specification {

    def "it should remove 'set' from method name"() {
        expect:
        NamingUtilities.removeSetPrefix("setCcy") == "Ccy"
    }

    def "it should return name if 'set' not detected"() {
        expect:
        NamingUtilities.removeSetPrefix("getCcy") == "getCcy"
    }
}

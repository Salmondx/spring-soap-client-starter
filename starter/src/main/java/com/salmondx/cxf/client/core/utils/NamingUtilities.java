package com.salmondx.cxf.client.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Salmondx on 08/09/16.
 */
public class NamingUtilities {

    public static String removeSetPrefix(String name) {
        return StringUtils.removeStart(name, "set");
    }
}

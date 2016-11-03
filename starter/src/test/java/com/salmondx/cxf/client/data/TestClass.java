package com.salmondx.cxf.client.data;

import lombok.Data;

/**
 * Created by Salmondx on 12/09/16.
 */
@Data
public class TestClass {
    private SecondInnerClass second;

    @Data
    public static class SecondInnerClass {
        private ThirdInnerClass third;
    }

    @Data
    public static class ThirdInnerClass {
        private final String testString = "FOUND";
    }
}

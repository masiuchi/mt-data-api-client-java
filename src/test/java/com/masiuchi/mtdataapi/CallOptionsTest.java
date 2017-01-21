package com.masiuchi.mtdataapi;

import junit.framework.TestCase;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class CallOptionsTest extends TestCase {
    public void testConstructor() {
        CallOptions options = new CallOptions();
        assertThat(
                options,
                instanceOf(CallOptions.class)
        );
        assertThat(
                options,
                instanceOf(HashMap.class)
        );
    }
}

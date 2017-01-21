package com.masiuchi.mtdataapi;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ClientOptionsTest extends TestCase {
    public void testConstructor() {
        ClientOptions options = new ClientOptions();
        assertThat(
                options,
                instanceOf(ClientOptions.class)
        );
    }

    public void testInitialInstanceVariables() {
        ClientOptions options = new ClientOptions();
        assertEquals("", options.accessToken);
        assertEquals(ClientOptions.DEFAULT_API_VERSION, options.apiVersion);
        assertEquals("", options.baseUrl);
        assertEquals("", options.clientId);
        assertNull(options.endpoints);
    }
}

package com.masiuchi.mtdataapi;

import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ClientTest extends TestCase {
    public void testConstructor() {
        ClientOptions options = getClientOptions();
        Client client = new Client(options);

        assertThat(
                client,
                instanceOf(Client.class)
        );
    }

    public void testCallWithoutEndpoint() {
    }

    public void testGetEndpoints() {
    }

    private ClientOptions getClientOptions() {
        ClientOptions options = new ClientOptions();
        options.baseUrl = "http://localhost/mt-data-api.cgi";
        options.clientId = "test-client";
        return options;
    }
}

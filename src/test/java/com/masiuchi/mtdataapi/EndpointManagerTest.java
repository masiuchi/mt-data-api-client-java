package com.masiuchi.mtdataapi;

import com.google.gson.*;
import junit.framework.TestCase;
import org.easymock.EasyMock;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EndpointManagerTest extends TestCase {
    public void testConstructor() {
        ClientOptions options = getValidClientOptions();
        EndpointManager endpointManager = new EndpointManager(options);
        assertThat(
                endpointManager,
                instanceOf(EndpointManager.class)
        );
    }

    public void testConstructorWithInvalidParameters() {
        ClientOptions options = new ClientOptions();
        try {
            new EndpointManager(options);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertTrue(false);
    }

    public void testFindEndpoint() {
        ClientOptions options = getValidClientOptionsWithEndpoints();
        EndpointManager endpointManager = new EndpointManager(options);
        Endpoint endpoint = endpointManager.findEndpoint("list_endpoints");
        assertListEndpoints(endpoint);
    }

    public void testFindEndpointWithoutEndpoints() {
        String listEndpointsJson = generateListEndpointsJson();
        APIResponse apiResponse = new APIResponse(listEndpointsJson);

        Endpoint mockEndpoint = EasyMock.createMock(Endpoint.class);
        try {
            EasyMock.expect(mockEndpoint.call("", null))
                    .andStubReturn(apiResponse);
        } catch (IOException e) {
            assertTrue(e.toString(), false);
            return;
        }
        EasyMock.replay(mockEndpoint);

        Endpoint originalListEndpoint = EndpointManager.listEndpoints;
        EndpointManager.listEndpoints = mockEndpoint;

        ClientOptions options = getValidClientOptions();
        EndpointManager endpointManager = new EndpointManager(options);

        Endpoint endpoint = endpointManager.findEndpoint("list_endpoints");
        assertListEndpoints(endpoint);

        EndpointManager.listEndpoints = originalListEndpoint;
    }

    public void testGetEndpoints() {
        ClientOptions options = getValidClientOptionsWithEndpoints();
        EndpointManager endpointManager = new EndpointManager(options);
        assertThat(
                endpointManager.getEndpoints(),
                is(options.endpoints)
        );
    }

    public void testSetEndpoints() {
        ClientOptions options = getValidClientOptions();
        EndpointManager endpointManager = new EndpointManager(options);
        APIResponse endpoints = new APIResponse("{\"test\":\"ok\"}");
        endpointManager.setEndpoints(endpoints);
        assertThat(
                endpointManager.getEndpoints(),
                is(endpoints)
        );
    }

    private ClientOptions getValidClientOptions() {
        ClientOptions options = new ClientOptions();
        options.baseUrl = "http://localhost/mt-data-api.cgi";
        options.clientId = "testClient";
        return options;
    }

    private ClientOptions getValidClientOptionsWithEndpoints () {
        ClientOptions options = getValidClientOptions();
        String endpointsJson = generateListEndpointsJson();
        options.endpoints = new APIResponse(endpointsJson);
        return options;
    }

    private String generateListEndpointsJson() {
        JsonObject root = new JsonObject();
        root.addProperty("totalResults", 1);

        JsonObject endpoint = new JsonObject();
        endpoint.addProperty("version", "1");
        endpoint.add("resources", JsonNull.INSTANCE);
        endpoint.addProperty("verb", "GET");
        endpoint.addProperty("route", "/endpoints");
        JsonObject component = new JsonObject();
        component.addProperty("id", "core");
        component.addProperty("name", "Core");
        endpoint.add("component", component);
        endpoint.add("format", JsonNull.INSTANCE);
        endpoint.addProperty("id", "list_endpoints");

        JsonArray items = new JsonArray();
        items.add(endpoint);
        root.add("items", items);

        return new Gson().toJson(root);
    }

    private void assertListEndpoints(Endpoint endpoint) {
        assertEquals("list_endpoints", endpoint.id);
        assertEquals("/endpoints", endpoint.route);
        assertEquals(1, endpoint.version);
        assertEquals(Endpoint.Verb.GET, endpoint.verb);
    }
}

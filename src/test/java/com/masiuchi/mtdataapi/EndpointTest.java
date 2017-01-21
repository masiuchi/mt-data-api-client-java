package com.masiuchi.mtdataapi;

import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.jetbrains.annotations.Contract;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Endpoint.class})
public class EndpointTest extends TestCase {

    @ClassRule
    public static void beforeClass() {
        Endpoint.apiUrl = "http://localhost/mt/mt-data-api.cgi/v3";
    }

    public void testConstructor() {
        assertThat(
                this.listEndpointsEndpoint().getClass().getName(),
                is("com.masiuchi.mtdataapi.Endpoint")
        );
    }

    public void testConstructorWithJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "list_endpoints");
        jsonObject.addProperty("route", "/endpoints");
        jsonObject.addProperty("version", 1);
        jsonObject.addProperty("verb", "GET");

        Endpoint endpoint = new Endpoint(jsonObject);
        assertThat(
                endpoint.getClass().getName(),
                is("com.masiuchi.mtdataapi.Endpoint")
        );
    }

    public void testCall() {
        String accessToken = "testToken";
        CallOptions options = new CallOptions();

        APIRequest mockRequest = PowerMock.createMock(APIRequest.class);
        try {
            EasyMock.expect(mockRequest.send(accessToken, options))
                    .andStubReturn("{\"accessToken\": \"testToken\"}");
        } catch (IOException e) {
            assertTrue(e.toString(), false);
            return;
        }
        EasyMock.replay(mockRequest);

        Endpoint mockEndpoint = PowerMock.createPartialMock(Endpoint.class, "apiRequest");
        try {
            PowerMock.expectPrivate(mockEndpoint, "apiRequest")
                    .andStubReturn(mockRequest);
        } catch (Exception e) {
        }
        PowerMock.replay(mockEndpoint);

        APIResponse response;
        try {
            response = mockEndpoint.call(accessToken, options);
        } catch (IOException e) {
            assertTrue(e.toString(), false);
            return;
        }
        assertThat(
                response.accessToken(),
                is(accessToken)
        );
    }

    public void testRequestUrlWithNoParameterRoute() {
        CallOptions options = new CallOptions();

        assertThat(
                this.listEndpointsEndpoint().requestUrl(options),
                is(Endpoint.apiUrl + "/endpoints")
        );
    }

    public void testRequestUrlWithParameterRoute() {
        CallOptions options = new CallOptions();
        options.put("site_id", "3");

        assertThat(
                this.listEntriesEndpoint().requestUrl(options),
                is(Endpoint.apiUrl + "/sites/3/entries")
        );
    }

    public void testRequestUrlWithQueryAndGetMethod() {
        CallOptions options = new CallOptions();
        options.put("offset", "2");
        options.put("limit", "3");

        assertThat(
                this.listEndpointsEndpoint().requestUrl(options),
                is(Endpoint.apiUrl + "/endpoints?limit=3&offset=2")
        );
    }

    public void testRequestUrlWithQueryAndNotGetMethod() {
        CallOptions options = new CallOptions();
        options.put("limit", "3");
        options.put("offset", "2");

        assertThat(
                this.postEndpoint().requestUrl(options),
                is(Endpoint.apiUrl + "/post")
        );
    }

    @Contract(" -> !null")
    private Endpoint listEndpointsEndpoint() {
        String id = "list_endpoints";
        String route = "/endpoints";
        int version = 1;
        Endpoint.Verb verb = Endpoint.Verb.GET;

        return new Endpoint(id, route, version, verb);
    }

    @Contract(" -> !null")
    private Endpoint listEntriesEndpoint() {
        String id = "list_entries";
        String route = "/sites/:site_id/entries";
        int version = 1;
        Endpoint.Verb verb = Endpoint.Verb.GET;

        return new Endpoint(id, route, version, verb);
    }

    @Contract(" -> !null")
    private Endpoint postEndpoint() {
        String id = "post";
        String route = "/post";
        int version = 1;
        Endpoint.Verb verb = Endpoint.Verb.POST;

        return new Endpoint(id, route, version, verb);
    }
}

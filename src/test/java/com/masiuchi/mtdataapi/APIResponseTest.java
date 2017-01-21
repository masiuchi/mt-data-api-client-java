package com.masiuchi.mtdataapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import junit.framework.TestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class APIResponseTest extends TestCase {
    public void testConstructorWithEmptyJSONObject() {
        String json = "{}";
        APIResponse response = new APIResponse(json);

        assertEquals("", response.accessToken());
        assertEquals("", response.error());
        assertEquals(-1, response.totalResults());
        assertNull(response.items());
    }

    public void testConstructorWithInvalidJSON() {
        String invalidJson = "123";
        try {
            new APIResponse(invalidJson);
        } catch (JsonSyntaxException e) {
            return;
        }
        assertTrue(false);
    }

    public void testAccessToken() {
        String json = "{\"accessToken\":\"testToken\"}";
        APIResponse response = new APIResponse(json);

        assertEquals("testToken", response.accessToken());
    }

    public void testError() {
        String json = "{\"error\":\"testError\"}";
        APIResponse response = new APIResponse(json);

        assertEquals("testError", response.error());
    }

    public void testTotalResult() {
        String json = "{\"totalResults\":10}";
        APIResponse response = new APIResponse(json);

        assertEquals(10, response.totalResults());
    }

    public void testItems() {
        String json = "{\"items\": [123, 456]}";
        APIResponse response = new APIResponse(json);

        assertThat(
                response.items(),
                instanceOf(JsonArray.class)
        );
    }

    public void testItemsWithInvalidJSON() {
        String json = "{\"items\":123}";
        APIResponse response = new APIResponse(json);

        assertNull(response.items());
    }
}

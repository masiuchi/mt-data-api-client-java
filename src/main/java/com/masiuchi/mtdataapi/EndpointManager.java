package com.masiuchi.mtdataapi;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EndpointManager {
    static Endpoint listEndpoints;
    static {
        String id = "list_endpoints";
        String route = "/endpoints";
        int version = 1;
        Endpoint.Verb verb = Endpoint.Verb.GET;
        listEndpoints = new Endpoint(id, route, version, verb);
    }

    private ClientOptions options;

    public EndpointManager(ClientOptions options) {
        this.options = options;
        if (hasInvalidParameters()) {
            throw new IllegalArgumentException();
        }
        Endpoint.apiUrl = apiUrl();
    }

    public Endpoint findEndpoint(String id) {
        if (options.endpoints == null) {
            options.endpoints = retrieveEndpoints();
        }

        Endpoint endpoint = null;
        for (JsonElement item : options.endpoints.items()) {
            String endpointId = item.getAsJsonObject().get("id").getAsString();
            if (endpointId.equals(id)) {
                endpoint = new Endpoint(item.getAsJsonObject());
            }
        }
        return endpoint;
    }

    public APIResponse getEndpoints() {
        return options.endpoints;
    }

    public void setEndpoints(APIResponse endpoints) {
        options.endpoints = endpoints;
    }

    @Nullable
    private APIResponse retrieveEndpoints() {
        APIResponse res;
        try {
            res = listEndpoints.call("", null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (res == null || !res.error().isEmpty()) {
            return null;
        }
        return res;
    }

    private boolean hasInvalidParameters() {
        return options.baseUrl.isEmpty() || options.clientId.isEmpty();
    }

    @NotNull
    private String apiUrl() {
        String apiUrl = options.baseUrl;

        int lastIndex = options.baseUrl.length() - 1;
        String lastChar = options.baseUrl.substring(lastIndex - 1, lastIndex);
        if (!lastChar.equals("/")) apiUrl +=  "/";

        return apiUrl + "v" + options.apiVersion;
    }
}

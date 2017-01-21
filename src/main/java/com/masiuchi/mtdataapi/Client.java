package com.masiuchi.mtdataapi;

import java.io.IOException;

public class Client {
    public static final String VERSION = "0.0.1";

    private String accessToken;
    private EndpointManager endpointManager;

    public Client(ClientOptions options) {
        accessToken = options.accessToken;
        endpointManager = new EndpointManager(options);
    }

    public APIResponse call(String id, CallOptions options) throws IOException {
        Endpoint endpoint = endpointManager.findEndpoint(id);
        if (endpoint == null) {
            return null;
        }

        APIResponse res = endpoint.call(accessToken, options);

        if (id.equals("authenticate")) {
            accessToken = res.accessToken();
        } else if (id.equals("list_endpoints")) {
            endpointManager.setEndpoints(res);
        }

        return res;
    }

    public APIResponse getEndpoints() {
        return endpointManager.getEndpoints();
    }
}

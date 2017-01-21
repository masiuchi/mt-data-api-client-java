package com.masiuchi.mtdataapi;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Endpoint {
    public enum Verb {
        GET,
        POST,
        PUT,
        DELETE
    }

    static String apiUrl;
    Verb verb;

    String id;
    String route;
    int version;

    Endpoint(String id, String route, int version, Verb verb) {
        this.id = id;
        this.route = route;
        this.version = version;
        this.verb = verb;
    }

    Endpoint(JsonObject json) {
        id = json.get("id").getAsString();
        route = json.get("route").getAsString();
        version = json.get("version").getAsInt();
        String verb = json.get("verb").getAsString();
        if (verb.equals("GET")) {
            this.verb = Verb.GET;
        } else if (verb.equals("POST")) {
            this.verb = Verb.POST;
        } else if (verb.equals("PUT")) {
            this.verb = Verb.PUT;
        } else if (verb.equals("DELETE")) {
            this.verb = Verb.DELETE;
        }
    }

    APIResponse call(String accessToken, CallOptions options) throws IOException {
        APIRequest req = apiRequest();
        String json = req.send(accessToken, options);
        if (json.isEmpty()) {
            return null;
        }
        return new APIResponse(json);
    }

    String requestUrl(CallOptions options) {
        StringBuilder buf = new StringBuilder();
        buf.append(apiUrl);
        buf.append(this.route(options));
        if (this.verb.equals(Verb.GET)) {
            buf.append(this.queryString(options));
        }
        return buf.toString();
    }

    @Contract(" -> !null")
    private APIRequest apiRequest() {
        return new APIRequest(this);
    }

    @NotNull
    @Contract(pure = true)
    private String route(CallOptions options) {
        String route = this.route;

        Matcher m = Pattern.compile(":[^:/]+(?=/|$)").matcher(this.route);
        while (m.find()) {
            // remove first colon
            String key = m.group().substring(1, m.group().length());
            String value = options.remove(key);
            if (value == null) {
                throw new IllegalArgumentException();
            }
            route = route.replaceFirst(m.group(), value);
        }

        return route;
    }

    @NotNull
    private String queryString(CallOptions options) {
        if (options == null || options.isEmpty()) {
            return "";
        }

        String[] keys = options.keySet().toArray(new String[options.size()]);
        Arrays.sort(keys);

        List<String> keyAndValue = new ArrayList<String>();
        for (String key : keys) keyAndValue.add(key + "=" + options.get(key));

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < keyAndValue.size(); i++) {
            if (i > 0) {
                buf.append("&");
            }
            buf.append(keyAndValue.get(i));
        }

        return "?" + buf.toString();
    }
}

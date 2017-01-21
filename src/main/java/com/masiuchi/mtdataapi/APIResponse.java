package com.masiuchi.mtdataapi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class APIResponse {
    private JsonObject json;

    public APIResponse(String json) {
        this.json = new Gson().fromJson(json, JsonObject.class);
    }

    public String accessToken() {
        if (json.has("accessToken")) {
            return json.get("accessToken").getAsString();
        } else {
            return "";
        }
    }

    public String error() {
        if (json.has("error")) {
            return json.get("error").getAsString();
        } else {
            return "";
        }
    }

    public int totalResults() {
        if (json.has("totalResults")) {
            return json.get("totalResults").getAsInt();
        } else {
            return -1;
        }
    }

    public JsonArray items() {
        if (!json.has("items")) {
            return null;
        }

        JsonArray items;
        try {
            items = json.get("items").getAsJsonArray();
        } catch (Exception e) {
            items = null;
        }
        return items;
    }
}

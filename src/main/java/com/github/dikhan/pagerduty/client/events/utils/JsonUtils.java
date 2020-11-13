package com.github.dikhan.pagerduty.client.events.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class JsonUtils {

    public static String getPropertyValue(HttpResponse<JsonNode> jsonResponse, String key) {
        if (jsonResponse.getBody().getObject().has(key)) {
            return jsonResponse.getBody().getObject().getString(key);
        }

        return null;
    }

    public static String getArrayValue(HttpResponse<JsonNode> jsonResponse, String key) {
        if (jsonResponse.getBody().getObject().has(key)) {
            return jsonResponse.getBody().getObject().getJSONArray(key).toString();
        }

        return null;
    }
}

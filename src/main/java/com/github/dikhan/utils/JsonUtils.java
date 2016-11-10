package com.github.dikhan.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class JsonUtils {

    public static String getPropertyValue(HttpResponse<JsonNode> jsonResponse, String key) {
        return jsonResponse.getBody().getObject().getString(key);
    }

    public static String getArrayValue(HttpResponse<JsonNode> jsonResponse, String key) {
        return jsonResponse.getBody().getObject().getJSONArray(key).toString();
    }
}

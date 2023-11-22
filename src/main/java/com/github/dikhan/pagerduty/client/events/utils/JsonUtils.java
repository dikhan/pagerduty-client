package com.github.dikhan.pagerduty.client.events.utils;

import org.json.JSONObject;

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
   
   // Helpers for JSONObject
   public static String getPropertyValue(JSONObject jsonResponse, String key) {
      if (jsonResponse.has(key)) {
         return jsonResponse.getString(key);
      }
      return null;
   }

   public static String getArrayValue(JSONObject jsonResponse, String key) {
      if (jsonResponse.has(key)) {
         return jsonResponse.getJSONArray(key).toString();
      }
      return null;
   }
}

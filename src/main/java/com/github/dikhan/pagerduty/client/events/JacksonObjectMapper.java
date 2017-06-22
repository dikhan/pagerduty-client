package com.github.dikhan.pagerduty.client.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.dikhan.utils.JSONObjectSerializer;
import org.json.JSONObject;

import java.io.IOException;

public class JacksonObjectMapper implements com.mashape.unirest.http.ObjectMapper {

    private static final ObjectMapper jacksonObjectMapper = makeObjectMapper();

    private static ObjectMapper makeObjectMapper() {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(JSONObject.class, new JSONObjectSerializer());
        mapper.registerModule(module);

        return mapper;
    }


    public <T> T readValue(String value, Class<T> valueType) {
        try {
            return jacksonObjectMapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String writeValue(Object value) {
        try {
            return jacksonObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

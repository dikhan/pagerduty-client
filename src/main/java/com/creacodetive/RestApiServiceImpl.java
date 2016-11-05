package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RestApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(RestApiServiceImpl.class);

    private final String eventApi;
    private final String apiAuthToken;

    public RestApiServiceImpl(String eventApi, String apiAuthToken) {
        this.eventApi = eventApi;
        this.apiAuthToken = apiAuthToken;
        initUnirest();
    }

    private void initUnirest() {
        ObjectMapper objectMapper = new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

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
        };
        Unirest.setObjectMapper(objectMapper);
    }

    public EventResult notifyEvent(Incident incident) throws NotifyEventException {
        try {
            HttpRequestWithBody request = Unirest.post(eventApi)
                    .header("Accept", "application/json")
                    .header("Authorization", "Token token=" + apiAuthToken);
            request.body(incident);
            HttpResponse<JsonNode> jsonResponse = request.asJson();
            log.debug(IOUtils.toString(jsonResponse.getRawBody()));
            switch(jsonResponse.getStatus()) {
                case HttpStatus.SC_OK:
                    return EventResult.successEvent(getPropertyValue(jsonResponse, "status"), getPropertyValue(jsonResponse, "message"), getPropertyValue(jsonResponse, "incident_key"));
                case HttpStatus.SC_BAD_REQUEST:
                    return EventResult.errorEvent(getPropertyValue(jsonResponse, "status"), getPropertyValue(jsonResponse, "message"), getArrayValue(jsonResponse, "errors"));
                default:
                    return EventResult.errorEvent(String.valueOf(jsonResponse.getStatus()), "", IOUtils.toString(jsonResponse.getRawBody()));
            }
        } catch (UnirestException | IOException e) {
            throw new NotifyEventException(e);
        }
    }

    private String getPropertyValue(HttpResponse<JsonNode> jsonResponse, String key) {
        return jsonResponse.getBody().getObject().getString(key);
    }

    private String getArrayValue(HttpResponse<JsonNode> jsonResponse, String key) {
        return jsonResponse.getBody().getObject().getJSONArray(key).toString();
    }

}

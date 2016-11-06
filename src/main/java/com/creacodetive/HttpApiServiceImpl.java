package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.creacodetive.utils.JsonUtils.getArrayValue;
import static com.creacodetive.utils.JsonUtils.getPropertyValue;

public class HttpApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(HttpApiServiceImpl.class);

    private final String eventApi;
    private final String apiAuthToken;

    public HttpApiServiceImpl(String eventApi, String apiAuthToken) {
        this.eventApi = eventApi;
        this.apiAuthToken = apiAuthToken;
        initUnirest();
    }

    private void initUnirest() {
        Unirest.setObjectMapper(new JacksonObjectMapper());
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

}

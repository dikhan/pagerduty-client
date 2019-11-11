package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.Incident;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import com.github.dikhan.pagerduty.client.events.utils.JsonUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(HttpApiServiceImpl.class);

    private final String eventApi;

    public HttpApiServiceImpl(String eventApi) {
        this.eventApi = eventApi;
        initUnirest();
    }

    public HttpApiServiceImpl(String eventApi, String proxyHost, Integer proxyPort) {
        this.eventApi = eventApi;
        initUnirestWithProxy(proxyHost, proxyPort);
    }

    private void initUnirest() {
        Unirest.setObjectMapper(new JacksonObjectMapper());
    }

    private void initUnirestWithProxy(String proxyHost, Integer proxyPort) {
        initUnirest();
        Unirest.setProxy(new HttpHost(proxyHost, proxyPort));
    }

    public EventResult notifyEvent(Incident incident) throws NotifyEventException {
        try {
            HttpRequestWithBody request = Unirest.post(eventApi)
                    .header("Accept", "application/json");
            request.body(incident);
            HttpResponse<JsonNode> jsonResponse = request.asJson();
            log.debug(IOUtils.toString(jsonResponse.getRawBody()));
            switch(jsonResponse.getStatus()) {
                case HttpStatus.SC_OK:
                case HttpStatus.SC_CREATED:
                case HttpStatus.SC_ACCEPTED:
                    return EventResult.successEvent(JsonUtils.getPropertyValue(jsonResponse, "status"), JsonUtils.getPropertyValue(jsonResponse, "message"), JsonUtils.getPropertyValue(jsonResponse, "dedup_key"));
                case HttpStatus.SC_BAD_REQUEST:
                    return EventResult.errorEvent(JsonUtils.getPropertyValue(jsonResponse, "status"), JsonUtils.getPropertyValue(jsonResponse, "message"), JsonUtils.getArrayValue(jsonResponse, "errors"));
                default:
                    return EventResult.errorEvent(String.valueOf(jsonResponse.getStatus()), "", IOUtils.toString(jsonResponse.getRawBody()));
            }
        } catch (UnirestException | IOException e) {
            throw new NotifyEventException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        HttpApiServiceImpl that = (HttpApiServiceImpl)o;

        return !(eventApi != null ? !eventApi.equals(that.eventApi) : that.eventApi != null);

    }

    @Override
    public int hashCode() {
        return eventApi != null ? eventApi.hashCode() : 0;
    }
}

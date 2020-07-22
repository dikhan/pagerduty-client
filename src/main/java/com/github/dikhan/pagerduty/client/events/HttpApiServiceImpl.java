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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpApiServiceImpl implements ApiService {

    private static final Logger log = LoggerFactory.getLogger(HttpApiServiceImpl.class);

    private static final int RATE_LIMIT_STATUS_CODE = 429;

    // The time between retries for each different result status codes.
    private static final Map<Integer, long[]> RETRY_WAIT_TIME_MILLISECONDS = new HashMap<>();
    static {
        // "quickly" retrying in case of 500s to recover from flapping errors
        RETRY_WAIT_TIME_MILLISECONDS.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, new long[]{500, 1_000, 2_000});
        // "slowly" retrying from Rate Limit to give it time to recover
        RETRY_WAIT_TIME_MILLISECONDS.put(RATE_LIMIT_STATUS_CODE, new long[]{10_000, 25_000, 55_000});
    }

    private final String eventApi;
    private final boolean doRetries;

    public HttpApiServiceImpl(String eventApi, boolean doRetries) {
        this.eventApi = eventApi;
        this.doRetries = doRetries;
        initUnirest();
    }

    public HttpApiServiceImpl(String eventApi, String proxyHost, Integer proxyPort, boolean doRetries) {
        this.eventApi = eventApi;
        this.doRetries = doRetries;
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
        return notifyEvent(incident, 0);
    }

    private EventResult notifyEvent(Incident incident, int retryCount) throws NotifyEventException {
        try {
            HttpRequestWithBody request = Unirest.post(eventApi)
                    .header("Accept", "application/json");
            request.body(incident);
            HttpResponse<JsonNode> jsonResponse = request.asJson();

            if (log.isDebugEnabled()) {
                log.debug(IOUtils.toString(jsonResponse.getRawBody()));
                // A reset, so we can get the contents from the body that were dumped in the log before
                jsonResponse.getRawBody().reset();
            }

            int responseStatus = jsonResponse.getStatus();
            switch(responseStatus) {
                case HttpStatus.SC_OK:
                case HttpStatus.SC_CREATED:
                case HttpStatus.SC_ACCEPTED:
                    return EventResult.successEvent(JsonUtils.getPropertyValue(jsonResponse, "status"), JsonUtils.getPropertyValue(jsonResponse, "message"), JsonUtils.getPropertyValue(jsonResponse, "dedup_key"));
                case HttpStatus.SC_BAD_REQUEST:
                    return EventResult.errorEvent(JsonUtils.getPropertyValue(jsonResponse, "status"), JsonUtils.getPropertyValue(jsonResponse, "message"), JsonUtils.getArrayValue(jsonResponse, "errors"));
                case RATE_LIMIT_STATUS_CODE:
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    if (doRetries) {
                        return handleRetries(incident, retryCount, jsonResponse, responseStatus);
                    } else {
                        return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(jsonResponse.getRawBody()));
                    }
                default:
                    return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(jsonResponse.getRawBody()));
            }
        } catch (UnirestException | IOException e) {
            throw new NotifyEventException(e);
        }
    }

    private EventResult handleRetries(Incident incident, int retryCount, HttpResponse<JsonNode> jsonResponse, int responseStatus) throws IOException, NotifyEventException {
        long[] retryDelays = RETRY_WAIT_TIME_MILLISECONDS.get(responseStatus);

        int maxRetries = retryDelays.length;
        if (retryCount == maxRetries) {
            log.debug("Received a {} response. Exhausted all the possibilities to retry.", responseStatus);
            return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(jsonResponse.getRawBody()));
        }

        log.debug("Received a {} response. Will retry again. ({}/{})", responseStatus, retryCount, maxRetries);

        try {
            Thread.sleep(retryDelays[retryCount]);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return notifyEvent(incident, retryCount + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HttpApiServiceImpl that = (HttpApiServiceImpl) o;

        return doRetries == that.doRetries && Objects.equals(eventApi, that.eventApi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventApi, doRetries);
    }
}
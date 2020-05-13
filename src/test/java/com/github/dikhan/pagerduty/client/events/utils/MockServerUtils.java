package com.github.dikhan.pagerduty.client.events.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.Incident;
import org.apache.http.HttpStatus;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class MockServerUtils {

    private static final String EVENT_END_POINT = "/v2/enqueue";
    private static final int SC_RATE_LIMIT = 429;

    /**
     * Prepare the mock server to receive the given incident and reply with a successful eventResult
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @param eventResult to be returned by the mock server
     * @throws JsonProcessingException
     */
    public static void prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(MockServerClient mockServerClient, Incident incident, EventResult eventResult) throws JsonProcessingException {
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_OK, getSuccessResponseBody(eventResult));
    }

    /**
     * Prepare the mock server to receive the given incident and reply with a accepted eventResult
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @param eventResult to be returned by the mock server
     * @throws JsonProcessingException
     */
    public static void prepareMockServerToReceiveGivenIncidentAndReplyWithAcceptedResponse(MockServerClient mockServerClient, Incident incident, EventResult eventResult) throws JsonProcessingException {
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_ACCEPTED, getSuccessResponseBody(eventResult));
    }

    /**
     * Prepare the mock server to receive the given incident and reply with an error eventResult
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @param eventResult to be returned by the mock server
     * @throws JsonProcessingException
     */
    public static void prepareMockServerToReceiveIncidentAndReplyWithWithErrorResponse(MockServerClient mockServerClient, Incident incident, EventResult eventResult) throws JsonProcessingException {
        String responseBody = "{\"status\":\"" + eventResult.getStatus()
                + "\",\"message\":\"" + eventResult.getMessage()
                + "\",\"errors\":" + eventResult.getErrors() + "}";
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_BAD_REQUEST, responseBody);
    }

    /**
     * Prepare the mock server to receive the given incident and reply with a predefined forbidden response with no content
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @throws JsonProcessingException
     */
    public static void prepareMockServerWithUnexpectedErrorResponse(MockServerClient mockServerClient, Incident incident) throws JsonProcessingException {
        String noContentResponseBody = "{}";
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_FORBIDDEN, noContentResponseBody);
    }

    /**
     * Prepare the mock server to receive the given incident and reply with a predefined internal server error (500) response with no content
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @throws JsonProcessingException
     */
    public static void prepareMockServerWithInternalServerErrorResponse(MockServerClient mockServerClient, Incident incident) throws JsonProcessingException {
        String noContentResponseBody = "{}";
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_INTERNAL_SERVER_ERROR, noContentResponseBody);
    }

    /**
     * Prepare the mock server to receive the given incident and reply with a predefined internal server error (500) response with no content
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @throws JsonProcessingException
     */
    public static void prepareMockServerWithRateLimitResponse(MockServerClient mockServerClient, Incident incident) throws JsonProcessingException {
        String noContentResponseBody = "{}";
        prepareMockServer(mockServerClient, incident, SC_RATE_LIMIT, noContentResponseBody);
    }

    private static void prepareMockServer(MockServerClient mockServerClient, Incident incident, int statusCode, String responseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mockServerClient
                .when(request()
                                .withMethod("POST")
                                .withPath(EVENT_END_POINT)
                                .withBody(exact(mapper.writeValueAsString(incident))),
                        exactly(1))
                .respond(response()
                                .withStatusCode(statusCode)
                                .withHeaders(
                                        new Header("Server", "MockServer"),
                                        new Header("Date", new Date().toString()),
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(responseBody)
                                .withDelay(new Delay(TimeUnit.SECONDS, 1))
                );
    }

    private static String getSuccessResponseBody(EventResult eventResult) {
        return "{\"status\":\"" + eventResult.getStatus()
                + "\",\"message\":\"" + eventResult.getMessage()
                + "\",\"dedup_key\":\"" + eventResult.getDedupKey() + "\"}";
    }
}

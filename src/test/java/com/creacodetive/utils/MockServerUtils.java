package com.creacodetive.utils;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String EVENT_END_POINT = "/generic/2010-04-15/create_event.json";

    /**
     * Prepare the mock server to receive the given incident and reply with a successful eventResult
     * @param mockServerClient mock client to configure the incident/event upon
     * @param incident expected to be received in the mock server from the client
     * @param eventResult to be returned by the mock server
     * @throws JsonProcessingException
     */
    public static void prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(MockServerClient mockServerClient, Incident incident, EventResult eventResult) throws JsonProcessingException {
        String responseBody = "{\"status\":\"" + eventResult.getStatus()
                + "\",\"message\":\"" + eventResult.getMessage()
                + "\",\"incident_key\":\"" + eventResult.getIncidentKey() + "\"}";
        prepareMockServer(mockServerClient, incident, HttpStatus.SC_OK, responseBody);
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
}

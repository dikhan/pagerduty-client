package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.ImageContext;
import com.creacodetive.domain.Incident;
import com.creacodetive.domain.LinkContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class HttpApiServiceImplTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private final String MOCK_PAGER_DUTY_HOSTNAME = "localhost";
    private final int MOCK_PAGER_DUTY_PORT = mockServerRule.getPort();

    private final String EVENT_END_POINT = "/generic/2010-04-15/create_event.json";
    private final String eventApi = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private HttpApiServiceImpl httpApiServiceImpl;

    @Before
    public void setUp() throws UnknownHostException {
        httpApiServiceImpl = new HttpApiServiceImpl(eventApi, "VALID_TOKEN");
    }

    @Test
    public void notifyIncidentEventAndSuccessfulResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareTriggerIncident();
        prepareMockServerWithSuccessfulResponse(incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = successEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAndErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareTriggerIncident();
        prepareMockServerWithErrorResponse(incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = errorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnUnexpectedErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareTriggerIncident();
        prepareMockServerWithUnexpectedErrorResponse(incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = unexpectedErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    private EventResult successEvent() {
        return EventResult.successEvent("success", "Event processed", "5a017e1795a84d6eaf6f6bbf009abc9c");
    }

    private EventResult errorEvent() {
        return EventResult.errorEvent("invalid event", "Event object is invalid", "[\"some error from upstream server...\"]");
    }

    private EventResult unexpectedErrorEvent() {
        return EventResult.errorEvent(String.valueOf(HttpStatus.SC_FORBIDDEN), "", "");
    }

    private Incident prepareTriggerIncident() {
        LinkContext linkContext = new LinkContext("http://link-context.com");
        ImageContext imageContext = new ImageContext("http://image-context.com");
        return Incident.IncidentBuilder
                .trigger("SERVICE_KEY", "HealthCheck failed")
                .client("PagerDutyClientTest")
                .details("Issue details")
                .client_url("http://www.issue-origin.com")
                .contexts(Arrays.asList(linkContext, imageContext))
                .build();
    }

    private void prepareMockServerWithSuccessfulResponse(Incident incident) throws JsonProcessingException {
        String responseBody = "{\"status\":\"" + successEvent().getStatus()
                + "\",\"message\":\"" + successEvent().getMessage()
                + "\",\"incident_key\":\"" + successEvent().getIncidentKey() + "\"}";
        prepareMockServer(incident, HttpStatus.SC_OK, responseBody);
    }

    private void prepareMockServerWithErrorResponse(Incident incident) throws JsonProcessingException {
        String responseBody = "{\"status\":\"" + errorEvent().getStatus()
                + "\",\"message\":\"" + errorEvent().getMessage()
                + "\",\"errors\":" + errorEvent().getErrors() + "}";
        prepareMockServer(incident, HttpStatus.SC_BAD_REQUEST, responseBody);
    }

    private void prepareMockServerWithUnexpectedErrorResponse(Incident incident) throws JsonProcessingException {
        String noContentResponseBody = "{}";
        prepareMockServer(incident, HttpStatus.SC_FORBIDDEN, noContentResponseBody);
    }

    private void prepareMockServer(Incident incident, int statusCode, String responseBody) throws JsonProcessingException {
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
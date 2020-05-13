package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.Incident;
import com.github.dikhan.pagerduty.client.events.utils.EventHelper;
import com.github.dikhan.pagerduty.client.events.utils.MockServerUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.net.UnknownHostException;

import static com.github.dikhan.pagerduty.client.events.utils.IncidentHelper.prepareSampleTriggerIncident;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpApiServiceImplTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private final String MOCK_PAGER_DUTY_HOSTNAME = "localhost";
    private final int MOCK_PAGER_DUTY_PORT = mockServerRule.getPort();

    private final String EVENT_END_POINT = "/v2/enqueue";
    private final String EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private HttpApiServiceImpl httpApiServiceImpl;
    private HttpApiServiceImpl httpApiServiceImplWithRetriesEnabled;

    @Before
    public void setUp() throws UnknownHostException {
        httpApiServiceImpl = new HttpApiServiceImpl(EVENT_API, false);
        httpApiServiceImplWithRetriesEnabled = new HttpApiServiceImpl(EVENT_API, true);
    }

    @After
    public void afterEach() {
        mockServerClient.reset();
    }

    @Test
    public void notifyIncidentEventAndSuccessfulResponseFromUpstreamServer() throws Exception {
        String dedupKey = "DEDUP_KEY";
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(dedupKey));

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.successEvent(dedupKey);

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAndAcceptedResponseFromUpstreamServer() throws Exception {
        String dedupKey = "DEDUP_KEY";
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithAcceptedResponse(mockServerClient, incident,
                        EventHelper.successEvent(dedupKey));

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.successEvent(dedupKey);

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAndErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils.prepareMockServerToReceiveIncidentAndReplyWithWithErrorResponse(mockServerClient, incident,
                EventHelper.errorEvent());

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.errorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnUnexpectedErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils.prepareMockServerWithUnexpectedErrorResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.unexpectedErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }


    @Test
    public void notifyIncidentEventAnServerInternalErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.internalServerErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnServerInternalErrorResponseFromUpstreamServerWithRetries() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");

        // 1 initial request + 3 retries
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(incident);
        EventResult expectedResult = EventHelper.internalServerErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnServerInternalErrorResponseFromUpstreamServerWithRetriesAndRecovery() throws Exception {
        String dedupKey = "DEDUP_KEY";
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");

        // 1 initial request + 2 retry + 1 success
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, incident);
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(dedupKey));

        EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(incident);
        EventResult expectedResult = EventHelper.successEvent(dedupKey);

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnServerRateLimitResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.rateLimitErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnServerRateLimitResponseFromUpstreamServerWithRetries() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");

        // 1 initial request + 3 retries
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(incident);
        EventResult expectedResult = EventHelper.rateLimitErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnServerRateLimitResponseFromUpstreamServerWithRetriesWithRecovery() throws Exception {
        String dedupKey = "DEDUP_KEY";
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");

        // 1 initial request + 1 retry + 1 success
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);
        MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, incident);
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(dedupKey));

        EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(incident);
        EventResult expectedResult = EventHelper.successEvent(dedupKey);

        assertThat(eventResult).isEqualTo(expectedResult);

    }

}
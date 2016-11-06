package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import static com.creacodetive.utils.EventHelper.successEvent;
import static com.creacodetive.utils.IncidentHelper.prepareSampleAcknowledgementIncident;
import static com.creacodetive.utils.IncidentHelper.prepareSampleResolveIncident;
import static com.creacodetive.utils.IncidentHelper.prepareSampleTriggerIncident;
import static com.creacodetive.utils.MockServerUtils.prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;

public class PagerDutyClientTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private final String MOCK_PAGER_DUTY_HOSTNAME = "localhost";
    private final int MOCK_PAGER_DUTY_PORT = mockServerRule.getPort();

    private final String EVENT_END_POINT = "/generic/2010-04-15/create_event.json";
    private final String EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private final String API_KEY = "API_KEY";
    private final String SERVICE_KEY = "SERVICE_KEY";
    private final String INCIDENT_KEY = "INCIDENT_KEY";

    private PagerDutyClient pagerDutyClient;

    @Before
    public void setUp() {
        pagerDutyClient = PagerDutyClient.create(API_KEY, EVENT_API);
    }

    @Test
    public void triggerAlert() throws Exception {
        Incident incident = prepareSampleTriggerIncident(SERVICE_KEY);
        prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident, successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyClient.trigger(incident);
        EventResult expectedEventResult = successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void acknowledgeAlert() throws Exception {
        Incident incident = prepareSampleAcknowledgementIncident(SERVICE_KEY, INCIDENT_KEY);
        prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident, successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyClient.acknowledge(SERVICE_KEY, INCIDENT_KEY);
        EventResult expectedEventResult = successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void resolveAlert() throws Exception {
        Incident incident = prepareSampleResolveIncident(SERVICE_KEY, INCIDENT_KEY);
        prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident, successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyClient.resolve(SERVICE_KEY, INCIDENT_KEY);
        EventResult expectedEventResult = successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

}

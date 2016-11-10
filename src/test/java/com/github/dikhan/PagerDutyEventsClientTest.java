package com.github.dikhan;

import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.utils.EventHelper;
import com.github.dikhan.utils.IncidentHelper;
import com.github.dikhan.utils.MockServerUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import static org.assertj.core.api.Assertions.assertThat;

public class PagerDutyEventsClientTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private final String MOCK_PAGER_DUTY_HOSTNAME = "localhost";
    private final int MOCK_PAGER_DUTY_PORT = mockServerRule.getPort();

    private final String EVENT_END_POINT = "/generic/2010-04-15/create_event.json";
    private final String EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private final String SERVICE_KEY = "SERVICE_KEY";
    private final String INCIDENT_KEY = "INCIDENT_KEY";

    private PagerDutyEventsClient pagerDutyEventsClient;

    @Before
    public void setUp() {
        pagerDutyEventsClient = PagerDutyEventsClient.create(EVENT_API);
    }

    @Test
    public void triggerAlert() throws Exception {
        Incident incident = IncidentHelper.prepareSampleTriggerIncident(SERVICE_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyEventsClient.trigger(incident);
        EventResult expectedEventResult = EventHelper.successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void acknowledgeAlert() throws Exception {
        Incident incident = IncidentHelper.prepareSampleAcknowledgementIncident(SERVICE_KEY, INCIDENT_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyEventsClient.acknowledge(SERVICE_KEY, INCIDENT_KEY);
        EventResult expectedEventResult = EventHelper.successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void resolveAlert() throws Exception {
        Incident incident = IncidentHelper.prepareSampleResolveIncident(SERVICE_KEY, INCIDENT_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(INCIDENT_KEY));

        EventResult eventResult = pagerDutyEventsClient.resolve(SERVICE_KEY, INCIDENT_KEY);
        EventResult expectedEventResult = EventHelper.successEvent(INCIDENT_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

}

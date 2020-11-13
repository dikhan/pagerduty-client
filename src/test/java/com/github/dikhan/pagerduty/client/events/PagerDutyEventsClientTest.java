package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.AcknowledgeIncident;
import com.github.dikhan.pagerduty.client.events.domain.ChangeEvent;
import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.ResolveIncident;
import com.github.dikhan.pagerduty.client.events.domain.TriggerIncident;
import com.github.dikhan.pagerduty.client.events.utils.ChangeEventHelper;
import com.github.dikhan.pagerduty.client.events.utils.EventHelper;
import com.github.dikhan.pagerduty.client.events.utils.MockServerUtils;
import com.github.dikhan.pagerduty.client.events.utils.IncidentHelper;

import org.junit.After;
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

    private final String EVENT_END_POINT = "/v2/enqueue";
    private final String EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private final String CHANGE_END_POINT = "/v2/change/enqueue";
    private final String CHANGE_EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + CHANGE_END_POINT;

    private final String ROUTING_KEY = "ROUTING_KEY";
    private final String DEDUP_KEY = "DEDUP_KEY";

    private PagerDutyEventsClient pagerDutyEventsClient;

    @Before
    public void setUp() {
        pagerDutyEventsClient = PagerDutyEventsClient.create(EVENT_API, CHANGE_EVENT_API);
    }

    @After
    public void afterEach() {
        mockServerClient.reset();
    }

    @Test
    public void triggerAlert() throws Exception {
        TriggerIncident incident = IncidentHelper.prepareSampleTriggerIncident(ROUTING_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(DEDUP_KEY));

        EventResult eventResult = pagerDutyEventsClient.trigger(incident);
        EventResult expectedEventResult = EventHelper.successEvent(DEDUP_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void acknowledgeAlert() throws Exception {
        AcknowledgeIncident ack = IncidentHelper.prepareSampleAcknowledgementIncident(ROUTING_KEY, DEDUP_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, ack,
                        EventHelper.successEvent(DEDUP_KEY));

        EventResult eventResult = pagerDutyEventsClient.acknowledge(ack);
        EventResult expectedEventResult = EventHelper.successEvent(DEDUP_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void resolveAlert() throws Exception {
        ResolveIncident resolveIncident = IncidentHelper.prepareSampleResolveIncident(ROUTING_KEY, DEDUP_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, resolveIncident,
                        EventHelper.successEvent(DEDUP_KEY));

        EventResult eventResult = pagerDutyEventsClient.resolve(resolveIncident);
        EventResult expectedEventResult = EventHelper.successEvent(DEDUP_KEY);
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

    @Test
    public void trackChange() throws Exception {
        ChangeEvent changeEvent = ChangeEventHelper.prepareSampleChangeEvent(ROUTING_KEY);
        MockServerUtils
                .prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, changeEvent,
                        EventHelper.successEvent());

        EventResult eventResult = pagerDutyEventsClient.trackChange(changeEvent);
        EventResult expectedEventResult = EventHelper.successEvent();
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }
}

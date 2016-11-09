package com.github.dikhan;

import static com.github.dikhan.utils.IncidentHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.utils.EventHelper;
import com.github.dikhan.utils.MockServerUtils;

public class HttpApiServiceImplTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;

    private final String MOCK_PAGER_DUTY_HOSTNAME = "localhost";
    private final int MOCK_PAGER_DUTY_PORT = mockServerRule.getPort();

    private final String EVENT_END_POINT = "/generic/2010-04-15/create_event.json";
    private final String EVENT_API = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

    private HttpApiServiceImpl httpApiServiceImpl;

    @Before
    public void setUp() throws UnknownHostException {
        httpApiServiceImpl = new HttpApiServiceImpl(EVENT_API);
    }

    @Test
    public void notifyIncidentEventAndSuccessfulResponseFromUpstreamServer() throws Exception {
        String incidentKey = "INCIDENT_KEY";
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        MockServerUtils
                .prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident,
                        EventHelper.successEvent(incidentKey));

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = EventHelper.successEvent(incidentKey);

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

}
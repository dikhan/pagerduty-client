package com.creacodetive;

import static com.creacodetive.utils.EventHelper.*;
import static com.creacodetive.utils.IncidentHelper.*;
import static com.creacodetive.utils.MockServerUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;

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
        prepareMockServerToReceiveGivenIncidentAndReplyWithSuccessfulResponse(mockServerClient, incident, successEvent(incidentKey));

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = successEvent(incidentKey);

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAndErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        prepareMockServerToReceiveIncidentAndReplyWithWithErrorResponse(mockServerClient, incident, errorEvent());

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = errorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

    @Test
    public void notifyIncidentEventAnUnexpectedErrorResponseFromUpstreamServer() throws Exception {
        Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
        prepareMockServerWithUnexpectedErrorResponse(mockServerClient, incident);

        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        EventResult expectedResult = unexpectedErrorEvent();

        assertThat(eventResult).isEqualTo(expectedResult);
    }

}
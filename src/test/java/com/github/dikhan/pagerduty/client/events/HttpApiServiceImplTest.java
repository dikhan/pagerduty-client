package com.github.dikhan.pagerduty.client.events;

import static com.github.dikhan.pagerduty.client.events.utils.ChangeEventHelper.prepareSampleChangeEvent;
import static com.github.dikhan.pagerduty.client.events.utils.IncidentHelper.prepareSampleTriggerIncident;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;

import com.github.dikhan.pagerduty.client.events.domain.ChangeEvent;
import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.Incident;
import com.github.dikhan.pagerduty.client.events.utils.EventHelper;
import com.github.dikhan.pagerduty.client.events.utils.MockServerUtils;

public class HttpApiServiceImplTest {

   @Rule
   public MockServerRule      mockServerRule           = new MockServerRule(this);
   private MockServerClient   mockServerClient;

   private final String       MOCK_PAGER_DUTY_HOSTNAME = "localhost";
   private final int          MOCK_PAGER_DUTY_PORT     = mockServerRule.getPort();

   private final String       EVENT_END_POINT          = "/v2/enqueue";
   private final String       EVENT_API                = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/" + EVENT_END_POINT;

   private final String       CHANGE_EVENT_END_POINT   = "/v2/change/enqueue";
   private final String       CHANGE_EVENT_API         = "http://" + MOCK_PAGER_DUTY_HOSTNAME + ":" + MOCK_PAGER_DUTY_PORT + "/"
         + CHANGE_EVENT_END_POINT;

   private HttpApiServiceImpl httpApiServiceImpl;
   private HttpApiServiceImpl httpApiServiceImplWithRetriesEnabled;

   @Before
   public void setUp() throws UnknownHostException {
      httpApiServiceImpl = new HttpApiServiceImpl(EVENT_API, CHANGE_EVENT_API, false);
      httpApiServiceImplWithRetriesEnabled = new HttpApiServiceImpl(EVENT_API, CHANGE_EVENT_API, true);
   }

   @After
   public void afterEach() {
      mockServerClient.reset();
   }

   @Test
   public void notifyIncidentEventAndSuccessfulResponseFromUpstreamServer() throws Exception {
      String dedupKey = "DEDUP_KEY";
      Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, incident,
                                                                                         EventHelper.successEvent(dedupKey));

      EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
      EventResult expectedResult = EventHelper.successEvent(dedupKey);

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyIncidentEventAndAcceptedResponseFromUpstreamServer() throws Exception {
      String dedupKey = "DEDUP_KEY";
      Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithAcceptedResponse(mockServerClient, incident,
                                                                                       EventHelper.successEvent(dedupKey));

      EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
      EventResult expectedResult = EventHelper.successEvent(dedupKey);

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyIncidentEventAndErrorResponseFromUpstreamServer() throws Exception {
      Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveEventAndReplyWithWithErrorResponse(mockServerClient, incident, EventHelper.errorEvent());

      EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
      EventResult expectedResult = EventHelper.errorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyIncidentEventAndErrorResponseFromUpstreamServerInvalidRoutingKey() throws Exception {
      Incident incident = prepareSampleTriggerIncident("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveEventAndReplyWithWithErrorResponseInvalidRoutingKey(mockServerClient, incident,
                                                                                                    EventHelper.errorEvent());

      EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
      EventResult expectedResult = EventHelper.errorEventInvalidRoutingKey();

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
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, incident,
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
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, incident,
                                                                                         EventHelper.successEvent(dedupKey));

      EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(incident);
      EventResult expectedResult = EventHelper.successEvent(dedupKey);

      assertThat(eventResult).isEqualTo(expectedResult);

   }

   @Test
   public void notifyChangeEventAndAcceptedResponseFromUpstreamServer() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithAcceptedResponse(mockServerClient, changeEvent, EventHelper.successEvent());

      EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.successEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAndErrorResponseFromUpstreamServer() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");
      MockServerUtils.prepareMockServerToReceiveEventAndReplyWithWithErrorResponse(mockServerClient, changeEvent, EventHelper.errorEvent());

      EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.errorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnUnexpectedErrorResponseFromUpstreamServer() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");
      MockServerUtils.prepareMockServerWithUnexpectedErrorResponse(mockServerClient, changeEvent);

      EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.unexpectedErrorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerInternalErrorResponseFromUpstreamServer() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);

      EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.internalServerErrorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerInternalErrorResponseFromUpstreamServerWithRetries() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");

      // 1 initial request + 3 retries
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);

      EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.internalServerErrorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerInternalErrorResponseFromUpstreamServerWithRetriesAndRecovery() throws Exception {
      String dedupKey = "DEDUP_KEY";
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");

      // 1 initial request + 2 retry + 1 success
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithInternalServerErrorResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, changeEvent, EventHelper.successEvent());

      EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.successEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerRateLimitResponseFromUpstreamServer() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);

      EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.rateLimitErrorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerRateLimitResponseFromUpstreamServerWithRetries() throws Exception {
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");

      // 1 initial request + 3 retries
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);

      EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.rateLimitErrorEvent();

      assertThat(eventResult).isEqualTo(expectedResult);
   }

   @Test
   public void notifyChangeEventAnServerRateLimitResponseFromUpstreamServerWithRetriesWithRecovery() throws Exception {
      String dedupKey = "DEDUP_KEY";
      ChangeEvent changeEvent = prepareSampleChangeEvent("SERVICE_KEY");

      // 1 initial request + 1 retry + 1 success
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerWithRateLimitResponse(mockServerClient, changeEvent);
      MockServerUtils.prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(mockServerClient, changeEvent,
                                                                                         EventHelper.successEvent(dedupKey));

      EventResult eventResult = httpApiServiceImplWithRetriesEnabled.notifyEvent(changeEvent);
      EventResult expectedResult = EventHelper.successEvent(dedupKey);

      assertThat(eventResult).isEqualTo(expectedResult);
   }
}
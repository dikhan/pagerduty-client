package com.github.dikhan.pagerduty.client.events.utils;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dikhan.pagerduty.client.events.domain.ChangeEvent;
import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.PagerDutyEvent;

public class MockServerUtils {

   private static final String CHANGE_EVENT_END_POINT = "/v2/change/enqueue";
   private static final String EVENT_END_POINT        = "/v2/enqueue";
   private static final int    SC_RATE_LIMIT          = 429;

   /**
    * Prepare the mock server to receive the given event and reply with a successful eventResult
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @param eventResult to be returned by the mock server
    * @throws JsonProcessingException
    */
   public static void prepareMockServerToReceiveGivenEventAndReplyWithSuccessfulResponse(MockServerClient mockServerClient, PagerDutyEvent event,
                                                                                         EventResult eventResult) throws JsonProcessingException {
      prepareMockServer(mockServerClient, event, HttpStatus.SC_OK, getSuccessResponseBody(eventResult));
   }

   /**
    * Prepare the mock server to receive the given event and reply with a accepted eventResult
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @param eventResult to be returned by the mock server
    * @throws JsonProcessingException
    */
   public static void prepareMockServerToReceiveGivenEventAndReplyWithAcceptedResponse(MockServerClient mockServerClient, PagerDutyEvent event,
                                                                                       EventResult eventResult) throws JsonProcessingException {
      prepareMockServer(mockServerClient, event, HttpStatus.SC_ACCEPTED, getSuccessResponseBody(eventResult));
   }

   /**
    * Prepare the mock server to receive the given event and reply with an error eventResult
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @param eventResult to be returned by the mock server
    * @throws JsonProcessingException
    */
   public static void prepareMockServerToReceiveEventAndReplyWithWithErrorResponse(MockServerClient mockServerClient, PagerDutyEvent event,
                                                                                   EventResult eventResult) throws JsonProcessingException {
      String responseBody = "{\"status\":\"" + eventResult.getStatus() + "\",\"message\":\"" + eventResult.getMessage() + "\",\"errors\":"
            + eventResult.getErrors() + "}";
      prepareMockServer(mockServerClient, event, HttpStatus.SC_BAD_REQUEST, responseBody);
   }

   /**
    * Prepare the mock server to receive the given event and reply with an error eventResult
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @param eventResult to be returned by the mock server
    * @throws JsonProcessingException
    */
   public static void prepareMockServerToReceiveEventAndReplyWithWithErrorResponseInvalidRoutingKey(MockServerClient mockServerClient,
                                                                                                    PagerDutyEvent event,
                                                                                                    EventResult eventResult) throws JsonProcessingException {
      String responseBody = "Invalid routing key";
      prepareMockServer(mockServerClient, event, HttpStatus.SC_BAD_REQUEST, responseBody);
   }

   /**
    * Prepare the mock server to receive the given event and reply with a predefined forbidden response with no content
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @throws JsonProcessingException
    */
   public static void prepareMockServerWithUnexpectedErrorResponse(MockServerClient mockServerClient,
                                                                   PagerDutyEvent event) throws JsonProcessingException {
      String noContentResponseBody = "{}";
      prepareMockServer(mockServerClient, event, HttpStatus.SC_FORBIDDEN, noContentResponseBody);
   }

   /**
    * Prepare the mock server to receive the given event and reply with a predefined internal server error (500) response with no content
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @throws JsonProcessingException
    */
   public static void prepareMockServerWithInternalServerErrorResponse(MockServerClient mockServerClient,
                                                                       PagerDutyEvent event) throws JsonProcessingException {
      String noContentResponseBody = "{}";
      prepareMockServer(mockServerClient, event, HttpStatus.SC_INTERNAL_SERVER_ERROR, noContentResponseBody);
   }

   /**
    * Prepare the mock server to receive the given event and reply with a predefined internal server error (500) response with no content
    * @param mockServerClient mock client to configure the incident/event upon
    * @param event expected to be received in the mock server from the client
    * @throws JsonProcessingException
    */
   public static void prepareMockServerWithRateLimitResponse(MockServerClient mockServerClient, PagerDutyEvent event) throws JsonProcessingException {
      String noContentResponseBody = "{}";
      prepareMockServer(mockServerClient, event, SC_RATE_LIMIT, noContentResponseBody);
   }

   private static void prepareMockServer(MockServerClient mockServerClient, PagerDutyEvent event, int statusCode,
                                         String responseBody) throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      String endpoint = event instanceof ChangeEvent ? CHANGE_EVENT_END_POINT : EVENT_END_POINT;
      mockServerClient.when(request().withMethod("POST").withPath(endpoint).withBody(exact(mapper.writeValueAsString(event))), exactly(1))
            .respond(response().withStatusCode(statusCode)
                  .withHeaders(new Header("Server", "MockServer"), new Header("Date", new Date().toString()),
                               new Header("Content-Type", "application/json; charset=utf-8"))
                  .withBody(responseBody)
                  .withDelay(new Delay(TimeUnit.SECONDS, 1)));
   }

   private static String getSuccessResponseBody(EventResult eventResult) {
      if (eventResult.getDedupKey() == null) {
         return "{\"status\":\"" + eventResult.getStatus() + "\",\"message\":\"" + eventResult.getMessage() + "\"}";
      }

      return "{\"status\":\"" + eventResult.getStatus() + "\",\"message\":\"" + eventResult.getMessage() + "\",\"dedup_key\":\""
            + eventResult.getDedupKey() + "\"}";
   }
}

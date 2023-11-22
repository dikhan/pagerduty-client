package com.github.dikhan.pagerduty.client.events;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dikhan.pagerduty.client.events.domain.ChangeEvent;
import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.PagerDutyEvent;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import com.github.dikhan.pagerduty.client.events.utils.JsonUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

public class HttpApiServiceImpl implements ApiService {

   private static final Logger               log                          = LoggerFactory.getLogger(HttpApiServiceImpl.class);

   private static final int                  RATE_LIMIT_STATUS_CODE       = 429;

   // The time between retries for each different result status codes.
   private static final Map<Integer, long[]> RETRY_WAIT_TIME_MILLISECONDS = new HashMap<>();
   static {
      // "quickly" retrying in case of 500s to recover from flapping errors
      RETRY_WAIT_TIME_MILLISECONDS.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, new long[] { 500, 1_000, 2_000 });
      // "slowly" retrying from Rate Limit to give it time to recover
      RETRY_WAIT_TIME_MILLISECONDS.put(RATE_LIMIT_STATUS_CODE, new long[] { 10_000, 25_000, 55_000 });
   }

   private final String  eventApi;
   private final String  changeEventApi;
   private final boolean doRetries;

   public HttpApiServiceImpl(String eventApi, String changeEventApi, boolean doRetries) {
      this.eventApi = eventApi;
      this.changeEventApi = changeEventApi;
      this.doRetries = doRetries;
      initUnirest();
   }

   public HttpApiServiceImpl(String eventApi, String changeEventApi, String proxyHost, Integer proxyPort, boolean doRetries) {
      this.eventApi = eventApi;
      this.changeEventApi = changeEventApi;
      this.doRetries = doRetries;
      initUnirestWithProxy(proxyHost, proxyPort);
   }

   private void initUnirest() {
      Unirest.setObjectMapper(new JacksonObjectMapper());
   }

   private void initUnirestWithProxy(String proxyHost, Integer proxyPort) {
      initUnirest();
      Unirest.setProxy(new HttpHost(proxyHost, proxyPort));
   }

   public EventResult notifyEvent(PagerDutyEvent event) throws NotifyEventException {
      if (event instanceof ChangeEvent) {
         return notifyEvent(event, changeEventApi, 0);
      }
      return notifyEvent(event, eventApi, 0);
   }

   private EventResult notifyEvent(PagerDutyEvent event, String api, int retryCount) throws NotifyEventException {
      try {
         HttpRequestWithBody request = Unirest.post(api)
               .header("Content-Type", "application/json") //
               .header("Accept", "application/json");
         request.body(event);
         HttpResponse<String> httpResponse = request.asString();

         if (log.isDebugEnabled()) {
            log.debug(IOUtils.toString(httpResponse.getRawBody()));
            // A reset, so we can get the contents from the body that were dumped in the log before
            httpResponse.getRawBody().reset();
         }

         int responseStatus = httpResponse.getStatus();
         switch (responseStatus) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_CREATED:
            case HttpStatus.SC_ACCEPTED:
               JSONObject jsonBody = new JSONObject(httpResponse.getBody());
               return EventResult.successEvent(JsonUtils.getPropertyValue(jsonBody, "status"),
                                               JsonUtils.getPropertyValue(jsonBody, "message"),
                                               JsonUtils.getPropertyValue(jsonBody, "dedup_key"));
            case HttpStatus.SC_BAD_REQUEST:
               try {
                  jsonBody = new JSONObject(httpResponse.getBody());
                  return EventResult.errorEvent(JsonUtils.getPropertyValue(jsonBody, "status"),
                                                JsonUtils.getPropertyValue(jsonBody, "message"), JsonUtils.getArrayValue(jsonBody, "errors"));
               } catch (JSONException e) {
                  // No Json payload returned
                  return EventResult.errorEvent(httpResponse.getStatusText(), httpResponse.getBody(), IOUtils.toString(httpResponse.getRawBody()));
                  //                 return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(httpResponse.getRawBody()));
               }
            case RATE_LIMIT_STATUS_CODE:
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
               if (doRetries) {
                  return handleRetries(event, api, retryCount, httpResponse, responseStatus);
               } else {
                  return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(httpResponse.getRawBody()));
               }
            default:
               return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(httpResponse.getRawBody()));
         }
      } catch (UnirestException | IOException e) {
         throw new NotifyEventException(e);
      }
   }

   private EventResult handleRetries(PagerDutyEvent event, String api, int retryCount, HttpResponse<String> httpResponse,
                                     int responseStatus) throws IOException, NotifyEventException {
      long[] retryDelays = RETRY_WAIT_TIME_MILLISECONDS.get(responseStatus);

      int maxRetries = retryDelays.length;
      if (retryCount == maxRetries) {
         log.debug("Received a {} response. Exhausted all the possibilities to retry.", responseStatus);
         return EventResult.errorEvent(String.valueOf(responseStatus), "", IOUtils.toString(httpResponse.getRawBody()));
      }

      log.debug("Received a {} response. Will retry again. ({}/{})", responseStatus, retryCount, maxRetries);

      try {
         Thread.sleep(retryDelays[retryCount]);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }

      return notifyEvent(event, api, retryCount + 1);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      HttpApiServiceImpl that = (HttpApiServiceImpl) o;

      return doRetries == that.doRetries && Objects.equals(eventApi, that.eventApi) && Objects.equals(changeEventApi, that.changeEventApi);
   }

   @Override
   public int hashCode() {
      return Objects.hash(eventApi, changeEventApi, doRetries);
   }
}
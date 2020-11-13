package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.*;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagerDutyEventsClient {

    private static final Logger log = LoggerFactory.getLogger(PagerDutyEventsClient.class);

    private final ApiService httpApiServiceImpl;

    protected PagerDutyEventsClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        String eventApi = pagerDutyClientBuilder.getEventApi();
        String changeEventApi = pagerDutyClientBuilder.getChangeEventApi();
        String proxyHost = pagerDutyClientBuilder.getProxyHost();
        Integer proxyPort = pagerDutyClientBuilder.getProxyPort();
        boolean doRetries = pagerDutyClientBuilder.getDoRetries();
        this.httpApiServiceImpl = new ApiServiceFactory(eventApi, changeEventApi, proxyHost, proxyPort, doRetries).getDefault();
    }

    public static void main(String[] args) throws NotifyEventException {
        String routingKey = "ROUTING_KEY";
        String dedupKey = "DEDUP_KEY";

        PagerDutyEventsClient pagerDutyEventsClient = create();

        JSONObject customDetails = new JSONObject();
        customDetails.put("field", "value1");
        customDetails.put("field2", "value2");

        Payload payload = Payload.Builder.newBuilder()
                .setSummary("This is an incident test to test PagerDutyEventsClient")
                .setSource("testing host")
                .setSeverity(Severity.INFO)
                .setTimestamp(OffsetDateTime.now())
                .setCustomDetails(customDetails)
                .build();

        List<ImageContext> imageContextList = new ArrayList<>();
        imageContextList.add(new ImageContext("src1"));
        List<LinkContext> linkContextList = new ArrayList<>();
        linkContextList.add(new LinkContext("href", "text"));

        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder
                .newBuilder(routingKey, payload)
                .setDedupKey(dedupKey)
                .setClient("client")
                .setClientUrl("https://monitoring.example.com")
                .setLinks(linkContextList)
                .setImages(imageContextList)
                .build();

        pagerDutyEventsClient.trigger(incident);

        AcknowledgeIncident ack = AcknowledgeIncident.AcknowledgeIncidentBuilder
                .newBuilder(routingKey, dedupKey)
                .build();
        pagerDutyEventsClient.acknowledge(ack);

        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder
                .newBuilder(routingKey, dedupKey)
                .build();
        pagerDutyEventsClient.resolve(resolve);

        ChangeEventPayload changeEventPayload = ChangeEventPayload.Builder.newBuilder()
                .setSummary("This is an change event test to test PagerDutyEventsClient")
                .setSource("testing host")
                .setTimestamp(OffsetDateTime.now())
                .setCustomDetails(customDetails)
                .build();

        ChangeEvent changeEvent = ChangeEvent.ChangeEventBuilder
                .newBuilder(routingKey, changeEventPayload)
                .setLinks(linkContextList)
                .build();

        pagerDutyEventsClient.trackChange(changeEvent);
    }

    /**
     * Helper method to newBuilder a PagerDuty Events Client. Note that an ApiAccess key is not needed in order to make
     * requests to PagerDuty Events API, only integration keys are needed (specified in the Incidents) which target the
     * service where the incident will be created.
     * For more information about the difference between PagerDuty APIs (REST API vs Events API) refer
     * to:
     *
     * @return PagerDuty client which allows interaction with the service via PagerDuty Events API
     * @see <a href="https://support.pagerduty.com/hc/en-us/articles/214794907-What-is-the-difference-between-PagerDuty-APIs-">What is the difference between PagerDuty APIs?</a>
     */
    public static PagerDutyEventsClient create() {
        return create(false);
    }

    /**
     * Helper method to newBuilder a PagerDuty Events Client that allows for failing requests to be retried. Note that an ApiAccess key is not needed in order to make
     * requests to PagerDuty Events API, only integration keys are needed (specified in the Incidents) which target the
     * service where the incident will be created.
     * For more information about the difference between PagerDuty APIs (REST API vs Events API) refer
     * to:
     *
     * @return PagerDuty client which allows interaction with the service via PagerDuty Events API
     * @see <a href="https://support.pagerduty.com/hc/en-us/articles/214794907-What-is-the-difference-between-PagerDuty-APIs-">What is the difference between PagerDuty APIs?</a>
     */
    public static PagerDutyEventsClient create(boolean doRetries) {
        return new PagerDutyClientBuilder().withDoRetries(doRetries).build();
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with a specific event api definition.
     *
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String eventApi) {
        return create(eventApi, false);
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with specific event api definition and allows for failing requests to be retried.
     *
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String eventApi, boolean doRetries) {
        return new PagerDutyClientBuilder().withEventApi(eventApi).withDoRetries(doRetries).build();
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with a specific event api and change event api definition.
     *
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @param changeEventApi Url of the end point to post change events. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String eventApi, String changeEventApi) {
        return create(eventApi, changeEventApi, false);
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with a specific event api and change event api definition.
     *
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @param changeEventApi Url of the end point to post change events. This method should only be used for testing purposes as
     *                 event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String eventApi, String changeEventApi, boolean doRetries) {
        return new PagerDutyClientBuilder().withEventApi(eventApi).withChangeEventApi(changeEventApi).withDoRetries(doRetries).build();
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with specific proxy configuration
     *
     * @param proxyHost Host of the configured proxy used by the PagerDuty client
     * @param proxyPort Port of the configured proxy used by the PagerDuty client
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String proxyHost, Integer proxyPort) {
        return create(proxyHost, proxyPort, false);
    }

    /**
     * Simple helper method to newBuilder a PagerDuty client with specific proxy configuration and allows for failing requests to be retried.
     *
     * @param proxyHost Host of the configured proxy used by the PagerDuty client
     * @param proxyPort Port of the configured proxy used by the PagerDuty client
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String proxyHost, Integer proxyPort, boolean doRetries) {
        return new PagerDutyClientBuilder().withProxyHost(proxyHost).withProxyPort(proxyPort).withDoRetries(doRetries).build();
    }


    public EventResult trigger(TriggerIncident incident) throws NotifyEventException {
        EventResult eventResult = sendEvent(incident);
        return eventResult;
    }

    public EventResult acknowledge(AcknowledgeIncident ack) throws NotifyEventException {
        EventResult eventResult = sendEvent(ack);
        return eventResult;
    }

    public EventResult resolve(ResolveIncident resolve) throws NotifyEventException {
        EventResult eventResult = sendEvent(resolve);
        return eventResult;
    }

    public EventResult trackChange(ChangeEvent changeEvent) throws NotifyEventException {
        EventResult eventResult = sendEvent(changeEvent);
        return eventResult;
    }

    private EventResult sendEvent(Incident incident) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        log.debug("Event result {} for {}", eventResult, incident);
        return eventResult;
    }

    private EventResult sendEvent(ChangeEvent changeEvent) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(changeEvent);
        log.debug("Event result {} for {}", eventResult, changeEvent);
        return eventResult;
    }

    protected static class PagerDutyClientBuilder {

        private static final String PAGER_DUTY_EVENT_API = "https://events.pagerduty.com/v2/enqueue";

        private static final String PAGER_DUTY_CHANGE_EVENT_API = "https://events.pagerduty.com/v2/change/enqueue";

        private String eventApi;
        private String changeEventApi;

        private String proxyHost;
        private Integer proxyPort;

        private Boolean doRetries = false;

        public PagerDutyClientBuilder() {
        }

        public PagerDutyClientBuilder withEventApi(String eventApi) {
            this.eventApi = eventApi;
            return this;
        }

        public PagerDutyClientBuilder withChangeEventApi(String changeEventApi) {
            this.changeEventApi = changeEventApi;
            return this;
        }

        public PagerDutyClientBuilder withProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public PagerDutyClientBuilder withProxyPort(Integer proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        public PagerDutyClientBuilder withDoRetries(boolean doRetries) {
            this.doRetries = doRetries;
            return this;
        }

        public PagerDutyEventsClient build() {
            if (StringUtils.isBlank(eventApi)) {
                eventApi = PAGER_DUTY_EVENT_API;
            }

            if (StringUtils.isBlank(changeEventApi)) {
                changeEventApi = PAGER_DUTY_CHANGE_EVENT_API;
            }

            return new PagerDutyEventsClient(this);
        }

        public String getEventApi() {
            return eventApi;
        }

        public String getChangeEventApi() {
            return changeEventApi;
        }

        public String getProxyHost() {
            return proxyHost;
        }

        public Integer getProxyPort() {
            return proxyPort;
        }

        public Boolean getDoRetries() {
            return doRetries;
        }
    }
}

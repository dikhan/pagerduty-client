package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagerDutyClient {

    private static final Logger log = LoggerFactory.getLogger(PagerDutyClient.class);

    private final String eventApi;
    private final String apiAccessKey;
    private final ApiService httpApiServiceImpl;

    private PagerDutyClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        this.apiAccessKey = pagerDutyClientBuilder.getApiAuthToken();
        this.eventApi = pagerDutyClientBuilder.getEventApi();
        this.httpApiServiceImpl = new ApiServiceFactory(eventApi, apiAccessKey).getDefault();
    }

    public static void main(String[] args) throws NotifyEventException {
        PagerDutyClient pagerDutyClient = create("API_AUTH_TOKEN");
        Incident incident = Incident.IncidentBuilder
                .trigger("SERVICE_KEY", "Incident Test")
                .client("Creacodetive - PagerDutyClient")
                .clientUrl("http://www.creacodetive.com")
                .details("This is an incident test to test PagerDutyClient")
                .build();
        pagerDutyClient.trigger(incident);
    }

    /**
     * Helper method to create a PagerDuty client
     * @param apiAccessKey API Access Key which should be  generated via 'Api Access' menu in PagerDuty
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyClient create(String apiAccessKey) {
        return new PagerDutyClientBuilder(apiAccessKey).build();
    }

    /**
     * Simple helper method to create a PagerDuty client with specific event api definition.
     * @param apiAccessKey API Access Key which should be generated via 'Api Access' menu in PagerDuty
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing
     *                 purposes as event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyClient create(String apiAccessKey, String eventApi) {
        return new PagerDutyClientBuilder(apiAccessKey).withEventApi(eventApi).build();
    }

    public EventResult trigger(Incident incident) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        log.debug("Event result {}", eventResult);
        return eventResult;
    }

    public EventResult acknowledge(String serviceKey, String incidentKey) throws NotifyEventException {
        Incident ack = Incident.IncidentBuilder.acknowledge(serviceKey, incidentKey);
        EventResult eventResult = httpApiServiceImpl.notifyEvent(ack);
        log.debug("Event result {} for acknowledge incident {}", eventResult, ack);
        return eventResult;
    }

    public EventResult resolve(String serviceKey, String incidentKey) throws NotifyEventException {
        Incident resolve = Incident.IncidentBuilder.resolve(serviceKey, incidentKey);
        EventResult eventResult = httpApiServiceImpl.notifyEvent(resolve);
        log.debug("Event result {} for resolve incident {}", eventResult, resolve);
        return eventResult;
    }

    private static class PagerDutyClientBuilder {

        private static final String PAGER_DUTY_EVENT_API = "https://events.pagerduty.com/generic/2010-04-15/create_event.json";

        private final String apiAuthToken;
        private String eventApi;

        public PagerDutyClientBuilder(String apiAuthToken) {
            this.apiAuthToken = apiAuthToken;
        }

        public PagerDutyClientBuilder withEventApi(String eventApi) {
            this.eventApi = eventApi;
            return this;
        }

        public PagerDutyClient build() {
            if(StringUtils.isBlank(eventApi)) {
                eventApi = PAGER_DUTY_EVENT_API;
            }
            return new PagerDutyClient(this);
        }

        public String getApiAuthToken() {
            return apiAuthToken;
        }

        public String getEventApi() {
            return eventApi;
        }
    }
}

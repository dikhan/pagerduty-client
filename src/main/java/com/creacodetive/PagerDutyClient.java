package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;
import org.apache.commons.lang3.StringUtils;

public class PagerDutyClient {

    private final String eventApi;
    private final String apiAccessKey;
    private final RestApiServiceImpl restApiServiceImpl;

    private PagerDutyClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        this.apiAccessKey = pagerDutyClientBuilder.getApiAuthToken();
        this.eventApi = pagerDutyClientBuilder.getEventApi();
        this.restApiServiceImpl = new RestApiServiceImpl(eventApi, apiAccessKey);
    }

    public static void main(String[] args) throws NotifyEventException {
        PagerDutyClient pagerDutyClient = create("hb9NvfYMpYFx22ugWv9a");
        Incident incident = Incident.IncidentBuilder
                .trigger("3125909d661a4591b72fc586b3647ecc_", "Incident Test")
                .client("Creacodetive - PagerDutyClient")
                .client_url("http://www.creacodetive.com")
                .details("This is an incident test to test PagerDutyClient")
                .build();
        pagerDutyClient.trigger(incident);
    }

    /**
     * Simple helper method to create a PagerDuty client
     * @param apiAccessKey API Access Key which should be  generated via 'Api Access' menu in PagerDuty
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyClient create(String apiAccessKey) {
        return new PagerDutyClientBuilder(apiAccessKey).build();
    }

    /**
     * Simple helper method to create a PagerDuty client with specific event api definition.
     * @param apiAuthToken API Access Key which should be generated via 'Api Access' menu in PagerDuty
     * @param eventApi Url of the end point to post notifications. This method should only be used for testing
     *                 purposes as event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyClient create(String apiAuthToken, String eventApi) {
        return new PagerDutyClientBuilder(apiAuthToken).withEventApi(eventApi).build();
    }

    public EventResult trigger(Incident incident) throws NotifyEventException {
        return restApiServiceImpl.notifyEvent(incident);
    }

    private static class PagerDutyClientBuilder {

        private static final String EVENT_API = "https://events.pagerduty.com/generic/2010-04-15/create_event.json";

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
                eventApi = EVENT_API;
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

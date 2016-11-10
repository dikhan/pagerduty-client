package com.github.dikhan;

import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.exceptions.NotifyEventException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagerDutyEventsClient {

    private static final Logger log = LoggerFactory.getLogger(PagerDutyEventsClient.class);

    private final ApiService httpApiServiceImpl;

    private PagerDutyEventsClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        String eventApi = pagerDutyClientBuilder.getEventApi();
        this.httpApiServiceImpl = new ApiServiceFactory(eventApi).getDefault();
    }

    public static void main(String[] args) throws NotifyEventException {
        PagerDutyEventsClient pagerDutyEventsClient = create();
        Incident incident = Incident.IncidentBuilder.trigger("SERVICE_KEY", "Incident Test")
                .client("Creacodetive - PagerDutyEventsClient").clientUrl("http://www.creacodetive.com")
                .details("This is an incident test to test PagerDutyEventsClient").build();
        pagerDutyEventsClient.trigger(incident);
    }

    /**
     * Helper method to create a PagerDuty Events Client. Note that an ApiAccess key is not needed in order to make
     * requests to PagerDuty Events API, only integration keys are needed (specified in the Incidents) which target the
     * service where the incident will be created.
     * For more information about the difference between PagerDuty APIs (REST API vs Events API) refer
     * to:
     * @see <a href="https://support.pagerduty.com/hc/en-us/articles/214794907-What-is-the-difference-between-PagerDuty-APIs-">What is the difference between PagerDuty APIs?</a>
     * 
     * @return PagerDuty client which allows interaction with the service via PagerDuty Events API
     */
    public static PagerDutyEventsClient create() {
        return new PagerDutyClientBuilder().build();
    }

    /**
     * Simple helper method to create a PagerDuty client with specific event api definition.
     *
     * @param eventApi
     *            Url of the end point to post notifications. This method should only be used for testing purposes as
     *            event should always be sent to events.pagerduty.com
     * @return PagerDuty client which allows interaction with the service via API calls
     */
    public static PagerDutyEventsClient create(String eventApi) {
        return new PagerDutyClientBuilder().withEventApi(eventApi).build();
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

        private String eventApi;

        public PagerDutyClientBuilder() {
        }

        public PagerDutyClientBuilder withEventApi(String eventApi) {
            this.eventApi = eventApi;
            return this;
        }

        public PagerDutyEventsClient build() {
            if (StringUtils.isBlank(eventApi)) {
                eventApi = PAGER_DUTY_EVENT_API;
            }
            return new PagerDutyEventsClient(this);
        }

        public String getEventApi() {
            return eventApi;
        }
    }
}

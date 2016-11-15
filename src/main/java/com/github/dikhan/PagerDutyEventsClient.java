package com.github.dikhan;

import java.util.Arrays;

import com.github.dikhan.domain.ImageContext;
import com.github.dikhan.domain.LinkContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dikhan.domain.AcknowledgeIncident;
import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.ResolveIncident;
import com.github.dikhan.domain.TriggerIncident;
import com.github.dikhan.exceptions.NotifyEventException;

public class PagerDutyEventsClient {

    private static final Logger log = LoggerFactory.getLogger(PagerDutyEventsClient.class);

    private final ApiService httpApiServiceImpl;

    protected PagerDutyEventsClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        String eventApi = pagerDutyClientBuilder.getEventApi();
        this.httpApiServiceImpl = new ApiServiceFactory(eventApi).getDefault();
    }

    public static void main(String[] args) throws NotifyEventException {
        PagerDutyEventsClient pagerDutyEventsClient = create();
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder
                .create("SERVICE_KEY", "Incident Test")
                .client("Creacodetive - PagerDutyEventsClient")
                .clientUrl("http://www.creacodetive.com")
                .details("This is an incident test to test PagerDutyEventsClient")
                .contexts(Arrays.asList(new ImageContext("http://src.com"), new LinkContext("http://href.com")))
                .build();
        pagerDutyEventsClient.trigger(incident);

        AcknowledgeIncident ack = AcknowledgeIncident.AcknowledgeIncidentBuilder
                .create("SERVICE_KEY", "INCIDENT_KEY")
                .build();
        pagerDutyEventsClient.acknowledge(ack);

        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder
                .create("SERVICE_KEY", "INCIDENT_KEY")
                .details("Resolving - This is an incident test to test PagerDutyEventsClient")
                .description("Resolving description").build();
        pagerDutyEventsClient.resolve(resolve);
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

    public EventResult trigger(TriggerIncident incident) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(incident);
        log.debug("Event result {}", eventResult);
        return eventResult;
    }

    public EventResult acknowledge(AcknowledgeIncident ack) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(ack);
        log.debug("Event result {} for acknowledge incident {}", eventResult, ack);
        return eventResult;
    }

    public EventResult resolve(ResolveIncident resolve) throws NotifyEventException {
        EventResult eventResult = httpApiServiceImpl.notifyEvent(resolve);
        log.debug("Event result {} for resolve incident {}", eventResult, resolve);
        return eventResult;
    }

    protected static class PagerDutyClientBuilder {

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

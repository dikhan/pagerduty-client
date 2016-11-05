package com.creacodetive.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Incident {

    @JsonProperty("service_key")
    private final String serviceKey;
    @JsonProperty("event_type")
    private final EventType eventType;
    @JsonProperty("incident_key")
    private final String incidentKey;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("details")
    private final String details;
    @JsonProperty("client")
    private final String client;
    @JsonProperty("client_url")
    private final String clientUrl;
    @JsonProperty("contexts")
    private List<Context> contexts;

    private Incident(IncidentBuilder builder) {
        this.serviceKey = builder.service_key;
        this.eventType = builder.event_type;
        this.incidentKey = builder.incident_key;
        this.description = builder.description;
        this.details = builder.details;
        this.client = builder.client;
        this.clientUrl = builder.client_url;
        this.contexts = builder.contexts;
    }

    public static class IncidentBuilder {
        private final String service_key;
        private final EventType event_type;
        private final String description;
        private String incident_key;
        private String details;
        private String client;
        private String client_url;
        private List<Context> contexts;

        /**
         * Builder which helps constructing new incident instances
         *
         * @param service_key The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
                * service detail page.
         * @param event_type The type of event. Can be trigger, acknowledge or resolve.
         * @param description Text that will appear in the incident's log associated with this event. Required for trigger events.
         */
        private IncidentBuilder(String service_key, EventType event_type, String description) {
            this.service_key = service_key;
            this.event_type = event_type;
            this.description = description;
        }

        public static IncidentBuilder trigger(String service_key, String description) {
            return new IncidentBuilder(service_key, EventType.TRIGGER, description);
        }

        public static IncidentBuilder acknowledge(String service_key, String description) {
            return new IncidentBuilder(service_key, EventType.ACKNOWLEDGE, description);
        }

        public static IncidentBuilder resolve(String service_key, String description) {
            return new IncidentBuilder(service_key, EventType.RESOLVE, description);
        }

        /**
         * Identifies the incident to trigger, acknowledge, or resolve. Required unless the eventType is trigger.
         */
        public IncidentBuilder incident_key(String incident_key) {
            this.incident_key = incident_key;
            return this;
        }

        /**
         * An arbitrary JSON object containing any data you'd like included in the incident log.
         */
        public IncidentBuilder details(String details) {
            this.details = details;
            return this;
        }

        /**
         * The name of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         */
        public IncidentBuilder client(String client) {
            this.client = client;
            return this;
        }

        /**
         * The URL of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         */
        public IncidentBuilder client_url(String client_url) {
            this.client_url = client_url;
            return this;
        }

        /**
         * Contexts to be included with the incident trigger such as links to graphs or images. (This field is only used for trigger events.)
         */
        public IncidentBuilder contexts(List<Context> contexts) {
            this.contexts = contexts;
            return this;
        }

        public Incident build() {
            return new Incident(this);
        }
    }
}

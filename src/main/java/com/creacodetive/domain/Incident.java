package com.creacodetive.domain;

import java.util.List;

public class Incident {

    /**
     * The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
     * service detail page.
     */
    private final String service_key;

    /**
     * The type of event. Can be trigger, acknowledge or resolve.
     */
    private final EventType event_type;

    /**
     * Identifies the incident to trigger, acknowledge, or resolve. Required unless the event_type is trigger.
     */
    private final String incident_key;

    /**
     * Text that will appear in the incident's log associated with this event. Required for trigger events.
     */
    private final String description;

    /**
     * An arbitrary JSON object containing any data you'd like included in the incident log.
     */
    private final String details;

    /**
     * The name of the monitoring client that is triggering this event. (This field is only used for trigger events.)
     */
    private final String client;

    /**
     * The URL of the monitoring client that is triggering this event. (This field is only used for trigger events.)
     */
    private final String client_url;

    /**
     * Contexts to be included with the incident trigger such as links to graphs or images. (This field is only used for trigger events.)
     */
    private List<Context> contexts;


    private Incident(IncidentBuilder builder) {
        this.service_key = builder.service_key;
        this.event_type = builder.event_type;
        this.incident_key = builder.incident_key;
        this.description = builder.description;
        this.details = builder.details;
        this.client = builder.client;
        this.client_url = builder.client_url;
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

        public IncidentBuilder incident_key(String incident_key) {
            this.incident_key = incident_key;
            return this;
        }

        public IncidentBuilder details(String details) {
            this.details = details;
            return this;
        }

        public IncidentBuilder client(String client) {
            this.client = client;
            return this;
        }

        public IncidentBuilder client_url(String client_url) {
            this.client_url = client_url;
            return this;
        }

        public IncidentBuilder contexts(List<Context> contexts) {
            this.contexts = contexts;
            return this;
        }

        public Incident build() {
            return new Incident(this);
        }
    }
}

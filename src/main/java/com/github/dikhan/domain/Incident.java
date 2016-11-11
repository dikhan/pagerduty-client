package com.github.dikhan.domain;

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
        this.serviceKey = builder.getServiceKey();
        this.eventType = builder.getEventType();
        this.incidentKey = builder.getIncidentKey();
        this.description = builder.getDescription();
        this.details = builder.getDetails();
        this.client = builder.getClient();
        this.clientUrl = builder.getClientUrl();
        this.contexts = builder.getContexts();
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public String getClient() {
        return client;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public static class IncidentBuilder {
        private final String serviceKey;
        private final EventType eventType;
        private String description;
        private String incidentKey;
        private String details;
        private String client;
        private String clientUrl;
        private List<Context> contexts;

        /**
         * Builder which helps constructing new incident instances
         *
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         * service detail page.
         * @param eventType The type of event. Can be trigger, acknowledge or resolve.
         */
        private IncidentBuilder(String serviceKey, EventType eventType) {
            this.serviceKey = serviceKey;
            this.eventType = eventType;
        }

        /**
         * Method to create a new incident of type trigger
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param description Text that will appear in the incident's log associated with this event. Required for trigger events.
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static IncidentBuilder trigger(String serviceKey, String description) {
            return new IncidentBuilder(serviceKey, EventType.TRIGGER).description(description);
        }

        /**
         * Method to create a new incident of type acknowledge
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param incidentKey Identifies the incident to acknowledge
         * @return Incident of type acknowledge
         */
        public static Incident acknowledge(String serviceKey, String incidentKey) {
            return new IncidentBuilder(serviceKey, EventType.ACKNOWLEDGE).incidentKey(incidentKey).build();
        }

        /**
         * Method to create a new incident of type resolve
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param incidentKey Identifies the incident to resolve
         * @return Incident of type resolve
         */
        public static Incident resolve(String serviceKey, String incidentKey) {
            return new IncidentBuilder(serviceKey, EventType.RESOLVE).incidentKey(incidentKey).build();
        }

        /**
         * @param description Text that will appear in the incident's log associated with this event. Required for trigger events.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        private IncidentBuilder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * @param incidentKey Identifies the incident to trigger, acknowledge, or resolve. Required unless the eventType is trigger. If
         * event is trigger and the incidentKey is left empty then PagerDuty will auto-generate one in the fly when
         * saving the new incident.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public IncidentBuilder incidentKey(String incidentKey) {
            this.incidentKey = incidentKey;
            return this;
        }

        /**
         * @param details An arbitrary JSON object containing any data you'd like included in the incident log.
         * @return IncidentBuilder with details field populated to be able to keep populating the instance
         */
        public IncidentBuilder details(String details) {
            this.details = details;
            return this;
        }

        /**
         * @param client The name of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with client field populated to be able to keep populating the instance
         */
        public IncidentBuilder client(String client) {
            this.client = client;
            return this;
        }

        /**
         * @param clientUrl The URL of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with clientUrl field populated to be able to keep populating the instance
         */
        public IncidentBuilder clientUrl(String clientUrl) {
            this.clientUrl = clientUrl;
            return this;
        }

        /**
         * @param contexts Contexts to be included with the incident trigger such as links to graphs or images. (This field is only used for trigger events.)
         * @return IncidentBuilder with contexts field populated to be able to keep populating the instance
         */
        public IncidentBuilder contexts(List<Context> contexts) {
            this.contexts = contexts;
            return this;
        }

        public Incident build() {
            return new Incident(this);
        }

        public String getServiceKey() {
            return serviceKey;
        }

        public EventType getEventType() {
            return eventType;
        }

        public String getDescription() {
            return description;
        }

        public String getIncidentKey() {
            return incidentKey;
        }

        public String getDetails() {
            return details;
        }

        public String getClient() {
            return client;
        }

        public String getClientUrl() {
            return clientUrl;
        }

        public List<Context> getContexts() {
            return contexts;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Incident incident = (Incident)o;

        if (serviceKey != null ? !serviceKey.equals(incident.serviceKey) : incident.serviceKey != null)
            return false;
        if (eventType != incident.eventType)
            return false;
        if (incidentKey != null ? !incidentKey.equals(incident.incidentKey) : incident.incidentKey != null)
            return false;
        if (description != null ? !description.equals(incident.description) : incident.description != null)
            return false;
        if (details != null ? !details.equals(incident.details) : incident.details != null)
            return false;
        if (client != null ? !client.equals(incident.client) : incident.client != null)
            return false;
        if (clientUrl != null ? !clientUrl.equals(incident.clientUrl) : incident.clientUrl != null)
            return false;
        return !(contexts != null ? !contexts.equals(incident.contexts) : incident.contexts != null);

    }

    @Override
    public int hashCode() {
        int result = serviceKey != null ? serviceKey.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (incidentKey != null ? incidentKey.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (clientUrl != null ? clientUrl.hashCode() : 0);
        result = 31 * result + (contexts != null ? contexts.hashCode() : 0);
        return result;
    }
}

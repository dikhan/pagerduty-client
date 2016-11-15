package com.github.dikhan.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    protected Incident(IncidentBuilder builder) {
        this.serviceKey = builder.getServiceKey();
        this.eventType = builder.getEventType();
        this.incidentKey = builder.getIncidentKey();
        this.description = builder.getDescription();
        this.details = builder.getDetails();
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

    protected static class IncidentBuilder<T extends IncidentBuilder> {
        private final String serviceKey;
        private final EventType eventType;
        private String description;
        private String incidentKey;
        private String details;

        /**
         * Builder which helps constructing new incident instances
         *
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         * service detail page.
         * @param eventType The type of event. Can be trigger, acknowledge or resolve.
         */
        protected IncidentBuilder(String serviceKey, EventType eventType) {
            Objects.requireNonNull(serviceKey, "serviceKey must not be null, it is a mandatory param");
            Objects.requireNonNull(eventType, "eventType must not be null, it is a mandatory param");
            this.serviceKey = serviceKey;
            this.eventType = eventType;
        }

        /**
         * @param description Text that will appear in the incident's log associated with this event. Required for trigger events.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public T description(String description) {
            this.description = description;
            return (T)this;
        }

        /**
         * @param incidentKey Identifies the incident to trigger, acknowledge, or resolve. Required unless the eventType is trigger. If
         * event is trigger and the incidentKey is left empty then PagerDuty will auto-generate one in the fly when
         * saving the new incident.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public T incidentKey(String incidentKey) {
            this.incidentKey = incidentKey;
            return (T)this;
        }

        /**
         * @param details An arbitrary JSON object containing any data you'd like included in the incident log.
         * @return IncidentBuilder with details field populated to be able to keep populating the instance
         */
        public T details(String details) {
            this.details = details;
            return (T)this;
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
        return true;
    }

    @Override
    public int hashCode() {
        int result = serviceKey != null ? serviceKey.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (incidentKey != null ? incidentKey.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }
}

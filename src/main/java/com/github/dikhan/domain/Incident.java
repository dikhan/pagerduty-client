package com.github.dikhan.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Builder;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Incident {
    @JsonProperty("routing_key")
    private final String routingKey;
    @JsonProperty("event_action")
    private final EventType eventType;
    @JsonProperty("dedup_key")
    private final String dedupKey;
    @JsonProperty("payload")
    private final Payload payload;

    protected Incident(IncidentBuilder builder) {
        this.routingKey = builder.getRoutingKey();
        this.eventType = builder.getEventType();
        this.dedupKey = builder.getDedupKey();
        this.payload = builder.getPayload();
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getDedupKey() {
        return dedupKey;
    }

    public Payload getPayload() {
        return payload;
    }

    protected static abstract class IncidentBuilder<T extends IncidentBuilder> implements Builder {
        protected static final String BLANK_FIELD = "BLANK";

        private final String routingKey;
        private final EventType eventType;
        private String dedupKey;
        private Payload payload;

        /**
         * Builder which helps constructing new incident instances
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                   service detail page.
         * @param eventType  The type of event. Can be trigger, acknowledge or resolve.
         */
        protected IncidentBuilder(String routingKey, EventType eventType) {
            if (StringUtils.isBlank(routingKey)) {
                throw new IllegalArgumentException("routingKey must not be null, it is a mandatory param");
            }
            Objects.requireNonNull(eventType, "eventType must not be null, it is a mandatory param");
            this.routingKey = routingKey;
            this.eventType = eventType;
        }

        /**
         * @param dedupKey Identifies the incident to trigger, acknowledge, or resolve. Required unless the eventType is trigger. If
         *                 event is trigger and the dedupKey is left empty then PagerDuty will auto-generate one in the fly when
         *                 saving the new incident.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public T setDedupKey(String dedupKey) {
            this.dedupKey = dedupKey;
            return (T) this;
        }

        /**
         * @param payload The message of the incident.  Summary, source, and severity are required for the incident to be
         *                processed.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public T setPayload(Payload payload) {
            this.payload = payload;
            return (T) this;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public EventType getEventType() {
            return eventType;
        }

        public String getDedupKey() {
            return dedupKey;
        }

        public Payload getPayload() {
            return payload;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Incident incident = (Incident) o;

        if (routingKey != null ? !routingKey.equals(incident.routingKey) : incident.routingKey != null)
            return false;
        if (eventType != incident.eventType)
            return false;
        if (dedupKey != null ? !dedupKey.equals(incident.dedupKey) : incident.dedupKey != null)
            return false;
        if (payload != null ? !payload.equals(incident.getPayload()) : incident.getPayload() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = routingKey != null ? routingKey.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (dedupKey != null ? dedupKey.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}

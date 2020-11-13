package com.github.dikhan.pagerduty.client.events.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeEvent implements PagerDutyEvent {
    @JsonProperty("routing_key")
    private final String routingKey;
    @JsonProperty("payload")
    private final ChangeEventPayload payload;
    @JsonProperty("links")
    private final List<LinkContext> links;

    protected ChangeEvent(ChangeEventBuilder builder) {
        this.routingKey = builder.getRoutingKey();
        this.payload = builder.getPayload();
        this.links = builder.getLinks();
    }

    public String toString() {
        return "ChangeEvent {" +
                " routingKey='" + routingKey + "'" +
                ", payload=" + payload +
                ", links=" + links +
                " }";
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public ChangeEventPayload getPayload() {
        return payload;
    }

    public List<LinkContext> getLinks() {
        return links;
    }

    public static class ChangeEventBuilder implements Builder {
        private final String routingKey;
        private ChangeEventPayload payload;
        private List<LinkContext> links;

        /**
         * Method to newBuilder a new change event
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param payload    The payload of this change event.  Payload is a mandatory field required to track a change.
         * @return ChangeEvent builder to be able to keep populating the instance with other properties
         */
        public static ChangeEventBuilder newBuilder(String routingKey, ChangeEventPayload payload) {
            return new ChangeEventBuilder(routingKey, payload);
        }

        /**
         * Builder which helps constructing new change event instances
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                   service detail page.
         */
        protected ChangeEventBuilder(String routingKey) {
            if (StringUtils.isBlank(routingKey)) {
                throw new IllegalArgumentException("routingKey must not be null, it is a mandatory param");
            }
            this.routingKey = routingKey;
        }

        private ChangeEventBuilder(String routingKey, ChangeEventPayload payload) {
            this(routingKey);
            setPayload(payload);
        }

        /**
         * @param payload The message of the incident.  Summary, source, and severity are required for the incident to be
         *                processed.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public ChangeEventBuilder setPayload(ChangeEventPayload payload) {
            this.payload = payload;
            return this;
        }

        /**
         * @param links array of objects.
         * @return Payload Builder with links field populated to be able to keep populating the instance
         */
        public ChangeEventBuilder setLinks(List<LinkContext> links) {
            this.links = links;
            return this;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public ChangeEventPayload getPayload() {
            return payload;
        }

        public List<LinkContext> getLinks() {
            return links;
        }

        @Override
        public ChangeEvent build() {
            Objects.requireNonNull(getPayload(), "payload must not be null, it is a mandatory param");
            return new ChangeEvent(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChangeEvent changeEvent = (ChangeEvent) o;

        if (!Objects.equals(routingKey, changeEvent.routingKey)) {
            return false;
        }

        if (payload != null ? !payload.equals(changeEvent.getPayload()) : changeEvent.getPayload() != null) {
            return false;
        }

        if (links != null ? !links.equals(changeEvent.getLinks()) : changeEvent.getLinks() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = routingKey != null ? routingKey.hashCode() : 0;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }
}

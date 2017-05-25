package com.github.dikhan.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Incident {
    @JsonProperty("routing_key")
    private final String routingKey;
    @JsonProperty("event_action")
    private final EventAction eventAction;
    @JsonProperty("dedup_key")
    private final String dedupKey;
    @JsonProperty("payload")
    private final Payload payload;
    @JsonProperty("images")
    private final List<ImageContext> images;
    @JsonProperty("links")
    private final List<LinkContext> links;

    protected Incident(IncidentBuilder builder) {
        this.routingKey = builder.getRoutingKey();
        this.eventAction = builder.getEventAction();
        this.dedupKey = builder.getDedupKey();
        this.payload = builder.getPayload();
        this.images = builder.getImages();
        this.links = builder.getLinks();
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public EventAction getEventAction() {
        return eventAction;
    }

    public String getDedupKey() {
        return dedupKey;
    }

    public Payload getPayload() {
        return payload;
    }

    public List<ImageContext> getImages() {
        return images;
    }

    public List<LinkContext> getLinks() {
        return links;
    }

    protected static abstract class IncidentBuilder<T extends IncidentBuilder> implements Builder {
        protected static final String BLANK_FIELD = "BLANK";

        private final String routingKey;
        private final EventAction eventAction;
        private String dedupKey;
        private Payload payload;
        private List<ImageContext> images;
        private List<LinkContext> links;

        /**
         * Builder which helps constructing new incident instances
         *
         * @param routingKey  The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                    service detail page.
         * @param eventAction The type of event. Can be trigger, acknowledge or resolve.
         */
        protected IncidentBuilder(String routingKey, EventAction eventAction) {
            if (StringUtils.isBlank(routingKey)) {
                throw new IllegalArgumentException("routingKey must not be null, it is a mandatory param");
            }
            Objects.requireNonNull(eventAction, "eventAction must not be null, it is a mandatory param");
            this.routingKey = routingKey;
            this.eventAction = eventAction;
        }

        /**
         * @param dedupKey Identifies the incident to trigger, acknowledge, or resolve. Required unless the eventAction is trigger. If
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

        /**
         * @param images array of objects.
         * @return Payload Builder with images field populated to be able to keep populating the instance
         */
        public Builder setImages(List<ImageContext> images) {
            this.images = images;
            return this;
        }

        /**
         * @param links array of objects.
         * @return Payload Builder with links field populated to be able to keep populating the instance
         */
        public Builder setLinks(List<LinkContext> links) {
            this.links = links;
            return this;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public EventAction getEventAction() {
            return eventAction;
        }

        public String getDedupKey() {
            return dedupKey;
        }

        public Payload getPayload() {
            return payload;
        }

        public List<ImageContext> getImages() {
            return images;
        }

        public List<LinkContext> getLinks() {
            return links;
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
        if (eventAction != incident.eventAction)
            return false;
        if (dedupKey != null ? !dedupKey.equals(incident.dedupKey) : incident.dedupKey != null)
            return false;
        if (payload != null ? !payload.equals(incident.getPayload()) : incident.getPayload() != null)
            return false;
        if (images != null ? !images.equals(incident.getImages()) : incident.getImages() != null)
            return false;
        if (links != null ? !links.equals(incident.getLinks()) : incident.getLinks() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = routingKey != null ? routingKey.hashCode() : 0;
        result = 31 * result + (eventAction != null ? eventAction.hashCode() : 0);
        result = 31 * result + (dedupKey != null ? dedupKey.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }
}

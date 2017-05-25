package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static com.github.dikhan.domain.Incident.IncidentBuilder.BLANK_FIELD;

/**
 * Incident's payload.  Created to properly nest the payload field.
 */
public class Payload {
    @JsonProperty("summary")
    private final String summary;
    @JsonProperty("source")
    private final String source;
    @JsonProperty("severity")
    private final Severity severity;
    @JsonProperty("timestamp")
    private final String timestamp;
    @JsonProperty("component")
    private final String component;
    @JsonProperty("group")
    private final String group;
    @JsonProperty("class")
    private final String eventClass;
    @JsonProperty("custom_details")
    private final String customDetails;

    private Payload(Builder builder) {
        this.summary = builder.getSummary();
        this.source = builder.getSource();
        this.severity = builder.getSeverity();
        this.timestamp = builder.getTimestamp() != null ?
                builder.getTimestamp().toString() : null;
        this.component = builder.getComponent();
        this.group = builder.getGroup();
        this.eventClass = builder.getEventClass();
        this.customDetails = builder.getCustomDetails();
    }

    public String getSummary() {
        return summary;
    }

    public String getSource() {
        return source;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getComponent() {
        return component;
    }

    public String getGroup() {
        return group;
    }

    public String getEventClass() {
        return eventClass;
    }

    public String getCustomDetails() {
        return customDetails;
    }

    public static class Builder {
        private String summary;
        private String source;
        private Severity severity;
        private OffsetDateTime timestamp;
        private String component;
        private String group;
        private String eventClass;
        private String customDetails;

        /**
         * Builder which helps constructing new payload instances
         */
        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Create an empty payload with arbitrary field.  Use for acknowledgeIncident and resolveIncident.
         */
        public static Payload createEmpty() {
            return newBuilder().setSummary(BLANK_FIELD).setSource(BLANK_FIELD).setSeverity(Severity.INFO).build();
        }

        /**
         * @param summary A brief text summary of the event, used to generate the summaries/titles of any associated alerts.
         * @return Payload Builder with timestamp field populated to be able to keep populating the instance
         */
        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * @param source The unique location of the affected system, preferably a hostname or FQDN.
         * @return Payload Builder with timestamp field populated to be able to keep populating the instance
         */
        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        /**
         * @param severity The perceived severity of the status the event is describing with respect to the affected
         *                 system. This can be critical, error, warning or info.
         * @return Payload Builder with timestamp field populated to be able to keep populating the instance
         */
        public Builder setSeverity(Severity severity) {
            this.severity = severity;
            return this;
        }

        /**
         * @param timestamp The time at which the emitting tool detected or generated the event in ISO 8601 format.
         * @return Payload Builder with timestamp field populated to be able to keep populating the instance
         */
        public Builder setTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * @param component Component of the source machine that is responsible for the event, for example mysql or eth0.
         * @return Payload Builder with component field populated to be able to keep populating the instance
         */
        public Builder setComponent(String component) {
            this.component = component;
            return this;
        }

        /**
         * @param group Logical grouping of components of a service, for example app-stack.
         * @return Payload Builder with group field populated to be able to keep populating the instance
         */
        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        /**
         * @param eventClass The class/type of the event, for example ping failure or cpu load.
         * @return Payload Builder with eventClass field populated to be able to keep populating the instance
         */
        public Builder setEventClass(String eventClass) {
            this.eventClass = eventClass;
            return this;
        }

        /**
         * @param customDetails An arbitrary JSON object containing any data you'd like included in the incident log.
         * @return Payload Builder with customDetails field populated to be able to keep populating the instance
         */
        public Builder setCustomDetails(String customDetails) {
            this.customDetails = customDetails;
            return this;
        }

        public String getSummary() {
            return summary;
        }

        public String getSource() {
            return source;
        }

        public Severity getSeverity() {
            return severity;
        }

        public OffsetDateTime getTimestamp() {
            return timestamp;
        }

        public String getComponent() {
            return component;
        }

        public String getGroup() {
            return group;
        }

        public String getEventClass() {
            return eventClass;
        }

        public String getCustomDetails() {
            return customDetails;
        }

        /**
         * Make sure the required fields are not empty, then create a payload.
         */
        public Payload build() {
            Payload payload = new Payload(this);
            if (StringUtils.isBlank(payload.getSummary())) {
                throw new IllegalArgumentException("summary cannot be blank.");
            }
            if (StringUtils.isBlank(payload.getSource())) {
                throw new IllegalArgumentException("source cannot be blank.");
            }
            Objects.requireNonNull(payload.getSeverity(), "severity cannot be null.");

            return payload;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Payload payload = (Payload) o;

        if (summary != null ? !summary.equals(payload.getSummary()) : payload.getSummary() != null)
            return false;
        if (source != null ? !source.equals(payload.getSource()) : payload.getSource() != null)
            return false;
        if (severity != payload.getSeverity())
            return false;
        if (timestamp != null ? !timestamp.equals(payload.getTimestamp()) : payload.getTimestamp() != null)
            return false;
        if (component != null ? !component.equals(payload.getComponent()) : payload.getComponent() != null)
            return false;
        if (group != null ? !group.equals(payload.getGroup()) : payload.getGroup() != null)
            return false;
        if (eventClass != null ? !eventClass.equals(payload.getEventClass()) : payload.getEventClass() != null)
            return false;
        if (customDetails != null ?
                !customDetails.equals(payload.getCustomDetails()) :
                payload.getCustomDetails() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = summary != null ? summary.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        result = 31 * result + (timestamp != null ? severity.hashCode() : 0);
        result = 31 * result + (component != null ? component.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (eventClass != null ? eventClass.hashCode() : 0);
        result = 31 * result + (customDetails != null ? customDetails.hashCode() : 0);
        return result;
    }
}

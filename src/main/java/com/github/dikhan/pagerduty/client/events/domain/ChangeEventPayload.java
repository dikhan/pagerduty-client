package com.github.dikhan.pagerduty.client.events.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.time.OffsetDateTime;

/**
 * Change event's payload.  Created to properly nest the payload field.
 */
public class ChangeEventPayload {
    @JsonProperty("summary")
    private final String summary;
    @JsonProperty("source")
    private final String source;
    @JsonProperty("timestamp")
    private final String timestamp;
    @JsonProperty("custom_details")
    private final JSONObject customDetails;

    private ChangeEventPayload(Builder builder) {
        this.summary = builder.getSummary();
        this.source = builder.getSource();
        this.timestamp = builder.getTimestamp() != null ? builder.getTimestamp().toString() : null;
        this.customDetails = builder.getCustomDetails();
    }

    @Override
    public String toString() {
        return "ChangeEventPayload { " +
                "summary=" + summary +
                ", source=" + source +
                ", timestamp=" + timestamp +
                ", custom_details=" + customDetails +
                " }";
    }

    public String getSummary() {
        return summary;
    }

    public String getSource() {
        return source;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Object getCustomDetails() {
        return customDetails;
    }

    public static class Builder {
        private String summary;
        private String source;
        private OffsetDateTime timestamp;
        private JSONObject customDetails;

        /**
         * Builder which helps constructing new change event payload instances
         */
        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * @param summary A brief text summary of the change event.
         * @return ChangeEventPayload Builder with the summary field populated to be able to keep populating the instance
         */
        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * @param source Where the change event originated from.
         * @return ChangeEventPayload Builder with the source field populated to be able to keep populating the instance
         */
        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        /**
         * @param timestamp The time at which the change took place, in ISO 8601 format.
         * @return ChangeEventPayload Builder with the timestamp field populated to be able to keep populating the instance
         */
        public Builder setTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * @param customDetails An arbitrary JSON object containing any data you'd like included with the change event.
         * @return ChangeEventPayload Builder with the customDetails field populated to be able to keep populating the instance
         */
        public Builder setCustomDetails(JSONObject customDetails) {
            this.customDetails = customDetails;
            return this;
        }

        public String getSummary() {
            return summary;
        }

        public String getSource() {
            return source;
        }

        public OffsetDateTime getTimestamp() {
            return timestamp;
        }

        public JSONObject getCustomDetails() {
            return customDetails;
        }

        /**
         * Make sure the required fields are not empty, then create a payload.
         */
        public ChangeEventPayload build() {
            if (StringUtils.isBlank(getSummary())) {
                throw new IllegalArgumentException("summary cannot be blank.");
            }

            return new ChangeEventPayload(this);
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

        ChangeEventPayload payload = (ChangeEventPayload) o;

        if (summary != null ? !summary.equals(payload.getSummary()) : payload.getSummary() != null) {
            return false;
        }

        if (source != null ? !source.equals(payload.getSource()) : payload.getSource() != null) {
            return false;
        }

        if (timestamp != null ? !timestamp.equals(payload.getTimestamp()) : payload.getTimestamp() != null) {
            return false;
        }

        if (customDetails != null ? !customDetails.equals(payload.getCustomDetails()) : payload.getCustomDetails() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = summary != null ? summary.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (customDetails != null ? customDetails.hashCode() : 0);
        return result;
    }
}

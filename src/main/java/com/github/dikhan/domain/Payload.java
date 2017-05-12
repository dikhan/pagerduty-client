package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.github.dikhan.domain.Incident.IncidentBuilder.BLANK_FIELD;

/**
 * Incident's payload.  Created to properly nest the payload field.
 */
public class Payload {
    @JsonProperty("summary")
    private final String summary;
    @JsonProperty("custom_details")
    private final String customDetails;
    @JsonProperty("source")
    private final String source;
    @JsonProperty("severity")
    private final Severity severity;

    private Payload(Builder builder) {
        this.summary = builder.getSummary();
        this.customDetails = builder.getCustomDetails();
        this.source = builder.getSource();
        this.severity = builder.getSeverity();
    }

    public String getSummary() {
        return summary;
    }

    public String getCustomDetails() {
        return customDetails;
    }

    public String getSource() {
        return source;
    }

    public Severity getSeverity() {
        return severity;
    }

    public static class Builder {
        private String summary;
        private String customDetails;
        private String source;
        private Severity severity;

        /**
         * Builder which helps constructing new payload instances
         */
        public Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Create an empty payload with arbitrary field.  Use for acknowledgeIncident and resolveIncident.
         */
        public static Payload createEmpty() {
            return new Builder()
                    .setSummary(BLANK_FIELD)
                    .setSource(BLANK_FIELD)
                    .setSeverity(Severity.INFO).build();
        }

        /**
         * @param summary A brief text summary of the event, used to generate the summaries/titles of any associated alerts.
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * @param customDetails An arbitrary JSON object containing any data you'd like included in the incident log.
         * @return IncidentBuilder with customDetails field populated to be able to keep populating the instance
         */
        public Builder setCustomDetails(String customDetails) {
            this.customDetails = customDetails;
            return this;
        }

        /**
         * @param source The unique location of the affected system, preferably a hostname or FQDN.
         * @return IncidentBuilder with customDetails field populated to be able to keep populating the instance
         */
        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        /**
         * @param severity The perceived severity of the status the event is describing with respect to the affected
         *                 system. This can be critical, error, warning or info.
         * @return IncidentBuilder with customDetails field populated to be able to keep populating the instance
         */
        public Builder setSeverity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public String getSummary() {
            return summary;
        }

        public String getCustomDetails() {
            return customDetails;
        }

        public String getSource() {
            return source;
        }

        public Severity getSeverity() {
            return severity;
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
        if (customDetails != null ?
                !customDetails.equals(payload.getCustomDetails()) :
                payload.getCustomDetails() != null)
            return false;
        if (severity != payload.getSeverity())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = summary != null ? summary.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (customDetails != null ? customDetails.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        return result;
    }
}

package com.github.dikhan.domain;

import org.apache.commons.lang3.StringUtils;

public class AcknowledgeIncident extends Incident {

    private AcknowledgeIncident(IncidentBuilder builder) {
        super(builder);
    }

    public static class AcknowledgeIncidentBuilder extends Incident.IncidentBuilder<AcknowledgeIncidentBuilder> {

        private AcknowledgeIncidentBuilder(String routingKey, String dedupKey) {
            super(routingKey, EventAction.ACKNOWLEDGE);
            if (StringUtils.isBlank(dedupKey)) {
                throw new IllegalArgumentException("dedupKey must not be null, it is a mandatory param");
            }
            super.setDedupKey(dedupKey);
        }

        /**
         * Method to newBuilder a new incident of type acknowledge
         *
         * @param routingKey  The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                    Generic API's service detail page.
         * @param dedupKey Identifies the incident to acknowledge
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static AcknowledgeIncidentBuilder newBuilder(String routingKey, String dedupKey) {
            return new AcknowledgeIncidentBuilder(routingKey, dedupKey);
        }

        @Override
        public AcknowledgeIncident build() {
            if (getPayload() == null) {
                setPayload(Payload.Builder.createEmpty());
            }
            return new AcknowledgeIncident(this);
        }

    }
}

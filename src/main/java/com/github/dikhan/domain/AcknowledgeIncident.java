package com.github.dikhan.domain;

import java.util.Objects;

public class AcknowledgeIncident extends Incident {

    private AcknowledgeIncident(IncidentBuilder builder) {
        super(builder);
    }

    public static class AcknowledgeIncidentBuilder extends Incident.IncidentBuilder<AcknowledgeIncidentBuilder> {

        private AcknowledgeIncidentBuilder(String serviceKey, String incidentKey) {
            super(serviceKey, EventType.ACKNOWLEDGE);
            super.incidentKey(incidentKey);
            Objects.requireNonNull(serviceKey, "serviceKey must not be null, it is a mandatory param");
            Objects.requireNonNull(incidentKey, "incidentKey must not be null, it is a mandatory param");
        }

        /**
         * Method to create a new incident of type acknowledge
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param incidentKey Identifies the incident to acknowledge
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static AcknowledgeIncidentBuilder create(String serviceKey, String incidentKey) {
            return new AcknowledgeIncidentBuilder(serviceKey, incidentKey);
        }

        /**
         * Important: PagerDuty does not support description on acknowledge incidents. If the value is specified
         * it will be ignored
         *
         * @param description this value will be ignored, pager duty does not support a description on acknowledge incidents
         * @return IncidentBuilder to be able to keep populating the instance
         */
        public AcknowledgeIncidentBuilder description(String description) {
            // PagerDuty does not support description on acknowledge incidents
            return this;
        }

        /**
         * Important: PagerDuty does not support details on acknowledge incidents. If the value is specified
         * it will be ignored
         * @param details this value will be ignored, pager duty does not support details on acknowledge incidents
         * @return IncidentBuilder with details field populated to be able to keep populating the instance
         */
        public AcknowledgeIncidentBuilder details(String details) {
            // PagerDuty does not support details on acknowledge incidents
            return this;
        }

        @Override
        public AcknowledgeIncident build() {
            return new AcknowledgeIncident(this);
        }

    }
}

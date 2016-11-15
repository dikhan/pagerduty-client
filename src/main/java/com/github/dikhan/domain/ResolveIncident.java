package com.github.dikhan.domain;

import java.util.Objects;

public class ResolveIncident extends Incident {

    private ResolveIncident(IncidentBuilder builder) {
        super(builder);
    }

    public static class ResolveIncidentBuilder extends Incident.IncidentBuilder<ResolveIncidentBuilder> {

        private ResolveIncidentBuilder(String serviceKey, String incidentKey) {
            super(serviceKey, EventType.RESOLVE);
            super.incidentKey(incidentKey);
            Objects.requireNonNull(serviceKey, "serviceKey must not be null, it is a mandatory param");
            Objects.requireNonNull(incidentKey, "incidentKey must not be null, it is a mandatory param");
        }

        /**
         * Method to create a new incident of type resolve
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param incidentKey Identifies the incident to resolve
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static ResolveIncidentBuilder create(String serviceKey, String incidentKey) {
            return new ResolveIncidentBuilder(serviceKey, incidentKey);
        }

        @Override
        public ResolveIncident build() {
            return new ResolveIncident(this);
        }

    }
}

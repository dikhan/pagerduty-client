package com.github.dikhan.domain;

import org.apache.commons.lang3.StringUtils;

public class ResolveIncident extends Incident {

    private ResolveIncident(IncidentBuilder builder) {
        super(builder);
    }

    public static class ResolveIncidentBuilder extends Incident.IncidentBuilder<ResolveIncidentBuilder> {

        private ResolveIncidentBuilder(String routingKey, String dedupKey) {
            super(routingKey, EventAction.RESOLVE);
            if (StringUtils.isBlank(dedupKey)) {
                throw new IllegalArgumentException("dedupKey must not be null, it is a mandatory param");
            }
            super.setDedupKey(dedupKey);
        }

        /**
         * Method to newBuilder a new incident of type resolve
         *
         * @param routingKey  The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                    Generic API's service detail page.
         * @param dedupKey Identifies the incident to resolve
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static ResolveIncidentBuilder newBuilder(String routingKey, String dedupKey) {
            return new ResolveIncidentBuilder(routingKey, dedupKey);
        }

        @Override
        public ResolveIncident build() {
            if (getPayload() == null) {
                setPayload(Payload.Builder.createEmpty());
            }
            return new ResolveIncident(this);
        }

    }
}

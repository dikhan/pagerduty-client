package com.github.dikhan.domain;

import java.util.Objects;

public class TriggerIncident extends Incident {
    private TriggerIncident(TriggerIncidentBuilder builder) {
        super(builder);
    }

    public static class TriggerIncidentBuilder extends Incident.IncidentBuilder<TriggerIncidentBuilder> {
        private TriggerIncidentBuilder(String routingKey) {
            super(routingKey, EventType.TRIGGER);
        }

        /**
         * Method to newBuilder a new incident of type trigger
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static TriggerIncidentBuilder newBuilder(String routingKey) {
            return new TriggerIncidentBuilder(routingKey);
        }

        @Override
        public TriggerIncident build() {
            TriggerIncident triggerIncident = new TriggerIncident(this);
            Objects.requireNonNull(triggerIncident.getPayload(),
                    "payload must not be null, it is a mandatory param");

            return new TriggerIncident(this);
        }
    }

}

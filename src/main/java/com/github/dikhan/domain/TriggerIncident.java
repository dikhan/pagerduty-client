package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TriggerIncident extends Incident {
    @JsonProperty("client")
    private String client;
    @JsonProperty("client_url")
    private String clientUrl;

    private TriggerIncident(TriggerIncidentBuilder builder) {
        super(builder);
        this.client = builder.getClient();
        this.clientUrl = builder.getClientUrl();
    }

    public static class TriggerIncidentBuilder extends Incident.IncidentBuilder<TriggerIncidentBuilder> {
        private String client;
        private String clientUrl;

        private TriggerIncidentBuilder(String routingKey, Payload payload) {
            super(routingKey, EventAction.TRIGGER);
            setPayload(payload);
        }

        /**
         * Method to newBuilder a new incident of type trigger
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param payload    The payload of this incident.  Payload is a mandatory field requires to trigger an incident.
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static TriggerIncidentBuilder newBuilder(String routingKey, Payload payload) {
            return new TriggerIncidentBuilder(routingKey, payload);
        }

        /**
         * @param client The name of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with client field populated to be able to keep populating the instance
         */
        public TriggerIncidentBuilder setClient(String client) {
            this.client = client;
            return this;
        }

        /**
         * @param clientUrl The URL of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with clientUrl field populated to be able to keep populating the instance
         */
        public TriggerIncidentBuilder setClientUrl(String clientUrl) {
            this.clientUrl = clientUrl;
            return this;
        }

        public String getClient() {
            return client;
        }

        public String getClientUrl() {
            return clientUrl;
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

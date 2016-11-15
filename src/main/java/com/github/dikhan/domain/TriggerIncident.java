package com.github.dikhan.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerIncident extends Incident {

    @JsonProperty("client")
    private final String client;
    @JsonProperty("client_url")
    private final String clientUrl;
    @JsonProperty("contexts")
    private List<Context> contexts;

    private TriggerIncident(TriggerIncidentBuilder builder) {
        super(builder);
        this.client = builder.getClient();
        this.clientUrl = builder.getClientUrl();
        this.contexts = builder.getContexts();
    }

    public String getClient() {
        return client;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public static class TriggerIncidentBuilder extends Incident.IncidentBuilder<TriggerIncidentBuilder> {

        private String client;
        private String clientUrl;
        private List<Context> contexts;

        private TriggerIncidentBuilder(String serviceKey, String description) {
            super(serviceKey, EventType.TRIGGER);
            super.description(description);
            Objects.requireNonNull(serviceKey, "serviceKey must not be null, it is a mandatory param");
            Objects.requireNonNull(description, "description must not be null, it is a mandatory param");
        }

        /**
         * Method to create a new incident of type trigger
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a
         *                   Generic API's service detail page.
         * @param description Text that will appear in the incident's log associated with this event. Required for trigger events.
         * @return Incident builder to be able to keep populating the instance with other properties
         */
        public static TriggerIncidentBuilder create(String serviceKey, String description) {
            return new TriggerIncidentBuilder(serviceKey, description);
        }

        /**
         * @param client The name of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with client field populated to be able to keep populating the instance
         */
        public TriggerIncidentBuilder client(String client) {
            this.client = client;
            return this;
        }

        /**
         * @param clientUrl The URL of the monitoring client that is triggering this event. (This field is only used for trigger events.)
         * @return IncidentBuilder with clientUrl field populated to be able to keep populating the instance
         */
        public TriggerIncidentBuilder clientUrl(String clientUrl) {
            this.clientUrl = clientUrl;
            return this;
        }

        /**
         * @param contexts Contexts to be included with the incident trigger such as links to graphs or images. (This field is only used for trigger events.)
         * @return IncidentBuilder with contexts field populated to be able to keep populating the instance
         */
        public TriggerIncidentBuilder contexts(List<Context> contexts) {
            this.contexts = contexts;
            return this;
        }

        @Override
        public TriggerIncident build() {
            return new TriggerIncident(this);
        }

        public String getClient() {
            return client;
        }

        public String getClientUrl() {
            return clientUrl;
        }

        public List<Context> getContexts() {
            return contexts;
        }

    }

}

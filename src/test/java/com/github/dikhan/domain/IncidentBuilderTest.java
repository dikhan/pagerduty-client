package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class IncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        EventAction eventAction = EventAction.ACKNOWLEDGE;
        IncidentBuilderMock.create(routingKey, eventAction);
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldEventTypeIsNull() throws NotifyEventException {
        String routingKey = "routingKey";
        EventAction eventAction = null;
        IncidentBuilderMock.create(routingKey, eventAction);
    }

    private static class IncidentBuilderMock extends Incident.IncidentBuilder<IncidentBuilderMock> {

        /**
         * Builder which helps constructing new incident instances
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                   service detail page.
         * @param eventAction  The type of event. Can be trigger, acknowledge or resolve.
         */
        private IncidentBuilderMock(String routingKey, EventAction eventAction) {
            super(routingKey, eventAction);
        }

        /**
         * to satisfy the Builder interface
         */
        public IncidentBuilderMock build() {
           return this;
        }

        public static IncidentBuilderMock create(String routingKey, EventAction eventAction) {
            return new IncidentBuilderMock(routingKey, eventAction);
        }
    }
}
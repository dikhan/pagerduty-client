package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class IncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        EventType eventType = EventType.ACKNOWLEDGE;
        IncidentBuilderMock.create(routingKey, eventType);
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldEventTypeIsNull() throws NotifyEventException {
        String routingKey = "routingKey";
        EventType eventType = null;
        IncidentBuilderMock.create(routingKey, eventType);
    }

    private static class IncidentBuilderMock extends Incident.IncidentBuilder<IncidentBuilderMock> {

        /**
         * Builder which helps constructing new incident instances
         *
         * @param routingKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                   service detail page.
         * @param eventType  The type of event. Can be trigger, acknowledge or resolve.
         */
        private IncidentBuilderMock(String routingKey, EventType eventType) {
            super(routingKey, eventType);
        }

        /**
         * to satisfy the Builder interface
         */
        public IncidentBuilderMock build() {
           return this;
        }

        public static IncidentBuilderMock create(String routingKey, EventType eventType) {
            return new IncidentBuilderMock(routingKey, eventType);
        }
    }
}
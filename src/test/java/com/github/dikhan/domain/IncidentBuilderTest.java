package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class IncidentBuilderTest {

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldServiceKeyIsNull() throws NotifyEventException {
        String serviceKey = null;
        EventType eventType = EventType.ACKNOWLEDGE;
        IncidentBuilderMock.create(serviceKey, eventType);
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldEventTypeIsNull() throws NotifyEventException {
        String serviceKey = "ServiceKey";
        EventType eventType = null;
        IncidentBuilderMock.create(serviceKey, eventType);
    }

    private static class IncidentBuilderMock extends Incident.IncidentBuilder<IncidentBuilderMock> {

        /**
         * Builder which helps constructing new incident instances
         *
         * @param serviceKey The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic API's
         *                   service detail page.
         * @param eventType  The type of event. Can be trigger, acknowledge or resolve.
         */
        private IncidentBuilderMock(String serviceKey, EventType eventType) {
            super(serviceKey, eventType);
        }

        public static IncidentBuilderMock create(String serviceKey, EventType eventType) {
            return new IncidentBuilderMock(serviceKey, eventType);
        }
    }
}
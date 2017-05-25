package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class TriggerIncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        Payload payload = Payload.Builder.createEmpty();
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey, payload).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsBlank() throws NotifyEventException {
        String routingKey = "";
        Payload payload = Payload.Builder.createEmpty();
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey, payload).build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfPayloadIsNotSet() throws NotifyEventException {
        String routingKey = "routingKey";
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey, null).build();
    }
}
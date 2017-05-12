package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class TriggerIncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsBlank() throws NotifyEventException {
        String routingKey = "";
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey).build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfPayloadIsNotSet() throws NotifyEventException {
        String routingKey = "routingKey";
        TriggerIncident.TriggerIncidentBuilder.newBuilder(routingKey).build();
    }
}
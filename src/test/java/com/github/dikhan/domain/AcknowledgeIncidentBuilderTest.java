package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class AcknowledgeIncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        AcknowledgeIncident.AcknowledgeIncidentBuilder.newBuilder(routingKey, "DedupKey").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldDedupKeyIsNull() throws NotifyEventException {
        String dedupKey = null;
        AcknowledgeIncident.AcknowledgeIncidentBuilder.newBuilder("RoutingKey", dedupKey).build();
    }

}
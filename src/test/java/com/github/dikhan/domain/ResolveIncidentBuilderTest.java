package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class ResolveIncidentBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        String routingKey = null;
        ResolveIncident.ResolveIncidentBuilder.newBuilder(routingKey, "DedupKey").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldDedupKeyIsNull() throws NotifyEventException {
        String dedupKey = null;
        ResolveIncident.ResolveIncidentBuilder.newBuilder("routingKey", dedupKey).build();
    }

}
package com.github.dikhan.pagerduty.client.events.domain;

import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import org.junit.Test;

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
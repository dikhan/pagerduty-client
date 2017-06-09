package com.github.dikhan.pagerduty.client.events.domain;

import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import org.junit.Test;

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
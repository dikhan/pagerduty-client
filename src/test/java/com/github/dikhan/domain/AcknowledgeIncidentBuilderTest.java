package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class AcknowledgeIncidentBuilderTest {

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldServiceKeyIsNull() throws NotifyEventException {
        String serviceKey = null;
        AcknowledgeIncident.AcknowledgeIncidentBuilder.create(serviceKey, "IncidentKey").build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldIncidentKeyIsNull() throws NotifyEventException {
        String incidentKey = null;
        AcknowledgeIncident.AcknowledgeIncidentBuilder.create("ServiceKey", incidentKey).build();
    }

}
package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class ResolveIncidentBuilderTest {

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldServiceKeyIsNull() throws NotifyEventException {
        String serviceKey = null;
        ResolveIncident.ResolveIncidentBuilder.create(serviceKey, "IncidentKey").build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldIncidentKeyIsNull() throws NotifyEventException {
        String incidentKey = null;
        ResolveIncident.ResolveIncidentBuilder.create("ServiceKey", incidentKey).build();
    }

}
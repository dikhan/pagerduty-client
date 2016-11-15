package com.github.dikhan.domain;

import org.junit.Test;

import com.github.dikhan.exceptions.NotifyEventException;

public class TriggerIncidentBuilderTest {

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldServiceKeyIsNull() throws NotifyEventException {
        String serviceKey = null;
        TriggerIncident.TriggerIncidentBuilder.create(serviceKey, "description").build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreateTriggerIncidentIfMandatoryFieldDescriptionIsNull() throws NotifyEventException {
        String description = null;
        TriggerIncident.TriggerIncidentBuilder.create("ServiceKey", description).build();
    }

}
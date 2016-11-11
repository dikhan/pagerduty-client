package com.github.dikhan.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.dikhan.domain.Incident;
import com.github.dikhan.exceptions.NotifyEventException;

public class FakePagerDutyEventsClientTest {

    private final FakePagerDutyEventsClient fakePagerDutyEventsClient = FakePagerDutyEventsClient.create();

    @Test
    public void triggerIncident() throws NotifyEventException {
        Incident incident = Incident.IncidentBuilder.trigger("ServiceKey", "Some issue description").build();
        fakePagerDutyEventsClient.trigger(incident);
        assertThat(fakePagerDutyEventsClient.openIncidents()).hasSize(1);
    }

    @Test
    public void triggerIncidentWithIncidentKey() throws NotifyEventException {
        Incident incident = Incident.IncidentBuilder
                .trigger("ServiceKey", "Some issue description")
                .incidentKey("IncidentKey").build();
        fakePagerDutyEventsClient.trigger(incident);
        assertThat(fakePagerDutyEventsClient.openIncidents()).containsExactly(incident);
    }

    @Test
    public void acknowledgeIncident() throws NotifyEventException {
        Incident incident = Incident.IncidentBuilder.acknowledge("ServiceKey", "IncidentKey");
        fakePagerDutyEventsClient.acknowledge(incident.getServiceKey(), incident.getIncidentKey());
        assertThat(fakePagerDutyEventsClient.acknowledgedIncidents()).containsExactly(incident);
    }

    @Test
    public void resolveIncident() throws NotifyEventException {
        Incident incident = Incident.IncidentBuilder.resolve("ServiceKey", "IncidentKey");
        fakePagerDutyEventsClient.resolve(incident.getServiceKey(), incident.getIncidentKey());
        assertThat(fakePagerDutyEventsClient.resolvedIncidents()).containsExactly(incident);
    }

}
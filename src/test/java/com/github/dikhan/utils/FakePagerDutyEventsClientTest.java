package com.github.dikhan.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.dikhan.domain.AcknowledgeIncident;
import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.ResolveIncident;
import com.github.dikhan.domain.TriggerIncident;
import com.github.dikhan.exceptions.NotifyEventException;

public class FakePagerDutyEventsClientTest {

    private final FakePagerDutyEventsClient fakePagerDutyEventsClient = FakePagerDutyEventsClient.create();

    @Test
    public void triggerIncident() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder.create("ServiceKey", "Some issue description").build();
        fakePagerDutyEventsClient.trigger(incident);
        assertThat(fakePagerDutyEventsClient.openIncidents()).hasSize(1);
    }

    @Test
    public void triggerIncidentWithIncidentKey() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder
                .create("ServiceKey", "Some issue description")
                .incidentKey("IncidentKey").build();
        fakePagerDutyEventsClient.trigger(incident);
        assertThat(fakePagerDutyEventsClient.openIncidents()).containsExactly(incident);
    }

    @Test
    public void acknowledgeIncident() throws NotifyEventException {
        AcknowledgeIncident ack = AcknowledgeIncident.AcknowledgeIncidentBuilder.create("ServiceKey", "IncidentKey").build();
        fakePagerDutyEventsClient.acknowledge(ack);
        assertThat(fakePagerDutyEventsClient.acknowledgedIncidents()).containsExactly(ack);
    }

    @Test
    public void resolveIncident() throws NotifyEventException {
        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder.create("ServiceKey", "IncidentKey").build();
        fakePagerDutyEventsClient.resolve(resolve);
        assertThat(fakePagerDutyEventsClient.resolvedIncidents()).containsExactly(resolve);
    }

    @Test
    public void triggerAndResolveIncident() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder.create("ServiceKey", "Some issue description").build();
        EventResult eventResult = fakePagerDutyEventsClient.trigger(incident);

        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder.create("ServiceKey", eventResult.getIncidentKey()).build();
        fakePagerDutyEventsClient.resolve(resolve);
        assertThat(fakePagerDutyEventsClient.resolvedIncidents()).containsExactly(resolve);
        assertThat(fakePagerDutyEventsClient.openIncidents()).isEmpty();
    }

}
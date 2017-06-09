package com.github.dikhan.pagerduty.client.events.utils;

import com.github.dikhan.pagerduty.client.events.domain.*;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class FakePagerDutyEventsClientTest {

    private final FakePagerDutyEventsClient fakePagerDutyEventsClient = FakePagerDutyEventsClient.create();
    private Payload payload;
    private final String ROUTING_KEY = "ROUTING_KEY";
    private final String DEDUP_KEY = "DedupKey";


    @Before
    public void setup() {
        payload = Payload.Builder.newBuilder()
                .setSource("testing host")
                .setSeverity(Severity.INFO)
                .setSummary("This is an incident test to test PagerDutyEventsClient")
                .build();
    }

    @Test
    public void triggerIncident() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder.newBuilder(ROUTING_KEY, payload)
                .build();
        fakePagerDutyEventsClient.trigger(incident);
        Assertions.assertThat(fakePagerDutyEventsClient.openIncidents()).hasSize(1);
    }

    @Test
    public void triggerIncidentWithDedupKey() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder.newBuilder(ROUTING_KEY, payload)
                .setDedupKey("DedupKey")
                .build();
        fakePagerDutyEventsClient.trigger(incident);
        Assertions.assertThat(fakePagerDutyEventsClient.openIncidents()).containsExactly(incident);
    }

    @Test
    public void acknowledgeIncident() throws NotifyEventException {
        AcknowledgeIncident ack = AcknowledgeIncident.AcknowledgeIncidentBuilder.newBuilder(ROUTING_KEY, DEDUP_KEY).build();
        fakePagerDutyEventsClient.acknowledge(ack);
        Assertions.assertThat(fakePagerDutyEventsClient.acknowledgedIncidents()).containsExactly(ack);
    }

    @Test
    public void resolveIncident() throws NotifyEventException {
        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder.newBuilder(ROUTING_KEY, DEDUP_KEY).build();
        fakePagerDutyEventsClient.resolve(resolve);
        Assertions.assertThat(fakePagerDutyEventsClient.resolvedIncidents()).containsExactly(resolve);
    }

    @Test
    public void triggerAndResolveIncident() throws NotifyEventException {
        TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder.newBuilder(ROUTING_KEY, payload)
                .build();
        EventResult eventResult = fakePagerDutyEventsClient.trigger(incident);

        ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder.newBuilder(ROUTING_KEY, eventResult.getDedupKey()).build();
        fakePagerDutyEventsClient.resolve(resolve);
        Assertions.assertThat(fakePagerDutyEventsClient.resolvedIncidents()).containsExactly(resolve);
        Assertions.assertThat(fakePagerDutyEventsClient.openIncidents()).isEmpty();
    }

}
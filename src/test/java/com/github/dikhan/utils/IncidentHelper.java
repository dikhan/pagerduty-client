package com.github.dikhan.utils;

import com.github.dikhan.domain.*;

public class IncidentHelper {

    public static TriggerIncident prepareSampleTriggerIncident(String routingKey) {
        return TriggerIncident.TriggerIncidentBuilder
                .newBuilder(routingKey, Payload.Builder.newBuilder()
                        .setSeverity(Severity.INFO)
                        .setSummary("HealthCheck failed")
                        .setSource("testing source")
                        .build())
                .build();
    }

    public static AcknowledgeIncident prepareSampleAcknowledgementIncident(String routingKey, String dedupKey) {
        return AcknowledgeIncident.AcknowledgeIncidentBuilder.newBuilder(routingKey, dedupKey).build();
    }

    public static ResolveIncident prepareSampleResolveIncident(String routingKey, String dedupKey) {
        return ResolveIncident.ResolveIncidentBuilder.newBuilder(routingKey, dedupKey).build();
    }

}

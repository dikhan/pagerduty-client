package com.github.dikhan.utils;

import java.util.Arrays;

import com.github.dikhan.domain.AcknowledgeIncident;
import com.github.dikhan.domain.ImageContext;
import com.github.dikhan.domain.LinkContext;
import com.github.dikhan.domain.ResolveIncident;
import com.github.dikhan.domain.TriggerIncident;

public class IncidentHelper {

    public static TriggerIncident prepareSampleTriggerIncident(String serviceKey) {
        LinkContext linkContext = new LinkContext("http://link-context.com");
        ImageContext imageContext = new ImageContext("http://image-context.com");
        return TriggerIncident.TriggerIncidentBuilder
                .create(serviceKey, "HealthCheck failed")
                .client("PagerDutyEventsClientTest")
                .details("Issue details")
                .clientUrl("http://www.issue-origin.com")
                .contexts(Arrays.asList(linkContext, imageContext))
                .build();
    }

    public static AcknowledgeIncident prepareSampleAcknowledgementIncident(String serviceKey, String incidentKey) {
        return AcknowledgeIncident.AcknowledgeIncidentBuilder.create(serviceKey, incidentKey).build();
    }

    public static ResolveIncident prepareSampleResolveIncident(String serviceKey, String incidentKey) {
        return ResolveIncident.ResolveIncidentBuilder.create(serviceKey, incidentKey).build();
    }

}

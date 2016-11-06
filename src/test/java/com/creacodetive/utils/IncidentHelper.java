package com.creacodetive.utils;

import com.creacodetive.domain.ImageContext;
import com.creacodetive.domain.Incident;
import com.creacodetive.domain.LinkContext;

import java.util.Arrays;

public class IncidentHelper {

    public static Incident prepareSampleTriggerIncident(String serviceKey) {
        LinkContext linkContext = new LinkContext("http://link-context.com");
        ImageContext imageContext = new ImageContext("http://image-context.com");
        return Incident.IncidentBuilder
                .trigger(serviceKey, "HealthCheck failed")
                .client("PagerDutyClientTest")
                .details("Issue details")
                .clientUrl("http://www.issue-origin.com")
                .contexts(Arrays.asList(linkContext, imageContext))
                .build();
    }

    public static Incident prepareSampleAcknowledgementIncident(String serviceKey, String incidentKey) {
        return Incident.IncidentBuilder.acknowledge(serviceKey, incidentKey);
    }

    public static Incident prepareSampleResolveIncident(String serviceKey, String incidentKey) {
        return Incident.IncidentBuilder.resolve(serviceKey, incidentKey);
    }

}

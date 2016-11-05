package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.ImageContext;
import com.creacodetive.domain.Incident;
import com.creacodetive.domain.LinkContext;
import com.creacodetive.exceptions.NotifyEventException;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PagerDutyClientTest {

    private final String API_KEY = "API_KEY";
    private final String SERVICE_KEY = "SERVICE_KEY";

    private PagerDutyClient pagerDutyClient;

    @Test
    public void triggerAlert() throws NotifyEventException {
        LinkContext linkContext = new LinkContext("http://link-context.com");
        ImageContext imageContext = new ImageContext("http://image-context.com");
        Incident incident = Incident.IncidentBuilder
                .trigger(SERVICE_KEY, "HealthCheck failed")
                .client("PagerDutyClientTest")
                .details("Issue details")
                .client_url("http://www.issue-origin.com")
                .contexts(Arrays.asList(linkContext, imageContext))
                .build();

        pagerDutyClient = PagerDutyClient.create(API_KEY);
        EventResult eventResult = pagerDutyClient.trigger(incident);
        EventResult expectedEventResult = EventResult.successEvent("success", "Event processed", "incidentKey");
        assertThat(eventResult).isEqualTo(expectedEventResult);
    }

}

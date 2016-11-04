package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;

public class PagerDutyClient {

    private final String apiKey;

    public PagerDutyClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public EventResult trigger(Incident incident) {
        return new EventResult("success", "Event processed", "incidentKey");
    }
}

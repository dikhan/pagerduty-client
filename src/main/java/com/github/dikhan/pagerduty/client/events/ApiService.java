package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.Incident;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;

public interface ApiService {

    EventResult notifyEvent(Incident incident) throws NotifyEventException;

}

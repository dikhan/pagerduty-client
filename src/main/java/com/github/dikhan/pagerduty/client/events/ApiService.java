package com.github.dikhan.pagerduty.client.events;

import com.github.dikhan.pagerduty.client.events.domain.EventResult;
import com.github.dikhan.pagerduty.client.events.domain.PagerDutyEvent;
import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;

public interface ApiService {

    EventResult notifyEvent(PagerDutyEvent event) throws NotifyEventException;

}

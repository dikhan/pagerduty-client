package com.github.dikhan.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.dikhan.PagerDutyEventsClient;
import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.EventType;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.exceptions.NotifyEventException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class provided for integration testing purposes
 *
 * @author Daniel I. Khan Ramiro
 */
public class FakePagerDutyEventsClient extends PagerDutyEventsClient {

    private static final Logger log = LoggerFactory.getLogger(FakePagerDutyEventsClient.class);

    private final Set<Incident> incidents = new HashSet<>();
    private final Random random = new Random();

    private FakePagerDutyEventsClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        super(pagerDutyClientBuilder);
    }

    public static FakePagerDutyEventsClient create() {
        return new FakePagerDutyEventsClient(new PagerDutyClientBuilder());
    }

    @Override
    public EventResult trigger(Incident incident) throws NotifyEventException {
        EventResult eventResult = addIncidentAndCreateEventResult(incident);
        log.debug("Event result {}", eventResult);
        return eventResult;
    }

    @Override
    public EventResult acknowledge(String serviceKey, String incidentKey) throws NotifyEventException {
        Incident ack = Incident.IncidentBuilder.acknowledge(serviceKey, incidentKey);
        EventResult eventResult = addIncidentAndCreateEventResult(ack);
        log.debug("Event result {} for acknowledge incident {}", eventResult, ack);
        return eventResult;
    }

    @Override
    public EventResult resolve(String serviceKey, String incidentKey) throws NotifyEventException {
        Incident resolve = Incident.IncidentBuilder.resolve(serviceKey, incidentKey);
        EventResult eventResult = addIncidentAndCreateEventResult(resolve);
        log.debug("Event result {} for resolve incident {}", eventResult, resolve);
        return eventResult;
    }

    public Set<Incident> openIncidents() {
        return incidents.stream()
                .filter(incident -> incident.getEventType() == EventType.TRIGGER)
                .collect(Collectors.toSet());
    }

    public Set<Incident> resolvedIncidents() {
        return incidents.stream()
                .filter(incident -> incident.getEventType() == EventType.RESOLVE)
                .collect(Collectors.toSet());
    }

    public Set<Incident> acknowledgedIncidents() {
        return incidents.stream()
                .filter(incident -> incident.getEventType() == EventType.ACKNOWLEDGE)
                .collect(Collectors.toSet());
    }

    private EventResult addIncidentAndCreateEventResult(Incident incident) {
        incidents.add(incident);
        String incidentKey = StringUtils.isBlank(incident.getIncidentKey()) ? String.valueOf(random.nextLong()) : incident.getIncidentKey();
        return EventResult.successEvent("success-" + incident.getEventType().getEventType(), "Event processed", incidentKey);
    }

}

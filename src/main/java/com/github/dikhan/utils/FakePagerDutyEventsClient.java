package com.github.dikhan.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.dikhan.domain.AcknowledgeIncident;
import com.github.dikhan.domain.ResolveIncident;
import com.github.dikhan.domain.TriggerIncident;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dikhan.PagerDutyEventsClient;
import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.exceptions.NotifyEventException;

/**
 * Helper class provided for integration testing purposes
 *
 * @author Daniel I. Khan Ramiro
 */
public class FakePagerDutyEventsClient extends PagerDutyEventsClient {

    private static final Logger log = LoggerFactory.getLogger(FakePagerDutyEventsClient.class);

    private final Set<TriggerIncident> openIncidents = new HashSet<>();
    private final Set<ResolveIncident> resolvedIncidents = new HashSet<>();
    private final Set<AcknowledgeIncident> acknowledgedIncidents = new HashSet<>();
    private final Random random = new Random();

    private FakePagerDutyEventsClient(PagerDutyClientBuilder pagerDutyClientBuilder) {
        super(pagerDutyClientBuilder);
    }

    public static FakePagerDutyEventsClient create() {
        return new FakePagerDutyEventsClient(new PagerDutyClientBuilder());
    }

    @Override
    public EventResult trigger(TriggerIncident incident) throws NotifyEventException {
        EventResult eventResult;
        if(StringUtils.isBlank(incident.getDedupKey())) {
            String dedupKey = String.valueOf(random.nextLong());
            incident = updateTriggerIncidentWithKey(incident, dedupKey);
            eventResult = createEventResult(incident);
        } else {
            eventResult = createEventResult(incident);
        }
        openIncidents.add(incident);
        log.debug("Event result {}", eventResult);
        return eventResult;
    }

    @Override
    public EventResult acknowledge(AcknowledgeIncident ack) throws NotifyEventException {
        if(StringUtils.isBlank(ack.getRoutingKey()) || StringUtils.isBlank(ack.getDedupKey())) {
            throw new NotifyEventException("routingKey and dedupKey are required parameters to be able to acknowledge an incident");
        }
        acknowledgedIncidents.add(ack);
        EventResult eventResult = createEventResult(ack);
        log.debug("Event result {} for acknowledge incident {}", eventResult, ack);
        return eventResult;
    }

    @Override
    public EventResult resolve(ResolveIncident resolve) throws NotifyEventException {
        if(StringUtils.isBlank(resolve.getRoutingKey()) || StringUtils.isBlank(resolve.getDedupKey())) {
            throw new NotifyEventException("routingKey and dedupKey are required parameters to be able to resolve an incident");
        }
        resolvedIncidents.add(resolve);
        EventResult eventResult = createEventResult(resolve);
        log.debug("Event result {} for resolve incident {}", eventResult, resolve);
        return eventResult;
    }

    public Set<TriggerIncident> openIncidents() {
        Set<String> dedupKeysResolved = dedupKeysResolved();
        return openIncidents.stream()
                .filter(incident -> !dedupKeysResolved.contains(incident.getDedupKey()))
                .collect(Collectors.toSet());
    }

    public Set<ResolveIncident> resolvedIncidents() {
        return resolvedIncidents;
    }

    public Set<AcknowledgeIncident> acknowledgedIncidents() {
        return acknowledgedIncidents;
    }

    private EventResult createEventResult(Incident incident) {
        return EventResult.successEvent("success-" + incident.getEventAction().getEventType(), "Event processed", incident.getDedupKey());
    }

    public Set<String> dedupKeysResolved() {
        return resolvedIncidents.stream()
                .map(Incident::getDedupKey)
                .collect(Collectors.toSet());
    }

    private TriggerIncident updateTriggerIncidentWithKey(TriggerIncident incident, String dedupKey) {
        return TriggerIncident.TriggerIncidentBuilder
                .newBuilder(incident.getRoutingKey(), incident.getPayload())
                .setDedupKey(dedupKey)
                .build();
    }

}

package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of event used for incidents.
 */
public enum EventAction {

    TRIGGER("trigger"),
    ACKNOWLEDGE("acknowledge"),
    RESOLVE("resolve");

    private final String eventAction;

    EventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    @JsonValue
    public String getEventType() {
        return eventAction;
    }

    @Override
    public String toString() {
        return getEventType();
    }
}

package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of event used for incidents.
 */
public enum EventType {

    TRIGGER("trigger"),
    ACKNOWLEDGE("acknowledge"),
    RESOLVE("resolve");

    private final String eventType;

    EventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonValue
    public String getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return getEventType();
    }
}

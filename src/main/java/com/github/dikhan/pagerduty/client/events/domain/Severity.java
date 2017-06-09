package com.github.dikhan.pagerduty.client.events.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The perceived severity of the status the event is describing with respect to the affected system.
 */
public enum Severity {
    CRITICAL("critical"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info");

    private final String severity;

    Severity(String severity) {
        this.severity = severity;
    }

    @JsonValue
    public String getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return getSeverity();
    }
}

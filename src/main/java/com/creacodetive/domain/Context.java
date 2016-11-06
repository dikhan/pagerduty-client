package com.creacodetive.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The context field is an informational asset that can be attached to the incident.
 */
public class Context {

    @JsonProperty
    private final String type;

    @JsonCreator
    public Context(String type) {
        this.type = type;
    }

}

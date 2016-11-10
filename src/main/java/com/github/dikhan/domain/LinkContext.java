package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The link type is used to attach hyperlinks to an incident.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkContext extends Context {

    @JsonProperty
    private final String href;
    @JsonProperty
    private String text;

    @JsonCreator
    public LinkContext(String href) {
        super("link");
        this.href = href;
    }

    public String href() {
        return href;
    }

    public String text() {
        return text;
    }

    public void text(String text) {
        this.text = text;
    }
}

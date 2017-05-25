package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This link context is used to attach text links to the incident
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkContext {
    @JsonProperty("href")
    private final String href;
    @JsonProperty("text")
    private final String text;

    /**
     * @param href URL of the link to be attached.
     * @param text Plain text that describes the purpose of the link, and can be used as the link's text.
     */
    @JsonCreator
    public LinkContext(String href, String text) {
        this.href = href;
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public String getText() {
        return text;
    }
}

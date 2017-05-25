package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The image context is used to attach images to an incident. Images must be served via HTTPS.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageContext {

    @JsonProperty("src")
    private final String src;
    @JsonProperty("href")
    private String href;
    @JsonProperty("alt")
    private String alt;

    /**
     * @param src The source of the image being attached to the incident. This image must be served via HTTPS.
     */
    @JsonCreator
    public ImageContext(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public String getHref() {
        return href;
    }

    public String getAlt() {
        return alt;
    }

    /**
     * @param href optional URL; makes the image a clickable link.
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @param alt Optional alternative text for the image.
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }
}

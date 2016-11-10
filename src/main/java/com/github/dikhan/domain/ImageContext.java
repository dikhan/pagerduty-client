package com.github.dikhan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The image type is used to attach images to an incident. Images must be served via HTTPS.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageContext extends Context {

    @JsonProperty
    private final String src;
    @JsonProperty
    private String href;
    @JsonProperty
    private String alt;

    @JsonCreator
    public ImageContext(String src) {
        super("image");
        this.src = src;
    }

    public String src() {
        return src;
    }

    public String href() {
        return href;
    }

    public String alt() {
        return alt;
    }

    public void href(String href) {
        this.href = href;
    }

    public void alt(String alt) {
        this.alt = alt;
    }
}

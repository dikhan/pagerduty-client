package com.creacodetive.domain;

/**
 * The link type is used to attach hyperlinks to an incident.
 */
public class LinkContext extends Context {

    private final String href;
    private String text;

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

package com.creacodetive.domain;

/**
 * The image type is used to attach images to an incident. Images must be served via HTTPS.
 */
public class ImageContext extends Context {

    private final String src;
    private String href;
    private String alt;

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

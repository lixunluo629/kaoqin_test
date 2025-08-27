package org.springframework.boot.context.embedded;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/Compression.class */
public class Compression {
    private boolean enabled = false;
    private String[] mimeTypes = {"text/html", "text/xml", "text/plain", "text/css", "text/javascript", MappingJackson2JsonView.DEFAULT_JSONP_CONTENT_TYPE};
    private String[] excludedUserAgents = null;
    private int minResponseSize = 2048;

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getMimeTypes() {
        return this.mimeTypes;
    }

    public void setMimeTypes(String[] mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public int getMinResponseSize() {
        return this.minResponseSize;
    }

    public void setMinResponseSize(int minSize) {
        this.minResponseSize = minSize;
    }

    public String[] getExcludedUserAgents() {
        return this.excludedUserAgents;
    }

    public void setExcludedUserAgents(String[] excludedUserAgents) {
        this.excludedUserAgents = excludedUserAgents;
    }
}

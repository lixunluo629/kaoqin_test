package org.springframework.boot.autoconfigure.web;

import org.springframework.beans.factory.annotation.Value;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorProperties.class */
public class ErrorProperties {

    @Value("${error.path:/error}")
    private String path = "/error";
    private IncludeStacktrace includeStacktrace = IncludeStacktrace.NEVER;

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorProperties$IncludeStacktrace.class */
    public enum IncludeStacktrace {
        NEVER,
        ALWAYS,
        ON_TRACE_PARAM
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IncludeStacktrace getIncludeStacktrace() {
        return this.includeStacktrace;
    }

    public void setIncludeStacktrace(IncludeStacktrace includeStacktrace) {
        this.includeStacktrace = includeStacktrace;
    }
}

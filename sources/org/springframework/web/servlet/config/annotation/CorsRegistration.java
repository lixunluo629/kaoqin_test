package org.springframework.web.servlet.config.annotation;

import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/CorsRegistration.class */
public class CorsRegistration {
    private final String pathPattern;
    private final CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();

    public CorsRegistration(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    public CorsRegistration allowedOrigins(String... origins) {
        this.config.setAllowedOrigins(Arrays.asList(origins));
        return this;
    }

    public CorsRegistration allowedMethods(String... methods) {
        this.config.setAllowedMethods(Arrays.asList(methods));
        return this;
    }

    public CorsRegistration allowedHeaders(String... headers) {
        this.config.setAllowedHeaders(Arrays.asList(headers));
        return this;
    }

    public CorsRegistration exposedHeaders(String... headers) {
        this.config.setExposedHeaders(Arrays.asList(headers));
        return this;
    }

    public CorsRegistration maxAge(long maxAge) {
        this.config.setMaxAge(Long.valueOf(maxAge));
        return this;
    }

    public CorsRegistration allowCredentials(boolean allowCredentials) {
        this.config.setAllowCredentials(Boolean.valueOf(allowCredentials));
        return this;
    }

    protected String getPathPattern() {
        return this.pathPattern;
    }

    protected CorsConfiguration getCorsConfiguration() {
        return this.config;
    }
}

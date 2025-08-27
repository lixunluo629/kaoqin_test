package org.springframework.boot.context.config;

import org.springframework.core.io.Resource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ResourceNotFoundException.class */
public class ResourceNotFoundException extends RuntimeException {
    private final String propertyName;
    private final Resource resource;

    public ResourceNotFoundException(String propertyName, Resource resource) {
        super(String.format("%s defined by '%s' does not exist", resource, propertyName));
        this.propertyName = propertyName;
        this.resource = resource;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public Resource getResource() {
        return this.resource;
    }
}

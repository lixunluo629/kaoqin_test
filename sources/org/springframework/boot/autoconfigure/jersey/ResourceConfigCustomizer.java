package org.springframework.boot.autoconfigure.jersey;

import org.glassfish.jersey.server.ResourceConfig;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jersey/ResourceConfigCustomizer.class */
public interface ResourceConfigCustomizer {
    void customize(ResourceConfig resourceConfig);
}

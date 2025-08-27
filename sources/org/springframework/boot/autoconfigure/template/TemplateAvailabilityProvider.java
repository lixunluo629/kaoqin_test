package org.springframework.boot.autoconfigure.template;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/template/TemplateAvailabilityProvider.class */
public interface TemplateAvailabilityProvider {
    boolean isTemplateAvailable(String str, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader);
}

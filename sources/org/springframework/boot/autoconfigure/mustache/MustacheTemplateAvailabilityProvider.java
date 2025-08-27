package org.springframework.boot.autoconfigure.mustache;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheTemplateAvailabilityProvider.class */
public class MustacheTemplateAvailabilityProvider implements TemplateAvailabilityProvider {
    @Override // org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider
    public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        if (ClassUtils.isPresent("com.samskivert.mustache.Template", classLoader)) {
            PropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.mustache.");
            String prefix = resolver.getProperty("prefix", "classpath:/templates/");
            String suffix = resolver.getProperty("suffix", ".html");
            return resourceLoader.getResource(prefix + view + suffix).exists();
        }
        return false;
    }
}

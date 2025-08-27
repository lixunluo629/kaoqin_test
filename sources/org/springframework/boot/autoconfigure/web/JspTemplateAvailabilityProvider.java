package org.springframework.boot.autoconfigure.web;

import java.io.File;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/JspTemplateAvailabilityProvider.class */
public class JspTemplateAvailabilityProvider implements TemplateAvailabilityProvider {
    @Override // org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider
    public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        if (ClassUtils.isPresent("org.apache.jasper.compiler.JspConfig", classLoader)) {
            String resourceName = getResourceName(view, environment);
            if (resourceLoader.getResource(resourceName).exists()) {
                return true;
            }
            return new File("src/main/webapp", resourceName).exists();
        }
        return false;
    }

    private String getResourceName(String view, Environment environment) {
        PropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.mvc.view.");
        String prefix = resolver.getProperty("prefix", "");
        String suffix = resolver.getProperty("suffix", "");
        return prefix + view + suffix;
    }
}

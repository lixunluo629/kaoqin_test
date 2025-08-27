package org.springframework.boot.autoconfigure.template;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/template/PathBasedTemplateAvailabilityProvider.class */
public abstract class PathBasedTemplateAvailabilityProvider implements TemplateAvailabilityProvider {
    private final String className;
    private final Class<? extends TemplateAvailabilityProperties> propertiesClass;
    private final String propertyPrefix;

    public PathBasedTemplateAvailabilityProvider(String className, Class<? extends TemplateAvailabilityProperties> propertiesClass, String propertyPrefix) {
        this.className = className;
        this.propertiesClass = propertiesClass;
        this.propertyPrefix = propertyPrefix;
    }

    @Override // org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider
    public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        if (ClassUtils.isPresent(this.className, classLoader)) {
            TemplateAvailabilityProperties properties = (TemplateAvailabilityProperties) BeanUtils.instantiateClass(this.propertiesClass);
            RelaxedDataBinder binder = new RelaxedDataBinder(properties, this.propertyPrefix);
            binder.bind(new PropertySourcesPropertyValues(((ConfigurableEnvironment) environment).getPropertySources()));
            return isTemplateAvailable(view, resourceLoader, properties);
        }
        return false;
    }

    private boolean isTemplateAvailable(String view, ResourceLoader resourceLoader, TemplateAvailabilityProperties properties) {
        String location = properties.getPrefix() + view + properties.getSuffix();
        for (String path : properties.getLoaderPath()) {
            if (resourceLoader.getResource(path + location).exists()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/template/PathBasedTemplateAvailabilityProvider$TemplateAvailabilityProperties.class */
    public static abstract class TemplateAvailabilityProperties {
        private String prefix;
        private String suffix;

        protected abstract List<String> getLoaderPath();

        protected TemplateAvailabilityProperties(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }
}

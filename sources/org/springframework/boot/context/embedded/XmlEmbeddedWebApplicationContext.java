package org.springframework.boot.context.embedded;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/XmlEmbeddedWebApplicationContext.class */
public class XmlEmbeddedWebApplicationContext extends EmbeddedWebApplicationContext {
    private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

    public XmlEmbeddedWebApplicationContext() {
        this.reader.setEnvironment(getEnvironment());
    }

    public XmlEmbeddedWebApplicationContext(Resource... resources) {
        load(resources);
        refresh();
    }

    public XmlEmbeddedWebApplicationContext(String... resourceLocations) {
        load(resourceLocations);
        refresh();
    }

    public XmlEmbeddedWebApplicationContext(Class<?> relativeClass, String... resourceNames) {
        load(relativeClass, resourceNames);
        refresh();
    }

    public void setValidating(boolean validating) {
        this.reader.setValidating(validating);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ConfigurableApplicationContext
    public void setEnvironment(ConfigurableEnvironment environment) {
        super.setEnvironment(environment);
        this.reader.setEnvironment(getEnvironment());
    }

    public final void load(Resource... resources) {
        this.reader.loadBeanDefinitions(resources);
    }

    public final void load(String... resourceLocations) {
        this.reader.loadBeanDefinitions(resourceLocations);
    }

    public final void load(Class<?> relativeClass, String... resourceNames) {
        Resource[] resources = new Resource[resourceNames.length];
        for (int i = 0; i < resourceNames.length; i++) {
            resources[i] = new ClassPathResource(resourceNames[i], relativeClass);
        }
        this.reader.loadBeanDefinitions(resources);
    }
}

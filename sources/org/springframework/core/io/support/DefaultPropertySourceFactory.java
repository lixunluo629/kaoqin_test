package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/support/DefaultPropertySourceFactory.class */
public class DefaultPropertySourceFactory implements PropertySourceFactory {
    @Override // org.springframework.core.io.support.PropertySourceFactory
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        return name != null ? new ResourcePropertySource(name, resource) : new ResourcePropertySource(resource);
    }
}

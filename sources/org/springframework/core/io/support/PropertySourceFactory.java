package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/support/PropertySourceFactory.class */
public interface PropertySourceFactory {
    PropertySource<?> createPropertySource(String str, EncodedResource encodedResource) throws IOException;
}

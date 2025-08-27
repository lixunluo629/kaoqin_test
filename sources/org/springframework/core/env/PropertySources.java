package org.springframework.core.env;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/PropertySources.class */
public interface PropertySources extends Iterable<PropertySource<?>> {
    boolean contains(String str);

    PropertySource<?> get(String str);
}

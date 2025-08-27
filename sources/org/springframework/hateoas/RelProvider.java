package org.springframework.hateoas;

import org.springframework.plugin.core.Plugin;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/RelProvider.class */
public interface RelProvider extends Plugin<Class<?>> {
    String getItemResourceRelFor(Class<?> cls);

    String getCollectionResourceRelFor(Class<?> cls);
}

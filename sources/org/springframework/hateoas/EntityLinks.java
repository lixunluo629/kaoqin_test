package org.springframework.hateoas;

import org.springframework.plugin.core.Plugin;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/EntityLinks.class */
public interface EntityLinks extends Plugin<Class<?>> {
    LinkBuilder linkFor(Class<?> cls);

    LinkBuilder linkFor(Class<?> cls, Object... objArr);

    LinkBuilder linkForSingleResource(Class<?> cls, Object obj);

    LinkBuilder linkForSingleResource(Identifiable<?> identifiable);

    Link linkToCollectionResource(Class<?> cls);

    Link linkToSingleResource(Class<?> cls, Object obj);

    Link linkToSingleResource(Identifiable<?> identifiable);
}

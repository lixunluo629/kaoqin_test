package org.springframework.hateoas.core;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/EmbeddedWrapper.class */
public interface EmbeddedWrapper {
    String getRel();

    boolean hasRel(String str);

    boolean isCollectionValue();

    Object getValue();

    Class<?> getRelTargetType();
}

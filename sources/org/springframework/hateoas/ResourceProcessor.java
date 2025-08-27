package org.springframework.hateoas;

import org.springframework.hateoas.ResourceSupport;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/ResourceProcessor.class */
public interface ResourceProcessor<T extends ResourceSupport> {
    T process(T t);
}

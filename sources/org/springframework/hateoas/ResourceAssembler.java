package org.springframework.hateoas;

import org.springframework.hateoas.ResourceSupport;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/ResourceAssembler.class */
public interface ResourceAssembler<T, D extends ResourceSupport> {
    D toResource(T t);
}

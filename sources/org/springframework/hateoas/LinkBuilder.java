package org.springframework.hateoas;

import java.net.URI;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/LinkBuilder.class */
public interface LinkBuilder {
    LinkBuilder slash(Object obj);

    LinkBuilder slash(Identifiable<?> identifiable);

    URI toUri();

    Link withRel(String str);

    Link withSelfRel();
}

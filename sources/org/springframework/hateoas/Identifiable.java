package org.springframework.hateoas;

import java.io.Serializable;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/Identifiable.class */
public interface Identifiable<ID extends Serializable> {
    ID getId();
}

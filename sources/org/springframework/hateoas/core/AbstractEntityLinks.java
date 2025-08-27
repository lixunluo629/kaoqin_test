package org.springframework.hateoas.core;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/AbstractEntityLinks.class */
public abstract class AbstractEntityLinks implements EntityLinks {
    @Override // org.springframework.hateoas.EntityLinks
    public Link linkToSingleResource(Identifiable<?> entity) {
        Assert.notNull(entity);
        return linkToSingleResource(entity.getClass(), entity.getId());
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkForSingleResource(Class<?> type, Object id) {
        return linkFor(type).slash(id);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkForSingleResource(Identifiable<?> entity) {
        Assert.notNull(entity);
        return linkForSingleResource(entity.getClass(), entity.getId());
    }
}

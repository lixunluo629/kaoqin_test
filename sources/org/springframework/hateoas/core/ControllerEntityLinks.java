package org.springframework.hateoas.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.LinkBuilderFactory;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/ControllerEntityLinks.class */
public class ControllerEntityLinks extends AbstractEntityLinks {
    private final Map<Class<?>, Class<?>> entityToController;
    private final LinkBuilderFactory<? extends LinkBuilder> linkBuilderFactory;

    public ControllerEntityLinks(Iterable<? extends Class<?>> controllerTypes, LinkBuilderFactory<? extends LinkBuilder> linkBuilderFactory) {
        Assert.notNull(controllerTypes);
        Assert.notNull(linkBuilderFactory);
        this.linkBuilderFactory = linkBuilderFactory;
        this.entityToController = new HashMap();
        for (Class<?> controllerType : controllerTypes) {
            registerControllerClass(controllerType);
        }
    }

    private void registerControllerClass(Class<?> controllerType) {
        Assert.notNull(controllerType, "Controller type must nor be null!");
        ExposesResourceFor annotation = (ExposesResourceFor) AnnotationUtils.findAnnotation(controllerType, ExposesResourceFor.class);
        if (annotation != null) {
            this.entityToController.put(annotation.value(), controllerType);
            return;
        }
        throw new IllegalArgumentException(String.format("Controller %s must be annotated with @ExposesResourceFor!", controllerType.getName()));
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkFor(Class<?> entity) {
        return linkFor(entity, new Object[0]);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkFor(Class<?> entity, Object... parameters) {
        Assert.notNull(entity);
        Class<?> controllerType = this.entityToController.get(entity);
        if (controllerType == null) {
            throw new IllegalArgumentException(String.format("Type %s is not managed by a Spring MVC controller. Make sure you have annotated your controller with %s!", entity.getName(), ExposesResourceFor.class.getName()));
        }
        return this.linkBuilderFactory.linkTo(controllerType, parameters);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public Link linkToCollectionResource(Class<?> entity) {
        return linkFor(entity).withSelfRel();
    }

    @Override // org.springframework.hateoas.EntityLinks
    public Link linkToSingleResource(Class<?> entity, Object id) {
        return linkFor(entity).slash(id).withSelfRel();
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return this.entityToController.containsKey(delimiter);
    }
}

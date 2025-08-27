package org.springframework.hateoas.mvc;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ResourceAssemblerSupport.class */
public abstract class ResourceAssemblerSupport<T, D extends ResourceSupport> implements ResourceAssembler<T, D> {
    private final Class<?> controllerClass;
    private final Class<D> resourceType;

    public ResourceAssemblerSupport(Class<?> controllerClass, Class<D> resourceType) {
        Assert.notNull(controllerClass);
        Assert.notNull(resourceType);
        this.controllerClass = controllerClass;
        this.resourceType = resourceType;
    }

    public List<D> toResources(Iterable<? extends T> entities) {
        Assert.notNull(entities);
        List<D> result = new ArrayList<>();
        for (T entity : entities) {
            result.add(toResource(entity));
        }
        return result;
    }

    protected D createResourceWithId(Object obj, T t) {
        return (D) createResourceWithId(obj, t, new Object[0]);
    }

    protected D createResourceWithId(Object obj, T t, Object... objArr) {
        Assert.notNull(t);
        Assert.notNull(obj);
        D d = (D) instantiateResource(t);
        d.add(ControllerLinkBuilder.linkTo(this.controllerClass, objArr).slash(obj).withSelfRel());
        return d;
    }

    protected D instantiateResource(T entity) {
        return (D) BeanUtils.instantiateClass(this.resourceType);
    }
}

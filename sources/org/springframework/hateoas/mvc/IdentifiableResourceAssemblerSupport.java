package org.springframework.hateoas.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/IdentifiableResourceAssemblerSupport.class */
public abstract class IdentifiableResourceAssemblerSupport<T extends Identifiable<?>, D extends ResourceSupport> extends ResourceAssemblerSupport<T, D> {
    private final Class<?> controllerClass;

    public IdentifiableResourceAssemblerSupport(Class<?> controllerClass, Class<D> resourceType) {
        super(controllerClass, resourceType);
        this.controllerClass = controllerClass;
    }

    protected D createResource(T t) {
        return (D) createResource(t, new Object[0]);
    }

    protected D createResource(T t, Object... objArr) {
        return (D) createResourceWithId((Object) t.getId(), (Serializable) t, objArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.mvc.ResourceAssemblerSupport
    public D createResourceWithId(Object id, T entity, Object... parameters) {
        Assert.notNull(entity);
        Assert.notNull(id);
        D instance = instantiateResource(entity);
        instance.add(ControllerLinkBuilder.linkTo(this.controllerClass, unwrapIdentifyables(parameters)).slash(id).withSelfRel());
        return instance;
    }

    private Object[] unwrapIdentifyables(Object[] values) {
        List<Object> result = new ArrayList<>(values.length);
        for (Object element : Arrays.asList(values)) {
            result.add(element instanceof Identifiable ? ((Identifiable) element).getId() : element);
        }
        return result.toArray();
    }
}

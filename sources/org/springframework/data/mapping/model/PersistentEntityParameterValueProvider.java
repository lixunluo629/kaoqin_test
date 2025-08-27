package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PersistentEntityParameterValueProvider.class */
public class PersistentEntityParameterValueProvider<P extends PersistentProperty<P>> implements ParameterValueProvider<P> {
    private final PersistentEntity<?, P> entity;
    private final PropertyValueProvider<P> provider;
    private final Object parent;

    public PersistentEntityParameterValueProvider(PersistentEntity<?, P> entity, PropertyValueProvider<P> provider, Object parent) {
        Assert.notNull(entity, "Entity must not be null!");
        Assert.notNull(provider, "Provider must not be null!");
        this.entity = entity;
        this.provider = provider;
        this.parent = parent;
    }

    @Override // org.springframework.data.mapping.model.ParameterValueProvider
    public <T> T getParameterValue(PreferredConstructor.Parameter<T, P> parameter) {
        if (this.entity.getPersistenceConstructor().isEnclosingClassParameter(parameter)) {
            return (T) this.parent;
        }
        PersistentProperty persistentProperty = this.entity.getPersistentProperty(parameter.getName());
        if (persistentProperty == null) {
            throw new MappingException(String.format("No property %s found on entity %s to bind constructor parameter to!", parameter.getName(), this.entity.getType()));
        }
        return (T) this.provider.getPropertyValue(persistentProperty);
    }
}

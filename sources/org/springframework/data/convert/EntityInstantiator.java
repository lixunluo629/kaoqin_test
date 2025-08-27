package org.springframework.data.convert;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.ParameterValueProvider;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/EntityInstantiator.class */
public interface EntityInstantiator {
    <T, E extends PersistentEntity<? extends T, P>, P extends PersistentProperty<P>> T createInstance(E e, ParameterValueProvider<P> parameterValueProvider);
}

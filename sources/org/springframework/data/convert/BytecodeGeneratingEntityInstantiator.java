package org.springframework.data.convert;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.ParameterValueProvider;

@Deprecated
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/BytecodeGeneratingEntityInstantiator.class */
public enum BytecodeGeneratingEntityInstantiator implements EntityInstantiator {
    INSTANCE;

    private final ClassGeneratingEntityInstantiator delegate = new ClassGeneratingEntityInstantiator();

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/BytecodeGeneratingEntityInstantiator$ObjectInstantiator.class */
    public interface ObjectInstantiator {
        Object newInstance(Object... objArr);
    }

    BytecodeGeneratingEntityInstantiator() {
    }

    @Override // org.springframework.data.convert.EntityInstantiator
    public <T, E extends PersistentEntity<? extends T, P>, P extends PersistentProperty<P>> T createInstance(E e, ParameterValueProvider<P> parameterValueProvider) {
        return (T) this.delegate.createInstance(e, parameterValueProvider);
    }
}

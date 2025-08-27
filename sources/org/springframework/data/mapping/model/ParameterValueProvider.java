package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/ParameterValueProvider.class */
public interface ParameterValueProvider<P extends PersistentProperty<P>> {
    <T> T getParameterValue(PreferredConstructor.Parameter<T, P> parameter);
}

package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PropertyValueProvider.class */
public interface PropertyValueProvider<P extends PersistentProperty<P>> {
    <T> T getPropertyValue(P p);
}

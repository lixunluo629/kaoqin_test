package org.springframework.data.mapping;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PropertyHandler.class */
public interface PropertyHandler<P extends PersistentProperty<P>> {
    void doWithPersistentProperty(P p);
}

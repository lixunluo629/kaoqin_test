package org.springframework.data.mapping.model;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/MutablePersistentEntity.class */
public interface MutablePersistentEntity<T, P extends PersistentProperty<P>> extends PersistentEntity<T, P> {
    void addPersistentProperty(P p);

    void addAssociation(Association<P> association);

    void verify() throws MappingException;

    void setPersistentPropertyAccessorFactory(PersistentPropertyAccessorFactory persistentPropertyAccessorFactory);
}

package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentPropertyAccessor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PersistentPropertyAccessorFactory.class */
public interface PersistentPropertyAccessorFactory {
    PersistentPropertyAccessor getPropertyAccessor(PersistentEntity<?, ?> persistentEntity, Object obj);

    boolean isSupported(PersistentEntity<?, ?> persistentEntity);
}

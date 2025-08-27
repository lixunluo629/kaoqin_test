package org.springframework.data.keyvalue.core.mapping;

import org.springframework.data.mapping.model.MutablePersistentEntity;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/KeyValuePersistentEntity.class */
public interface KeyValuePersistentEntity<T> extends MutablePersistentEntity<T, KeyValuePersistentProperty> {
    String getKeySpace();
}

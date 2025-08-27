package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/FieldNamingStrategy.class */
public interface FieldNamingStrategy {
    String getFieldName(PersistentProperty<?> persistentProperty);
}

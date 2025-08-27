package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PropertyNameFieldNamingStrategy.class */
public enum PropertyNameFieldNamingStrategy implements FieldNamingStrategy {
    INSTANCE;

    @Override // org.springframework.data.mapping.model.FieldNamingStrategy
    public String getFieldName(PersistentProperty<?> property) {
        return property.getName();
    }
}

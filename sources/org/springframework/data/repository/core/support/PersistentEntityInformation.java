package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.data.mapping.PersistentEntity;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/PersistentEntityInformation.class */
public class PersistentEntityInformation<T, ID extends Serializable> extends AbstractEntityInformation<T, ID> {
    private final PersistentEntity<T, ?> persistentEntity;

    public PersistentEntityInformation(PersistentEntity<T, ?> entity) {
        super(entity.getType());
        this.persistentEntity = entity;
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public ID getId(T entity) {
        return (ID) this.persistentEntity.getIdentifierAccessor(entity).getIdentifier();
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public Class<ID> getIdType() {
        return (Class<ID>) this.persistentEntity.getIdProperty().getType();
    }
}

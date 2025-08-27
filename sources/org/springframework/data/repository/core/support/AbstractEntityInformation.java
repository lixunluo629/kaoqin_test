package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/AbstractEntityInformation.class */
public abstract class AbstractEntityInformation<T, ID extends Serializable> implements EntityInformation<T, ID> {
    private final Class<T> domainClass;

    public AbstractEntityInformation(Class<T> domainClass) {
        Assert.notNull(domainClass, "DomainClass must not be null!");
        this.domainClass = domainClass;
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public boolean isNew(T entity) {
        ID id = getId(entity);
        Class<ID> idType = getIdType();
        if (!idType.isPrimitive()) {
            return id == null;
        }
        if (id instanceof Number) {
            return ((Number) id).longValue() == 0;
        }
        throw new IllegalArgumentException(String.format("Unsupported primitive id type %s!", idType));
    }

    @Override // org.springframework.data.repository.core.EntityMetadata
    public Class<T> getJavaType() {
        return this.domainClass;
    }
}

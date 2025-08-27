package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/DelegatingEntityInformation.class */
public class DelegatingEntityInformation<T, ID extends Serializable> implements EntityInformation<T, ID> {
    private final EntityInformation<T, ID> delegate;

    public DelegatingEntityInformation(EntityInformation<T, ID> delegate) {
        Assert.notNull(delegate, "Delegate EnittyInformation must not be null!");
        this.delegate = delegate;
    }

    @Override // org.springframework.data.repository.core.EntityMetadata
    public Class<T> getJavaType() {
        return this.delegate.getJavaType();
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public boolean isNew(T entity) {
        return this.delegate.isNew(entity);
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public ID getId(T t) {
        return (ID) this.delegate.getId(t);
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public Class<ID> getIdType() {
        return this.delegate.getIdType();
    }
}

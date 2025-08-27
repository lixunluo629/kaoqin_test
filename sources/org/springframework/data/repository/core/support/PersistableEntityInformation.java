package org.springframework.data.repository.core.support;

import java.io.Serializable;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Persistable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/PersistableEntityInformation.class */
public class PersistableEntityInformation<T extends Persistable<ID>, ID extends Serializable> extends AbstractEntityInformation<T, ID> {
    private Class<ID> idClass;

    public PersistableEntityInformation(Class<T> cls) {
        super(cls);
        this.idClass = (Class<ID>) GenericTypeResolver.resolveTypeArgument(cls, Persistable.class);
    }

    @Override // org.springframework.data.repository.core.support.AbstractEntityInformation, org.springframework.data.repository.core.EntityInformation
    public boolean isNew(T entity) {
        return entity.isNew();
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public ID getId(T t) {
        return (ID) t.getId();
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public Class<ID> getIdType() {
        return this.idClass;
    }
}

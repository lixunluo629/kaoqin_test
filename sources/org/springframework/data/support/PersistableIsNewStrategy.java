package org.springframework.data.support;

import org.springframework.data.domain.Persistable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/support/PersistableIsNewStrategy.class */
public enum PersistableIsNewStrategy implements IsNewStrategy {
    INSTANCE;

    @Override // org.springframework.data.support.IsNewStrategy
    public boolean isNew(Object entity) {
        if (!(entity instanceof Persistable)) {
            throw new IllegalArgumentException(String.format("Given object of type %s does not implement %s!", entity.getClass(), Persistable.class));
        }
        return ((Persistable) entity).isNew();
    }
}

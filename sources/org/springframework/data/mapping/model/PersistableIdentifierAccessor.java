package org.springframework.data.mapping.model;

import org.springframework.data.domain.Persistable;
import org.springframework.data.mapping.IdentifierAccessor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/PersistableIdentifierAccessor.class */
class PersistableIdentifierAccessor implements IdentifierAccessor {
    private final Persistable<?> target;

    public PersistableIdentifierAccessor(Persistable<?> target) {
        this.target = target;
    }

    @Override // org.springframework.data.mapping.IdentifierAccessor
    public Object getIdentifier() {
        return this.target.getId();
    }
}

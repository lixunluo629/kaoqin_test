package org.springframework.data.mapping.model;

import org.springframework.data.mapping.IdentifierAccessor;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/IdPropertyIdentifierAccessor.class */
public class IdPropertyIdentifierAccessor implements IdentifierAccessor {
    private final PersistentPropertyAccessor accessor;
    private final PersistentProperty<?> idProperty;

    public IdPropertyIdentifierAccessor(PersistentEntity<?, ?> entity, Object target) {
        Assert.notNull(entity, "PersistentEntity must not be 'null'");
        Assert.isTrue(entity.hasIdProperty(), "PersistentEntity does not have an identifier property!");
        this.idProperty = entity.getIdProperty();
        this.accessor = entity.getPropertyAccessor(target);
    }

    @Override // org.springframework.data.mapping.IdentifierAccessor
    public Object getIdentifier() {
        return this.accessor.getProperty(this.idProperty);
    }
}

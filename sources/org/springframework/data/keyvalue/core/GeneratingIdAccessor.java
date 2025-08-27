package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import org.springframework.data.mapping.IdentifierAccessor;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/GeneratingIdAccessor.class */
class GeneratingIdAccessor implements IdentifierAccessor {
    private final PersistentPropertyAccessor accessor;
    private final PersistentProperty<?> identifierProperty;
    private final IdentifierGenerator generator;

    public GeneratingIdAccessor(PersistentPropertyAccessor accessor, PersistentProperty<?> identifierProperty, IdentifierGenerator generator) {
        Assert.notNull(accessor, "PersistentPropertyAccessor must not be null!");
        Assert.notNull(identifierProperty, "Identifier property must not be null!");
        Assert.notNull(generator, "IdentifierGenerator must not be null!");
        this.accessor = accessor;
        this.identifierProperty = identifierProperty;
        this.generator = generator;
    }

    @Override // org.springframework.data.mapping.IdentifierAccessor
    public Object getIdentifier() {
        return this.accessor.getProperty(this.identifierProperty);
    }

    public Object getOrGenerateIdentifier() {
        Serializable existingIdentifier = (Serializable) getIdentifier();
        if (existingIdentifier != null) {
            return existingIdentifier;
        }
        Object generatedIdentifier = this.generator.generateIdentifierOfType(this.identifierProperty.getTypeInformation());
        this.accessor.setProperty(this.identifierProperty, generatedIdentifier);
        return generatedIdentifier;
    }
}

package org.springframework.data.keyvalue.core.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/KeyValuePersistentProperty.class */
public class KeyValuePersistentProperty extends AnnotationBasedPersistentProperty<KeyValuePersistentProperty> {
    public KeyValuePersistentProperty(Field field, PropertyDescriptor propertyDescriptor, PersistentEntity<?, KeyValuePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
        super(field, propertyDescriptor, owner, simpleTypeHolder);
    }

    @Override // org.springframework.data.mapping.model.AbstractPersistentProperty
    protected Association<KeyValuePersistentProperty> createAssociation() {
        return new Association<>(this, null);
    }
}

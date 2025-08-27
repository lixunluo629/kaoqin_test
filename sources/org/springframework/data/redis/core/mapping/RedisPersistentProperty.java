package org.springframework.data.redis.core.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisPersistentProperty.class */
public class RedisPersistentProperty extends KeyValuePersistentProperty {
    private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet();

    static {
        SUPPORTED_ID_PROPERTY_NAMES.add("id");
    }

    public RedisPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, PersistentEntity<?, KeyValuePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
        super(field, propertyDescriptor, owner, simpleTypeHolder);
    }

    @Override // org.springframework.data.mapping.model.AnnotationBasedPersistentProperty, org.springframework.data.mapping.PersistentProperty
    public boolean isIdProperty() {
        if (super.isIdProperty()) {
            return true;
        }
        return SUPPORTED_ID_PROPERTY_NAMES.contains(getName());
    }
}

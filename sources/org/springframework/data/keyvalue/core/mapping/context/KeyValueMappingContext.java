package org.springframework.data.keyvalue.core.mapping.context;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import org.springframework.data.keyvalue.core.mapping.BasicKeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/context/KeyValueMappingContext.class */
public class KeyValueMappingContext extends AbstractMappingContext<KeyValuePersistentEntity<?>, KeyValuePersistentProperty> {
    private KeySpaceResolver fallbackKeySpaceResolver;

    public void setFallbackKeySpaceResolver(KeySpaceResolver fallbackKeySpaceResolver) {
        this.fallbackKeySpaceResolver = fallbackKeySpaceResolver;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.mapping.context.AbstractMappingContext
    public <T> KeyValuePersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
        return new BasicKeyValuePersistentEntity(typeInformation, this.fallbackKeySpaceResolver);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.mapping.context.AbstractMappingContext
    public KeyValuePersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor, KeyValuePersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
        return new KeyValuePersistentProperty(field, descriptor, owner, simpleTypeHolder);
    }
}

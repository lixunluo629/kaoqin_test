package org.springframework.data.keyvalue.core.mapping;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/BasicKeyValuePersistentEntity.class */
public class BasicKeyValuePersistentEntity<T> extends BasicPersistentEntity<T, KeyValuePersistentProperty> implements KeyValuePersistentEntity<T> {
    private static final KeySpaceResolver DEFAULT_FALLBACK_RESOLVER = ClassNameKeySpaceResolver.INSTANCE;
    private final String keyspace;

    public BasicKeyValuePersistentEntity(TypeInformation<T> information, KeySpaceResolver fallbackKeySpaceResolver) {
        super(information);
        this.keyspace = detectKeySpace(information.getType(), fallbackKeySpaceResolver);
    }

    private static String detectKeySpace(Class<?> type, KeySpaceResolver fallback) throws SecurityException {
        String keySpace = AnnotationBasedKeySpaceResolver.INSTANCE.resolveKeySpace(type);
        if (StringUtils.hasText(keySpace)) {
            return keySpace;
        }
        return (fallback == null ? DEFAULT_FALLBACK_RESOLVER : fallback).resolveKeySpace(type);
    }

    @Override // org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity
    public String getKeySpace() {
        return this.keyspace;
    }
}

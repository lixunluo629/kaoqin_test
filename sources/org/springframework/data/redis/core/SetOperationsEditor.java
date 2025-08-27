package org.springframework.data.redis.core;

import java.beans.PropertyEditorSupport;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/SetOperationsEditor.class */
class SetOperationsEditor extends PropertyEditorSupport {
    SetOperationsEditor() {
    }

    public void setValue(Object value) {
        if (value instanceof RedisOperations) {
            super.setValue(((RedisOperations) value).opsForSet());
            return;
        }
        throw new IllegalArgumentException("Editor supports only conversion of type " + RedisOperations.class);
    }
}

package org.springframework.data.redis.core;

import java.beans.PropertyEditorSupport;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ZSetOperationsEditor.class */
class ZSetOperationsEditor extends PropertyEditorSupport {
    ZSetOperationsEditor() {
    }

    public void setValue(Object value) {
        if (value instanceof RedisOperations) {
            super.setValue(((RedisOperations) value).opsForZSet());
            return;
        }
        throw new IllegalArgumentException("Editor supports only conversion of type " + RedisOperations.class);
    }
}

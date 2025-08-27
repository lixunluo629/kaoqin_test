package org.springframework.cache.support;

import org.springframework.cache.Cache;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/support/SimpleValueWrapper.class */
public class SimpleValueWrapper implements Cache.ValueWrapper {
    private final Object value;

    public SimpleValueWrapper(Object value) {
        this.value = value;
    }

    @Override // org.springframework.cache.Cache.ValueWrapper
    public Object get() {
        return this.value;
    }
}

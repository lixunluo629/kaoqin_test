package org.springframework.cache.support;

import java.io.Serializable;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/support/NullValue.class */
public final class NullValue implements Serializable {
    public static final Object INSTANCE = new NullValue();
    private static final long serialVersionUID = 1;

    private NullValue() {
    }

    private Object readResolve() {
        return INSTANCE;
    }
}

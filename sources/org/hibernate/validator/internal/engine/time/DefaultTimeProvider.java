package org.hibernate.validator.internal.engine.time;

import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/time/DefaultTimeProvider.class */
public class DefaultTimeProvider implements TimeProvider {
    private static final DefaultTimeProvider INSTANCE = new DefaultTimeProvider();

    private DefaultTimeProvider() {
    }

    public static final DefaultTimeProvider getInstance() {
        return INSTANCE;
    }

    @Override // org.hibernate.validator.spi.time.TimeProvider
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}

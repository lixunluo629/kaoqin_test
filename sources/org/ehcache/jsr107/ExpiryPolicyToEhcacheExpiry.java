package org.ehcache.jsr107;

import java.io.Closeable;
import java.io.IOException;
import javax.cache.expiry.ExpiryPolicy;
import org.ehcache.ValueSupplier;
import org.ehcache.expiry.Duration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/ExpiryPolicyToEhcacheExpiry.class */
class ExpiryPolicyToEhcacheExpiry<K, V> extends Eh107Expiry<K, V> implements Closeable {
    private final ExpiryPolicy expiryPolicy;

    ExpiryPolicyToEhcacheExpiry(ExpiryPolicy expiryPolicy) {
        this.expiryPolicy = expiryPolicy;
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForCreation(K key, V value) {
        try {
            javax.cache.expiry.Duration duration = this.expiryPolicy.getExpiryForCreation();
            if (duration.isEternal()) {
                return Duration.INFINITE;
            }
            return new Duration(duration.getDurationAmount(), duration.getTimeUnit());
        } catch (Throwable th) {
            return Duration.ZERO;
        }
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForAccess(K key, ValueSupplier<? extends V> value) {
        if (isShortCircuitAccessCalls()) {
            return null;
        }
        try {
            javax.cache.expiry.Duration duration = this.expiryPolicy.getExpiryForAccess();
            if (duration == null) {
                return null;
            }
            if (duration.isEternal()) {
                return Duration.INFINITE;
            }
            return new Duration(duration.getDurationAmount(), duration.getTimeUnit());
        } catch (Throwable th) {
            return Duration.ZERO;
        }
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForUpdate(K key, ValueSupplier<? extends V> oldValue, V newValue) {
        try {
            javax.cache.expiry.Duration duration = this.expiryPolicy.getExpiryForUpdate();
            if (duration == null) {
                return null;
            }
            if (duration.isEternal()) {
                return Duration.INFINITE;
            }
            return new Duration(duration.getDurationAmount(), duration.getTimeUnit());
        } catch (Throwable th) {
            return Duration.ZERO;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.expiryPolicy instanceof Closeable) {
            this.expiryPolicy.close();
        }
    }
}

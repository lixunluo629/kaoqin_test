package org.ehcache.jsr107;

import org.ehcache.ValueSupplier;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EhcacheExpiryWrapper.class */
class EhcacheExpiryWrapper<K, V> extends Eh107Expiry<K, V> {
    private final Expiry<? super K, ? super V> wrappedExpiry;

    EhcacheExpiryWrapper(Expiry<? super K, ? super V> wrappedExpiry) {
        this.wrappedExpiry = wrappedExpiry;
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForCreation(K key, V value) {
        return this.wrappedExpiry.getExpiryForCreation(key, value);
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForAccess(K key, ValueSupplier<? extends V> value) {
        return this.wrappedExpiry.getExpiryForAccess(key, value);
    }

    @Override // org.ehcache.expiry.Expiry
    public Duration getExpiryForUpdate(K key, ValueSupplier<? extends V> oldValue, V newValue) {
        return this.wrappedExpiry.getExpiryForUpdate(key, oldValue, newValue);
    }
}

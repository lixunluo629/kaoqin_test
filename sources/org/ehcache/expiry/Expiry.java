package org.ehcache.expiry;

import org.ehcache.ValueSupplier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/expiry/Expiry.class */
public interface Expiry<K, V> {
    Duration getExpiryForCreation(K k, V v);

    Duration getExpiryForAccess(K k, ValueSupplier<? extends V> valueSupplier);

    Duration getExpiryForUpdate(K k, ValueSupplier<? extends V> valueSupplier, V v);
}

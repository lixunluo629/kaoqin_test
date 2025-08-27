package org.ehcache.core.internal.util;

import org.ehcache.ValueSupplier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/ValueSuppliers.class */
public class ValueSuppliers {
    public static <V> ValueSupplier<V> supplierOf(final V value) {
        return new ValueSupplier<V>() { // from class: org.ehcache.core.internal.util.ValueSuppliers.1
            @Override // org.ehcache.ValueSupplier
            public V value() {
                return (V) value;
            }
        };
    }

    private ValueSuppliers() {
    }
}

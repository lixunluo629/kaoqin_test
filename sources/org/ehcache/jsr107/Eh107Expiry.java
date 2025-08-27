package org.ehcache.jsr107;

import org.ehcache.expiry.Expiry;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Expiry.class */
abstract class Eh107Expiry<K, V> implements Expiry<K, V> {
    private final ThreadLocal<Object> shortCircuitAccess = new ThreadLocal<>();

    Eh107Expiry() {
    }

    void enableShortCircuitAccessCalls() {
        this.shortCircuitAccess.set(this);
    }

    void disableShortCircuitAccessCalls() {
        this.shortCircuitAccess.remove();
    }

    boolean isShortCircuitAccessCalls() {
        return this.shortCircuitAccess.get() != null;
    }
}

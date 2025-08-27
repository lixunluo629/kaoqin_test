package org.ehcache;

import java.io.Closeable;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/UserManagedCache.class */
public interface UserManagedCache<K, V> extends Cache<K, V>, Closeable {
    void init() throws StateTransitionException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws StateTransitionException;

    Status getStatus();
}

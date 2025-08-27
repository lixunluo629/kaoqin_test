package org.ehcache.impl.copy;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/copy/IdentityCopier.class */
public final class IdentityCopier<T> extends ReadWriteCopier<T> {
    @Override // org.ehcache.impl.copy.ReadWriteCopier
    public T copy(T obj) {
        return obj;
    }
}

package org.ehcache.impl.copy;

import org.ehcache.spi.copy.Copier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/copy/ReadWriteCopier.class */
public abstract class ReadWriteCopier<T> implements Copier<T> {
    public abstract T copy(T t);

    @Override // org.ehcache.spi.copy.Copier
    public T copyForRead(T obj) {
        return copy(obj);
    }

    @Override // org.ehcache.spi.copy.Copier
    public T copyForWrite(T obj) {
        return copy(obj);
    }
}

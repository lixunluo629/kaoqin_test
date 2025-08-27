package org.ehcache.jsr107;

import org.ehcache.impl.copy.ReadWriteCopier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107IdentityCopier.class */
class Eh107IdentityCopier<T> extends ReadWriteCopier<T> {
    @Override // org.ehcache.impl.copy.ReadWriteCopier
    public T copy(T obj) {
        return obj;
    }
}

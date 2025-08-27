package org.ehcache.spi.copy;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/copy/Copier.class */
public interface Copier<T> {
    T copyForRead(T t);

    T copyForWrite(T t);
}

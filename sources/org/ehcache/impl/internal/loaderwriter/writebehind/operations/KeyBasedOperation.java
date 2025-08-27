package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/KeyBasedOperation.class */
public interface KeyBasedOperation<K> {
    K getKey();

    long getCreationTime();
}

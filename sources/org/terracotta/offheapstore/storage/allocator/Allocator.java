package org.terracotta.offheapstore.storage.allocator;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/Allocator.class */
public interface Allocator extends Iterable<Long> {
    long allocate(long j);

    void free(long j);

    void clear();

    void expand(long j);

    long occupied();

    void validateAllocator();

    long getLastUsedAddress();

    long getLastUsedPointer();

    int getMinimalSize();

    long getMaximumAddress();
}

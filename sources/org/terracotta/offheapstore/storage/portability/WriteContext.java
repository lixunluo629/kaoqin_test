package org.terracotta.offheapstore.storage.portability;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/WriteContext.class */
public interface WriteContext {
    void setLong(int i, long j);

    void flush();
}

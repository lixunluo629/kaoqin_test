package org.terracotta.offheapstore.storage.portability;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/Portability.class */
public interface Portability<T> {
    ByteBuffer encode(T t);

    T decode(ByteBuffer byteBuffer);

    boolean equals(Object obj, ByteBuffer byteBuffer);
}

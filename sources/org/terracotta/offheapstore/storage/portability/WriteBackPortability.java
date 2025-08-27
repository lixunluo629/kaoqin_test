package org.terracotta.offheapstore.storage.portability;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/WriteBackPortability.class */
public interface WriteBackPortability<T> extends Portability<T> {
    T decode(ByteBuffer byteBuffer, WriteContext writeContext);
}

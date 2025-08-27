package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/BinaryStorageEngine.class */
public interface BinaryStorageEngine {
    int readKeyHash(long j);

    ByteBuffer readBinaryKey(long j);

    ByteBuffer readBinaryValue(long j);

    boolean equalsBinaryKey(ByteBuffer byteBuffer, long j);

    Long writeBinaryMapping(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2);

    Long writeBinaryMapping(ByteBuffer[] byteBufferArr, ByteBuffer[] byteBufferArr2, int i, int i2);
}

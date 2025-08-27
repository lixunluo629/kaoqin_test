package org.terracotta.offheapstore.buffersource;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/buffersource/HeapBufferSource.class */
public class HeapBufferSource implements BufferSource {
    @Override // org.terracotta.offheapstore.buffersource.BufferSource
    public ByteBuffer allocateBuffer(int size) {
        return ByteBuffer.allocate(size);
    }

    public String toString() {
        return "On-Heap Buffer Source";
    }
}

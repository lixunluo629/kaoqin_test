package org.terracotta.offheapstore.buffersource;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/buffersource/OffHeapBufferSource.class */
public class OffHeapBufferSource implements BufferSource {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) OffHeapBufferSource.class);

    @Override // org.terracotta.offheapstore.buffersource.BufferSource
    public ByteBuffer allocateBuffer(int size) {
        try {
            return ByteBuffer.allocateDirect(size);
        } catch (OutOfMemoryError e) {
            LOGGER.debug("Failed to allocate " + size + " byte offheap buffer", (Throwable) e);
            return null;
        }
    }

    public String toString() {
        return "Off-Heap Buffer Source";
    }
}

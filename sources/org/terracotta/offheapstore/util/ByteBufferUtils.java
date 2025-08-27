package org.terracotta.offheapstore.util;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/ByteBufferUtils.class */
public final class ByteBufferUtils {
    private ByteBufferUtils() {
    }

    public static int totalLength(ByteBuffer[] buffers) {
        int total = 0;
        for (ByteBuffer buffer : buffers) {
            total += buffer.remaining();
        }
        return total;
    }

    public static final ByteBuffer aggregate(ByteBuffer[] buffers) {
        if (buffers.length == 1) {
            return buffers[0];
        }
        ByteBuffer aggregate = ByteBuffer.allocate(totalLength(buffers));
        for (ByteBuffer element : buffers) {
            aggregate.put(element);
        }
        return (ByteBuffer) aggregate.flip();
    }
}

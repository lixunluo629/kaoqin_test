package org.terracotta.offheapstore.buffersource;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/buffersource/BufferSource.class */
public interface BufferSource {
    ByteBuffer allocateBuffer(int i);
}

package io.netty.util.internal;

import java.nio.ByteBuffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/Cleaner.class */
interface Cleaner {
    void freeDirectBuffer(ByteBuffer byteBuffer);
}

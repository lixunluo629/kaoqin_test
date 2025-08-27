package org.ehcache.impl.internal.store;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/BinaryValueHolder.class */
public interface BinaryValueHolder {
    ByteBuffer getBinaryValue() throws IllegalStateException;

    boolean isBinaryValueAvailable();
}

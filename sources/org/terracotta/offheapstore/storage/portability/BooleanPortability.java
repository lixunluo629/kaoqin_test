package org.terracotta.offheapstore.storage.portability;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/BooleanPortability.class */
public class BooleanPortability implements Portability<Boolean> {
    public static final BooleanPortability INSTANCE = new BooleanPortability();
    private static final ByteBuffer TRUE = ByteBuffer.allocateDirect(1);
    private static final ByteBuffer FALSE = ByteBuffer.allocateDirect(1);

    static {
        TRUE.put((byte) 1).flip();
        FALSE.put((byte) 0).flip();
    }

    private BooleanPortability() {
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(Boolean object) {
        return object.booleanValue() ? TRUE.duplicate() : FALSE.duplicate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public Boolean decode(ByteBuffer buffer) {
        if (buffer.get(0) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object object, ByteBuffer buffer) {
        return object.equals(decode(buffer));
    }
}

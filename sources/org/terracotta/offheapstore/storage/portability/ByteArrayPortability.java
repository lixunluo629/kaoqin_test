package org.terracotta.offheapstore.storage.portability;

import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/ByteArrayPortability.class */
public class ByteArrayPortability implements Portability<byte[]> {
    public static final ByteArrayPortability INSTANCE = new ByteArrayPortability();

    protected ByteArrayPortability() {
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(byte[] object) {
        return ByteBuffer.wrap(object);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public byte[] decode(ByteBuffer buffer) {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return data;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object value, ByteBuffer readBuffer) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Byte arrays cannot be compared in their serialized forms - byte array eqaulity is identity based.");
    }
}

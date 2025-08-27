package org.terracotta.offheapstore.storage.portability;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import org.terracotta.offheapstore.disk.persistent.PersistentPortability;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/portability/StringPortability.class */
public class StringPortability implements PersistentPortability<String> {
    public static final StringPortability INSTANCE = new StringPortability();

    private StringPortability() {
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public ByteBuffer encode(String object) {
        ByteBuffer buffer = ByteBuffer.allocate(object.length() * 2);
        buffer.asCharBuffer().put(object).clear();
        return buffer;
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public String decode(ByteBuffer buffer) {
        return buffer.asCharBuffer().toString();
    }

    @Override // org.terracotta.offheapstore.storage.portability.Portability
    public boolean equals(Object value, ByteBuffer readBuffer) {
        return value.equals(decode(readBuffer));
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void persist(ObjectOutput output) throws IOException {
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void bootstrap(ObjectInput input) throws IOException {
    }
}

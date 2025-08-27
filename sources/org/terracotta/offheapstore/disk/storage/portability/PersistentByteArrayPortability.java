package org.terracotta.offheapstore.disk.storage.portability;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.terracotta.offheapstore.disk.persistent.PersistentPortability;
import org.terracotta.offheapstore.storage.portability.ByteArrayPortability;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/portability/PersistentByteArrayPortability.class */
public class PersistentByteArrayPortability extends ByteArrayPortability implements PersistentPortability<byte[]> {
    public static final PersistentByteArrayPortability INSTANCE = new PersistentByteArrayPortability();

    private PersistentByteArrayPortability() {
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

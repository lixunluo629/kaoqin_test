package org.terracotta.offheapstore.disk.storage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.terracotta.offheapstore.disk.persistent.PersistentHalfStorageEngine;
import org.terracotta.offheapstore.storage.IntegerStorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/PersistentIntegerStorageEngine.class */
public class PersistentIntegerStorageEngine extends IntegerStorageEngine implements PersistentHalfStorageEngine<Integer> {
    private static final PersistentIntegerStorageEngine SINGLETON = new PersistentIntegerStorageEngine();
    private static final Factory<PersistentIntegerStorageEngine> FACTORY = new Factory<PersistentIntegerStorageEngine>() { // from class: org.terracotta.offheapstore.disk.storage.PersistentIntegerStorageEngine.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.terracotta.offheapstore.util.Factory
        public PersistentIntegerStorageEngine newInstance() {
            return PersistentIntegerStorageEngine.SINGLETON;
        }
    };

    public static Factory<PersistentIntegerStorageEngine> createPersistentFactory() {
        return FACTORY;
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

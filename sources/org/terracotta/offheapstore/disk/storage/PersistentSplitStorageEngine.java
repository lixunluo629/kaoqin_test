package org.terracotta.offheapstore.disk.storage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.terracotta.offheapstore.disk.persistent.Persistent;
import org.terracotta.offheapstore.disk.persistent.PersistentHalfStorageEngine;
import org.terracotta.offheapstore.disk.persistent.PersistentStorageEngine;
import org.terracotta.offheapstore.storage.SplitStorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/PersistentSplitStorageEngine.class */
public class PersistentSplitStorageEngine<K, V> extends SplitStorageEngine<K, V> implements PersistentStorageEngine<K, V> {
    public static <K, V> Factory<PersistentSplitStorageEngine<K, V>> createPersistentFactory(final Factory<? extends PersistentHalfStorageEngine<K>> keyFactory, final Factory<? extends PersistentHalfStorageEngine<V>> valueFactory) {
        return new Factory<PersistentSplitStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.disk.storage.PersistentSplitStorageEngine.1
            @Override // org.terracotta.offheapstore.util.Factory
            public PersistentSplitStorageEngine<K, V> newInstance() {
                return new PersistentSplitStorageEngine<>((PersistentHalfStorageEngine) keyFactory.newInstance(), (PersistentHalfStorageEngine) valueFactory.newInstance());
            }
        };
    }

    public PersistentSplitStorageEngine(PersistentHalfStorageEngine<K> keyStorageEngine, PersistentHalfStorageEngine<V> valueStorageEngine) {
        super(keyStorageEngine, valueStorageEngine);
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void flush() throws IOException {
        ((Persistent) this.keyStorageEngine).flush();
        ((Persistent) this.valueStorageEngine).flush();
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void close() throws IOException {
        ((Persistent) this.keyStorageEngine).close();
        ((Persistent) this.valueStorageEngine).close();
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void persist(ObjectOutput output) throws IOException {
        ((Persistent) this.keyStorageEngine).persist(output);
        ((Persistent) this.valueStorageEngine).persist(output);
    }

    @Override // org.terracotta.offheapstore.disk.persistent.Persistent
    public void bootstrap(ObjectInput input) throws IOException {
        ((Persistent) this.keyStorageEngine).bootstrap(input);
        ((Persistent) this.valueStorageEngine).bootstrap(input);
    }
}

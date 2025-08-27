package org.terracotta.offheapstore.storage;

import java.io.Serializable;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.storage.portability.SerializablePortability;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/SerializableStorageEngine.class */
public class SerializableStorageEngine extends OffHeapBufferStorageEngine<Serializable, Serializable> {
    public static Factory<SerializableStorageEngine> createFactory(final PointerSize width, final PageSource source, final int pageSize) {
        return new Factory<SerializableStorageEngine>() { // from class: org.terracotta.offheapstore.storage.SerializableStorageEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.terracotta.offheapstore.util.Factory
            public SerializableStorageEngine newInstance() {
                return new SerializableStorageEngine(width, source, pageSize);
            }
        };
    }

    public static Factory<SerializableStorageEngine> createFactory(final PointerSize width, final PageSource source, final int pageSize, final Portability<Serializable> portability) {
        return new Factory<SerializableStorageEngine>() { // from class: org.terracotta.offheapstore.storage.SerializableStorageEngine.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.terracotta.offheapstore.util.Factory
            public SerializableStorageEngine newInstance() {
                return new SerializableStorageEngine(width, source, pageSize, portability);
            }
        };
    }

    public SerializableStorageEngine(PointerSize width, PageSource source, int pageSize) {
        super(width, source, pageSize, new SerializablePortability(), new SerializablePortability());
    }

    protected SerializableStorageEngine(PointerSize width, PageSource source, int pageSize, Portability<Serializable> portability) {
        super(width, source, pageSize, portability, portability);
    }
}

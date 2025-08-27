package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.storage.portability.StringPortability;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/StringStorageEngine.class */
public class StringStorageEngine extends OffHeapBufferStorageEngine<String, String> {
    private static final Portability<String> PORTABILITY = StringPortability.INSTANCE;

    public static Factory<StringStorageEngine> createFactory(final PointerSize width, final PageSource source, final int pageSize) {
        return new Factory<StringStorageEngine>() { // from class: org.terracotta.offheapstore.storage.StringStorageEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.terracotta.offheapstore.util.Factory
            public StringStorageEngine newInstance() {
                return new StringStorageEngine(width, source, pageSize);
            }
        };
    }

    public StringStorageEngine(PointerSize width, PageSource source, int pageSize) {
        super(width, source, pageSize, PORTABILITY, PORTABILITY);
    }
}

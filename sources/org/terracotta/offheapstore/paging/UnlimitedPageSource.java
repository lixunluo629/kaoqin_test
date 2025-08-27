package org.terracotta.offheapstore.paging;

import java.nio.ByteBuffer;
import org.terracotta.offheapstore.buffersource.BufferSource;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/UnlimitedPageSource.class */
public class UnlimitedPageSource implements PageSource {
    private final BufferSource source;

    public UnlimitedPageSource(BufferSource source) {
        this.source = source;
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public Page allocate(int size, boolean thief, boolean victim, OffHeapStorageArea owner) {
        ByteBuffer buffer = this.source.allocateBuffer(size);
        if (buffer == null) {
            return null;
        }
        return new Page(buffer, owner);
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public void free(Page page) {
    }
}

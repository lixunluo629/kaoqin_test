package org.terracotta.offheapstore.disk.paging;

import java.nio.MappedByteBuffer;
import org.terracotta.offheapstore.paging.Page;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/paging/MappedPage.class */
public class MappedPage extends Page {
    public MappedPage(MappedByteBuffer buffer) {
        super(buffer, null);
    }

    @Override // org.terracotta.offheapstore.paging.Page
    public MappedByteBuffer asByteBuffer() {
        return (MappedByteBuffer) super.asByteBuffer();
    }
}

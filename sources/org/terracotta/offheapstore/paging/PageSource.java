package org.terracotta.offheapstore.paging;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/PageSource.class */
public interface PageSource {
    Page allocate(int i, boolean z, boolean z2, OffHeapStorageArea offHeapStorageArea);

    void free(Page page);
}

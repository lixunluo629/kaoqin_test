package org.terracotta.offheapstore.paging;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/PhantomReferenceLimitedPageSource.class */
public class PhantomReferenceLimitedPageSource implements PageSource {
    private final ReferenceQueue<ByteBuffer> allocatedBuffers = new ReferenceQueue<>();
    private final Map<PhantomReference<ByteBuffer>, Integer> bufferSizes = new ConcurrentHashMap();
    private final AtomicLong max;

    public PhantomReferenceLimitedPageSource(long max) {
        this.max = new AtomicLong(max);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0033, code lost:
    
        return null;
     */
    @Override // org.terracotta.offheapstore.paging.PageSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.terracotta.offheapstore.paging.Page allocate(int r9, boolean r10, boolean r11, org.terracotta.offheapstore.paging.OffHeapStorageArea r12) {
        /*
            r8 = this;
        L0:
            r0 = r8
            r0.processQueue()
            r0 = r8
            java.util.concurrent.atomic.AtomicLong r0 = r0.max
            long r0 = r0.get()
            r13 = r0
            r0 = r13
            r1 = r9
            long r1 = (long) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L17
            r0 = 0
            return r0
        L17:
            r0 = r8
            java.util.concurrent.atomic.AtomicLong r0 = r0.max
            r1 = r13
            r2 = r13
            r3 = r9
            long r3 = (long) r3
            long r2 = r2 - r3
            boolean r0 = r0.compareAndSet(r1, r2)
            if (r0 == 0) goto L5c
            r0 = r9
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocateDirect(r0)     // Catch: java.lang.OutOfMemoryError -> L31
            r15 = r0
            goto L35
        L31:
            r16 = move-exception
            r0 = 0
            return r0
        L35:
            r0 = r8
            java.util.Map<java.lang.ref.PhantomReference<java.nio.ByteBuffer>, java.lang.Integer> r0 = r0.bufferSizes
            java.lang.ref.PhantomReference r1 = new java.lang.ref.PhantomReference
            r2 = r1
            r3 = r15
            r4 = r8
            java.lang.ref.ReferenceQueue<java.nio.ByteBuffer> r4 = r4.allocatedBuffers
            r2.<init>(r3, r4)
            r2 = r9
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.Object r0 = r0.put(r1, r2)
            org.terracotta.offheapstore.paging.Page r0 = new org.terracotta.offheapstore.paging.Page
            r1 = r0
            r2 = r15
            r3 = r12
            r1.<init>(r2, r3)
            return r0
        L5c:
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.paging.PhantomReferenceLimitedPageSource.allocate(int, boolean, boolean, org.terracotta.offheapstore.paging.OffHeapStorageArea):org.terracotta.offheapstore.paging.Page");
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public void free(Page buffer) {
    }

    private void processQueue() {
        while (true) {
            Reference<?> ref = this.allocatedBuffers.poll();
            if (ref == null) {
                return;
            }
            Integer size = this.bufferSizes.remove(ref);
            this.max.addAndGet(size.longValue());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PhantomReferenceLimitedBufferSource\n");
        Collection<Integer> buffers = this.bufferSizes.values();
        sb.append("Bytes Available   : ").append(this.max.get()).append('\n');
        sb.append("Buffers Allocated : (count=").append(buffers.size()).append(") ").append(buffers);
        return sb.toString();
    }
}

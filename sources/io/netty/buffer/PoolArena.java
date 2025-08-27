package io.netty.buffer;

import io.netty.util.internal.LongCounter;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolArena.class */
abstract class PoolArena<T> implements PoolArenaMetric {
    static final boolean HAS_UNSAFE;
    static final int numTinySubpagePools = 32;
    final PooledByteBufAllocator parent;
    private final int maxOrder;
    final int pageSize;
    final int pageShifts;
    final int chunkSize;
    final int subpageOverflowMask;
    final int numSmallSubpagePools;
    final int directMemoryCacheAlignment;
    final int directMemoryCacheAlignmentMask;
    private final PoolSubpage<T>[] smallSubpagePools;
    private final PoolChunkList<T> q050;
    private final PoolChunkList<T> q025;
    private final PoolChunkList<T> q000;
    private final PoolChunkList<T> qInit;
    private final PoolChunkList<T> q075;
    private final PoolChunkList<T> q100;
    private final List<PoolChunkListMetric> chunkListMetrics;
    private long allocationsNormal;
    private long deallocationsTiny;
    private long deallocationsSmall;
    private long deallocationsNormal;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final LongCounter allocationsTiny = PlatformDependent.newLongCounter();
    private final LongCounter allocationsSmall = PlatformDependent.newLongCounter();
    private final LongCounter allocationsHuge = PlatformDependent.newLongCounter();
    private final LongCounter activeBytesHuge = PlatformDependent.newLongCounter();
    private final LongCounter deallocationsHuge = PlatformDependent.newLongCounter();
    final AtomicInteger numThreadCaches = new AtomicInteger();
    private final PoolSubpage<T>[] tinySubpagePools = newSubpagePoolArray(32);

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolArena$SizeClass.class */
    enum SizeClass {
        Tiny,
        Small,
        Normal
    }

    abstract boolean isDirect();

    protected abstract PoolChunk<T> newChunk(int i, int i2, int i3, int i4);

    protected abstract PoolChunk<T> newUnpooledChunk(int i);

    protected abstract PooledByteBuf<T> newByteBuf(int i);

    protected abstract void memoryCopy(T t, int i, PooledByteBuf<T> pooledByteBuf, int i2);

    protected abstract void destroyChunk(PoolChunk<T> poolChunk);

    static {
        $assertionsDisabled = !PoolArena.class.desiredAssertionStatus();
        HAS_UNSAFE = PlatformDependent.hasUnsafe();
    }

    protected PoolArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int cacheAlignment) {
        this.parent = parent;
        this.pageSize = pageSize;
        this.maxOrder = maxOrder;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.directMemoryCacheAlignment = cacheAlignment;
        this.directMemoryCacheAlignmentMask = cacheAlignment - 1;
        this.subpageOverflowMask = (pageSize - 1) ^ (-1);
        for (int i = 0; i < this.tinySubpagePools.length; i++) {
            this.tinySubpagePools[i] = newSubpagePoolHead(pageSize);
        }
        this.numSmallSubpagePools = pageShifts - 9;
        this.smallSubpagePools = newSubpagePoolArray(this.numSmallSubpagePools);
        for (int i2 = 0; i2 < this.smallSubpagePools.length; i2++) {
            this.smallSubpagePools[i2] = newSubpagePoolHead(pageSize);
        }
        this.q100 = new PoolChunkList<>(this, null, 100, Integer.MAX_VALUE, chunkSize);
        this.q075 = new PoolChunkList<>(this, this.q100, 75, 100, chunkSize);
        this.q050 = new PoolChunkList<>(this, this.q075, 50, 100, chunkSize);
        this.q025 = new PoolChunkList<>(this, this.q050, 25, 75, chunkSize);
        this.q000 = new PoolChunkList<>(this, this.q025, 1, 50, chunkSize);
        this.qInit = new PoolChunkList<>(this, this.q000, Integer.MIN_VALUE, 25, chunkSize);
        this.q100.prevList(this.q075);
        this.q075.prevList(this.q050);
        this.q050.prevList(this.q025);
        this.q025.prevList(this.q000);
        this.q000.prevList(null);
        this.qInit.prevList(this.qInit);
        List<PoolChunkListMetric> metrics = new ArrayList<>(6);
        metrics.add(this.qInit);
        metrics.add(this.q000);
        metrics.add(this.q025);
        metrics.add(this.q050);
        metrics.add(this.q075);
        metrics.add(this.q100);
        this.chunkListMetrics = Collections.unmodifiableList(metrics);
    }

    private PoolSubpage<T> newSubpagePoolHead(int pageSize) {
        PoolSubpage<T> head = new PoolSubpage<>(pageSize);
        head.prev = head;
        head.next = head;
        return head;
    }

    private PoolSubpage<T>[] newSubpagePoolArray(int size) {
        return new PoolSubpage[size];
    }

    PooledByteBuf<T> allocate(PoolThreadCache cache, int reqCapacity, int maxCapacity) {
        PooledByteBuf<T> buf = newByteBuf(maxCapacity);
        allocate(cache, buf, reqCapacity);
        return buf;
    }

    static int tinyIdx(int normCapacity) {
        return normCapacity >>> 4;
    }

    static int smallIdx(int normCapacity) {
        int tableIdx = 0;
        int i = normCapacity >>> 10;
        while (i != 0) {
            i >>>= 1;
            tableIdx++;
        }
        return tableIdx;
    }

    boolean isTinyOrSmall(int normCapacity) {
        return (normCapacity & this.subpageOverflowMask) == 0;
    }

    static boolean isTiny(int normCapacity) {
        return (normCapacity & (-512)) == 0;
    }

    private void allocate(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity) {
        int tableIdx;
        PoolSubpage<T>[] table;
        int normCapacity = normalizeCapacity(reqCapacity);
        if (isTinyOrSmall(normCapacity)) {
            boolean tiny = isTiny(normCapacity);
            if (tiny) {
                if (cache.allocateTiny(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = tinyIdx(normCapacity);
                table = this.tinySubpagePools;
            } else {
                if (cache.allocateSmall(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = smallIdx(normCapacity);
                table = this.smallSubpagePools;
            }
            PoolSubpage<T> head = table[tableIdx];
            synchronized (head) {
                PoolSubpage<T> s = head.next;
                if (s != head) {
                    if (!$assertionsDisabled && (!s.doNotDestroy || s.elemSize != normCapacity)) {
                        throw new AssertionError();
                    }
                    long handle = s.allocate();
                    if (!$assertionsDisabled && handle < 0) {
                        throw new AssertionError();
                    }
                    s.chunk.initBufWithSubpage(buf, null, handle, reqCapacity, cache);
                    incTinySmallAllocation(tiny);
                    return;
                }
                synchronized (this) {
                    allocateNormal(buf, reqCapacity, normCapacity, cache);
                }
                incTinySmallAllocation(tiny);
                return;
            }
        }
        if (normCapacity <= this.chunkSize) {
            if (cache.allocateNormal(this, buf, reqCapacity, normCapacity)) {
                return;
            }
            synchronized (this) {
                allocateNormal(buf, reqCapacity, normCapacity, cache);
                this.allocationsNormal++;
            }
            return;
        }
        allocateHuge(buf, reqCapacity);
    }

    private void allocateNormal(PooledByteBuf<T> buf, int reqCapacity, int normCapacity, PoolThreadCache threadCache) {
        if (this.q050.allocate(buf, reqCapacity, normCapacity, threadCache) || this.q025.allocate(buf, reqCapacity, normCapacity, threadCache) || this.q000.allocate(buf, reqCapacity, normCapacity, threadCache) || this.qInit.allocate(buf, reqCapacity, normCapacity, threadCache) || this.q075.allocate(buf, reqCapacity, normCapacity, threadCache)) {
            return;
        }
        PoolChunk<T> c = newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
        boolean success = c.allocate(buf, reqCapacity, normCapacity, threadCache);
        if (!$assertionsDisabled && !success) {
            throw new AssertionError();
        }
        this.qInit.add(c);
    }

    private void incTinySmallAllocation(boolean tiny) {
        if (tiny) {
            this.allocationsTiny.increment();
        } else {
            this.allocationsSmall.increment();
        }
    }

    private void allocateHuge(PooledByteBuf<T> buf, int reqCapacity) {
        PoolChunk<T> chunk = newUnpooledChunk(reqCapacity);
        this.activeBytesHuge.add(chunk.chunkSize());
        buf.initUnpooled(chunk, reqCapacity);
        this.allocationsHuge.increment();
    }

    void free(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, int normCapacity, PoolThreadCache cache) {
        if (chunk.unpooled) {
            int size = chunk.chunkSize();
            destroyChunk(chunk);
            this.activeBytesHuge.add(-size);
            this.deallocationsHuge.increment();
            return;
        }
        SizeClass sizeClass = sizeClass(normCapacity);
        if (cache != null && cache.add(this, chunk, nioBuffer, handle, normCapacity, sizeClass)) {
            return;
        }
        freeChunk(chunk, handle, sizeClass, nioBuffer, false);
    }

    private SizeClass sizeClass(int normCapacity) {
        if (isTinyOrSmall(normCapacity)) {
            return isTiny(normCapacity) ? SizeClass.Tiny : SizeClass.Small;
        }
        return SizeClass.Normal;
    }

    void freeChunk(PoolChunk<T> chunk, long handle, SizeClass sizeClass, ByteBuffer nioBuffer, boolean finalizer) {
        boolean destroyChunk;
        synchronized (this) {
            if (!finalizer) {
                switch (sizeClass) {
                    case Normal:
                        this.deallocationsNormal++;
                        break;
                    case Small:
                        this.deallocationsSmall++;
                        break;
                    case Tiny:
                        this.deallocationsTiny++;
                        break;
                    default:
                        throw new Error();
                }
            }
            destroyChunk = !chunk.parent.free(chunk, handle, nioBuffer);
        }
        if (destroyChunk) {
            destroyChunk(chunk);
        }
    }

    PoolSubpage<T> findSubpagePoolHead(int elemSize) {
        int tableIdx;
        PoolSubpage<T>[] table;
        if (isTiny(elemSize)) {
            tableIdx = tinyIdx(elemSize);
            table = this.tinySubpagePools;
        } else {
            tableIdx = smallIdx(elemSize);
            table = this.smallSubpagePools;
        }
        return table[tableIdx];
    }

    int normalizeCapacity(int reqCapacity) {
        ObjectUtil.checkPositiveOrZero(reqCapacity, "reqCapacity");
        if (reqCapacity >= this.chunkSize) {
            return this.directMemoryCacheAlignment == 0 ? reqCapacity : alignCapacity(reqCapacity);
        }
        if (!isTiny(reqCapacity)) {
            int normalizedCapacity = reqCapacity - 1;
            int normalizedCapacity2 = normalizedCapacity | (normalizedCapacity >>> 1);
            int normalizedCapacity3 = normalizedCapacity2 | (normalizedCapacity2 >>> 2);
            int normalizedCapacity4 = normalizedCapacity3 | (normalizedCapacity3 >>> 4);
            int normalizedCapacity5 = normalizedCapacity4 | (normalizedCapacity4 >>> 8);
            int normalizedCapacity6 = (normalizedCapacity5 | (normalizedCapacity5 >>> 16)) + 1;
            if (normalizedCapacity6 < 0) {
                normalizedCapacity6 >>>= 1;
            }
            if ($assertionsDisabled || this.directMemoryCacheAlignment == 0 || (normalizedCapacity6 & this.directMemoryCacheAlignmentMask) == 0) {
                return normalizedCapacity6;
            }
            throw new AssertionError();
        }
        if (this.directMemoryCacheAlignment > 0) {
            return alignCapacity(reqCapacity);
        }
        if ((reqCapacity & 15) == 0) {
            return reqCapacity;
        }
        return (reqCapacity & (-16)) + 16;
    }

    int alignCapacity(int reqCapacity) {
        int delta = reqCapacity & this.directMemoryCacheAlignmentMask;
        return delta == 0 ? reqCapacity : (reqCapacity + this.directMemoryCacheAlignment) - delta;
    }

    void reallocate(PooledByteBuf<T> buf, int newCapacity, boolean freeOldMemory) {
        int bytesToCopy;
        if (!$assertionsDisabled && (newCapacity < 0 || newCapacity > buf.maxCapacity())) {
            throw new AssertionError();
        }
        int oldCapacity = buf.length;
        if (oldCapacity == newCapacity) {
            return;
        }
        PoolChunk<T> oldChunk = buf.chunk;
        ByteBuffer oldNioBuffer = buf.tmpNioBuf;
        long oldHandle = buf.handle;
        T oldMemory = buf.memory;
        int oldOffset = buf.offset;
        int oldMaxLength = buf.maxLength;
        allocate(this.parent.threadCache(), buf, newCapacity);
        if (newCapacity > oldCapacity) {
            bytesToCopy = oldCapacity;
        } else {
            buf.trimIndicesToCapacity(newCapacity);
            bytesToCopy = newCapacity;
        }
        memoryCopy(oldMemory, oldOffset, buf, bytesToCopy);
        if (freeOldMemory) {
            free(oldChunk, oldNioBuffer, oldHandle, oldMaxLength, buf.cache);
        }
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numThreadCaches() {
        return this.numThreadCaches.get();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numTinySubpages() {
        return this.tinySubpagePools.length;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numSmallSubpages() {
        return this.smallSubpagePools.length;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public int numChunkLists() {
        return this.chunkListMetrics.size();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolSubpageMetric> tinySubpages() {
        return subPageMetricList(this.tinySubpagePools);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolSubpageMetric> smallSubpages() {
        return subPageMetricList(this.smallSubpagePools);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public List<PoolChunkListMetric> chunkLists() {
        return this.chunkListMetrics;
    }

    private static List<PoolSubpageMetric> subPageMetricList(PoolSubpage<?>[] pages) {
        List<PoolSubpageMetric> metrics = new ArrayList<>();
        for (PoolSubpage<?> head : pages) {
            if (head.next != head) {
                PoolSubpage<?> s = head.next;
                do {
                    metrics.add(s);
                    s = s.next;
                } while (s != head);
            }
        }
        return metrics;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numAllocations() {
        long allocsNormal;
        synchronized (this) {
            allocsNormal = this.allocationsNormal;
        }
        return this.allocationsTiny.value() + this.allocationsSmall.value() + allocsNormal + this.allocationsHuge.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numTinyAllocations() {
        return this.allocationsTiny.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numSmallAllocations() {
        return this.allocationsSmall.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public synchronized long numNormalAllocations() {
        return this.allocationsNormal;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numDeallocations() {
        long deallocs;
        synchronized (this) {
            deallocs = this.deallocationsTiny + this.deallocationsSmall + this.deallocationsNormal;
        }
        return deallocs + this.deallocationsHuge.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public synchronized long numTinyDeallocations() {
        return this.deallocationsTiny;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public synchronized long numSmallDeallocations() {
        return this.deallocationsSmall;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public synchronized long numNormalDeallocations() {
        return this.deallocationsNormal;
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numHugeAllocations() {
        return this.allocationsHuge.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numHugeDeallocations() {
        return this.deallocationsHuge.value();
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveAllocations() {
        long val;
        long val2 = ((this.allocationsTiny.value() + this.allocationsSmall.value()) + this.allocationsHuge.value()) - this.deallocationsHuge.value();
        synchronized (this) {
            val = val2 + (this.allocationsNormal - ((this.deallocationsTiny + this.deallocationsSmall) + this.deallocationsNormal));
        }
        return Math.max(val, 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveTinyAllocations() {
        return Math.max(numTinyAllocations() - numTinyDeallocations(), 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveSmallAllocations() {
        return Math.max(numSmallAllocations() - numSmallDeallocations(), 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveNormalAllocations() {
        long val;
        synchronized (this) {
            val = this.allocationsNormal - this.deallocationsNormal;
        }
        return Math.max(val, 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveHugeAllocations() {
        return Math.max(numHugeAllocations() - numHugeDeallocations(), 0L);
    }

    @Override // io.netty.buffer.PoolArenaMetric
    public long numActiveBytes() {
        long val = this.activeBytesHuge.value();
        synchronized (this) {
            for (int i = 0; i < this.chunkListMetrics.size(); i++) {
                for (PoolChunkMetric m : this.chunkListMetrics.get(i)) {
                    val += m.chunkSize();
                }
            }
        }
        return Math.max(0L, val);
    }

    public synchronized String toString() {
        StringBuilder buf = new StringBuilder().append("Chunk(s) at 0~25%:").append(StringUtil.NEWLINE).append(this.qInit).append(StringUtil.NEWLINE).append("Chunk(s) at 0~50%:").append(StringUtil.NEWLINE).append(this.q000).append(StringUtil.NEWLINE).append("Chunk(s) at 25~75%:").append(StringUtil.NEWLINE).append(this.q025).append(StringUtil.NEWLINE).append("Chunk(s) at 50~100%:").append(StringUtil.NEWLINE).append(this.q050).append(StringUtil.NEWLINE).append("Chunk(s) at 75~100%:").append(StringUtil.NEWLINE).append(this.q075).append(StringUtil.NEWLINE).append("Chunk(s) at 100%:").append(StringUtil.NEWLINE).append(this.q100).append(StringUtil.NEWLINE).append("tiny subpages:");
        appendPoolSubPages(buf, this.tinySubpagePools);
        buf.append(StringUtil.NEWLINE).append("small subpages:");
        appendPoolSubPages(buf, this.smallSubpagePools);
        buf.append(StringUtil.NEWLINE);
        return buf.toString();
    }

    private static void appendPoolSubPages(StringBuilder buf, PoolSubpage<?>[] subpages) {
        for (int i = 0; i < subpages.length; i++) {
            PoolSubpage<?> head = subpages[i];
            if (head.next != head) {
                buf.append(StringUtil.NEWLINE).append(i).append(": ");
                PoolSubpage poolSubpage = head.next;
                do {
                    buf.append(poolSubpage);
                    poolSubpage = poolSubpage.next;
                } while (poolSubpage != head);
            }
        }
    }

    protected final void finalize() throws Throwable {
        try {
            super.finalize();
            destroyPoolSubPages(this.smallSubpagePools);
            destroyPoolSubPages(this.tinySubpagePools);
            destroyPoolChunkLists(this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100);
        } catch (Throwable th) {
            destroyPoolSubPages(this.smallSubpagePools);
            destroyPoolSubPages(this.tinySubpagePools);
            destroyPoolChunkLists(this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100);
            throw th;
        }
    }

    private static void destroyPoolSubPages(PoolSubpage<?>[] pages) {
        for (PoolSubpage<?> page : pages) {
            page.destroy();
        }
    }

    private void destroyPoolChunkLists(PoolChunkList<T>... chunkLists) {
        for (PoolChunkList<T> chunkList : chunkLists) {
            chunkList.destroy(this);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolArena$HeapArena.class */
    static final class HeapArena extends PoolArena<byte[]> {
        HeapArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int directMemoryCacheAlignment) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
        }

        private static byte[] newByteArray(int size) {
            return PlatformDependent.allocateUninitializedArray(size);
        }

        @Override // io.netty.buffer.PoolArena
        boolean isDirect() {
            return false;
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<byte[]> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize) {
            return new PoolChunk<>(this, newByteArray(chunkSize), pageSize, maxOrder, pageShifts, chunkSize, 0);
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<byte[]> newUnpooledChunk(int capacity) {
            return new PoolChunk<>(this, newByteArray(capacity), capacity, 0);
        }

        @Override // io.netty.buffer.PoolArena
        protected void destroyChunk(PoolChunk<byte[]> chunk) {
        }

        @Override // io.netty.buffer.PoolArena
        protected PooledByteBuf<byte[]> newByteBuf(int maxCapacity) {
            return HAS_UNSAFE ? PooledUnsafeHeapByteBuf.newUnsafeInstance(maxCapacity) : PooledHeapByteBuf.newInstance(maxCapacity);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.buffer.PoolArena
        public void memoryCopy(byte[] src, int srcOffset, PooledByteBuf<byte[]> dst, int length) {
            if (length == 0) {
                return;
            }
            System.arraycopy(src, srcOffset, dst.memory, dst.offset, length);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolArena$DirectArena.class */
    static final class DirectArena extends PoolArena<ByteBuffer> {
        DirectArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int directMemoryCacheAlignment) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
        }

        @Override // io.netty.buffer.PoolArena
        boolean isDirect() {
            return true;
        }

        int offsetCacheLine(ByteBuffer memory) {
            int remainder = HAS_UNSAFE ? (int) (PlatformDependent.directBufferAddress(memory) & this.directMemoryCacheAlignmentMask) : 0;
            return this.directMemoryCacheAlignment - remainder;
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<ByteBuffer> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize) {
            if (this.directMemoryCacheAlignment == 0) {
                return new PoolChunk<>(this, allocateDirect(chunkSize), pageSize, maxOrder, pageShifts, chunkSize, 0);
            }
            ByteBuffer memory = allocateDirect(chunkSize + this.directMemoryCacheAlignment);
            return new PoolChunk<>(this, memory, pageSize, maxOrder, pageShifts, chunkSize, offsetCacheLine(memory));
        }

        @Override // io.netty.buffer.PoolArena
        protected PoolChunk<ByteBuffer> newUnpooledChunk(int capacity) {
            if (this.directMemoryCacheAlignment == 0) {
                return new PoolChunk<>(this, allocateDirect(capacity), capacity, 0);
            }
            ByteBuffer memory = allocateDirect(capacity + this.directMemoryCacheAlignment);
            return new PoolChunk<>(this, memory, capacity, offsetCacheLine(memory));
        }

        private static ByteBuffer allocateDirect(int capacity) {
            return PlatformDependent.useDirectBufferNoCleaner() ? PlatformDependent.allocateDirectNoCleaner(capacity) : ByteBuffer.allocateDirect(capacity);
        }

        @Override // io.netty.buffer.PoolArena
        protected void destroyChunk(PoolChunk<ByteBuffer> chunk) {
            if (PlatformDependent.useDirectBufferNoCleaner()) {
                PlatformDependent.freeDirectNoCleaner(chunk.memory);
            } else {
                PlatformDependent.freeDirectBuffer(chunk.memory);
            }
        }

        @Override // io.netty.buffer.PoolArena
        protected PooledByteBuf<ByteBuffer> newByteBuf(int maxCapacity) {
            if (HAS_UNSAFE) {
                return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
            }
            return PooledDirectByteBuf.newInstance(maxCapacity);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.buffer.PoolArena
        public void memoryCopy(ByteBuffer src, int srcOffset, PooledByteBuf<ByteBuffer> dstBuf, int length) {
            if (length == 0) {
                return;
            }
            if (HAS_UNSAFE) {
                PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(src) + srcOffset, PlatformDependent.directBufferAddress(dstBuf.memory) + dstBuf.offset, length);
                return;
            }
            ByteBuffer src2 = src.duplicate();
            ByteBuffer dst = dstBuf.internalNioBuffer();
            src2.position(srcOffset).limit(srcOffset + length);
            dst.position(dstBuf.offset);
            dst.put(src2);
        }
    }
}

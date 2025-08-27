package io.netty.buffer;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolChunk.class */
final class PoolChunk<T> implements PoolChunkMetric {
    private static final int INTEGER_SIZE_MINUS_ONE = 31;
    final PoolArena<T> arena;
    final T memory;
    final boolean unpooled;
    final int offset;
    private final byte[] memoryMap;
    private final byte[] depthMap;
    private final PoolSubpage<T>[] subpages;
    private final int subpageOverflowMask;
    private final int pageSize;
    private final int pageShifts;
    private final int maxOrder;
    private final int chunkSize;
    private final int log2ChunkSize;
    private final int maxSubpageAllocs;
    private final byte unusable;
    private final Deque<ByteBuffer> cachedNioBuffers;
    int freeBytes;
    PoolChunkList<T> parent;
    PoolChunk<T> prev;
    PoolChunk<T> next;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PoolChunk.class.desiredAssertionStatus();
    }

    PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize, int offset) {
        this.unpooled = false;
        this.arena = arena;
        this.memory = memory;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.maxOrder = maxOrder;
        this.chunkSize = chunkSize;
        this.offset = offset;
        this.unusable = (byte) (maxOrder + 1);
        this.log2ChunkSize = log2(chunkSize);
        this.subpageOverflowMask = (pageSize - 1) ^ (-1);
        this.freeBytes = chunkSize;
        if (!$assertionsDisabled && maxOrder >= 30) {
            throw new AssertionError("maxOrder should be < 30, but is: " + maxOrder);
        }
        this.maxSubpageAllocs = 1 << maxOrder;
        this.memoryMap = new byte[this.maxSubpageAllocs << 1];
        this.depthMap = new byte[this.memoryMap.length];
        int memoryMapIndex = 1;
        for (int d = 0; d <= maxOrder; d++) {
            int depth = 1 << d;
            for (int p = 0; p < depth; p++) {
                this.memoryMap[memoryMapIndex] = (byte) d;
                this.depthMap[memoryMapIndex] = (byte) d;
                memoryMapIndex++;
            }
        }
        this.subpages = newSubpageArray(this.maxSubpageAllocs);
        this.cachedNioBuffers = new ArrayDeque(8);
    }

    PoolChunk(PoolArena<T> arena, T memory, int size, int offset) {
        this.unpooled = true;
        this.arena = arena;
        this.memory = memory;
        this.offset = offset;
        this.memoryMap = null;
        this.depthMap = null;
        this.subpages = null;
        this.subpageOverflowMask = 0;
        this.pageSize = 0;
        this.pageShifts = 0;
        this.maxOrder = 0;
        this.unusable = (byte) (this.maxOrder + 1);
        this.chunkSize = size;
        this.log2ChunkSize = log2(this.chunkSize);
        this.maxSubpageAllocs = 0;
        this.cachedNioBuffers = null;
    }

    private PoolSubpage<T>[] newSubpageArray(int size) {
        return new PoolSubpage[size];
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int usage() {
        int freeBytes;
        synchronized (this.arena) {
            freeBytes = this.freeBytes;
        }
        return usage(freeBytes);
    }

    private int usage(int freeBytes) {
        if (freeBytes == 0) {
            return 100;
        }
        int freePercentage = (int) ((freeBytes * 100) / this.chunkSize);
        if (freePercentage == 0) {
            return 99;
        }
        return 100 - freePercentage;
    }

    boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int normCapacity, PoolThreadCache threadCache) {
        long handle;
        if ((normCapacity & this.subpageOverflowMask) != 0) {
            handle = allocateRun(normCapacity);
        } else {
            handle = allocateSubpage(normCapacity);
        }
        if (handle < 0) {
            return false;
        }
        ByteBuffer nioBuffer = this.cachedNioBuffers != null ? this.cachedNioBuffers.pollLast() : null;
        initBuf(buf, nioBuffer, handle, reqCapacity, threadCache);
        return true;
    }

    private void updateParentsAlloc(int id) {
        while (id > 1) {
            int parentId = id >>> 1;
            byte val1 = value(id);
            byte val2 = value(id ^ 1);
            byte val = val1 < val2 ? val1 : val2;
            setValue(parentId, val);
            id = parentId;
        }
    }

    private void updateParentsFree(int id) {
        int logChild = depth(id) + 1;
        while (id > 1) {
            int parentId = id >>> 1;
            byte val1 = value(id);
            byte val2 = value(id ^ 1);
            logChild--;
            if (val1 == logChild && val2 == logChild) {
                setValue(parentId, (byte) (logChild - 1));
            } else {
                byte val = val1 < val2 ? val1 : val2;
                setValue(parentId, val);
            }
            id = parentId;
        }
    }

    private int allocateNode(int d) {
        int id = 1;
        int initial = -(1 << d);
        byte val = value(1);
        if (val > d) {
            return -1;
        }
        while (true) {
            if (val >= d && (id & initial) != 0) {
                break;
            }
            id <<= 1;
            val = value(id);
            if (val > d) {
                id ^= 1;
                val = value(id);
            }
        }
        byte value = value(id);
        if (!$assertionsDisabled && (value != d || (id & initial) != (1 << d))) {
            throw new AssertionError(String.format("val = %d, id & initial = %d, d = %d", Byte.valueOf(value), Integer.valueOf(id & initial), Integer.valueOf(d)));
        }
        setValue(id, this.unusable);
        updateParentsAlloc(id);
        return id;
    }

    private long allocateRun(int normCapacity) {
        int d = this.maxOrder - (log2(normCapacity) - this.pageShifts);
        int id = allocateNode(d);
        if (id < 0) {
            return id;
        }
        this.freeBytes -= runLength(id);
        return id;
    }

    private long allocateSubpage(int normCapacity) {
        PoolSubpage<T> head = this.arena.findSubpagePoolHead(normCapacity);
        int d = this.maxOrder;
        synchronized (head) {
            int id = allocateNode(d);
            if (id < 0) {
                return id;
            }
            PoolSubpage<T>[] subpages = this.subpages;
            int pageSize = this.pageSize;
            this.freeBytes -= pageSize;
            int subpageIdx = subpageIdx(id);
            PoolSubpage<T> subpage = subpages[subpageIdx];
            if (subpage == null) {
                subpage = new PoolSubpage<>(head, this, id, runOffset(id), pageSize, normCapacity);
                subpages[subpageIdx] = subpage;
            } else {
                subpage.init(head, normCapacity);
            }
            return subpage.allocate();
        }
    }

    void free(long handle, ByteBuffer nioBuffer) {
        int memoryMapIdx = memoryMapIdx(handle);
        int bitmapIdx = bitmapIdx(handle);
        if (bitmapIdx != 0) {
            PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
            if (!$assertionsDisabled && (subpage == null || !subpage.doNotDestroy)) {
                throw new AssertionError();
            }
            PoolSubpage<T> head = this.arena.findSubpagePoolHead(subpage.elemSize);
            synchronized (head) {
                if (subpage.free(head, bitmapIdx & 1073741823)) {
                    return;
                }
            }
        }
        this.freeBytes += runLength(memoryMapIdx);
        setValue(memoryMapIdx, depth(memoryMapIdx));
        updateParentsFree(memoryMapIdx);
        if (nioBuffer != null && this.cachedNioBuffers != null && this.cachedNioBuffers.size() < PooledByteBufAllocator.DEFAULT_MAX_CACHED_BYTEBUFFERS_PER_CHUNK) {
            this.cachedNioBuffers.offer(nioBuffer);
        }
    }

    void initBuf(PooledByteBuf<T> buf, ByteBuffer nioBuffer, long handle, int reqCapacity, PoolThreadCache threadCache) {
        int memoryMapIdx = memoryMapIdx(handle);
        int bitmapIdx = bitmapIdx(handle);
        if (bitmapIdx == 0) {
            byte val = value(memoryMapIdx);
            if (!$assertionsDisabled && val != this.unusable) {
                throw new AssertionError(String.valueOf((int) val));
            }
            buf.init(this, nioBuffer, handle, runOffset(memoryMapIdx) + this.offset, reqCapacity, runLength(memoryMapIdx), threadCache);
            return;
        }
        initBufWithSubpage(buf, nioBuffer, handle, bitmapIdx, reqCapacity, threadCache);
    }

    void initBufWithSubpage(PooledByteBuf<T> buf, ByteBuffer nioBuffer, long handle, int reqCapacity, PoolThreadCache threadCache) {
        initBufWithSubpage(buf, nioBuffer, handle, bitmapIdx(handle), reqCapacity, threadCache);
    }

    private void initBufWithSubpage(PooledByteBuf<T> buf, ByteBuffer nioBuffer, long handle, int bitmapIdx, int reqCapacity, PoolThreadCache threadCache) {
        if (!$assertionsDisabled && bitmapIdx == 0) {
            throw new AssertionError();
        }
        int memoryMapIdx = memoryMapIdx(handle);
        PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
        if (!$assertionsDisabled && !subpage.doNotDestroy) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && reqCapacity > subpage.elemSize) {
            throw new AssertionError();
        }
        buf.init(this, nioBuffer, handle, runOffset(memoryMapIdx) + ((bitmapIdx & 1073741823) * subpage.elemSize) + this.offset, reqCapacity, subpage.elemSize, threadCache);
    }

    private byte value(int id) {
        return this.memoryMap[id];
    }

    private void setValue(int id, byte val) {
        this.memoryMap[id] = val;
    }

    private byte depth(int id) {
        return this.depthMap[id];
    }

    private static int log2(int val) {
        return 31 - Integer.numberOfLeadingZeros(val);
    }

    private int runLength(int id) {
        return 1 << (this.log2ChunkSize - depth(id));
    }

    private int runOffset(int id) {
        int shift = id ^ (1 << depth(id));
        return shift * runLength(id);
    }

    private int subpageIdx(int memoryMapIdx) {
        return memoryMapIdx ^ this.maxSubpageAllocs;
    }

    private static int memoryMapIdx(long handle) {
        return (int) handle;
    }

    private static int bitmapIdx(long handle) {
        return (int) (handle >>> 32);
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int chunkSize() {
        return this.chunkSize;
    }

    @Override // io.netty.buffer.PoolChunkMetric
    public int freeBytes() {
        int i;
        synchronized (this.arena) {
            i = this.freeBytes;
        }
        return i;
    }

    public String toString() {
        int freeBytes;
        synchronized (this.arena) {
            freeBytes = this.freeBytes;
        }
        return "Chunk(" + Integer.toHexString(System.identityHashCode(this)) + ": " + usage(freeBytes) + "%, " + (this.chunkSize - freeBytes) + '/' + this.chunkSize + ')';
    }

    void destroy() {
        this.arena.destroyChunk(this);
    }
}

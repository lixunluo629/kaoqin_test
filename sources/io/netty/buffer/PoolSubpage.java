package io.netty.buffer;

import com.google.common.primitives.Longs;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolSubpage.class */
final class PoolSubpage<T> implements PoolSubpageMetric {
    final PoolChunk<T> chunk;
    private final int memoryMapIdx;
    private final int runOffset;
    private final int pageSize;
    private final long[] bitmap;
    PoolSubpage<T> prev;
    PoolSubpage<T> next;
    boolean doNotDestroy;
    int elemSize;
    private int maxNumElems;
    private int bitmapLength;
    private int nextAvail;
    private int numAvail;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PoolSubpage.class.desiredAssertionStatus();
    }

    PoolSubpage(int pageSize) {
        this.chunk = null;
        this.memoryMapIdx = -1;
        this.runOffset = -1;
        this.elemSize = -1;
        this.pageSize = pageSize;
        this.bitmap = null;
    }

    PoolSubpage(PoolSubpage<T> head, PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
        this.chunk = chunk;
        this.memoryMapIdx = memoryMapIdx;
        this.runOffset = runOffset;
        this.pageSize = pageSize;
        this.bitmap = new long[pageSize >>> 10];
        init(head, elemSize);
    }

    void init(PoolSubpage<T> head, int elemSize) {
        this.doNotDestroy = true;
        this.elemSize = elemSize;
        if (elemSize != 0) {
            int i = this.pageSize / elemSize;
            this.numAvail = i;
            this.maxNumElems = i;
            this.nextAvail = 0;
            this.bitmapLength = this.maxNumElems >>> 6;
            if ((this.maxNumElems & 63) != 0) {
                this.bitmapLength++;
            }
            for (int i2 = 0; i2 < this.bitmapLength; i2++) {
                this.bitmap[i2] = 0;
            }
        }
        addToPool(head);
    }

    long allocate() {
        if (this.elemSize == 0) {
            return toHandle(0);
        }
        if (this.numAvail == 0 || !this.doNotDestroy) {
            return -1L;
        }
        int bitmapIdx = getNextAvail();
        int q = bitmapIdx >>> 6;
        int r = bitmapIdx & 63;
        if (!$assertionsDisabled && ((this.bitmap[q] >>> r) & 1) != 0) {
            throw new AssertionError();
        }
        long[] jArr = this.bitmap;
        jArr[q] = jArr[q] | (1 << r);
        int i = this.numAvail - 1;
        this.numAvail = i;
        if (i == 0) {
            removeFromPool();
        }
        return toHandle(bitmapIdx);
    }

    boolean free(PoolSubpage<T> head, int bitmapIdx) {
        if (this.elemSize == 0) {
            return true;
        }
        int q = bitmapIdx >>> 6;
        int r = bitmapIdx & 63;
        if (!$assertionsDisabled && ((this.bitmap[q] >>> r) & 1) == 0) {
            throw new AssertionError();
        }
        long[] jArr = this.bitmap;
        jArr[q] = jArr[q] ^ (1 << r);
        setNextAvail(bitmapIdx);
        int i = this.numAvail;
        this.numAvail = i + 1;
        if (i == 0) {
            addToPool(head);
            return true;
        }
        if (this.numAvail != this.maxNumElems || this.prev == this.next) {
            return true;
        }
        this.doNotDestroy = false;
        removeFromPool();
        return false;
    }

    private void addToPool(PoolSubpage<T> head) {
        if (!$assertionsDisabled && (this.prev != null || this.next != null)) {
            throw new AssertionError();
        }
        this.prev = head;
        this.next = head.next;
        this.next.prev = this;
        head.next = this;
    }

    private void removeFromPool() {
        if (!$assertionsDisabled && (this.prev == null || this.next == null)) {
            throw new AssertionError();
        }
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
    }

    private void setNextAvail(int bitmapIdx) {
        this.nextAvail = bitmapIdx;
    }

    private int getNextAvail() {
        int nextAvail = this.nextAvail;
        if (nextAvail >= 0) {
            this.nextAvail = -1;
            return nextAvail;
        }
        return findNextAvail();
    }

    private int findNextAvail() {
        long[] bitmap = this.bitmap;
        int bitmapLength = this.bitmapLength;
        for (int i = 0; i < bitmapLength; i++) {
            long bits = bitmap[i];
            if ((bits ^ (-1)) != 0) {
                return findNextAvail0(i, bits);
            }
        }
        return -1;
    }

    private int findNextAvail0(int i, long bits) {
        int maxNumElems = this.maxNumElems;
        int baseVal = i << 6;
        for (int j = 0; j < 64; j++) {
            if ((bits & 1) == 0) {
                int val = baseVal | j;
                if (val < maxNumElems) {
                    return val;
                }
                return -1;
            }
            bits >>>= 1;
        }
        return -1;
    }

    private long toHandle(int bitmapIdx) {
        return Longs.MAX_POWER_OF_TWO | (bitmapIdx << 32) | this.memoryMapIdx;
    }

    public String toString() {
        boolean doNotDestroy;
        int maxNumElems;
        int numAvail;
        int elemSize;
        if (this.chunk == null) {
            doNotDestroy = true;
            maxNumElems = 0;
            numAvail = 0;
            elemSize = -1;
        } else {
            synchronized (this.chunk.arena) {
                if (!this.doNotDestroy) {
                    doNotDestroy = false;
                    elemSize = -1;
                    numAvail = -1;
                    maxNumElems = -1;
                } else {
                    doNotDestroy = true;
                    maxNumElems = this.maxNumElems;
                    numAvail = this.numAvail;
                    elemSize = this.elemSize;
                }
            }
        }
        if (!doNotDestroy) {
            return "(" + this.memoryMapIdx + ": not in use)";
        }
        return "(" + this.memoryMapIdx + ": " + (maxNumElems - numAvail) + '/' + maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + elemSize + ')';
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int maxNumElements() {
        int i;
        if (this.chunk == null) {
            return 0;
        }
        synchronized (this.chunk.arena) {
            i = this.maxNumElems;
        }
        return i;
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int numAvailable() {
        int i;
        if (this.chunk == null) {
            return 0;
        }
        synchronized (this.chunk.arena) {
            i = this.numAvail;
        }
        return i;
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int elementSize() {
        int i;
        if (this.chunk == null) {
            return -1;
        }
        synchronized (this.chunk.arena) {
            i = this.elemSize;
        }
        return i;
    }

    @Override // io.netty.buffer.PoolSubpageMetric
    public int pageSize() {
        return this.pageSize;
    }

    void destroy() {
        if (this.chunk != null) {
            this.chunk.destroy();
        }
    }
}

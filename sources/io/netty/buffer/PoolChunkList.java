package io.netty.buffer;

import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolChunkList.class */
final class PoolChunkList<T> implements PoolChunkListMetric {
    private static final Iterator<PoolChunkMetric> EMPTY_METRICS;
    private final PoolArena<T> arena;
    private final PoolChunkList<T> nextList;
    private final int minUsage;
    private final int maxUsage;
    private final int maxCapacity;
    private PoolChunk<T> head;
    private final int freeMinThreshold;
    private final int freeMaxThreshold;
    private PoolChunkList<T> prevList;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PoolChunkList.class.desiredAssertionStatus();
        EMPTY_METRICS = Collections.emptyList().iterator();
    }

    PoolChunkList(PoolArena<T> arena, PoolChunkList<T> nextList, int minUsage, int maxUsage, int chunkSize) {
        if (!$assertionsDisabled && minUsage > maxUsage) {
            throw new AssertionError();
        }
        this.arena = arena;
        this.nextList = nextList;
        this.minUsage = minUsage;
        this.maxUsage = maxUsage;
        this.maxCapacity = calculateMaxCapacity(minUsage, chunkSize);
        this.freeMinThreshold = maxUsage == 100 ? 0 : (int) ((chunkSize * ((100.0d - maxUsage) + 0.99999999d)) / 100.0d);
        this.freeMaxThreshold = minUsage == 100 ? 0 : (int) ((chunkSize * ((100.0d - minUsage) + 0.99999999d)) / 100.0d);
    }

    private static int calculateMaxCapacity(int minUsage, int chunkSize) {
        int minUsage2 = minUsage0(minUsage);
        if (minUsage2 == 100) {
            return 0;
        }
        return (int) ((chunkSize * (100 - minUsage2)) / 100);
    }

    void prevList(PoolChunkList<T> prevList) {
        if (!$assertionsDisabled && this.prevList != null) {
            throw new AssertionError();
        }
        this.prevList = prevList;
    }

    boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int normCapacity, PoolThreadCache threadCache) {
        if (normCapacity > this.maxCapacity) {
            return false;
        }
        PoolChunk<T> poolChunk = this.head;
        while (true) {
            PoolChunk<T> cur = poolChunk;
            if (cur != null) {
                if (!cur.allocate(buf, reqCapacity, normCapacity, threadCache)) {
                    poolChunk = cur.next;
                } else {
                    if (cur.freeBytes <= this.freeMinThreshold) {
                        remove(cur);
                        this.nextList.add(cur);
                        return true;
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    boolean free(PoolChunk<T> chunk, long handle, ByteBuffer nioBuffer) {
        chunk.free(handle, nioBuffer);
        if (chunk.freeBytes > this.freeMaxThreshold) {
            remove(chunk);
            return move0(chunk);
        }
        return true;
    }

    private boolean move(PoolChunk<T> chunk) {
        if (!$assertionsDisabled && chunk.usage() >= this.maxUsage) {
            throw new AssertionError();
        }
        if (chunk.freeBytes > this.freeMaxThreshold) {
            return move0(chunk);
        }
        add0(chunk);
        return true;
    }

    private boolean move0(PoolChunk<T> chunk) {
        if (this.prevList == null) {
            if ($assertionsDisabled || chunk.usage() == 0) {
                return false;
            }
            throw new AssertionError();
        }
        return this.prevList.move(chunk);
    }

    void add(PoolChunk<T> chunk) {
        if (chunk.freeBytes <= this.freeMinThreshold) {
            this.nextList.add(chunk);
        } else {
            add0(chunk);
        }
    }

    void add0(PoolChunk<T> chunk) {
        chunk.parent = this;
        if (this.head == null) {
            this.head = chunk;
            chunk.prev = null;
            chunk.next = null;
        } else {
            chunk.prev = null;
            chunk.next = this.head;
            this.head.prev = chunk;
            this.head = chunk;
        }
    }

    private void remove(PoolChunk<T> cur) {
        if (cur == this.head) {
            this.head = cur.next;
            if (this.head != null) {
                this.head.prev = null;
                return;
            }
            return;
        }
        PoolChunk<T> next = cur.next;
        cur.prev.next = next;
        if (next != null) {
            next.prev = cur.prev;
        }
    }

    @Override // io.netty.buffer.PoolChunkListMetric
    public int minUsage() {
        return minUsage0(this.minUsage);
    }

    @Override // io.netty.buffer.PoolChunkListMetric
    public int maxUsage() {
        return Math.min(this.maxUsage, 100);
    }

    private static int minUsage0(int value) {
        return Math.max(1, value);
    }

    @Override // java.lang.Iterable
    public Iterator<PoolChunkMetric> iterator() {
        synchronized (this.arena) {
            if (this.head == null) {
                return EMPTY_METRICS;
            }
            List<PoolChunkMetric> metrics = new ArrayList<>();
            PoolChunk<T> cur = this.head;
            do {
                metrics.add(cur);
                cur = cur.next;
            } while (cur != null);
            return metrics.iterator();
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        synchronized (this.arena) {
            if (this.head == null) {
                return "none";
            }
            PoolChunk<T> cur = this.head;
            while (true) {
                buf.append(cur);
                cur = cur.next;
                if (cur != null) {
                    buf.append(StringUtil.NEWLINE);
                } else {
                    return buf.toString();
                }
            }
        }
    }

    void destroy(PoolArena<T> arena) {
        PoolChunk<T> poolChunk = this.head;
        while (true) {
            PoolChunk<T> chunk = poolChunk;
            if (chunk != null) {
                arena.destroyChunk(chunk);
                poolChunk = chunk.next;
            } else {
                this.head = null;
                return;
            }
        }
    }
}

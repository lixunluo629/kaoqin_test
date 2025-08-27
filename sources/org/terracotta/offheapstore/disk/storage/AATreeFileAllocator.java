package org.terracotta.offheapstore.disk.storage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import org.terracotta.offheapstore.util.AATreeSet;
import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/AATreeFileAllocator.class */
public class AATreeFileAllocator extends AATreeSet<Region> {
    private static final boolean VALIDATING = Validation.shouldValidate(AATreeFileAllocator.class);
    private static final int MAGIC = 1128813129;
    private static final int MAGIC_REGION = 1398034256;
    private final long capacity;
    private long occupied;

    public AATreeFileAllocator(long size) {
        this.capacity = size;
        add((AATreeFileAllocator) new Region(0L, this.capacity - 1));
    }

    public AATreeFileAllocator(long size, DataInput input) throws IOException {
        this.capacity = size;
        this.occupied = size;
        if (input.readInt() != MAGIC) {
            throw new IOException("Invalid magic number");
        }
        while (true) {
            int magic = input.readInt();
            if (magic != -1) {
                if (magic != MAGIC_REGION) {
                    throw new IOException("Invalid magic number");
                }
                long start = input.readLong();
                long end = input.readLong();
                Region r = new Region(start, end);
                add((AATreeFileAllocator) r);
                freed(r);
            } else {
                return;
            }
        }
    }

    public long allocate(long size) {
        if (Long.bitCount(size) != 1) {
            size = Long.highestOneBit(size) << 1;
        }
        Region r = find(size);
        if (r == null) {
            return -1L;
        }
        Region current = removeAndReturn((Object) Long.valueOf(r.start()));
        Region newRange = current.remove(r);
        if (newRange != null) {
            add((AATreeFileAllocator) current);
            add((AATreeFileAllocator) newRange);
        } else if (!current.isNull()) {
            add((AATreeFileAllocator) current);
        }
        allocated(r);
        return r.start();
    }

    public void free(long address, long length) {
        if (Long.bitCount(length) != 1) {
            length = Long.highestOneBit(length) << 1;
        }
        if (length != 0) {
            Region r = new Region(address, (address + length) - 1);
            free(r);
            freed(r);
        }
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet
    public Region removeAndReturn(Object o) {
        Region r = (Region) super.removeAndReturn(o);
        if (r != null) {
            return new Region(r);
        }
        return null;
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet
    public Region find(Object o) {
        Region r = (Region) super.find(o);
        if (r != null) {
            return new Region(r);
        }
        return null;
    }

    public long occupied() {
        return this.occupied;
    }

    public long capacity() {
        return this.capacity;
    }

    private void allocated(Region r) {
        this.occupied += r.size();
    }

    private void freed(Region r) {
        this.occupied -= r.size();
    }

    private void free(Region r) {
        Region prev = removeAndReturn((Object) Long.valueOf(r.start() - 1));
        if (prev != null) {
            prev.merge(r);
            Region next = removeAndReturn((Object) Long.valueOf(r.end() + 1));
            if (next != null) {
                prev.merge(next);
            }
            add((AATreeFileAllocator) prev);
            return;
        }
        Region next2 = removeAndReturn((Object) Long.valueOf(r.end() + 1));
        if (next2 != null) {
            next2.merge(r);
            add((AATreeFileAllocator) next2);
        } else {
            add((AATreeFileAllocator) r);
        }
    }

    private Region find(long size) {
        Validation.validate(!VALIDATING || Long.bitCount(size) == 1);
        AATreeSet.Node<Region> currentNode = getRoot();
        Region currentRegion = (Region) currentNode.getPayload();
        if (currentRegion == null || (currentRegion.available() & size) == 0) {
            return null;
        }
        while (true) {
            Region left = (Region) currentNode.getLeft().getPayload();
            if (left != null && (left.available() & size) != 0) {
                currentNode = currentNode.getLeft();
                currentRegion = (Region) currentNode.getPayload();
            } else {
                if ((currentRegion.availableHere() & size) != 0) {
                    long mask = size - 1;
                    long a = (currentRegion.start() + mask) & (mask ^ (-1));
                    return new Region(a, (a + size) - 1);
                }
                Region right = (Region) currentNode.getRight().getPayload();
                if (right == null || (right.available() & size) == 0) {
                    break;
                }
                currentNode = currentNode.getRight();
                currentRegion = (Region) currentNode.getPayload();
            }
        }
        throw new AssertionError();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return "RegionSet = { " + super.toString() + " }";
    }

    void persist(DataOutput output) throws IOException {
        output.writeInt(MAGIC);
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            Region r = i$.next();
            persist(output, r);
        }
        output.writeInt(-1);
    }

    void persist(DataOutput output, Region r) throws IOException {
        output.writeInt(MAGIC_REGION);
        output.writeLong(r.start());
        output.writeLong(r.end());
    }
}

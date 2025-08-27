package org.terracotta.offheapstore.storage.allocator;

import org.terracotta.offheapstore.util.AATreeSet;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/PowerOfTwoAllocator.class */
public class PowerOfTwoAllocator extends AATreeSet<Region> {
    private static final boolean DEBUG = Boolean.getBoolean(PowerOfTwoAllocator.class.getName() + ".DEBUG");
    private static final boolean VALIDATING = Validation.shouldValidate(PowerOfTwoAllocator.class);
    private final int size;
    private volatile int occupied;

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/PowerOfTwoAllocator$Packing.class */
    public enum Packing {
        FLOOR { // from class: org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing.1
            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            AATreeSet.Node<Region> prefered(AATreeSet.Node<Region> node) {
                return node.getLeft();
            }

            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            AATreeSet.Node<Region> fallback(AATreeSet.Node<Region> node) {
                return node.getRight();
            }

            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            Region slice(Region region, int size) {
                int mask = size - 1;
                int a = (region.start() + mask) & (mask ^ (-1));
                return new Region(a, (a + size) - 1);
            }
        },
        CEILING { // from class: org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing.2
            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            AATreeSet.Node<Region> prefered(AATreeSet.Node<Region> node) {
                return node.getRight();
            }

            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            AATreeSet.Node<Region> fallback(AATreeSet.Node<Region> node) {
                return node.getLeft();
            }

            @Override // org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator.Packing
            Region slice(Region region, int size) {
                int mask = size - 1;
                int a = (region.end() + 1) & (mask ^ (-1));
                return new Region(a - size, a - 1);
            }
        };

        abstract AATreeSet.Node<Region> prefered(AATreeSet.Node<Region> node);

        abstract AATreeSet.Node<Region> fallback(AATreeSet.Node<Region> node);

        abstract Region slice(Region region, int i);
    }

    public PowerOfTwoAllocator(int size) {
        add((PowerOfTwoAllocator) new Region(0, size - 1));
        this.size = size;
    }

    public int allocate(int size, Packing packing) {
        if (Integer.bitCount(size) != 1) {
            throw new AssertionError("Size " + size + " is not a power of two");
        }
        Region r = findRegion(size, packing);
        if (r == null) {
            return -1;
        }
        Region current = removeAndReturn((Object) Integer.valueOf(r.start()));
        Region newRange = current.remove(r);
        if (newRange != null) {
            insert(current);
            insert(newRange);
        } else if (!current.isNull()) {
            insert(current);
        }
        this.occupied += r.size();
        validateFreeSpace();
        return r.start();
    }

    public void free(int address, int length) {
        if (length != 0) {
            free(new Region(address, (address + length) - 1));
            this.occupied -= length;
            validateFreeSpace();
        }
    }

    public void tryFree(int address, int length) {
        if (length != 0 && tryFree(new Region(address, (address + length) - 1))) {
            this.occupied -= length;
            validateFreeSpace();
        }
    }

    public int find(int size, Packing packing) {
        if (Integer.bitCount(size) != 1) {
            throw new AssertionError("Size " + size + " is not a power of two");
        }
        Region r = findRegion(size, packing);
        if (r == null) {
            return -1;
        }
        return r.start();
    }

    public void claim(int address, int size) {
        Region current = removeAndReturn((Object) Integer.valueOf(address));
        Region r = new Region(address, (address + size) - 1);
        Region newRange = current.remove(r);
        if (newRange != null) {
            insert(current);
            insert(newRange);
        } else if (!current.isNull()) {
            insert(current);
        }
        this.occupied += size;
        validateFreeSpace();
    }

    public int occupied() {
        return this.occupied;
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

    private void free(Region r) {
        Region prev = removeAndReturn((Object) Integer.valueOf(r.start() - 1));
        if (prev != null) {
            prev.merge(r);
            Region next = removeAndReturn((Object) Integer.valueOf(r.end() + 1));
            if (next != null) {
                prev.merge(next);
            }
            insert(prev);
            return;
        }
        Region next2 = removeAndReturn((Object) Integer.valueOf(r.end() + 1));
        if (next2 != null) {
            next2.merge(r);
            insert(next2);
        } else {
            insert(r);
        }
    }

    private boolean tryFree(Region r) {
        Region prev = removeAndReturn((Object) Integer.valueOf(r.start() - 1));
        if (prev != null) {
            if (prev.tryMerge(r)) {
                Region next = removeAndReturn((Object) Integer.valueOf(r.end() + 1));
                if (next != null) {
                    prev.merge(next);
                }
                insert(prev);
                return true;
            }
            insert(prev);
            return false;
        }
        Region next2 = removeAndReturn((Object) Integer.valueOf(r.end() + 1));
        if (next2 != null) {
            if (next2.tryMerge(r)) {
                insert(next2);
                return true;
            }
            insert(next2);
            return false;
        }
        return tryInsert(r);
    }

    private void insert(Region x) {
        if (!tryInsert(x)) {
            throw new AssertionError(x + " is already inserted");
        }
    }

    private boolean tryInsert(Region x) {
        return add((PowerOfTwoAllocator) x);
    }

    private Region findRegion(int size, Packing packing) {
        Validation.validate(!VALIDATING || Integer.bitCount(size) == 1);
        AATreeSet.Node<Region> currentNode = getRoot();
        Region currentRegion = (Region) currentNode.getPayload();
        if (currentRegion == null || (currentRegion.available() & size) == 0) {
            return null;
        }
        while (true) {
            AATreeSet.Node<Region> prefered = packing.prefered(currentNode);
            Region preferedRegion = (Region) prefered.getPayload();
            if (preferedRegion != null && (preferedRegion.available() & size) != 0) {
                currentNode = prefered;
                currentRegion = preferedRegion;
            } else {
                if ((currentRegion.availableHere() & size) != 0) {
                    return packing.slice(currentRegion, size);
                }
                AATreeSet.Node<Region> fallback = packing.fallback(currentNode);
                Region fallbackRegion = (Region) fallback.getPayload();
                if (fallbackRegion == null || (fallbackRegion.available() & size) == 0) {
                    break;
                }
                currentNode = fallback;
                currentRegion = fallbackRegion;
            }
        }
        throw new AssertionError();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        Region rootRegion = (Region) getRoot().getPayload();
        StringBuilder sb = new StringBuilder("PowerOfTwoAllocator: Occupied ");
        sb.append(DebuggingUtils.toBase2SuffixedString(occupied())).append("B");
        sb.append(" [Largest Available Area ").append(DebuggingUtils.toBase2SuffixedString(Integer.highestOneBit(rootRegion == null ? 0 : rootRegion.available()))).append("B]");
        if (DEBUG) {
            sb.append("\nFree Regions = ").append(super.toString()).append("");
        }
        return sb.toString();
    }

    private void validateFreeSpace() {
        if (VALIDATING) {
            Region rootRegion = (Region) getRoot().getPayload();
            if (occupied() != this.size - (rootRegion == null ? 0 : rootRegion.treeSize())) {
                throw new AssertionError("Occupied:" + occupied() + " Size-TreeSize:" + (this.size - (rootRegion == null ? 0 : rootRegion.treeSize())));
            }
        }
    }
}

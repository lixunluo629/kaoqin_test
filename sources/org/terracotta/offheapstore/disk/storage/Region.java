package org.terracotta.offheapstore.disk.storage;

import org.terracotta.offheapstore.util.AATreeSet;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/storage/Region.class */
class Region extends AATreeSet.AbstractTreeNode<Region> implements Comparable<Comparable<?>> {
    private long start;
    private long end;
    private long availableBitSet;

    Region(long value) {
        this(value, value);
    }

    Region(long start, long end) {
        this.start = start;
        this.end = end;
        updateAvailable();
    }

    Region(Region r) {
        this(r.start(), r.end());
    }

    long available() {
        if (getLeft().getPayload() == null && getRight().getPayload() == null) {
            return availableHere();
        }
        return this.availableBitSet;
    }

    long availableHere() {
        long bits = 0;
        for (int i = 0; i < 63; i++) {
            long size = 1 << i;
            long mask = size - 1;
            long a = (this.start + mask) & (mask ^ (-1));
            if ((this.end - a) + 1 >= size) {
                bits |= size;
            }
        }
        return bits;
    }

    private void updateAvailable() {
        Region left = (Region) getLeft().getPayload();
        Region right = (Region) getRight().getPayload();
        long leftAvailable = left == null ? 0L : left.available();
        long rightAvailable = right == null ? 0L : right.available();
        this.availableBitSet = availableHere() | leftAvailable | rightAvailable;
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
    public void setLeft(AATreeSet.Node<Region> l) {
        super.setLeft(l);
        updateAvailable();
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
    public void setRight(AATreeSet.Node<Region> r) {
        super.setRight(r);
        updateAvailable();
    }

    public String toString() {
        return "Range(" + this.start + "," + this.end + ") available:" + Long.toBinaryString(availableHere());
    }

    public long size() {
        if (isNull()) {
            return 0L;
        }
        return (this.end - this.start) + 1;
    }

    protected boolean isNull() {
        return this.start > this.end;
    }

    public Region remove(Region r) {
        if (r.start < this.start || r.end > this.end) {
            throw new AssertionError("Ranges : Illegal value passed to remove : " + this + " remove called for : " + r);
        }
        if (this.start == r.start) {
            this.start = r.end + 1;
            updateAvailable();
            return null;
        }
        if (this.end == r.end) {
            this.end = r.start - 1;
            updateAvailable();
            return null;
        }
        Region newRegion = new Region(r.end + 1, this.end);
        this.end = r.start - 1;
        updateAvailable();
        return newRegion;
    }

    public void merge(Region r) {
        if (this.start == r.end + 1) {
            this.start = r.start;
        } else if (this.end == r.start - 1) {
            this.end = r.end;
        } else {
            throw new AssertionError("Ranges : Merge called on non contiguous values : [this]:" + this + " and " + r);
        }
        updateAvailable();
    }

    @Override // java.lang.Comparable
    public int compareTo(Comparable<?> other) {
        if (other instanceof Region) {
            Region r = (Region) other;
            if (this.start < r.start) {
                return -1;
            }
            if (this.end > r.end) {
                return 1;
            }
            return 0;
        }
        if (other instanceof Long) {
            Long l = (Long) other;
            if (l.longValue() > this.end) {
                return -1;
            }
            if (l.longValue() < this.start) {
                return 1;
            }
            return 0;
        }
        throw new AssertionError();
    }

    public boolean equals(Object other) {
        if (other instanceof Region) {
            Region r = (Region) other;
            return this.start == r.start && this.end == r.end;
        }
        throw new AssertionError();
    }

    public int hashCode() {
        return (((3 * ((int) this.start)) ^ (7 * ((int) (this.start >>> 32)))) ^ (5 * ((int) this.end))) ^ (11 * ((int) (this.end >>> 32)));
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.Node
    public Region getPayload() {
        return this;
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.Node
    public void swapPayload(AATreeSet.Node<Region> other) {
        if (other instanceof Region) {
            Region r = (Region) other;
            long temp = this.start;
            this.start = r.start;
            r.start = temp;
            long temp2 = this.end;
            this.end = r.end;
            r.end = temp2;
            updateAvailable();
            return;
        }
        throw new AssertionError();
    }

    public long start() {
        return this.start;
    }

    public long end() {
        return this.end;
    }
}

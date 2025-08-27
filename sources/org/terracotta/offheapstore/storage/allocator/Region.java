package org.terracotta.offheapstore.storage.allocator;

import org.terracotta.offheapstore.util.AATreeSet;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/allocator/Region.class */
class Region extends AATreeSet.AbstractTreeNode<Region> implements Comparable<Comparable<?>> {
    private int start;
    private int end;
    private int availableBitSet;

    Region(int value) {
        this(value, value);
    }

    Region(int start, int end) {
        this.start = start;
        this.end = end;
        updateAvailable();
    }

    Region(Region r) {
        this(r.start(), r.end());
    }

    int available() {
        if (getLeft().getPayload() == null && getRight().getPayload() == null) {
            return availableHere();
        }
        return this.availableBitSet;
    }

    private void updateAvailable() {
        Region left = (Region) getLeft().getPayload();
        Region right = (Region) getRight().getPayload();
        int leftAvailable = left == null ? 0 : left.available();
        int rightAvailable = right == null ? 0 : right.available();
        this.availableBitSet = availableHere() | leftAvailable | rightAvailable;
    }

    int availableHere() {
        int bits = 0;
        for (int i = 0; i < 31; i++) {
            int size = 1 << i;
            int mask = size - 1;
            int a = (this.start + mask) & (mask ^ (-1));
            if ((this.end - a) + 1 >= size) {
                bits |= size;
            }
        }
        return bits;
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
        return "Range(" + this.start + "," + this.end + ") available:" + Integer.toBinaryString(availableHere());
    }

    public int size() {
        if (isNull()) {
            return 0;
        }
        return (this.end - this.start) + 1;
    }

    protected boolean isNull() {
        return this.start > this.end;
    }

    int treeSize() {
        int treeSize = size();
        Region left = (Region) getLeft().getPayload();
        int treeSize2 = treeSize + (left == null ? 0 : left.treeSize());
        Region right = (Region) getRight().getPayload();
        return treeSize2 + (right == null ? 0 : right.treeSize());
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

    public boolean tryMerge(Region r) {
        if (this.start == r.end + 1) {
            this.start = r.start;
        } else if (this.end == r.start - 1) {
            this.end = r.end;
        } else {
            if (this.start <= r.start && this.end >= r.end) {
                return false;
            }
            throw new AssertionError("Ranges : Merge called on non contiguous values : [this]:" + this + " and " + r);
        }
        updateAvailable();
        return true;
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
        if (other instanceof Integer) {
            Integer l = (Integer) other;
            if (l.intValue() > this.end) {
                return -1;
            }
            if (l.intValue() < this.start) {
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
        return (3 * this.start) ^ (7 * this.end);
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.Node
    public Region getPayload() {
        return this;
    }

    @Override // org.terracotta.offheapstore.util.AATreeSet.Node
    public void swapPayload(AATreeSet.Node<Region> other) {
        if (other instanceof Region) {
            Region r = (Region) other;
            int temp = this.start;
            this.start = r.start;
            r.start = temp;
            int temp2 = this.end;
            this.end = r.end;
            r.end = temp2;
            updateAvailable();
            return;
        }
        throw new AssertionError();
    }

    public int start() {
        return this.start;
    }

    public int end() {
        return this.end;
    }
}

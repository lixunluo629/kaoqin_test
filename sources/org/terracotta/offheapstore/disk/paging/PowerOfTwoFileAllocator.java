package org.terracotta.offheapstore.disk.paging;

import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/paging/PowerOfTwoFileAllocator.class */
public class PowerOfTwoFileAllocator {
    private static final boolean DEBUG = Boolean.getBoolean(PowerOfTwoFileAllocator.class.getName() + ".DEBUG");
    private static final boolean VALIDATING = Validation.shouldValidate(PowerOfTwoFileAllocator.class);
    private static final Region NULL_NODE = new Region();
    private Region root;
    private Region deletedNode;
    private Region lastNode;
    private Region deletedElement;
    private long occupied;

    public PowerOfTwoFileAllocator() {
        this(Long.MAX_VALUE);
    }

    public PowerOfTwoFileAllocator(long size) {
        this.root = NULL_NODE;
        this.root = new Region(0L, size);
    }

    public Long allocate(long size) {
        if (Long.bitCount(size) != 1) {
            throw new AssertionError("Size " + size + " is not a power of two");
        }
        Region r = find(size);
        if (r == null) {
            return null;
        }
        mark(r);
        return Long.valueOf(r.start());
    }

    public void free(long address, long length) {
        if (length != 0) {
            Region r = new Region(address, (address + length) - 1);
            free(r);
            freed(r);
        }
    }

    public void mark(long address, long length) {
        if (length != 0) {
            mark(new Region(address, (address + length) - 1));
        }
    }

    public long occupied() {
        return this.occupied;
    }

    private void allocated(Region r) {
        this.occupied += r.size();
    }

    private void freed(Region r) {
        this.occupied -= r.size();
    }

    private void mark(Region r) {
        Region current = remove(find(r));
        Region newRange = current.remove(r);
        if (newRange != null) {
            insert(current);
            insert(newRange);
        } else if (!current.isNull()) {
            insert(current);
        }
        allocated(r);
    }

    private void free(Region r) {
        Region prev = find(new Region(r.start() - 1));
        if (prev != null) {
            Region prev2 = remove(prev);
            prev2.merge(r);
            Region next = remove(new Region(r.end() + 1));
            if (next != null) {
                prev2.merge(next);
            }
            insert(prev2);
            return;
        }
        Region next2 = find(new Region(r.end() + 1));
        if (next2 != null) {
            Region next3 = remove(next2);
            next3.merge(r);
            insert(next3);
            return;
        }
        insert(r);
    }

    private void insert(Region x) {
        this.root = insert(x, this.root);
    }

    private Region remove(Region x) {
        this.deletedNode = NULL_NODE;
        this.root = remove(x, this.root);
        Region d = this.deletedElement;
        this.deletedElement = null;
        if (d == null) {
            return null;
        }
        return new Region(d);
    }

    private Region find(long size) {
        Validation.validate(!VALIDATING || Long.bitCount(size) == 1);
        Region current = this.root;
        if ((current.available() & size) == 0) {
            return null;
        }
        while (true) {
            if (current.left != null && (current.left.available() & size) != 0) {
                current = current.left;
            } else {
                if ((current.availableHere() & size) != 0) {
                    long mask = size - 1;
                    long a = (current.start() + mask) & (mask ^ (-1));
                    return new Region(a, (a + size) - 1);
                }
                if (current.right == null || (current.right.available() & size) == 0) {
                    break;
                }
                current = current.right;
            }
        }
        throw new AssertionError();
    }

    private Region find(Region x) {
        Region region = this.root;
        while (true) {
            Region current = region;
            if (current != NULL_NODE) {
                long res = x.orderRelativeTo(current);
                if (res < 0) {
                    region = current.left;
                } else {
                    if (res <= 0) {
                        return current;
                    }
                    region = current.right;
                }
            } else {
                return null;
            }
        }
    }

    private Region insert(Region x, Region t) {
        if (t == NULL_NODE) {
            t = x;
        } else if (x.orderRelativeTo(t) < 0) {
            t.left(insert(x, t.left));
        } else if (x.orderRelativeTo(t) > 0) {
            t.right(insert(x, t.right));
        } else {
            throw new AssertionError("Cannot insert " + x + " into " + this);
        }
        return split(skew(t));
    }

    private Region remove(Region x, Region t) {
        if (t != NULL_NODE) {
            this.lastNode = t;
            if (x.orderRelativeTo(t) < 0) {
                t.left(remove(x, t.left));
            } else {
                this.deletedNode = t;
                t.right(remove(x, t.right));
            }
            if (t == this.lastNode) {
                if (this.deletedNode != NULL_NODE && x.orderRelativeTo(this.deletedNode) == 0) {
                    this.deletedNode.swap(t);
                    this.deletedElement = t;
                    t = t.right;
                }
            } else if (t.left.level < t.level - 1 || t.right.level < t.level - 1) {
                if (t.right.level > Region.access$306(t)) {
                    t.right.level = t.level;
                }
                Region t2 = skew(t);
                t2.right(skew(t2.right));
                t2.right.right(skew(t2.right.right));
                t = split(t2);
                t.right(split(t.right));
            }
        }
        return t;
    }

    private static Region skew(Region t) {
        if (t.left.level == t.level) {
            t = rotateWithLeftChild(t);
        }
        return t;
    }

    private static Region split(Region t) {
        if (t.right.right.level == t.level) {
            t = rotateWithRightChild(t);
            Region.access$308(t);
        }
        return t;
    }

    private static Region rotateWithLeftChild(Region k2) {
        Region k1 = k2.left;
        k2.left(k1.right);
        k1.right(k2);
        return k1;
    }

    private static Region rotateWithRightChild(Region k1) {
        Region k2 = k1.right;
        k1.right(k2.left);
        k2.left(k1);
        return k2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (DEBUG) {
            sb.append("\nFree Regions = ").append(this.root.dump()).append("");
        }
        return sb.toString();
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/paging/PowerOfTwoFileAllocator$Region.class */
    static class Region {
        private Region left;
        private Region right;
        private int level;
        private long start;
        private long end;
        private long availableBitSet;

        static /* synthetic */ int access$306(Region x0) {
            int i = x0.level - 1;
            x0.level = i;
            return i;
        }

        static /* synthetic */ int access$308(Region x0) {
            int i = x0.level;
            x0.level = i + 1;
            return i;
        }

        Region() {
            this.start = 1L;
            this.end = 0L;
            this.level = 0;
            this.left = this;
            this.right = this;
            this.availableBitSet = 0L;
        }

        Region(long value) {
            this(value, value);
        }

        Region(long start, long end) {
            this.start = start;
            this.end = end;
            this.left = PowerOfTwoFileAllocator.NULL_NODE;
            this.right = PowerOfTwoFileAllocator.NULL_NODE;
            this.level = 1;
            updateAvailable();
        }

        Region(Region r) {
            this(r.start(), r.end());
        }

        long available() {
            if (this.left == PowerOfTwoFileAllocator.NULL_NODE && this.right == PowerOfTwoFileAllocator.NULL_NODE) {
                return availableHere();
            }
            return this.availableBitSet;
        }

        private void updateAvailable() {
            this.availableBitSet = availableHere() | this.left.available() | this.right.available();
        }

        long availableHere() {
            long bits = 0;
            for (int i = 0; i < 63; i++) {
                long size = 1 << i;
                long mask = size - 1;
                long a = (this.start + mask) & (mask ^ (-1));
                if (this.end - a >= size - 1) {
                    bits |= size;
                }
            }
            return bits;
        }

        void left(Region l) {
            this.left = l;
            updateAvailable();
        }

        void right(Region r) {
            this.right = r;
            updateAvailable();
        }

        public String toString() {
            if (this == PowerOfTwoFileAllocator.NULL_NODE) {
                return "EMPTY";
            }
            return "Range(" + this.start + "," + this.end + ") available:" + Long.toBinaryString(availableHere());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String dump() {
            String ds;
            String ds2;
            if (this.left == PowerOfTwoFileAllocator.NULL_NODE) {
                ds = "(" + String.valueOf(this);
            } else {
                String ds3 = "(" + this.left.dump();
                ds = ds3 + " <= " + String.valueOf(this);
            }
            if (this.right != PowerOfTwoFileAllocator.NULL_NODE) {
                ds2 = ds + " => " + this.right.dump() + ")";
            } else {
                ds2 = ds + ")";
            }
            return ds2;
        }

        public long size() {
            if (isNull()) {
                return 0L;
            }
            return (this.end - this.start) + 1;
        }

        public boolean isNull() {
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

        public int orderRelativeTo(Region other) {
            if (this.start < other.start) {
                return -1;
            }
            if (this.end > other.end) {
                return 1;
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void swap(Region other) {
            long temp = this.start;
            this.start = other.start;
            other.start = temp;
            long temp2 = this.end;
            this.end = other.end;
            other.end = temp2;
            updateAvailable();
        }

        public long start() {
            return this.start;
        }

        public long end() {
            return this.end;
        }
    }
}

package org.terracotta.statistics.jsr166e;

import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.terracotta.statistics.util.VicariousThreadLocal;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/jsr166e/Striped64.class */
abstract class Striped64 extends Number {
    volatile transient Cell[] cells;
    volatile transient long base;
    volatile transient int busy;
    static final ThreadHashCode threadHashCode = new ThreadHashCode();
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final AtomicLongFieldUpdater<Striped64> BASE_UPDATER = AtomicLongFieldUpdater.newUpdater(Striped64.class, "base");
    static final AtomicIntegerFieldUpdater<Striped64> BUSY_UPDATER = AtomicIntegerFieldUpdater.newUpdater(Striped64.class, "busy");

    abstract long fn(long j, long j2);

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/jsr166e/Striped64$Cell.class */
    static final class Cell {
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long value;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        private static final AtomicLongFieldUpdater<Cell> VALUE_UPDATER = AtomicLongFieldUpdater.newUpdater(Cell.class, "value");

        Cell(long x) {
            VALUE_UPDATER.set(this, x);
        }

        final boolean cas(long cmp, long val) {
            return VALUE_UPDATER.compareAndSet(this, cmp, val);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/jsr166e/Striped64$HashCode.class */
    static final class HashCode {
        static final Random rng = new Random();
        int code;

        HashCode() {
            int h = rng.nextInt();
            this.code = h == 0 ? 1 : h;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/jsr166e/Striped64$ThreadHashCode.class */
    static final class ThreadHashCode extends VicariousThreadLocal<HashCode> {
        ThreadHashCode() {
        }

        @Override // java.lang.ThreadLocal
        public HashCode initialValue() {
            return new HashCode();
        }
    }

    Striped64() {
    }

    final boolean casBase(long cmp, long val) {
        return BASE_UPDATER.compareAndSet(this, cmp, val);
    }

    final boolean casBusy() {
        return BUSY_UPDATER.compareAndSet(this, 0, 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v29, types: [long, org.terracotta.statistics.jsr166e.Striped64$Cell] */
    final void retryUpdate(long x, HashCode hc, boolean wasUncontended) {
        int n;
        int m;
        int h = hc.code;
        boolean collide = false;
        while (true) {
            Cell[] cellArr = this.cells;
            if (cellArr != 0 && (n = cellArr.length) > 0) {
                ?? r0 = cellArr[(n - 1) & h];
                if (r0 == 0) {
                    if (this.busy == 0) {
                        Cell r = new Cell(x);
                        if (this.busy == 0 && casBusy()) {
                            boolean created = false;
                            try {
                                Cell[] rs = this.cells;
                                if (rs != null && (m = rs.length) > 0) {
                                    int j = (m - 1) & h;
                                    if (rs[j] == null) {
                                        rs[j] = r;
                                        created = true;
                                    }
                                }
                                BUSY_UPDATER.set(this, 0);
                                if (created) {
                                    break;
                                }
                            } finally {
                            }
                        }
                    }
                    collide = false;
                    int h2 = h ^ (h << 13);
                    int h3 = h2 ^ (h2 >>> 17);
                    h = h3 ^ (h3 << 5);
                } else {
                    if (!wasUncontended) {
                        wasUncontended = true;
                    } else {
                        if (r0.cas(r0.value, fn(r0, x))) {
                            break;
                        }
                        if (n >= NCPU || this.cells != cellArr) {
                            collide = false;
                        } else if (!collide) {
                            collide = true;
                        } else if (this.busy == 0 && casBusy()) {
                            try {
                                if (this.cells == cellArr) {
                                    Cell[] cellArr2 = new Cell[n << 1];
                                    for (int i = 0; i < n; i++) {
                                        cellArr2[i] = cellArr[i];
                                    }
                                    this.cells = cellArr2;
                                }
                                BUSY_UPDATER.set(this, 0);
                                collide = false;
                            } finally {
                            }
                        }
                    }
                    int h22 = h ^ (h << 13);
                    int h32 = h22 ^ (h22 >>> 17);
                    h = h32 ^ (h32 << 5);
                }
            } else if (this.busy == 0 && this.cells == cellArr && casBusy()) {
                boolean init = false;
                try {
                    if (this.cells == cellArr) {
                        Cell[] rs2 = new Cell[2];
                        rs2[h & 1] = new Cell(x);
                        this.cells = rs2;
                        init = true;
                    }
                    BUSY_UPDATER.set(this, 0);
                    if (init) {
                        break;
                    }
                } finally {
                    BUSY_UPDATER.set(this, 0);
                }
            } else {
                long v = this.base;
                if (casBase(v, fn(v, x))) {
                    break;
                }
            }
        }
        hc.code = h;
    }

    final void internalReset(long initialValue) {
        Cell[] as = this.cells;
        BASE_UPDATER.set(this, initialValue);
        if (as != null) {
            for (Cell a : as) {
                if (a != null) {
                    a.value = initialValue;
                }
            }
        }
    }
}

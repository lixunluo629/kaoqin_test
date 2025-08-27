package org.terracotta.statistics.jsr166e;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.terracotta.statistics.jsr166e.Striped64;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/jsr166e/LongMaxUpdater.class */
public class LongMaxUpdater extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    @Override // org.terracotta.statistics.jsr166e.Striped64
    final long fn(long v, long x) {
        return v > x ? v : x;
    }

    public LongMaxUpdater() {
        BASE_UPDATER.set(this, Long.MIN_VALUE);
    }

    public void update(long x) {
        int n;
        Striped64.Cell a;
        Striped64.Cell[] as = this.cells;
        if (as == null) {
            long b = this.base;
            if (b >= x || casBase(b, x)) {
                return;
            }
        }
        boolean uncontended = true;
        Striped64.HashCode hc = threadHashCode.get();
        int h = hc.code;
        if (as != null && (n = as.length) >= 1 && (a = as[(n - 1) & h]) != null) {
            if (a.value >= x) {
                return;
            }
            boolean zCas = a.cas(h, x);
            uncontended = zCas;
            if (zCas) {
                return;
            }
        }
        retryUpdate(x, hc, uncontended);
    }

    public long max() {
        Striped64.Cell[] as = this.cells;
        long max = this.base;
        if (as != null) {
            for (Striped64.Cell a : as) {
                if (a != null) {
                    long v = a.value;
                    if (v > max) {
                        max = v;
                    }
                }
            }
        }
        return max;
    }

    public void reset() {
        internalReset(Long.MIN_VALUE);
    }

    public long maxThenReset() {
        Striped64.Cell[] as = this.cells;
        long max = this.base;
        BASE_UPDATER.set(this, Long.MIN_VALUE);
        if (as != null) {
            for (Striped64.Cell a : as) {
                if (a != null) {
                    long v = a.value;
                    a.value = Long.MIN_VALUE;
                    if (v > max) {
                        max = v;
                    }
                }
            }
        }
        return max;
    }

    public String toString() {
        return Long.toString(max());
    }

    @Override // java.lang.Number
    public long longValue() {
        return max();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) max();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return max();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return max();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(max());
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        BUSY_UPDATER.set(this, 0);
        this.cells = null;
        BASE_UPDATER.set(this, s.readLong());
    }
}

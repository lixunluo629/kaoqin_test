package org.terracotta.offheapstore.util;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/MemoryUnit.class */
public enum MemoryUnit {
    BITS(-3),
    NIBBLES(-1),
    BYTES(0),
    KILOBYTES(10),
    MEGABYTES(20),
    GIGABYTES(30),
    TERABYTES(40);

    private final int index;

    MemoryUnit(int index) {
        this.index = index;
    }

    public long convert(long duration, MemoryUnit unit) {
        return doConvert(unit.index - this.index, duration);
    }

    public long toBits(long amount) {
        return doConvert(this.index - BITS.index, amount);
    }

    public int toBits(int amount) {
        return doConvert(this.index - BITS.index, amount);
    }

    public long toBytes(long amount) {
        return doConvert(this.index - BYTES.index, amount);
    }

    public int toBytes(int amount) {
        return doConvert(this.index - BYTES.index, amount);
    }

    private static long doConvert(int delta, long amount) {
        if (amount >= 0) {
            if (delta == 0) {
                return amount;
            }
            if (delta < 0) {
                return amount >>> (-delta);
            }
            if (delta >= Long.numberOfLeadingZeros(amount)) {
                return Long.MAX_VALUE;
            }
            return amount << delta;
        }
        throw new IllegalArgumentException();
    }

    private static int doConvert(int delta, int amount) {
        if (amount >= 0) {
            if (delta == 0) {
                return amount;
            }
            if (delta < 0) {
                return amount >>> (-delta);
            }
            if (delta >= Integer.numberOfLeadingZeros(amount)) {
                return Integer.MAX_VALUE;
            }
            return amount << delta;
        }
        throw new IllegalArgumentException();
    }
}

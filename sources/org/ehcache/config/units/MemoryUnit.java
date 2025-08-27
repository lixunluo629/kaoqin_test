package org.ehcache.config.units;

import org.ehcache.config.ResourceUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/units/MemoryUnit.class */
public enum MemoryUnit implements ResourceUnit {
    B("B", 0),
    KB("kB", 10),
    MB("MB", 20),
    GB("GB", 30),
    TB("TB", 40),
    PB("PB", 50);

    private final int index;
    private final String stringForm;

    MemoryUnit(String stringForm, int index) {
        this.stringForm = stringForm;
        this.index = index;
    }

    private static long doConvert(int delta, long amount) throws ArithmeticException {
        if (delta == 0 || amount == 0) {
            return amount;
        }
        if (delta < 0) {
            long t = amount >> ((-delta) - 1);
            return ((t >>> (64 + delta)) + amount) >> (-delta);
        }
        if (delta >= Long.numberOfLeadingZeros(amount < 0 ? amount ^ (-1) : amount)) {
            throw new ArithmeticException("Conversion overflows");
        }
        return amount << delta;
    }

    public long toBytes(long quantity) {
        return doConvert(this.index - B.index, quantity);
    }

    public long convert(long quantity, MemoryUnit unit) {
        return doConvert(unit.index - this.index, quantity);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.stringForm;
    }

    @Override // org.ehcache.config.ResourceUnit
    public int compareTo(long thisSize, long thatSize, ResourceUnit thatUnit) throws IllegalArgumentException {
        if (thatUnit instanceof MemoryUnit) {
            MemoryUnit mThatUnit = (MemoryUnit) thatUnit;
            if (this.index < mThatUnit.index) {
                try {
                    return Long.signum(thisSize - convert(thatSize, mThatUnit));
                } catch (ArithmeticException e) {
                    return Long.signum(mThatUnit.convert(thisSize, this) - thatSize);
                }
            }
            try {
                return Long.signum(mThatUnit.convert(thisSize, this) - thatSize);
            } catch (ArithmeticException e2) {
                return Long.signum(thisSize - convert(thatSize, mThatUnit));
            }
        }
        throw new IllegalArgumentException();
    }
}

package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import java.util.BitSet;

@GwtIncompatible("no precomputation is done in GWT")
/* loaded from: guava-18.0.jar:com/google/common/base/SmallCharMatcher.class */
final class SmallCharMatcher extends CharMatcher.FastMatcher {
    static final int MAX_SIZE = 1023;
    private final char[] table;
    private final boolean containsZero;
    private final long filter;
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5d;

    private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
        super(description);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
    }

    static int smear(int hashCode) {
        return C2 * Integer.rotateLeft(hashCode * C1, 15);
    }

    private boolean checkFilter(int c) {
        return 1 == (1 & (this.filter >> c));
    }

    @VisibleForTesting
    static int chooseTableSize(int setSize) {
        if (setSize == 1) {
            return 2;
        }
        int iHighestOneBit = Integer.highestOneBit(setSize - 1);
        while (true) {
            int tableSize = iHighestOneBit << 1;
            if (tableSize * DESIRED_LOAD_FACTOR < setSize) {
                iHighestOneBit = tableSize;
            } else {
                return tableSize;
            }
        }
    }

    static CharMatcher from(BitSet chars, String description) {
        int index;
        long filter = 0;
        int size = chars.cardinality();
        boolean containsZero = chars.get(0);
        char[] table = new char[chooseTableSize(size)];
        int mask = table.length - 1;
        int iNextSetBit = chars.nextSetBit(0);
        while (true) {
            int c = iNextSetBit;
            if (c != -1) {
                filter |= 1 << c;
                int iSmear = smear(c);
                while (true) {
                    index = iSmear & mask;
                    if (table[index] == 0) {
                        break;
                    }
                    iSmear = index + 1;
                }
                table[index] = (char) c;
                iNextSetBit = chars.nextSetBit(c + 1);
            } else {
                return new SmallCharMatcher(table, filter, containsZero, description);
            }
        }
    }

    @Override // com.google.common.base.CharMatcher
    public boolean matches(char c) {
        if (c == 0) {
            return this.containsZero;
        }
        if (!checkFilter(c)) {
            return false;
        }
        int mask = this.table.length - 1;
        int startingIndex = smear(c) & mask;
        int index = startingIndex;
        while (this.table[index] != 0) {
            if (this.table[index] == c) {
                return true;
            }
            index = (index + 1) & mask;
            if (index == startingIndex) {
                return false;
            }
        }
        return false;
    }

    @Override // com.google.common.base.CharMatcher
    void setBits(BitSet table) {
        if (this.containsZero) {
            table.set(0);
        }
        char[] arr$ = this.table;
        for (char c : arr$) {
            if (c != 0) {
                table.set(c);
            }
        }
    }
}

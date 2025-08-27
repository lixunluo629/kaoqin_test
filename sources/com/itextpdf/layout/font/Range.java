package com.itextpdf.layout.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/Range.class */
public class Range {
    private SubRange[] ranges;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Range.class.desiredAssertionStatus();
    }

    private Range() {
    }

    Range(List<SubRange> ranges) {
        if (ranges.size() == 0) {
            throw new IllegalArgumentException("Ranges shall not be empty");
        }
        this.ranges = normalizeSubRanges(ranges);
    }

    public boolean contains(int n) {
        int low = 0;
        int high = this.ranges.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (this.ranges[mid].compareTo(n) < 0) {
                low = mid + 1;
            } else if (this.ranges[mid].compareTo(n) > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range range = (Range) o;
        return Arrays.equals(this.ranges, range.ranges);
    }

    public int hashCode() {
        return Arrays.hashCode(this.ranges);
    }

    public String toString() {
        return Arrays.toString(this.ranges);
    }

    private static SubRange[] normalizeSubRanges(List<SubRange> ranges) {
        Collections.sort(ranges);
        List<SubRange> union = new ArrayList<>(ranges.size());
        if (!$assertionsDisabled && ranges.size() <= 0) {
            throw new AssertionError();
        }
        SubRange curr = ranges.get(0);
        union.add(curr);
        for (int i = 1; i < ranges.size(); i++) {
            SubRange next = ranges.get(i);
            if (next.low <= curr.high) {
                if (next.high > curr.high) {
                    curr.high = next.high;
                }
            } else {
                curr = next;
                union.add(curr);
            }
        }
        return (SubRange[]) union.toArray(new SubRange[0]);
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/Range$SubRange.class */
    static class SubRange implements Comparable<SubRange> {
        int low;
        int high;

        SubRange(int low, int high) {
            this.low = low;
            this.high = high;
        }

        @Override // java.lang.Comparable
        public int compareTo(SubRange o) {
            return this.low - o.low;
        }

        public int compareTo(int n) {
            if (n < this.low) {
                return 1;
            }
            return n > this.high ? -1 : 0;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SubRange subRange = (SubRange) o;
            return this.low == subRange.low && this.high == subRange.high;
        }

        public int hashCode() {
            return (31 * this.low) + this.high;
        }

        public String toString() {
            return "(" + this.low + "; " + this.high + ')';
        }
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/Range$FullRange.class */
    static class FullRange extends Range {
        FullRange() {
            super();
        }

        @Override // com.itextpdf.layout.font.Range
        public boolean contains(int uni) {
            return true;
        }

        @Override // com.itextpdf.layout.font.Range
        public boolean equals(Object o) {
            return this == o;
        }

        @Override // com.itextpdf.layout.font.Range
        public int hashCode() {
            return 1;
        }

        @Override // com.itextpdf.layout.font.Range
        public String toString() {
            return "[FullRange]";
        }
    }
}

package com.itextpdf.kernel.counter.data;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/WaitTime.class */
public final class WaitTime {
    private final long time;
    private final long initial;
    private final long maximum;

    public WaitTime(long initial, long maximum) {
        this(initial, maximum, initial);
    }

    public WaitTime(long initial, long maximum, long time) {
        this.initial = initial;
        this.maximum = maximum;
        this.time = time;
    }

    public long getInitial() {
        return this.initial;
    }

    public long getMaximum() {
        return this.maximum;
    }

    public long getTime() {
        return this.time;
    }
}

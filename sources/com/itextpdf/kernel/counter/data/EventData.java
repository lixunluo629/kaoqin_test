package com.itextpdf.kernel.counter.data;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventData.class */
public class EventData<T> {
    private final T signature;
    private long count;

    public EventData(T signature) {
        this(signature, 1L);
    }

    public EventData(T signature, long count) {
        this.signature = signature;
        this.count = count;
    }

    public final T getSignature() {
        return this.signature;
    }

    public final long getCount() {
        return this.count;
    }

    protected void mergeWith(EventData<T> data) {
        this.count += data.getCount();
    }
}

package com.itextpdf.kernel.counter;

import com.itextpdf.kernel.counter.context.IContext;
import com.itextpdf.kernel.counter.context.UnknownContext;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/EventCounter.class */
public abstract class EventCounter {
    final IContext fallback;

    protected abstract void onEvent(IEvent iEvent, IMetaInfo iMetaInfo);

    public EventCounter() {
        this(UnknownContext.PERMISSIVE);
    }

    public EventCounter(IContext fallback) {
        if (fallback == null) {
            throw new IllegalArgumentException("The fallback context in EventCounter constructor cannot be null");
        }
        this.fallback = fallback;
    }
}

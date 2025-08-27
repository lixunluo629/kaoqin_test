package com.itextpdf.kernel.counter.context;

import com.itextpdf.kernel.counter.event.IEvent;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/context/UnknownContext.class */
public class UnknownContext implements IContext {
    public static final IContext RESTRICTIVE = new UnknownContext(false);
    public static final IContext PERMISSIVE = new UnknownContext(true);
    private final boolean allowEvents;

    public UnknownContext(boolean allowEvents) {
        this.allowEvents = allowEvents;
    }

    @Override // com.itextpdf.kernel.counter.context.IContext
    public boolean allow(IEvent event) {
        return this.allowEvents;
    }
}

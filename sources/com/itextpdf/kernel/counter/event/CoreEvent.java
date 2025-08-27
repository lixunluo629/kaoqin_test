package com.itextpdf.kernel.counter.event;

import com.itextpdf.kernel.counter.NamespaceConstant;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/event/CoreEvent.class */
public class CoreEvent implements IGenericEvent {
    public static final CoreEvent PROCESS = new CoreEvent("process");
    private final String subtype;

    private CoreEvent(String subtype) {
        this.subtype = subtype;
    }

    @Override // com.itextpdf.kernel.counter.event.IEvent
    public String getEventType() {
        return "core-" + this.subtype;
    }

    @Override // com.itextpdf.kernel.counter.event.IGenericEvent
    public String getOriginId() {
        return NamespaceConstant.ITEXT;
    }
}

package com.itextpdf.kernel.counter.context;

import com.itextpdf.kernel.counter.event.IEvent;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/context/IContext.class */
public interface IContext {
    boolean allow(IEvent iEvent);
}

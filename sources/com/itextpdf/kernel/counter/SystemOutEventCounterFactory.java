package com.itextpdf.kernel.counter;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/SystemOutEventCounterFactory.class */
public class SystemOutEventCounterFactory implements IEventCounterFactory {
    @Override // com.itextpdf.kernel.counter.IEventCounterFactory
    public EventCounter getCounter(Class<?> cls) {
        return cls != null ? new SystemOutEventCounter(cls) : new SystemOutEventCounter();
    }
}

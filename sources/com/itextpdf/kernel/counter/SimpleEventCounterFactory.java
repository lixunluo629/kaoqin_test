package com.itextpdf.kernel.counter;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/SimpleEventCounterFactory.class */
public class SimpleEventCounterFactory implements IEventCounterFactory {
    private EventCounter counter;

    public SimpleEventCounterFactory(EventCounter counter) {
        this.counter = counter;
    }

    @Override // com.itextpdf.kernel.counter.IEventCounterFactory
    public EventCounter getCounter(Class<?> cls) {
        return this.counter;
    }
}

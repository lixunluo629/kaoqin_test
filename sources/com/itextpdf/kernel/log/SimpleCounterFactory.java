package com.itextpdf.kernel.log;

@Deprecated
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/log/SimpleCounterFactory.class */
public class SimpleCounterFactory implements ICounterFactory {
    private ICounter counter;

    public SimpleCounterFactory(ICounter counter) {
        this.counter = counter;
    }

    @Override // com.itextpdf.kernel.log.ICounterFactory
    public ICounter getCounter(Class<?> cls) {
        return this.counter;
    }
}

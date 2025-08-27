package com.itextpdf.kernel.log;

@Deprecated
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/log/SystemOutCounterFactory.class */
public class SystemOutCounterFactory implements ICounterFactory {
    @Override // com.itextpdf.kernel.log.ICounterFactory
    public ICounter getCounter(Class<?> cls) {
        return cls != null ? new SystemOutCounter(cls) : new SystemOutCounter();
    }
}

package com.itextpdf.kernel.log;

import com.itextpdf.io.util.MessageFormatUtil;

@Deprecated
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/log/SystemOutCounter.class */
public class SystemOutCounter implements ICounter {
    protected String name;

    public SystemOutCounter(String name) {
        this.name = name;
    }

    public SystemOutCounter() {
        this("iText");
    }

    public SystemOutCounter(Class<?> cls) {
        this(cls.getName());
    }

    @Override // com.itextpdf.kernel.log.ICounter
    public void onDocumentRead(long size) {
        System.out.println(MessageFormatUtil.format("[{0}] {1} bytes read", this.name, Long.valueOf(size)));
    }

    @Override // com.itextpdf.kernel.log.ICounter
    public void onDocumentWritten(long size) {
        System.out.println(MessageFormatUtil.format("[{0}] {1} bytes written", this.name, Long.valueOf(size)));
    }
}

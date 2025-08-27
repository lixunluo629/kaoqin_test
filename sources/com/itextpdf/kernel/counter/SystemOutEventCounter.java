package com.itextpdf.kernel.counter;

import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/SystemOutEventCounter.class */
public class SystemOutEventCounter extends EventCounter {
    protected String name;

    public SystemOutEventCounter(String name) {
        this.name = name;
    }

    public SystemOutEventCounter() {
        this("iText");
    }

    public SystemOutEventCounter(Class<?> cls) {
        this(cls.getName());
    }

    @Override // com.itextpdf.kernel.counter.EventCounter
    protected void onEvent(IEvent event, IMetaInfo metaInfo) {
        System.out.println(MessageFormatUtil.format("[{0}] {1} event", this.name, event.getEventType()));
    }
}

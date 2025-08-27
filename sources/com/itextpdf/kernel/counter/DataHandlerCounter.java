package com.itextpdf.kernel.counter;

import com.itextpdf.kernel.counter.context.IContext;
import com.itextpdf.kernel.counter.context.UnknownContext;
import com.itextpdf.kernel.counter.data.EventData;
import com.itextpdf.kernel.counter.data.EventDataHandler;
import com.itextpdf.kernel.counter.data.EventDataHandlerUtil;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/DataHandlerCounter.class */
public class DataHandlerCounter<T, V extends EventData<T>> extends EventCounter {
    private final EventDataHandler<T, V> dataHandler;

    public DataHandlerCounter(EventDataHandler<T, V> dataHandler) {
        this(dataHandler, UnknownContext.PERMISSIVE);
    }

    public DataHandlerCounter(EventDataHandler<T, V> dataHandler, IContext fallback) {
        super(fallback);
        this.dataHandler = dataHandler;
        EventDataHandlerUtil.registerProcessAllShutdownHook(dataHandler);
        EventDataHandlerUtil.registerTimedProcessing(dataHandler);
    }

    @Override // com.itextpdf.kernel.counter.EventCounter
    protected void onEvent(IEvent event, IMetaInfo metaInfo) {
        this.dataHandler.register(event, metaInfo);
    }
}

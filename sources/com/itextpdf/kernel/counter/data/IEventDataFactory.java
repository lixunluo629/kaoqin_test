package com.itextpdf.kernel.counter.data;

import com.itextpdf.kernel.counter.data.EventData;
import com.itextpdf.kernel.counter.event.IEvent;
import com.itextpdf.kernel.counter.event.IMetaInfo;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/IEventDataFactory.class */
public interface IEventDataFactory<T, V extends EventData<T>> {
    V create(IEvent iEvent, IMetaInfo iMetaInfo);
}

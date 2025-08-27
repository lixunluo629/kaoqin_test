package com.itextpdf.kernel.counter.data;

import com.itextpdf.kernel.counter.data.EventData;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/IEventDataCache.class */
public interface IEventDataCache<T, V extends EventData<T>> {
    void put(V v);

    V retrieveNext();

    List<V> clear();
}

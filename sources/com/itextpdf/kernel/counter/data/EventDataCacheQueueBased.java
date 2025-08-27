package com.itextpdf.kernel.counter.data;

import com.itextpdf.kernel.counter.data.EventData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventDataCacheQueueBased.class */
public class EventDataCacheQueueBased<T, V extends EventData<T>> implements IEventDataCache<T, V> {
    private Map<T, V> map = new HashMap();
    private LinkedList<T> signatureQueue = new LinkedList<>();

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public void put(V v) {
        if (v != null) {
            EventData<T> eventData = (EventData) this.map.put(v.getSignature(), v);
            if (eventData != null) {
                v.mergeWith(eventData);
            } else {
                this.signatureQueue.addLast(v.getSignature());
            }
        }
    }

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public V retrieveNext() {
        if (!this.signatureQueue.isEmpty()) {
            return this.map.remove(this.signatureQueue.pollFirst());
        }
        return null;
    }

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public List<V> clear() {
        ArrayList<V> result = new ArrayList<>((Collection<? extends V>) this.map.values());
        this.map.clear();
        this.signatureQueue.clear();
        return result;
    }
}

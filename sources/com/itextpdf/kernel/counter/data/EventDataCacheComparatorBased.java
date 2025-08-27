package com.itextpdf.kernel.counter.data;

import com.itextpdf.kernel.counter.data.EventData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/counter/data/EventDataCacheComparatorBased.class */
public class EventDataCacheComparatorBased<T, V extends EventData<T>> implements IEventDataCache<T, V> {
    private Map<T, V> map = new HashMap();
    private Set<V> orderedCache;

    public EventDataCacheComparatorBased(Comparator<V> comparator) {
        this.orderedCache = new TreeSet(comparator);
    }

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public void put(V v) {
        if (v != null) {
            EventData<T> eventData = (EventData) this.map.put(v.getSignature(), v);
            if (eventData != null) {
                this.orderedCache.remove(eventData);
                v.mergeWith(eventData);
                this.orderedCache.add(v);
                return;
            }
            this.orderedCache.add(v);
        }
    }

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public V retrieveNext() {
        for (V data : this.orderedCache) {
            if (data != null) {
                this.map.remove(data.getSignature());
                this.orderedCache.remove(data);
                return data;
            }
        }
        return null;
    }

    @Override // com.itextpdf.kernel.counter.data.IEventDataCache
    public List<V> clear() {
        ArrayList<V> result = new ArrayList<>((Collection<? extends V>) this.map.values());
        this.map.clear();
        this.orderedCache.clear();
        return result;
    }
}

package org.terracotta.offheapstore.set;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import org.terracotta.offheapstore.OffHeapHashMap;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/set/OffHeapHashSet.class */
public class OffHeapHashSet<E> extends AbstractSet<E> {
    private final OffHeapHashMap<E, Boolean> map;

    public OffHeapHashSet(PageSource source, boolean tableSteals, StorageEngine<? super E, Boolean> engine, int capacity) {
        this(new OffHeapHashMap(source, tableSteals, engine, capacity));
    }

    public OffHeapHashSet(OffHeapHashMap<E, Boolean> map) {
        this.map = map;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.map.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        return this.map.keySet().toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.map.keySet().toArray(tArr);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e) {
        return this.map.put(e, Boolean.TRUE) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        return this.map.remove(o) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> clctn) {
        return this.map.keySet().containsAll(clctn);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.map.clear();
    }

    public StorageEngine<? super E, ?> getStorageEngine() {
        return this.map.getStorageEngine();
    }
}

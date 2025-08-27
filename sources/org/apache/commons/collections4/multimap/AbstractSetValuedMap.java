package org.apache.commons.collections4.multimap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetValuedMap;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractSetValuedMap.class */
public abstract class AbstractSetValuedMap<K, V> extends AbstractMultiValuedMap<K, V> implements SetValuedMap<K, V> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap
    public abstract Set<V> createCollection();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap
    /* bridge */ /* synthetic */ Collection wrappedCollection(Object obj) {
        return wrappedCollection((AbstractSetValuedMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap, org.apache.commons.collections4.MultiValuedMap
    public /* bridge */ /* synthetic */ Collection get(Object obj) {
        return get((AbstractSetValuedMap<K, V>) obj);
    }

    protected AbstractSetValuedMap() {
    }

    protected AbstractSetValuedMap(Map<K, ? extends Set<V>> map) {
        super(map);
    }

    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap
    protected Map<K, Set<V>> getMap() {
        return super.getMap();
    }

    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap, org.apache.commons.collections4.MultiValuedMap
    public Set<V> get(K key) {
        return wrappedCollection((AbstractSetValuedMap<K, V>) key);
    }

    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap
    Set<V> wrappedCollection(K key) {
        return new WrappedSet(key);
    }

    @Override // org.apache.commons.collections4.multimap.AbstractMultiValuedMap, org.apache.commons.collections4.MultiValuedMap
    public Set<V> remove(Object key) {
        return SetUtils.emptyIfNull(getMap().remove(key));
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multimap/AbstractSetValuedMap$WrappedSet.class */
    private class WrappedSet extends AbstractMultiValuedMap<K, V>.WrappedCollection implements Set<V> {
        public WrappedSet(K key) {
            super(key);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object other) {
            Set<V> set = (Set) getMapping();
            if (set == null) {
                return Collections.emptySet().equals(other);
            }
            if (!(other instanceof Set)) {
                return false;
            }
            Set<?> otherSet = (Set) other;
            return SetUtils.isEqualSet(set, otherSet);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            Set<V> set = (Set) getMapping();
            return SetUtils.hashCodeForSet(set);
        }
    }
}

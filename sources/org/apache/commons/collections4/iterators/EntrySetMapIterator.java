package org.apache.commons.collections4.iterators;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/EntrySetMapIterator.class */
public class EntrySetMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {
    private final Map<K, V> map;
    private Iterator<Map.Entry<K, V>> iterator;
    private Map.Entry<K, V> last;
    private boolean canRemove = false;

    public EntrySetMapIterator(Map<K, V> map) {
        this.map = map;
        this.iterator = map.entrySet().iterator();
    }

    @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
    public K next() {
        this.last = this.iterator.next();
        this.canRemove = true;
        return this.last.getKey();
    }

    @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
    public void remove() {
        if (!this.canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()");
        }
        this.iterator.remove();
        this.last = null;
        this.canRemove = false;
    }

    @Override // org.apache.commons.collections4.MapIterator
    public K getKey() {
        if (this.last == null) {
            throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
        }
        return this.last.getKey();
    }

    @Override // org.apache.commons.collections4.MapIterator
    public V getValue() {
        if (this.last == null) {
            throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
        }
        return this.last.getValue();
    }

    @Override // org.apache.commons.collections4.MapIterator
    public V setValue(V value) {
        if (this.last == null) {
            throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
        }
        return this.last.setValue(value);
    }

    @Override // org.apache.commons.collections4.ResettableIterator
    public void reset() {
        this.iterator = this.map.entrySet().iterator();
        this.last = null;
        this.canRemove = false;
    }

    public String toString() {
        if (this.last != null) {
            return "MapIterator[" + getKey() + SymbolConstants.EQUAL_SYMBOL + getValue() + "]";
        }
        return "MapIterator[]";
    }
}

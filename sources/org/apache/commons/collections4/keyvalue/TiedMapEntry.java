package org.apache.commons.collections4.keyvalue;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections4.KeyValue;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/keyvalue/TiedMapEntry.class */
public class TiedMapEntry<K, V> implements Map.Entry<K, V>, KeyValue<K, V>, Serializable {
    private static final long serialVersionUID = -8453869361373831205L;
    private final Map<K, V> map;
    private final K key;

    public TiedMapEntry(Map<K, V> map, K key) {
        this.map = map;
        this.key = key;
    }

    @Override // java.util.Map.Entry, org.apache.commons.collections4.KeyValue
    public K getKey() {
        return this.key;
    }

    @Override // java.util.Map.Entry, org.apache.commons.collections4.KeyValue
    public V getValue() {
        return this.map.get(this.key);
    }

    @Override // java.util.Map.Entry
    public V setValue(V value) {
        if (value == this) {
            throw new IllegalArgumentException("Cannot set value to this map entry");
        }
        return this.map.put(this.key, value);
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> other = (Map.Entry) obj;
        Object value = getValue();
        if (this.key != null ? this.key.equals(other.getKey()) : other.getKey() == null) {
            if (value != null ? value.equals(other.getValue()) : other.getValue() == null) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        Object value = getValue();
        return (getKey() == null ? 0 : getKey().hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    public String toString() {
        return getKey() + SymbolConstants.EQUAL_SYMBOL + getValue();
    }
}

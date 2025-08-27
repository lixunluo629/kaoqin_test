package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.KeyValue;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/keyvalue/AbstractKeyValue.class */
public abstract class AbstractKeyValue<K, V> implements KeyValue<K, V> {
    private K key;
    private V value;

    protected AbstractKeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override // org.apache.commons.collections4.KeyValue
    public K getKey() {
        return this.key;
    }

    protected K setKey(K key) {
        K old = this.key;
        this.key = key;
        return old;
    }

    @Override // org.apache.commons.collections4.KeyValue
    public V getValue() {
        return this.value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    public String toString() {
        return new StringBuilder().append(getKey()).append('=').append(getValue()).toString();
    }
}

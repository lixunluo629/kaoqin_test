package org.ehcache.impl.internal.store.heap.holders;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/BaseOnHeapKey.class */
abstract class BaseOnHeapKey<K> implements OnHeapKey<K> {
    private final int hashCode;

    BaseOnHeapKey(int hashCode) {
        this.hashCode = hashCode;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (obj instanceof OnHeapKey) {
            OnHeapKey<?> other = (OnHeapKey) obj;
            return getActualKeyObject().equals(other.getActualKeyObject());
        }
        return false;
    }
}

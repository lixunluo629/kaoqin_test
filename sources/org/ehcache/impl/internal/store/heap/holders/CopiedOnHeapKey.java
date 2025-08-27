package org.ehcache.impl.internal.store.heap.holders;

import org.ehcache.spi.copy.Copier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/CopiedOnHeapKey.class */
public class CopiedOnHeapKey<K> extends BaseOnHeapKey<K> {
    private final K copiedKey;
    private final Copier<K> keyCopier;

    @Override // org.ehcache.impl.internal.store.heap.holders.BaseOnHeapKey
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public CopiedOnHeapKey(K actualKeyObject, Copier<K> keyCopier) {
        super(actualKeyObject.hashCode());
        this.keyCopier = keyCopier;
        this.copiedKey = keyCopier.copyForWrite(actualKeyObject);
    }

    @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapKey
    public K getActualKeyObject() {
        return this.keyCopier.copyForRead(this.copiedKey);
    }

    K getCopiedKey() {
        return this.copiedKey;
    }

    @Override // org.ehcache.impl.internal.store.heap.holders.BaseOnHeapKey
    public boolean equals(Object other) {
        if (other instanceof CopiedOnHeapKey) {
            return this.copiedKey.equals(((CopiedOnHeapKey) other).copiedKey);
        }
        if (other instanceof OnHeapKey) {
            return this.copiedKey.equals(((OnHeapKey) other).getActualKeyObject());
        }
        return false;
    }
}

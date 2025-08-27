package org.ehcache.impl.internal.store.heap.holders;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/holders/LookupOnlyOnHeapKey.class */
public class LookupOnlyOnHeapKey<K> extends BaseOnHeapKey<K> {
    private final K actualKeyObject;

    @Override // org.ehcache.impl.internal.store.heap.holders.BaseOnHeapKey
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public LookupOnlyOnHeapKey(K actualKeyObject) {
        super(actualKeyObject.hashCode());
        this.actualKeyObject = actualKeyObject;
    }

    @Override // org.ehcache.impl.internal.store.heap.holders.OnHeapKey
    public K getActualKeyObject() {
        return this.actualKeyObject;
    }

    @Override // org.ehcache.impl.internal.store.heap.holders.BaseOnHeapKey
    @SuppressFBWarnings({"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"})
    public boolean equals(Object other) {
        if (other instanceof CopiedOnHeapKey) {
            return this.actualKeyObject.equals(((CopiedOnHeapKey) other).getCopiedKey());
        }
        if (other instanceof OnHeapKey) {
            return this.actualKeyObject.equals(((OnHeapKey) other).getActualKeyObject());
        }
        return false;
    }
}

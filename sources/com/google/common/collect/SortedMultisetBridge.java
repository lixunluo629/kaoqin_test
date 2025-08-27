package com.google.common.collect;

import java.util.SortedSet;

/* loaded from: guava-18.0.jar:com/google/common/collect/SortedMultisetBridge.class */
interface SortedMultisetBridge<E> extends Multiset<E> {
    @Override // com.google.common.collect.Multiset
    SortedSet<E> elementSet();
}

package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
/* loaded from: guava-18.0.jar:com/google/common/base/PairwiseEquivalence.class */
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
    final Equivalence<? super T> elementEquivalence;
    private static final long serialVersionUID = 1;

    PairwiseEquivalence(Equivalence<? super T> elementEquivalence) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(elementEquivalence);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.base.Equivalence
    public boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
        Iterator<T> iteratorA = iterableA.iterator();
        Iterator<T> iteratorB = iterableB.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
                return false;
            }
        }
        return (iteratorA.hasNext() || iteratorB.hasNext()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.base.Equivalence
    public int doHash(Iterable<T> iterable) {
        int hash = 78721;
        for (T element : iterable) {
            hash = (hash * 24943) + this.elementEquivalence.hash(element);
        }
        return hash;
    }

    public boolean equals(@Nullable Object object) {
        if (object instanceof PairwiseEquivalence) {
            PairwiseEquivalence<?> that = (PairwiseEquivalence) object;
            return this.elementEquivalence.equals(that.elementEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(this.elementEquivalence));
        return new StringBuilder(11 + strValueOf.length()).append(strValueOf).append(".pairwise()").toString();
    }
}

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Predicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/AbstractQuantifierPredicate.class */
public abstract class AbstractQuantifierPredicate<T> implements PredicateDecorator<T>, Serializable {
    private static final long serialVersionUID = -3094696765038308799L;
    protected final Predicate<? super T>[] iPredicates;

    public AbstractQuantifierPredicate(Predicate<? super T>... predicates) {
        this.iPredicates = predicates;
    }

    @Override // org.apache.commons.collections4.functors.PredicateDecorator
    public Predicate<? super T>[] getPredicates() {
        return FunctorUtils.copy(this.iPredicates);
    }
}

package org.apache.commons.collections4.functors;

import java.util.Collection;
import org.apache.commons.collections4.Predicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/AllPredicate.class */
public final class AllPredicate<T> extends AbstractQuantifierPredicate<T> {
    private static final long serialVersionUID = -3094696765038308799L;

    public static <T> Predicate<T> allPredicate(Predicate<? super T>... predicates) {
        FunctorUtils.validate(predicates);
        if (predicates.length == 0) {
            return TruePredicate.truePredicate();
        }
        if (predicates.length == 1) {
            return FunctorUtils.coerce(predicates[0]);
        }
        return new AllPredicate(FunctorUtils.copy(predicates));
    }

    public static <T> Predicate<T> allPredicate(Collection<? extends Predicate<? super T>> predicates) {
        Predicate<? super T>[] preds = FunctorUtils.validate(predicates);
        if (preds.length == 0) {
            return TruePredicate.truePredicate();
        }
        if (preds.length == 1) {
            return FunctorUtils.coerce(preds[0]);
        }
        return new AllPredicate(preds);
    }

    public AllPredicate(Predicate<? super T>... predicates) {
        super(predicates);
    }

    @Override // org.apache.commons.collections4.Predicate
    public boolean evaluate(T object) {
        Predicate<? super T>[] arr$ = this.iPredicates;
        for (Predicate<? super T> iPredicate : arr$) {
            if (!iPredicate.evaluate(object)) {
                return false;
            }
        }
        return true;
    }
}

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Predicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/TruePredicate.class */
public final class TruePredicate<T> implements Predicate<T>, Serializable {
    private static final long serialVersionUID = 3374767158756189740L;
    public static final Predicate INSTANCE = new TruePredicate();

    public static <T> Predicate<T> truePredicate() {
        return INSTANCE;
    }

    private TruePredicate() {
    }

    @Override // org.apache.commons.collections4.Predicate
    public boolean evaluate(T object) {
        return true;
    }

    private Object readResolve() {
        return INSTANCE;
    }
}

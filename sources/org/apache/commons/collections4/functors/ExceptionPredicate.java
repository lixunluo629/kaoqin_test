package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/ExceptionPredicate.class */
public final class ExceptionPredicate<T> implements Predicate<T>, Serializable {
    private static final long serialVersionUID = 7179106032121985545L;
    public static final Predicate INSTANCE = new ExceptionPredicate();

    public static <T> Predicate<T> exceptionPredicate() {
        return INSTANCE;
    }

    private ExceptionPredicate() {
    }

    @Override // org.apache.commons.collections4.Predicate
    public boolean evaluate(T object) {
        throw new FunctorException("ExceptionPredicate invoked");
    }

    private Object readResolve() {
        return INSTANCE;
    }
}

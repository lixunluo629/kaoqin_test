package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/EqualPredicate.class */
public final class EqualPredicate<T> implements Predicate<T>, Serializable {
    private static final long serialVersionUID = 5633766978029907089L;
    private final T iValue;
    private final Equator<T> equator;

    public static <T> Predicate<T> equalPredicate(T object) {
        if (object == null) {
            return NullPredicate.nullPredicate();
        }
        return new EqualPredicate(object);
    }

    public static <T> Predicate<T> equalPredicate(T object, Equator<T> equator) {
        if (object == null) {
            return NullPredicate.nullPredicate();
        }
        return new EqualPredicate(object, equator);
    }

    public EqualPredicate(T object) {
        this(object, null);
    }

    public EqualPredicate(T object, Equator<T> equator) {
        this.iValue = object;
        this.equator = equator;
    }

    @Override // org.apache.commons.collections4.Predicate
    public boolean evaluate(T object) {
        if (this.equator != null) {
            return this.equator.equate(this.iValue, object);
        }
        return this.iValue.equals(object);
    }

    public Object getValue() {
        return this.iValue;
    }
}

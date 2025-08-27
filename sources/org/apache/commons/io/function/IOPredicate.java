package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOPredicate.class */
public interface IOPredicate<T> {
    boolean test(T t) throws IOException;

    static <T> IOPredicate<T> alwaysFalse() {
        return (IOPredicate<T>) Constants.IO_PREDICATE_FALSE;
    }

    static <T> IOPredicate<T> alwaysTrue() {
        return (IOPredicate<T>) Constants.IO_PREDICATE_TRUE;
    }

    static <T> IOPredicate<T> isEqual(Object target) {
        return null == target ? Objects::isNull : object -> {
            return target.equals(object);
        };
    }

    default IOPredicate<T> and(IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return obj -> {
            return test(obj) && other.test(obj);
        };
    }

    default Predicate<T> asPredicate() {
        return t -> {
            return Uncheck.test(this, t);
        };
    }

    default IOPredicate<T> negate() {
        return obj -> {
            return !test(obj);
        };
    }

    default IOPredicate<T> or(IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return obj -> {
            return test(obj) || other.test(obj);
        };
    }
}

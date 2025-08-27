package org.apache.commons.io.function;

import java.util.Objects;
import java.util.Spliterator;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOSpliteratorAdapter.class */
final class IOSpliteratorAdapter<T> implements IOSpliterator<T> {
    private final Spliterator<T> delegate;

    static <E> IOSpliteratorAdapter<E> adapt(Spliterator<E> delegate) {
        return new IOSpliteratorAdapter<>(delegate);
    }

    IOSpliteratorAdapter(Spliterator<T> delegate) {
        this.delegate = (Spliterator) Objects.requireNonNull(delegate, "delegate");
    }

    @Override // org.apache.commons.io.function.IOSpliterator
    public Spliterator<T> unwrap() {
        return this.delegate;
    }
}

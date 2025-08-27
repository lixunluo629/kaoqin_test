package org.apache.commons.io.function;

import java.util.Objects;
import java.util.Spliterator;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOSpliterator.class */
public interface IOSpliterator<T> {
    Spliterator<T> unwrap();

    static <E> IOSpliterator<E> adapt(Spliterator<E> iterator) {
        return IOSpliteratorAdapter.adapt((Spliterator) iterator);
    }

    default Spliterator<T> asSpliterator() {
        return new UncheckedIOSpliterator(this);
    }

    default int characteristics() {
        return unwrap().characteristics();
    }

    default long estimateSize() {
        return unwrap().estimateSize();
    }

    default void forEachRemaining(IOConsumer<? super T> action) {
        while (tryAdvance(action)) {
        }
    }

    default IOComparator<? super T> getComparator() {
        return (IOComparator) unwrap().getComparator();
    }

    default long getExactSizeIfKnown() {
        return unwrap().getExactSizeIfKnown();
    }

    default boolean hasCharacteristics(int characteristics) {
        return unwrap().hasCharacteristics(characteristics);
    }

    default boolean tryAdvance(IOConsumer<? super T> action) {
        return unwrap().tryAdvance(((IOConsumer) Objects.requireNonNull(action, ParameterMethodNameResolver.DEFAULT_PARAM_NAME)).asConsumer());
    }

    default IOSpliterator<T> trySplit() {
        return adapt(unwrap().trySplit());
    }
}

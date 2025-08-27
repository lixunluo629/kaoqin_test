package org.apache.commons.io.function;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;
import org.apache.commons.io.function.IOBaseStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOBaseStream.class */
public interface IOBaseStream<T, S extends IOBaseStream<T, S, B>, B extends BaseStream<T, B>> extends Closeable {
    B unwrap();

    S wrap(B b);

    default BaseStream<T, B> asBaseStream() {
        return new UncheckedIOBaseStream(this);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    default void close() {
        unwrap().close();
    }

    default boolean isParallel() {
        return unwrap().isParallel();
    }

    default IOIterator<T> iterator() {
        return IOIteratorAdapter.adapt((Iterator) unwrap().iterator());
    }

    default S onClose(IORunnable iORunnable) throws IOException {
        return (S) wrap(unwrap().onClose(() -> {
            Erase.run(iORunnable);
        }));
    }

    default S parallel() {
        return isParallel() ? this : (S) wrap(unwrap().parallel());
    }

    default S sequential() {
        return isParallel() ? (S) wrap(unwrap().sequential()) : this;
    }

    default IOSpliterator<T> spliterator() {
        return IOSpliteratorAdapter.adapt((Spliterator) unwrap().spliterator());
    }

    default S unordered() {
        return (S) wrap(unwrap().unordered());
    }
}

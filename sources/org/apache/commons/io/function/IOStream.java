package org.apache.commons.io.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.IOExceptionList;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IOStream.class */
public interface IOStream<T> extends IOBaseStream<T, IOStream<T>, Stream<T>> {
    static <T> IOStream<T> adapt(Stream<T> stream) {
        return IOStreamAdapter.adapt(stream);
    }

    static <T> IOStream<T> empty() {
        return IOStreamAdapter.adapt(Stream.empty());
    }

    static <T> IOStream<T> iterate(final T seed, final IOUnaryOperator<T> f) {
        Objects.requireNonNull(f);
        Iterator<T> iterator = new Iterator<T>() { // from class: org.apache.commons.io.function.IOStream.1
            T t = (T) IOStreams.NONE;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return true;
            }

            @Override // java.util.Iterator
            public T next() throws NoSuchElementException {
                try {
                    T tApply = this.t == IOStreams.NONE ? (T) seed : f.apply(this.t);
                    this.t = tApply;
                    return tApply;
                } catch (IOException e) {
                    NoSuchElementException noSuchElementException = new NoSuchElementException();
                    noSuchElementException.initCause(e);
                    throw noSuchElementException;
                }
            }
        };
        return adapt(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 1040), false));
    }

    static <T> IOStream<T> of(Iterable<T> values) {
        return values == null ? empty() : adapt(StreamSupport.stream(values.spliterator(), false));
    }

    @SafeVarargs
    static <T> IOStream<T> of(T... values) {
        return (values == null || values.length == 0) ? empty() : adapt(Arrays.stream(values));
    }

    static <T> IOStream<T> of(T t) {
        return adapt(Stream.of(t));
    }

    default boolean allMatch(IOPredicate<? super T> predicate) throws IOException {
        return unwrap().allMatch(t -> {
            return Erase.test(predicate, t);
        });
    }

    default boolean anyMatch(IOPredicate<? super T> predicate) throws IOException {
        return unwrap().anyMatch(t -> {
            return Erase.test(predicate, t);
        });
    }

    default <R, A> R collect(Collector<? super T, A, R> collector) {
        return (R) unwrap().collect(collector);
    }

    default <R> R collect(IOSupplier<R> iOSupplier, IOBiConsumer<R, ? super T> iOBiConsumer, IOBiConsumer<R, R> iOBiConsumer2) throws IOException {
        return (R) unwrap().collect(() -> {
            return Erase.get(iOSupplier);
        }, (t, u) -> {
            Erase.accept(iOBiConsumer, t, u);
        }, (t2, u2) -> {
            Erase.accept(iOBiConsumer2, t2, u2);
        });
    }

    default long count() {
        return unwrap().count();
    }

    default IOStream<T> distinct() {
        return adapt(unwrap().distinct());
    }

    default IOStream<T> filter(IOPredicate<? super T> predicate) throws IOException {
        return adapt(unwrap().filter(t -> {
            return Erase.test(predicate, t);
        }));
    }

    default Optional<T> findAny() {
        return unwrap().findAny();
    }

    default Optional<T> findFirst() {
        return unwrap().findFirst();
    }

    default <R> IOStream<R> flatMap(IOFunction<? super T, ? extends IOStream<? extends R>> mapper) throws IOException {
        return adapt(unwrap().flatMap(t -> {
            return ((IOStream) Erase.apply(mapper, t)).unwrap();
        }));
    }

    default DoubleStream flatMapToDouble(IOFunction<? super T, ? extends DoubleStream> mapper) throws IOException {
        return unwrap().flatMapToDouble(t -> {
            return (DoubleStream) Erase.apply(mapper, t);
        });
    }

    default IntStream flatMapToInt(IOFunction<? super T, ? extends IntStream> mapper) throws IOException {
        return unwrap().flatMapToInt(t -> {
            return (IntStream) Erase.apply(mapper, t);
        });
    }

    default LongStream flatMapToLong(IOFunction<? super T, ? extends LongStream> mapper) throws IOException {
        return unwrap().flatMapToLong(t -> {
            return (LongStream) Erase.apply(mapper, t);
        });
    }

    default void forAll(IOConsumer<T> action) throws IOExceptionList {
        forAll(action, (i, e) -> {
            return e;
        });
    }

    default void forAll(IOConsumer<T> action, BiFunction<Integer, IOException, IOException> exSupplier) throws IOExceptionList {
        AtomicReference<List<IOException>> causeList = new AtomicReference<>();
        AtomicInteger index = new AtomicInteger();
        IOConsumer<T> safeAction = IOStreams.toIOConsumer(action);
        unwrap().forEach(e -> {
            try {
                safeAction.accept(e);
            } catch (IOException innerEx) {
                if (causeList.get() == null) {
                    causeList.set(new ArrayList());
                }
                if (exSupplier != null) {
                    ((List) causeList.get()).add((IOException) exSupplier.apply(Integer.valueOf(index.get()), innerEx));
                }
            }
            index.incrementAndGet();
        });
        IOExceptionList.checkEmpty(causeList.get(), null);
    }

    default void forEach(IOConsumer<? super T> action) throws IOException {
        unwrap().forEach(e -> {
            Erase.accept(action, e);
        });
    }

    default void forEachOrdered(IOConsumer<? super T> action) throws IOException {
        unwrap().forEachOrdered(e -> {
            Erase.accept(action, e);
        });
    }

    default IOStream<T> limit(long maxSize) {
        return adapt(unwrap().limit(maxSize));
    }

    default <R> IOStream<R> map(IOFunction<? super T, ? extends R> mapper) throws IOException {
        return adapt(unwrap().map(t -> {
            return Erase.apply(mapper, t);
        }));
    }

    default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return unwrap().mapToDouble(mapper);
    }

    default IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return unwrap().mapToInt(mapper);
    }

    default LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return unwrap().mapToLong(mapper);
    }

    default Optional<T> max(IOComparator<? super T> comparator) throws IOException {
        return unwrap().max((t, u) -> {
            return Erase.compare(comparator, t, u);
        });
    }

    default Optional<T> min(IOComparator<? super T> comparator) throws IOException {
        return unwrap().min((t, u) -> {
            return Erase.compare(comparator, t, u);
        });
    }

    default boolean noneMatch(IOPredicate<? super T> predicate) throws IOException {
        return unwrap().noneMatch(t -> {
            return Erase.test(predicate, t);
        });
    }

    default IOStream<T> peek(IOConsumer<? super T> action) throws IOException {
        return adapt(unwrap().peek(t -> {
            Erase.accept(action, t);
        }));
    }

    default Optional<T> reduce(IOBinaryOperator<T> accumulator) throws IOException {
        return unwrap().reduce((t, u) -> {
            return Erase.apply(accumulator, t, u);
        });
    }

    default T reduce(T identity, IOBinaryOperator<T> accumulator) throws IOException {
        return unwrap().reduce(identity, (t, u) -> {
            return Erase.apply(accumulator, t, u);
        });
    }

    default <U> U reduce(U u, IOBiFunction<U, ? super T, U> iOBiFunction, IOBinaryOperator<U> iOBinaryOperator) throws IOException {
        return (U) unwrap().reduce(u, (t, u2) -> {
            return Erase.apply(iOBiFunction, t, u2);
        }, (t2, u3) -> {
            return Erase.apply(iOBinaryOperator, t2, u3);
        });
    }

    default IOStream<T> skip(long n) {
        return adapt(unwrap().skip(n));
    }

    default IOStream<T> sorted() {
        return adapt(unwrap().sorted());
    }

    default IOStream<T> sorted(IOComparator<? super T> comparator) throws IOException {
        return adapt(unwrap().sorted((t, u) -> {
            return Erase.compare(comparator, t, u);
        }));
    }

    default Object[] toArray() {
        return unwrap().toArray();
    }

    default <A> A[] toArray(IntFunction<A[]> intFunction) {
        return (A[]) unwrap().toArray(intFunction);
    }
}

package org.apache.commons.io.function;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/UncheckedIOSpliterator.class */
final class UncheckedIOSpliterator<T> implements Spliterator<T> {
    private final IOSpliterator<T> delegate;

    UncheckedIOSpliterator(IOSpliterator<T> delegate) {
        this.delegate = (IOSpliterator) Objects.requireNonNull(delegate, "delegate");
    }

    @Override // java.util.Spliterator
    public int characteristics() {
        return this.delegate.characteristics();
    }

    @Override // java.util.Spliterator
    public long estimateSize() {
        return this.delegate.estimateSize();
    }

    @Override // java.util.Spliterator
    public void forEachRemaining(Consumer<? super T> action) {
        IOSpliterator<T> iOSpliterator = this.delegate;
        Objects.requireNonNull(iOSpliterator);
        IOConsumer iOConsumer = iOSpliterator::forEachRemaining;
        Objects.requireNonNull(action);
        Uncheck.accept((IOConsumer<IOConsumer>) iOConsumer, action::accept);
    }

    @Override // java.util.Spliterator
    public Comparator<? super T> getComparator() {
        return this.delegate.getComparator().asComparator();
    }

    @Override // java.util.Spliterator
    public long getExactSizeIfKnown() {
        return this.delegate.getExactSizeIfKnown();
    }

    @Override // java.util.Spliterator
    public boolean hasCharacteristics(int characteristics) {
        return this.delegate.hasCharacteristics(characteristics);
    }

    @Override // java.util.Spliterator
    public boolean tryAdvance(Consumer<? super T> action) {
        IOSpliterator<T> iOSpliterator = this.delegate;
        Objects.requireNonNull(iOSpliterator);
        IOFunction iOFunction = iOSpliterator::tryAdvance;
        Objects.requireNonNull(action);
        return ((Boolean) Uncheck.apply(iOFunction, action::accept)).booleanValue();
    }

    @Override // java.util.Spliterator
    public Spliterator<T> trySplit() {
        IOSpliterator<T> iOSpliterator = this.delegate;
        Objects.requireNonNull(iOSpliterator);
        return ((IOSpliterator) Uncheck.get(iOSpliterator::trySplit)).unwrap();
    }
}

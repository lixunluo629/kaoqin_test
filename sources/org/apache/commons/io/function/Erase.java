package org.apache.commons.io.function;

import java.io.IOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/Erase.class */
public final class Erase {
    static <T, U> void accept(IOBiConsumer<T, U> consumer, T t, U u) throws Throwable {
        try {
            consumer.accept(t, u);
        } catch (IOException ex) {
            rethrow(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> void accept(IOConsumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (IOException ex) {
            rethrow(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T, U, R> R apply(IOBiFunction<? super T, ? super U, ? extends R> mapper, T t, U u) {
        try {
            return mapper.apply(t, u);
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T, R> R apply(IOFunction<? super T, ? extends R> mapper, T t) {
        try {
            return mapper.apply(t);
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> int compare(IOComparator<? super T> comparator, T t, T t2) {
        try {
            return comparator.compare(t, t2);
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    static <T> T get(IOSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    public static <T extends Throwable> RuntimeException rethrow(Throwable throwable) throws Throwable {
        throw throwable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void run(IORunnable runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> boolean test(IOPredicate<? super T> predicate, T t) {
        try {
            return predicate.test(t);
        } catch (IOException e) {
            throw rethrow(e);
        }
    }

    private Erase() {
    }
}

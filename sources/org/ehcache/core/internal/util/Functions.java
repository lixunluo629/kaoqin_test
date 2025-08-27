package org.ehcache.core.internal.util;

import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/Functions.class */
public class Functions {
    public static <A, T> Function<A, T> memoize(Function<A, T> f) {
        return new MemoizingFunction(f);
    }

    public static <A, B, T> BiFunction<A, B, T> memoize(BiFunction<A, B, T> f) {
        return new MemoizingBiFunction(f);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/Functions$MemoizingFunction.class */
    private static final class MemoizingFunction<A, T> implements Function<A, T> {
        private final Function<A, T> function;
        private boolean computed;
        private T value;

        private MemoizingFunction(Function<A, T> function) {
            this.function = function;
        }

        @Override // org.ehcache.core.spi.function.Function
        public T apply(A a) {
            if (this.computed) {
                return this.value;
            }
            this.value = this.function.apply(a);
            this.computed = true;
            return this.value;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/Functions$MemoizingBiFunction.class */
    private static final class MemoizingBiFunction<A, B, T> implements BiFunction<A, B, T> {
        private final BiFunction<A, B, T> function;
        private boolean computed;
        private T value;

        private MemoizingBiFunction(BiFunction<A, B, T> function) {
            this.function = function;
        }

        @Override // org.ehcache.core.spi.function.BiFunction
        public T apply(A a, B b) {
            if (this.computed) {
                return this.value;
            }
            this.computed = true;
            this.value = this.function.apply(a, b);
            return this.value;
        }
    }
}

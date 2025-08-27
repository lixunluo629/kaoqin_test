package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/base/Converter.class */
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;
    private transient Converter<B, A> reverse;

    protected abstract B doForward(A a);

    protected abstract A doBackward(B b);

    protected Converter() {
        this(true);
    }

    Converter(boolean handleNullAutomatically) {
        this.handleNullAutomatically = handleNullAutomatically;
    }

    @Nullable
    public final B convert(@Nullable A a) {
        return correctedDoForward(a);
    }

    @Nullable
    B correctedDoForward(@Nullable A a) {
        if (this.handleNullAutomatically) {
            if (a == null) {
                return null;
            }
            return (B) Preconditions.checkNotNull(doForward(a));
        }
        return doForward(a);
    }

    @Nullable
    A correctedDoBackward(@Nullable B b) {
        if (this.handleNullAutomatically) {
            if (b == null) {
                return null;
            }
            return (A) Preconditions.checkNotNull(doBackward(b));
        }
        return doBackward(b);
    }

    public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
        Preconditions.checkNotNull(fromIterable, "fromIterable");
        return new Iterable<B>() { // from class: com.google.common.base.Converter.1
            @Override // java.lang.Iterable
            public Iterator<B> iterator() {
                return new Iterator<B>() { // from class: com.google.common.base.Converter.1.1
                    private final Iterator<? extends A> fromIterator;

                    {
                        this.fromIterator = fromIterable.iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.fromIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public B next() {
                        return (B) Converter.this.convert(this.fromIterator.next());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        this.fromIterator.remove();
                    }
                };
            }
        };
    }

    public Converter<B, A> reverse() {
        Converter<B, A> result = this.reverse;
        if (result != null) {
            return result;
        }
        ReverseConverter reverseConverter = new ReverseConverter(this);
        this.reverse = reverseConverter;
        return reverseConverter;
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/Converter$ReverseConverter.class */
    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        final Converter<A, B> original;
        private static final long serialVersionUID = 0;

        ReverseConverter(Converter<A, B> original) {
            this.original = original;
        }

        @Override // com.google.common.base.Converter
        protected A doForward(B b) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected B doBackward(A a) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        @Nullable
        A correctedDoForward(@Nullable B b) {
            return this.original.correctedDoBackward(b);
        }

        @Override // com.google.common.base.Converter
        @Nullable
        B correctedDoBackward(@Nullable A a) {
            return this.original.correctedDoForward(a);
        }

        @Override // com.google.common.base.Converter
        public Converter<A, B> reverse() {
            return this.original;
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@Nullable Object object) {
            if (object instanceof ReverseConverter) {
                ReverseConverter<?, ?> that = (ReverseConverter) object;
                return this.original.equals(that.original);
            }
            return false;
        }

        public int hashCode() {
            return this.original.hashCode() ^ (-1);
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.original));
            return new StringBuilder(10 + strValueOf.length()).append(strValueOf).append(".reverse()").toString();
        }
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
        return doAndThen(secondConverter);
    }

    <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(secondConverter));
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/Converter$ConverterComposition.class */
    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
        final Converter<A, B> first;
        final Converter<B, C> second;
        private static final long serialVersionUID = 0;

        ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
            this.first = first;
            this.second = second;
        }

        @Override // com.google.common.base.Converter
        protected C doForward(A a) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(C c) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        @Nullable
        C correctedDoForward(@Nullable A a) {
            return (C) this.second.correctedDoForward(this.first.correctedDoForward(a));
        }

        @Override // com.google.common.base.Converter
        @Nullable
        A correctedDoBackward(@Nullable C c) {
            return (A) this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@Nullable Object object) {
            if (object instanceof ConverterComposition) {
                ConverterComposition<?, ?, ?> that = (ConverterComposition) object;
                return this.first.equals(that.first) && this.second.equals(that.second);
            }
            return false;
        }

        public int hashCode() {
            return (31 * this.first.hashCode()) + this.second.hashCode();
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.first));
            String strValueOf2 = String.valueOf(String.valueOf(this.second));
            return new StringBuilder(10 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(".andThen(").append(strValueOf2).append(")").toString();
        }
    }

    @Override // com.google.common.base.Function
    @Nullable
    @Deprecated
    public final B apply(@Nullable A a) {
        return convert(a);
    }

    @Override // com.google.common.base.Function
    public boolean equals(@Nullable Object object) {
        return super.equals(object);
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
        return new FunctionBasedConverter(forwardFunction, backwardFunction);
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/Converter$FunctionBasedConverter.class */
    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super A, ? extends B> forwardFunction;
        private final Function<? super B, ? extends A> backwardFunction;

        private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(forwardFunction);
            this.backwardFunction = (Function) Preconditions.checkNotNull(backwardFunction);
        }

        @Override // com.google.common.base.Converter
        protected B doForward(A a) {
            return this.forwardFunction.apply(a);
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(B b) {
            return this.backwardFunction.apply(b);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@Nullable Object object) {
            if (object instanceof FunctionBasedConverter) {
                FunctionBasedConverter<?, ?> that = (FunctionBasedConverter) object;
                return this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction);
            }
            return false;
        }

        public int hashCode() {
            return (this.forwardFunction.hashCode() * 31) + this.backwardFunction.hashCode();
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.forwardFunction));
            String strValueOf2 = String.valueOf(String.valueOf(this.backwardFunction));
            return new StringBuilder(18 + strValueOf.length() + strValueOf2.length()).append("Converter.from(").append(strValueOf).append(", ").append(strValueOf2).append(")").toString();
        }
    }

    public static <T> Converter<T, T> identity() {
        return IdentityConverter.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/Converter$IdentityConverter.class */
    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final IdentityConverter INSTANCE = new IdentityConverter();
        private static final long serialVersionUID = 0;

        private IdentityConverter() {
        }

        @Override // com.google.common.base.Converter
        protected T doForward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        protected T doBackward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        public IdentityConverter<T> reverse() {
            return this;
        }

        @Override // com.google.common.base.Converter
        <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
            return (Converter) Preconditions.checkNotNull(otherConverter, "otherConverter");
        }

        public String toString() {
            return "Converter.identity()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

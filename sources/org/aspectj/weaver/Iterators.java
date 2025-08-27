package org.aspectj.weaver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators.class */
public final class Iterators {

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators$Filter.class */
    public interface Filter<T> {
        Iterator<T> filter(Iterator<T> it);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators$Getter.class */
    public interface Getter<A, B> {
        Iterator<B> get(A a);
    }

    private Iterators() {
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: org.aspectj.weaver.Iterators$1, reason: invalid class name */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators$1.class */
    static class AnonymousClass1<T> implements Filter<T> {
        final Set<T> seen = new HashSet();

        AnonymousClass1() {
        }

        @Override // org.aspectj.weaver.Iterators.Filter
        public Iterator<T> filter(final Iterator<T> in) {
            return new Iterator<T>() { // from class: org.aspectj.weaver.Iterators.1.1
                boolean fresh = false;
                T peek;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (this.fresh) {
                        return true;
                    }
                    while (in.hasNext()) {
                        this.peek = (T) in.next();
                        if (!AnonymousClass1.this.seen.contains(this.peek)) {
                            this.fresh = true;
                            return true;
                        }
                        this.peek = null;
                    }
                    return false;
                }

                @Override // java.util.Iterator
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T ret = this.peek;
                    AnonymousClass1.this.seen.add(this.peek);
                    this.peek = null;
                    this.fresh = false;
                    return ret;
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public static <T> Filter<T> dupFilter() {
        return new AnonymousClass1();
    }

    public static <T> Iterator<T> array(final T[] o) {
        return new Iterator<T>() { // from class: org.aspectj.weaver.Iterators.2
            int i = 0;
            int len;

            {
                this.len = o == null ? 0 : o.length;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i < this.len;
            }

            @Override // java.util.Iterator
            public T next() {
                if (this.i < this.len) {
                    Object[] objArr = o;
                    int i = this.i;
                    this.i = i + 1;
                    return (T) objArr[i];
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators$ResolvedTypeArrayIterator.class */
    public static class ResolvedTypeArrayIterator implements Iterator<ResolvedType> {
        private ResolvedType[] array;
        private int index;
        private int len;
        private boolean wantGenerics;
        private List<String> alreadySeen;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Iterators.class.desiredAssertionStatus();
        }

        public ResolvedTypeArrayIterator(ResolvedType[] array, List<String> alreadySeen, boolean wantGenerics) {
            if (!$assertionsDisabled && array == null) {
                throw new AssertionError();
            }
            this.array = array;
            this.wantGenerics = wantGenerics;
            this.len = array.length;
            this.index = 0;
            this.alreadySeen = alreadySeen;
            moveToNextNewOne();
        }

        private void moveToNextNewOne() {
            while (this.index < this.len) {
                ResolvedType interfaceType = this.array[this.index];
                if (!this.wantGenerics && interfaceType.isParameterizedOrGenericType()) {
                    interfaceType = interfaceType.getRawType();
                }
                String signature = interfaceType.getSignature();
                if (this.alreadySeen.contains(signature)) {
                    this.index++;
                } else {
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.len;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ResolvedType next() {
            if (this.index < this.len) {
                ResolvedType[] resolvedTypeArr = this.array;
                int i = this.index;
                this.index = i + 1;
                ResolvedType oo = resolvedTypeArr[i];
                if (!this.wantGenerics && (oo.isParameterizedType() || oo.isGenericType())) {
                    oo = oo.getRawType();
                }
                this.alreadySeen.add(oo.getSignature());
                moveToNextNewOne();
                return oo;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static Iterator<ResolvedType> array(final ResolvedType[] o, final boolean genericsAware) {
        return new Iterator<ResolvedType>() { // from class: org.aspectj.weaver.Iterators.3
            int i = 0;
            int len;

            {
                this.len = o == null ? 0 : o.length;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.i < this.len;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public ResolvedType next() {
                if (this.i < this.len) {
                    ResolvedType[] resolvedTypeArr = o;
                    int i = this.i;
                    this.i = i + 1;
                    ResolvedType oo = resolvedTypeArr[i];
                    if (!genericsAware && (oo.isParameterizedType() || oo.isGenericType())) {
                        return oo.getRawType();
                    }
                    return oo;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [B] */
    /* renamed from: org.aspectj.weaver.Iterators$4, reason: invalid class name */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Iterators$4.class */
    static class AnonymousClass4<B> implements Iterator<B> {
        Iterator<B> delegate = new Iterator<B>() { // from class: org.aspectj.weaver.Iterators.4.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                if (!AnonymousClass4.this.val$a.hasNext()) {
                    return false;
                }
                Object next = AnonymousClass4.this.val$a.next();
                AnonymousClass4.this.delegate = Iterators.append1(AnonymousClass4.this.val$g.get(next), this);
                return AnonymousClass4.this.delegate.hasNext();
            }

            @Override // java.util.Iterator
            public B next() {
                if (!hasNext()) {
                    throw new UnsupportedOperationException();
                }
                return AnonymousClass4.this.delegate.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        final /* synthetic */ Iterator val$a;
        final /* synthetic */ Getter val$g;

        AnonymousClass4(Iterator it, Getter getter) {
            this.val$a = it;
            this.val$g = getter;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        @Override // java.util.Iterator
        public B next() {
            return this.delegate.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static <A, B> Iterator<B> mapOver(Iterator<A> a, Getter<A, B> g) {
        return new AnonymousClass4(a, g);
    }

    public static <A> Iterator<A> recur(final A a, final Getter<A, A> g) {
        return new Iterator<A>() { // from class: org.aspectj.weaver.Iterators.5
            Iterator<A> delegate;

            {
                this.delegate = Iterators.one(a);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            @Override // java.util.Iterator
            public A next() {
                A next = this.delegate.next();
                this.delegate = Iterators.append(g.get(next), this.delegate);
                return next;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> Iterator<T> append(Iterator<T> a, Iterator<T> b) {
        if (!b.hasNext()) {
            return a;
        }
        return append1(a, b);
    }

    public static <T> Iterator<T> append1(final Iterator<T> a, final Iterator<T> b) {
        if (!a.hasNext()) {
            return b;
        }
        return new Iterator<T>() { // from class: org.aspectj.weaver.Iterators.6
            @Override // java.util.Iterator
            public boolean hasNext() {
                return a.hasNext() || b.hasNext();
            }

            @Override // java.util.Iterator
            public T next() {
                if (a.hasNext()) {
                    return (T) a.next();
                }
                if (b.hasNext()) {
                    return (T) b.next();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> Iterator<T> snoc(final Iterator<T> first, final T last) {
        return new Iterator<T>() { // from class: org.aspectj.weaver.Iterators.7
            T last1;

            {
                this.last1 = (T) last;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return first.hasNext() || this.last1 != null;
            }

            @Override // java.util.Iterator
            public T next() {
                if (first.hasNext()) {
                    return (T) first.next();
                }
                if (this.last1 == null) {
                    throw new NoSuchElementException();
                }
                T t = this.last1;
                this.last1 = null;
                return t;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> Iterator<T> one(final T it) {
        return new Iterator<T>() { // from class: org.aspectj.weaver.Iterators.8
            boolean avail = true;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.avail;
            }

            @Override // java.util.Iterator
            public T next() {
                if (!this.avail) {
                    throw new NoSuchElementException();
                }
                this.avail = false;
                return (T) it;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

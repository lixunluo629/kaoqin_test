package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Ordering.class */
public abstract class Ordering<T> implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    @Override // java.util.Comparator
    public abstract int compare(@Nullable T t, @Nullable T t2);

    @GwtCompatible(serializable = true)
    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering) comparator : new ComparatorOrdering(comparator);
    }

    @GwtCompatible(serializable = true)
    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return (Ordering) Preconditions.checkNotNull(ordering);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
        return new ExplicitOrdering(valuesInOrder);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
        return explicit(Lists.asList(leastValue, remainingValuesInOrder));
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Ordering$ArbitraryOrderingHolder.class */
    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();

        private ArbitraryOrderingHolder() {
        }
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/collect/Ordering$ArbitraryOrdering.class */
    static class ArbitraryOrdering extends Ordering<Object> {
        private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function<Object, Integer>() { // from class: com.google.common.collect.Ordering.ArbitraryOrdering.1
            final AtomicInteger counter = new AtomicInteger(0);

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Function
            public Integer apply(Object from) {
                return Integer.valueOf(this.counter.getAndIncrement());
            }
        });

        ArbitraryOrdering() {
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(Object left, Object right) {
            if (left == right) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            int leftCode = identityHashCode(left);
            int rightCode = identityHashCode(right);
            if (leftCode != rightCode) {
                return leftCode < rightCode ? -1 : 1;
            }
            int result = this.uids.get(left).compareTo(this.uids.get(right));
            if (result == 0) {
                throw new AssertionError();
            }
            return result;
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }

        int identityHashCode(Object object) {
            return System.identityHashCode(object);
        }
    }

    protected Ordering() {
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering(function, this);
    }

    <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
        return (Ordering<Map.Entry<T2, ?>>) onResultOf(Maps.keyFunction());
    }

    @GwtCompatible(serializable = true)
    public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
        return new CompoundOrdering(this, (Comparator) Preconditions.checkNotNull(secondaryComparator));
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
        return new CompoundOrdering(comparators);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    public <E extends T> E min(Iterator<E> it) {
        E next = it.next();
        while (true) {
            E e = next;
            if (it.hasNext()) {
                next = (E) min(e, it.next());
            } else {
                return e;
            }
        }
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return (E) min(iterable.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends T> E min(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) <= 0 ? e : e2;
    }

    public <E extends T> E min(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        Object objMin = min(min(e, e2), e3);
        for (E e4 : eArr) {
            objMin = min(objMin, e4);
        }
        return (E) objMin;
    }

    public <E extends T> E max(Iterator<E> it) {
        E next = it.next();
        while (true) {
            E e = next;
            if (it.hasNext()) {
                next = (E) max(e, it.next());
            } else {
                return e;
            }
        }
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return (E) max(iterable.iterator());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends T> E max(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) >= 0 ? e : e2;
    }

    public <E extends T> E max(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        Object objMax = max(max(e, e2), e3);
        for (E e4 : eArr) {
            objMax = max(objMax, e4);
        }
        return (E) objMax;
    }

    public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
        if (iterable instanceof Collection) {
            Collection<E> collection = (Collection) iterable;
            if (collection.size() <= 2 * k) {
                Object[] array = collection.toArray();
                Arrays.sort(array, this);
                if (array.length > k) {
                    array = ObjectArrays.arraysCopyOf(array, k);
                }
                return Collections.unmodifiableList(Arrays.asList(array));
            }
        }
        return leastOf(iterable.iterator(), k);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r0v41 */
    /* JADX WARN: Type inference failed for: r0v46, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v66, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.lang.Object] */
    public <E extends T> List<E> leastOf(Iterator<E> elements, int k) {
        Preconditions.checkNotNull(elements);
        CollectPreconditions.checkNonnegative(k, "k");
        if (k == 0 || !elements.hasNext()) {
            return ImmutableList.of();
        }
        if (k >= 1073741823) {
            ArrayList<E> list = Lists.newArrayList(elements);
            Collections.sort(list, this);
            if (list.size() > k) {
                list.subList(k, list.size()).clear();
            }
            list.trimToSize();
            return Collections.unmodifiableList(list);
        }
        int bufferCap = k * 2;
        Object[] objArr = new Object[bufferCap];
        E threshold = elements.next();
        objArr[0] = threshold;
        int bufferSize = 1;
        while (bufferSize < k && elements.hasNext()) {
            E e = elements.next();
            int i = bufferSize;
            bufferSize++;
            objArr[i] = e;
            threshold = max(threshold, e);
        }
        while (elements.hasNext()) {
            E e2 = elements.next();
            if (compare(e2, threshold) < 0) {
                int i2 = bufferSize;
                bufferSize++;
                objArr[i2] = e2;
                if (bufferSize == bufferCap) {
                    int left = 0;
                    int right = bufferCap - 1;
                    int minThresholdPosition = 0;
                    while (left < right) {
                        int pivotIndex = ((left + right) + 1) >>> 1;
                        int pivotNewIndex = partition(objArr, left, right, pivotIndex);
                        if (pivotNewIndex > k) {
                            right = pivotNewIndex - 1;
                        } else {
                            if (pivotNewIndex >= k) {
                                break;
                            }
                            left = Math.max(pivotNewIndex, left + 1);
                            minThresholdPosition = pivotNewIndex;
                        }
                    }
                    bufferSize = k;
                    threshold = objArr[minThresholdPosition];
                    for (int i3 = minThresholdPosition + 1; i3 < bufferSize; i3++) {
                        threshold = max(threshold, objArr[i3]);
                    }
                }
            }
        }
        Arrays.sort(objArr, 0, bufferSize, this);
        return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(objArr, Math.min(bufferSize, k))));
    }

    private <E extends T> int partition(E[] values, int left, int right, int pivotIndex) {
        E pivotValue = values[pivotIndex];
        values[pivotIndex] = values[right];
        values[right] = pivotValue;
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (compare(values[i], pivotValue) < 0) {
                ObjectArrays.swap(values, storeIndex, i);
                storeIndex++;
            }
        }
        ObjectArrays.swap(values, right, storeIndex);
        return storeIndex;
    }

    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) {
        return reverse().leastOf(iterable, k);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) {
        return reverse().leastOf(iterator, k);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
        Object[] array = Iterables.toArray(elements);
        Arrays.sort(array, this);
        return Lists.newArrayList(Arrays.asList(array));
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
        Object[] array = Iterables.toArray(elements);
        for (Object obj : array) {
            Preconditions.checkNotNull(obj);
        }
        Arrays.sort(array, this);
        return ImmutableList.asImmutableList(array);
    }

    public boolean isOrdered(Iterable<? extends T> iterable) {
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (true) {
                T prev = next;
                if (it.hasNext()) {
                    T next2 = it.next();
                    if (compare(prev, next2) > 0) {
                        return false;
                    }
                    next = next2;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (true) {
                T prev = next;
                if (it.hasNext()) {
                    T next2 = it.next();
                    if (compare(prev, next2) >= 0) {
                        return false;
                    }
                    next = next2;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public int binarySearch(List<? extends T> sortedList, @Nullable T key) {
        return Collections.binarySearch(sortedList, key, this);
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/collect/Ordering$IncomparableValueException.class */
    static class IncomparableValueException extends ClassCastException {
        final Object value;
        private static final long serialVersionUID = 0;

        /* JADX WARN: Illegal instructions before constructor call */
        IncomparableValueException(Object value) {
            String strValueOf = String.valueOf(String.valueOf(value));
            super(new StringBuilder(22 + strValueOf.length()).append("Cannot compare value: ").append(strValueOf).toString());
            this.value = value;
        }
    }
}

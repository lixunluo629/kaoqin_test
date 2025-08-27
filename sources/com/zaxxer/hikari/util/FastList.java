package com.zaxxer.hikari.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/FastList.class */
public final class FastList<T> implements List<T>, RandomAccess, Serializable {
    private static final long serialVersionUID = -4598088075242913858L;
    private final Class<?> clazz;
    private T[] elementData;
    private int size;

    public FastList(Class<?> cls) {
        this.elementData = (T[]) ((Object[]) Array.newInstance(cls, 32));
        this.clazz = cls;
    }

    public FastList(Class<?> cls, int i) {
        this.elementData = (T[]) ((Object[]) Array.newInstance(cls, i));
        this.clazz = cls;
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(T t) {
        if (this.size < this.elementData.length) {
            T[] tArr = this.elementData;
            int i = this.size;
            this.size = i + 1;
            tArr[i] = t;
            return true;
        }
        int length = this.elementData.length;
        T[] tArr2 = (T[]) ((Object[]) Array.newInstance(this.clazz, length << 1));
        System.arraycopy(this.elementData, 0, tArr2, 0, length);
        int i2 = this.size;
        this.size = i2 + 1;
        tArr2[i2] = t;
        this.elementData = tArr2;
        return true;
    }

    @Override // java.util.List
    public T get(int index) {
        return this.elementData[index];
    }

    public T removeLast() {
        T[] tArr = this.elementData;
        int i = this.size - 1;
        this.size = i;
        T element = tArr[i];
        this.elementData[this.size] = null;
        return element;
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object element) {
        for (int index = this.size - 1; index >= 0; index--) {
            if (element == this.elementData[index]) {
                int numMoved = (this.size - index) - 1;
                if (numMoved > 0) {
                    System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
                }
                T[] tArr = this.elementData;
                int i = this.size - 1;
                this.size = i;
                tArr[i] = null;
                return true;
            }
        }
        return false;
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.List
    public T set(int index, T element) {
        T old = this.elementData[index];
        this.elementData[index] = element;
        return old;
    }

    @Override // java.util.List
    public T remove(int index) {
        if (this.size == 0) {
            return null;
        }
        T old = this.elementData[index];
        int numMoved = (this.size - index) - 1;
        if (numMoved > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
        }
        T[] tArr = this.elementData;
        int i = this.size - 1;
        this.size = i;
        tArr[i] = null;
        return old;
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: com.zaxxer.hikari.util.FastList.1
            private int index;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < FastList.this.size;
            }

            @Override // java.util.Iterator
            public T next() {
                if (this.index < FastList.this.size) {
                    Object[] objArr = FastList.this.elementData;
                    int i = this.index;
                    this.index = i + 1;
                    return (T) objArr[i];
                }
                throw new NoSuchElementException("No more elements in FastList");
            }
        };
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public <E> E[] toArray(E[] a) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public Object clone() {
        throw new UnsupportedOperationException();
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean removeIf(Predicate<? super T> filter) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void sort(Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }
}

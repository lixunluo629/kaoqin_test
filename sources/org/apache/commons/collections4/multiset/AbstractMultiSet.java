package org.apache.commons.collections4.multiset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Transformer;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multiset/AbstractMultiSet.class */
public abstract class AbstractMultiSet<E> extends AbstractCollection<E> implements MultiSet<E> {
    private transient Set<E> uniqueSet;
    private transient Set<MultiSet.Entry<E>> entrySet;

    protected abstract int uniqueElements();

    protected abstract Iterator<MultiSet.Entry<E>> createEntrySetIterator();

    protected AbstractMultiSet() {
    }

    @Override // java.util.AbstractCollection, java.util.Collection, org.apache.commons.collections4.MultiSet
    public int size() {
        int totalSize = 0;
        for (MultiSet.Entry<E> entry : entrySet()) {
            totalSize += entry.getCount();
        }
        return totalSize;
    }

    public int getCount(Object object) {
        for (MultiSet.Entry<E> entry : entrySet()) {
            E element = entry.getElement();
            if (element == object || (element != null && element.equals(object))) {
                return entry.getCount();
            }
        }
        return 0;
    }

    @Override // org.apache.commons.collections4.MultiSet
    public int setCount(E object, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count must not be negative.");
        }
        int oldCount = getCount(object);
        if (oldCount < count) {
            add(object, count - oldCount);
        } else {
            remove(object, oldCount - count);
        }
        return oldCount;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object object) {
        return getCount(object) > 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, org.apache.commons.collections4.MultiSet
    public Iterator<E> iterator() {
        return new MultiSetIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multiset/AbstractMultiSet$MultiSetIterator.class */
    private static class MultiSetIterator<E> implements Iterator<E> {
        private final AbstractMultiSet<E> parent;
        private final Iterator<MultiSet.Entry<E>> entryIterator;
        private int itemCount;
        private MultiSet.Entry<E> current = null;
        private boolean canRemove = false;

        public MultiSetIterator(AbstractMultiSet<E> parent) {
            this.parent = parent;
            this.entryIterator = parent.entrySet().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.itemCount > 0 || this.entryIterator.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.itemCount == 0) {
                this.current = this.entryIterator.next();
                this.itemCount = this.current.getCount();
            }
            this.canRemove = true;
            this.itemCount--;
            return this.current.getElement();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.canRemove) {
                throw new IllegalStateException();
            }
            int count = this.current.getCount();
            if (count > 1) {
                this.parent.remove(this.current.getElement());
            } else {
                this.entryIterator.remove();
            }
            this.canRemove = false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, org.apache.commons.collections4.MultiSet
    public boolean add(E object) {
        add(object, 1);
        return true;
    }

    public int add(E object, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        Iterator<MultiSet.Entry<E>> it = entrySet().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, org.apache.commons.collections4.MultiSet
    public boolean remove(Object object) {
        return remove(object, 1) != 0;
    }

    public int remove(Object object, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, org.apache.commons.collections4.MultiSet
    public boolean removeAll(Collection<?> coll) {
        boolean result = false;
        for (Object obj : coll) {
            boolean changed = remove(obj, getCount(obj)) != 0;
            result = result || changed;
        }
        return result;
    }

    @Override // org.apache.commons.collections4.MultiSet
    public Set<E> uniqueSet() {
        if (this.uniqueSet == null) {
            this.uniqueSet = createUniqueSet();
        }
        return this.uniqueSet;
    }

    protected Set<E> createUniqueSet() {
        return new UniqueSet(this);
    }

    protected Iterator<E> createUniqueSetIterator() {
        Transformer<MultiSet.Entry<E>, E> transformer = new Transformer<MultiSet.Entry<E>, E>() { // from class: org.apache.commons.collections4.multiset.AbstractMultiSet.1
            @Override // org.apache.commons.collections4.Transformer
            public E transform(MultiSet.Entry<E> entry) {
                return entry.getElement();
            }
        };
        return IteratorUtils.transformedIterator(entrySet().iterator(), transformer);
    }

    @Override // org.apache.commons.collections4.MultiSet
    public Set<MultiSet.Entry<E>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = createEntrySet();
        }
        return this.entrySet;
    }

    protected Set<MultiSet.Entry<E>> createEntrySet() {
        return new EntrySet(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multiset/AbstractMultiSet$UniqueSet.class */
    protected static class UniqueSet<E> extends AbstractSet<E> {
        protected final AbstractMultiSet<E> parent;

        protected UniqueSet(AbstractMultiSet<E> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return this.parent.createUniqueSetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object key) {
            return this.parent.contains(key);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> coll) {
            return this.parent.containsAll(coll);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object key) {
            return this.parent.remove(key, this.parent.getCount(key)) != 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.uniqueElements();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.parent.clear();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multiset/AbstractMultiSet$EntrySet.class */
    protected static class EntrySet<E> extends AbstractSet<MultiSet.Entry<E>> {
        private final AbstractMultiSet<E> parent;

        protected EntrySet(AbstractMultiSet<E> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.uniqueElements();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<MultiSet.Entry<E>> iterator() {
            return this.parent.createEntrySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof MultiSet.Entry)) {
                return false;
            }
            MultiSet.Entry<?> entry = (MultiSet.Entry) obj;
            Object element = entry.getElement();
            return this.parent.getCount(element) == entry.getCount();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int count;
            if (!(obj instanceof MultiSet.Entry)) {
                return false;
            }
            MultiSet.Entry<?> entry = (MultiSet.Entry) obj;
            Object element = entry.getElement();
            if (this.parent.contains(element) && entry.getCount() == (count = this.parent.getCount(element))) {
                this.parent.remove(element, count);
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/multiset/AbstractMultiSet$AbstractEntry.class */
    public static abstract class AbstractEntry<E> implements MultiSet.Entry<E> {
        protected AbstractEntry() {
        }

        @Override // org.apache.commons.collections4.MultiSet.Entry
        public boolean equals(Object object) {
            if (object instanceof MultiSet.Entry) {
                MultiSet.Entry<?> other = (MultiSet.Entry) object;
                E element = getElement();
                Object otherElement = other.getElement();
                return getCount() == other.getCount() && (element == otherElement || (element != null && element.equals(otherElement)));
            }
            return false;
        }

        @Override // org.apache.commons.collections4.MultiSet.Entry
        public int hashCode() {
            E element = getElement();
            return (element == null ? 0 : element.hashCode()) ^ getCount();
        }

        public String toString() {
            return String.format("%s:%d", getElement(), Integer.valueOf(getCount()));
        }
    }

    protected void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(entrySet().size());
        for (MultiSet.Entry<E> entry : entrySet()) {
            out.writeObject(entry.getElement());
            out.writeInt(entry.getCount());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int entrySize = in.readInt();
        for (int i = 0; i < entrySize; i++) {
            Object object = in.readObject();
            int count = in.readInt();
            setCount(object, count);
        }
    }

    @Override // java.util.Collection, org.apache.commons.collections4.MultiSet
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MultiSet)) {
            return false;
        }
        MultiSet<?> other = (MultiSet) object;
        if (other.size() != size()) {
            return false;
        }
        for (MultiSet.Entry<E> entry : entrySet()) {
            if (other.getCount(entry.getElement()) != getCount(entry.getElement())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, org.apache.commons.collections4.MultiSet
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return entrySet().toString();
    }
}

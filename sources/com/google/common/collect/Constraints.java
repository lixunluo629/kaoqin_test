package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Constraints.class */
final class Constraints {
    private Constraints() {
    }

    public static <E> Collection<E> constrainedCollection(Collection<E> collection, Constraint<? super E> constraint) {
        return new ConstrainedCollection(collection, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedCollection.class */
    static class ConstrainedCollection<E> extends ForwardingCollection<E> {
        private final Collection<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedCollection(Collection<E> delegate, Constraint<? super E> constraint) {
            this.delegate = (Collection) Preconditions.checkNotNull(delegate);
            this.constraint = (Constraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Collection<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }

    public static <E> Set<E> constrainedSet(Set<E> set, Constraint<? super E> constraint) {
        return new ConstrainedSet(set, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedSet.class */
    static class ConstrainedSet<E> extends ForwardingSet<E> {
        private final Set<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedSet(Set<E> delegate, Constraint<? super E> constraint) {
            this.delegate = (Set) Preconditions.checkNotNull(delegate);
            this.constraint = (Constraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }

    public static <E> SortedSet<E> constrainedSortedSet(SortedSet<E> sortedSet, Constraint<? super E> constraint) {
        return new ConstrainedSortedSet(sortedSet, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedSortedSet.class */
    private static class ConstrainedSortedSet<E> extends ForwardingSortedSet<E> {
        final SortedSet<E> delegate;
        final Constraint<? super E> constraint;

        ConstrainedSortedSet(SortedSet<E> delegate, Constraint<? super E> constraint) {
            this.delegate = (SortedSet) Preconditions.checkNotNull(delegate);
            this.constraint = (Constraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public SortedSet<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> headSet(E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.headSet(toElement), this.constraint);
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return Constraints.constrainedSortedSet(this.delegate.subSet(fromElement, toElement), this.constraint);
        }

        @Override // com.google.common.collect.ForwardingSortedSet, java.util.SortedSet
        public SortedSet<E> tailSet(E fromElement) {
            return Constraints.constrainedSortedSet(this.delegate.tailSet(fromElement), this.constraint);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }
    }

    public static <E> List<E> constrainedList(List<E> list, Constraint<? super E> constraint) {
        return list instanceof RandomAccess ? new ConstrainedRandomAccessList(list, constraint) : new ConstrainedList(list, constraint);
    }

    @GwtCompatible
    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedList.class */
    private static class ConstrainedList<E> extends ForwardingList<E> {
        final List<E> delegate;
        final Constraint<? super E> constraint;

        ConstrainedList(List<E> delegate, Constraint<? super E> constraint) {
            this.delegate = (List) Preconditions.checkNotNull(delegate);
            this.constraint = (Constraint) Preconditions.checkNotNull(constraint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingList, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public List<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E element) {
            this.constraint.checkElement(element);
            return this.delegate.add(element);
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public void add(int index, E element) {
            this.constraint.checkElement(element);
            this.delegate.add(index, element);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> elements) {
            return this.delegate.addAll(Constraints.checkElements(elements, this.constraint));
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public boolean addAll(int index, Collection<? extends E> elements) {
            return this.delegate.addAll(index, Constraints.checkElements(elements, this.constraint));
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public ListIterator<E> listIterator() {
            return Constraints.constrainedListIterator(this.delegate.listIterator(), this.constraint);
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public ListIterator<E> listIterator(int index) {
            return Constraints.constrainedListIterator(this.delegate.listIterator(index), this.constraint);
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public E set(int index, E element) {
            this.constraint.checkElement(element);
            return this.delegate.set(index, element);
        }

        @Override // com.google.common.collect.ForwardingList, java.util.List
        public List<E> subList(int fromIndex, int toIndex) {
            return Constraints.constrainedList(this.delegate.subList(fromIndex, toIndex), this.constraint);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedRandomAccessList.class */
    static class ConstrainedRandomAccessList<E> extends ConstrainedList<E> implements RandomAccess {
        ConstrainedRandomAccessList(List<E> delegate, Constraint<? super E> constraint) {
            super(delegate, constraint);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> ListIterator<E> constrainedListIterator(ListIterator<E> listIterator, Constraint<? super E> constraint) {
        return new ConstrainedListIterator(listIterator, constraint);
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/Constraints$ConstrainedListIterator.class */
    static class ConstrainedListIterator<E> extends ForwardingListIterator<E> {
        private final ListIterator<E> delegate;
        private final Constraint<? super E> constraint;

        public ConstrainedListIterator(ListIterator<E> delegate, Constraint<? super E> constraint) {
            this.delegate = delegate;
            this.constraint = constraint;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingListIterator, com.google.common.collect.ForwardingIterator, com.google.common.collect.ForwardingObject
        public ListIterator<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingListIterator, java.util.ListIterator
        public void add(E element) {
            this.constraint.checkElement(element);
            this.delegate.add(element);
        }

        @Override // com.google.common.collect.ForwardingListIterator, java.util.ListIterator
        public void set(E element) {
            this.constraint.checkElement(element);
            this.delegate.set(element);
        }
    }

    static <E> Collection<E> constrainedTypePreservingCollection(Collection<E> collection, Constraint<E> constraint) {
        if (collection instanceof SortedSet) {
            return constrainedSortedSet((SortedSet) collection, constraint);
        }
        if (collection instanceof Set) {
            return constrainedSet((Set) collection, constraint);
        }
        if (collection instanceof List) {
            return constrainedList((List) collection, constraint);
        }
        return constrainedCollection(collection, constraint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> Collection<E> checkElements(Collection<E> collection, Constraint<? super E> constraint) {
        ArrayList arrayListNewArrayList = Lists.newArrayList(collection);
        Iterator<E> it = arrayListNewArrayList.iterator();
        while (it.hasNext()) {
            constraint.checkElement((Object) it.next());
        }
        return arrayListNewArrayList;
    }
}

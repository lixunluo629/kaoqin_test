package org.apache.commons.pool2.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/LinkedBlockingDeque.class */
class LinkedBlockingDeque<E> extends AbstractQueue<E> implements Deque<E>, Serializable {
    private static final long serialVersionUID = -387911632671998426L;
    private transient Node<E> first;
    private transient Node<E> last;
    private transient int count;
    private final int capacity;
    private final InterruptibleReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/LinkedBlockingDeque$Node.class */
    private static final class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E x, Node<E> p, Node<E> n) {
            this.item = x;
            this.prev = p;
            this.next = n;
        }
    }

    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingDeque(boolean fairness) {
        this(Integer.MAX_VALUE, fairness);
    }

    public LinkedBlockingDeque(int capacity) {
        this(capacity, false);
    }

    public LinkedBlockingDeque(int capacity, boolean fairness) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.lock = new InterruptibleReentrantLock(fairness);
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
    }

    public LinkedBlockingDeque(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        this.lock.lock();
        try {
            for (E e : c) {
                if (e == null) {
                    throw new NullPointerException();
                }
                if (!linkLast(e)) {
                    throw new IllegalStateException("Deque full");
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    private boolean linkFirst(E e) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> f = this.first;
        Node<E> x = new Node<>(e, null, f);
        this.first = x;
        if (this.last == null) {
            this.last = x;
        } else {
            f.prev = x;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private boolean linkLast(E e) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> l = this.last;
        Node<E> x = new Node<>(e, l, null);
        this.last = x;
        if (this.first == null) {
            this.first = x;
        } else {
            l.next = x;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private E unlinkFirst() {
        Node<E> f = this.first;
        if (f == null) {
            return null;
        }
        Node<E> n = f.next;
        E item = f.item;
        f.item = null;
        f.next = f;
        this.first = n;
        if (n == null) {
            this.last = null;
        } else {
            n.prev = null;
        }
        this.count--;
        this.notFull.signal();
        return item;
    }

    private E unlinkLast() {
        Node<E> l = this.last;
        if (l == null) {
            return null;
        }
        Node<E> p = l.prev;
        E item = l.item;
        l.item = null;
        l.prev = l;
        this.last = p;
        if (p == null) {
            this.first = null;
        } else {
            p.next = null;
        }
        this.count--;
        this.notFull.signal();
        return item;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlink(Node<E> x) {
        Node<E> p = x.prev;
        Node<E> n = x.next;
        if (p == null) {
            unlinkFirst();
            return;
        }
        if (n == null) {
            unlinkLast();
            return;
        }
        p.next = n;
        n.prev = p;
        x.item = null;
        this.count--;
        this.notFull.signal();
    }

    @Override // java.util.Deque
    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    @Override // java.util.Deque
    public void addLast(E e) {
        if (!offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    @Override // java.util.Deque
    public boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            return linkFirst(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            return linkLast(e);
        } finally {
            this.lock.unlock();
        }
    }

    public void putFirst(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        while (!linkFirst(e)) {
            try {
                this.notFull.await();
            } finally {
                this.lock.unlock();
            }
        }
    }

    public void putLast(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        while (!linkLast(e)) {
            try {
                this.notFull.await();
            } finally {
                this.lock.unlock();
            }
        }
    }

    public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        while (!linkFirst(e)) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                this.lock.unlock();
            }
        }
        this.lock.unlock();
        return true;
    }

    public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        while (!linkLast(e)) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                this.lock.unlock();
            }
        }
        this.lock.unlock();
        return true;
    }

    @Override // java.util.Deque
    public E removeFirst() {
        E x = pollFirst();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override // java.util.Deque
    public E removeLast() {
        E x = pollLast();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override // java.util.Deque
    public E pollFirst() {
        this.lock.lock();
        try {
            return unlinkFirst();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public E pollLast() {
        this.lock.lock();
        try {
            return unlinkLast();
        } finally {
            this.lock.unlock();
        }
    }

    public E takeFirst() throws InterruptedException {
        this.lock.lock();
        while (true) {
            try {
                E x = unlinkFirst();
                if (x == null) {
                    this.notEmpty.await();
                } else {
                    return x;
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    public E takeLast() throws InterruptedException {
        this.lock.lock();
        while (true) {
            try {
                E x = unlinkLast();
                if (x == null) {
                    this.notEmpty.await();
                } else {
                    return x;
                }
            } finally {
                this.lock.unlock();
            }
        }
    }

    public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        while (true) {
            try {
                E x = unlinkFirst();
                if (x != null) {
                    this.lock.unlock();
                    return x;
                }
                if (nanos <= 0) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            } finally {
                this.lock.unlock();
            }
        }
    }

    public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        while (true) {
            try {
                E x = unlinkLast();
                if (x != null) {
                    this.lock.unlock();
                    return x;
                }
                if (nanos <= 0) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            } finally {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.Deque
    public E getFirst() {
        E x = peekFirst();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override // java.util.Deque
    public E getLast() {
        E x = peekLast();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override // java.util.Deque
    public E peekFirst() {
        this.lock.lock();
        try {
            return this.first == null ? null : this.first.item;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public E peekLast() {
        this.lock.lock();
        try {
            return this.last == null ? null : this.last.item;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    unlink(p);
                    this.lock.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.last; p != null; p = p.prev) {
                if (o.equals(p.item)) {
                    unlink(p);
                    this.lock.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.Queue, java.util.Deque
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override // java.util.Queue, java.util.Deque
    public boolean offer(E e) {
        return offerLast(e);
    }

    public void put(E e) throws InterruptedException {
        putLast(e);
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offerLast(e, timeout, unit);
    }

    @Override // java.util.AbstractQueue, java.util.Queue, java.util.Deque
    public E remove() {
        return removeFirst();
    }

    @Override // java.util.Queue, java.util.Deque
    public E poll() {
        return pollFirst();
    }

    public E take() throws InterruptedException {
        return takeFirst();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return pollFirst(timeout, unit);
    }

    @Override // java.util.AbstractQueue, java.util.Queue, java.util.Deque
    public E element() {
        return getFirst();
    }

    @Override // java.util.Queue, java.util.Deque
    public E peek() {
        return peekFirst();
    }

    public int remainingCapacity() {
        this.lock.lock();
        try {
            return this.capacity - this.count;
        } finally {
            this.lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        this.lock.lock();
        try {
            int iMin = Math.min(i, this.count);
            for (int i2 = 0; i2 < iMin; i2++) {
                collection.add(this.first.item);
                unlinkFirst();
            }
            return iMin;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Deque
    public void push(E e) {
        addFirst(e);
    }

    @Override // java.util.Deque
    public E pop() {
        return removeFirst();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Deque
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Deque
    public int size() {
        this.lock.lock();
        try {
            return this.count;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Deque
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    return true;
                }
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        this.lock.lock();
        try {
            Object[] a = new Object[this.count];
            int k = 0;
            for (Node<E> p = this.first; p != null; p = p.next) {
                int i = k;
                k++;
                a[i] = p.item;
            }
            return a;
        } finally {
            this.lock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.lang.Object[]] */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] a) {
        this.lock.lock();
        try {
            if (a.length < this.count) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), this.count);
            }
            int k = 0;
            for (Node<E> p = this.first; p != null; p = p.next) {
                int i = k;
                k++;
                a[i] = p.item;
            }
            if (a.length > k) {
                a[k] = null;
            }
            return a;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        this.lock.lock();
        try {
            return super.toString();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.lock.lock();
        try {
            Node<E> f = this.first;
            while (f != null) {
                f.item = null;
                Node<E> n = f.next;
                f.prev = null;
                f.next = null;
                f = n;
            }
            this.last = null;
            this.first = null;
            this.count = 0;
            this.notFull.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Deque
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/LinkedBlockingDeque$AbstractItr.class */
    private abstract class AbstractItr implements Iterator<E> {
        Node<E> next;
        E nextItem;
        private Node<E> lastRet;

        abstract Node<E> firstNode();

        abstract Node<E> nextNode(Node<E> node);

        AbstractItr() {
            LinkedBlockingDeque.this.lock.lock();
            try {
                this.next = firstNode();
                this.nextItem = this.next == null ? null : this.next.item;
            } finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }

        private Node<E> succ(Node<E> n) {
            while (true) {
                Node<E> s = nextNode(n);
                if (s == null) {
                    return null;
                }
                if (s.item != null) {
                    return s;
                }
                if (s == n) {
                    return firstNode();
                }
                n = s;
            }
        }

        void advance() {
            LinkedBlockingDeque.this.lock.lock();
            try {
                this.next = succ(this.next);
                this.nextItem = this.next == null ? null : this.next.item;
            } finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.next;
            E x = this.nextItem;
            advance();
            return x;
        }

        @Override // java.util.Iterator
        public void remove() {
            Node<E> n = this.lastRet;
            if (n == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            LinkedBlockingDeque.this.lock.lock();
            try {
                if (n.item != null) {
                    LinkedBlockingDeque.this.unlink(n);
                }
            } finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/LinkedBlockingDeque$Itr.class */
    private class Itr extends LinkedBlockingDeque<E>.AbstractItr {
        private Itr() {
            super();
        }

        @Override // org.apache.commons.pool2.impl.LinkedBlockingDeque.AbstractItr
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }

        @Override // org.apache.commons.pool2.impl.LinkedBlockingDeque.AbstractItr
        Node<E> nextNode(Node<E> n) {
            return n.next;
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/LinkedBlockingDeque$DescendingItr.class */
    private class DescendingItr extends LinkedBlockingDeque<E>.AbstractItr {
        private DescendingItr() {
            super();
        }

        @Override // org.apache.commons.pool2.impl.LinkedBlockingDeque.AbstractItr
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }

        @Override // org.apache.commons.pool2.impl.LinkedBlockingDeque.AbstractItr
        Node<E> nextNode(Node<E> n) {
            return n.prev;
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        this.lock.lock();
        try {
            s.defaultWriteObject();
            for (Node<E> p = this.first; p != null; p = p.next) {
                s.writeObject(p.item);
            }
            s.writeObject(null);
        } finally {
            this.lock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            Object object = s.readObject();
            if (object != null) {
                add(object);
            } else {
                return;
            }
        }
    }

    public boolean hasTakeWaiters() {
        this.lock.lock();
        try {
            return this.lock.hasWaiters(this.notEmpty);
        } finally {
            this.lock.unlock();
        }
    }

    public int getTakeQueueLength() {
        this.lock.lock();
        try {
            return this.lock.getWaitQueueLength(this.notEmpty);
        } finally {
            this.lock.unlock();
        }
    }

    public void interuptTakeWaiters() {
        this.lock.lock();
        try {
            this.lock.interruptWaiters(this.notEmpty);
        } finally {
            this.lock.unlock();
        }
    }
}

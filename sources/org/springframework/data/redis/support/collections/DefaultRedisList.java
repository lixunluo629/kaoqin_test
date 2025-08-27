package org.springframework.data.redis.support.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisList.class */
public class DefaultRedisList<E> extends AbstractRedisCollection<E> implements RedisList<E> {
    private final BoundListOperations<String, E> listOps;
    private volatile int maxSize;
    private volatile boolean capped;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/DefaultRedisList$DefaultRedisListIterator.class */
    private class DefaultRedisListIterator extends RedisIterator<E> {
        public DefaultRedisListIterator(Iterator<E> delegate) {
            super(delegate);
        }

        @Override // org.springframework.data.redis.support.collections.RedisIterator
        protected void removeFromRedisStorage(E item) {
            DefaultRedisList.this.remove(item);
        }
    }

    public DefaultRedisList(String key, RedisOperations<String, E> operations) {
        this(operations.boundListOps(key));
    }

    public DefaultRedisList(BoundListOperations<String, E> boundOps) {
        this(boundOps, 0);
    }

    public DefaultRedisList(BoundListOperations<String, E> boundOps, int maxSize) {
        super(boundOps.getKey(), boundOps.getOperations());
        this.maxSize = 0;
        this.capped = false;
        this.listOps = boundOps;
        setMaxSize(maxSize);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        this.capped = maxSize > 0;
    }

    @Override // org.springframework.data.redis.support.collections.RedisList
    public List<E> range(long start, long end) {
        return this.listOps.range(start, end);
    }

    @Override // org.springframework.data.redis.support.collections.RedisList
    public RedisList<E> trim(int start, int end) {
        this.listOps.trim(start, end);
        return this;
    }

    private List<E> content() {
        return this.listOps.range(0L, -1L);
    }

    private void cap() {
        if (this.capped) {
            this.listOps.trim(0L, this.maxSize - 1);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List, java.util.concurrent.BlockingDeque, java.util.Deque
    public Iterator<E> iterator() {
        List<E> list = content();
        checkResult(list);
        return new DefaultRedisListIterator(list.iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.concurrent.BlockingDeque, java.util.Deque
    public int size() {
        Long size = this.listOps.size();
        checkResult(size);
        return size.intValue();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue, java.util.Queue, java.util.Deque
    public boolean add(E value) {
        this.listOps.rightPush(value);
        cap();
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.listOps.trim(size() + 1, 0L);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue, java.util.Deque
    public boolean remove(Object o) {
        Long result = this.listOps.remove(1L, o);
        return result != null && result.longValue() > 0;
    }

    @Override // java.util.List
    public void add(int index, E element) {
        if (index == 0) {
            this.listOps.leftPush(element);
            cap();
            return;
        }
        int size = size();
        if (index == size()) {
            this.listOps.rightPush(element);
            cap();
        } else {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            throw new IllegalArgumentException("Redis supports insertion only at the beginning or the end of the list");
        }
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index == 0) {
            Collection<? extends E> reverseC = CollectionUtils.reverse(c);
            for (E e : reverseC) {
                this.listOps.leftPush(e);
                cap();
            }
            return true;
        }
        int size = size();
        if (index == size()) {
            for (E e2 : c) {
                this.listOps.rightPush(e2);
                cap();
            }
            return true;
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("Redis supports insertion only at the beginning or the end of the list");
    }

    @Override // java.util.List
    public E get(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.listOps.index(index);
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
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public E set(int index, E e) {
        E object = get(index);
        this.listOps.set(index, e);
        return object;
    }

    @Override // java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Queue, java.util.Deque
    public E element() {
        E value = peek();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue, java.util.Queue, java.util.Deque
    public boolean offer(E e) {
        this.listOps.rightPush(e);
        cap();
        return true;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Queue, java.util.Deque
    public E peek() {
        return this.listOps.index(0L);
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Queue, java.util.Deque
    public E poll() {
        return this.listOps.leftPop();
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Queue, java.util.Deque
    public E remove() {
        E value = poll();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public void addFirst(E e) {
        this.listOps.leftPush(e);
        cap();
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public void addLast(E e) {
        add(e);
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        List<E> content = content();
        Collections.reverse(content);
        return new DefaultRedisListIterator(content.iterator());
    }

    @Override // java.util.Deque
    public E getFirst() {
        return element();
    }

    @Override // java.util.Deque
    public E getLast() {
        E e = peekLast();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override // java.util.Deque
    public E peekFirst() {
        return peek();
    }

    @Override // java.util.Deque
    public E peekLast() {
        return this.listOps.index(-1L);
    }

    @Override // java.util.Deque
    public E pollFirst() {
        return poll();
    }

    @Override // java.util.Deque
    public E pollLast() {
        return this.listOps.rightPop();
    }

    @Override // java.util.Deque
    public E pop() {
        E e = poll();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public void push(E e) {
        addFirst(e);
    }

    @Override // java.util.Deque
    public E removeFirst() {
        return pop();
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override // java.util.Deque
    public E removeLast() {
        E e = pollLast();
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.Deque
    public boolean removeLastOccurrence(Object o) {
        Long result = this.listOps.remove(-1L, o);
        return result != null && result.longValue() > 0;
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (equals(c)) {
            throw new IllegalArgumentException("Cannot drain a queue to itself");
        }
        int size = size();
        int loop = size >= maxElements ? maxElements : size;
        for (int index = 0; index < loop; index++) {
            c.add(poll());
        }
        return loop;
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, size());
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(e);
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E element = this.listOps.leftPop(timeout, unit);
        if (element == null) {
            return null;
        }
        return element;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public void put(E e) throws InterruptedException {
        offer(e);
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override // java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue
    public E take() throws InterruptedException {
        return poll(0L, TimeUnit.SECONDS);
    }

    @Override // java.util.concurrent.BlockingDeque
    public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offerFirst(e);
    }

    @Override // java.util.concurrent.BlockingDeque
    public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offerLast(e);
    }

    @Override // java.util.concurrent.BlockingDeque
    public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
        return poll(timeout, unit);
    }

    @Override // java.util.concurrent.BlockingDeque
    public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
        E element = this.listOps.rightPop(timeout, unit);
        if (element == null) {
            return null;
        }
        return element;
    }

    @Override // java.util.concurrent.BlockingDeque
    public void putFirst(E e) throws InterruptedException {
        add(e);
    }

    @Override // java.util.concurrent.BlockingDeque
    public void putLast(E e) throws InterruptedException {
        put(e);
    }

    @Override // java.util.concurrent.BlockingDeque
    public E takeFirst() throws InterruptedException {
        return take();
    }

    @Override // java.util.concurrent.BlockingDeque
    public E takeLast() throws InterruptedException {
        return pollLast(0L, TimeUnit.SECONDS);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.LIST;
    }
}

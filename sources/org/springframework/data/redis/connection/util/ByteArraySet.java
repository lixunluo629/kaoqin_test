package org.springframework.data.redis.connection.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/util/ByteArraySet.class */
public class ByteArraySet implements Set<ByteArrayWrapper> {
    LinkedHashSet<ByteArrayWrapper> delegate;

    public ByteArraySet() {
        this.delegate = new LinkedHashSet<>();
    }

    public ByteArraySet(Collection<byte[]> values) {
        this();
        addAll((Iterable<byte[]>) values);
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.delegate.size();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        if (o instanceof byte[]) {
            return this.delegate.contains(new ByteArrayWrapper((byte[]) o));
        }
        return this.delegate.contains(o);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(ByteArrayWrapper e) {
        return this.delegate.add(e);
    }

    public boolean add(byte[] e) {
        return this.delegate.add(new ByteArrayWrapper(e));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (o instanceof byte[]) {
                if (!contains(new ByteArrayWrapper((byte[]) o))) {
                    return false;
                }
            } else if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends ByteArrayWrapper> c) {
        return this.delegate.addAll(c);
    }

    public boolean addAll(Iterable<byte[]> c) {
        for (byte[] o : c) {
            add(o);
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<ByteArrayWrapper> iterator() {
        return this.delegate.iterator();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.delegate.toArray(tArr);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        if (o instanceof byte[]) {
            this.delegate.remove(new ByteArrayWrapper((byte[]) o));
        }
        return this.delegate.remove(o);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        this.delegate.clear();
    }

    public Set<byte[]> asRawSet() {
        Set<byte[]> result = new LinkedHashSet<>();
        Iterator<ByteArrayWrapper> it = this.delegate.iterator();
        while (it.hasNext()) {
            ByteArrayWrapper wrapper = it.next();
            result.add(wrapper.getArray());
        }
        return result;
    }
}

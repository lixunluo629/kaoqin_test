package org.apache.commons.collections4.queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/queue/UnmodifiableQueue.class */
public final class UnmodifiableQueue<E> extends AbstractQueueDecorator<E> implements Unmodifiable {
    private static final long serialVersionUID = 1832948656215393357L;

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> Queue<E> unmodifiableQueue(Queue<? extends E> queue) {
        if (queue instanceof Unmodifiable) {
            return queue;
        }
        return new UnmodifiableQueue(queue);
    }

    private UnmodifiableQueue(Queue<? extends E> queue) {
        super(queue);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        setCollection((Collection) in.readObject());
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, java.lang.Iterable, org.apache.commons.collections4.Bag
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean add(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public boolean addAll(Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.collection.AbstractCollectionDecorator, java.util.Collection, org.apache.commons.collections4.Bag
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.queue.AbstractQueueDecorator, java.util.Queue
    public boolean offer(E obj) {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.queue.AbstractQueueDecorator, java.util.Queue
    public E poll() {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.collections4.queue.AbstractQueueDecorator, java.util.Queue
    public E remove() {
        throw new UnsupportedOperationException();
    }
}

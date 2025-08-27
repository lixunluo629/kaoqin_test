package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
import java.util.Iterator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/MpscArrayQueue.class */
public class MpscArrayQueue<E> extends MpscArrayQueueL3Pad<E> {
    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public /* bridge */ /* synthetic */ long currentConsumerIndex() {
        return super.currentConsumerIndex();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public /* bridge */ /* synthetic */ long currentProducerIndex() {
        return super.currentProducerIndex();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ int capacity() {
        return super.capacity();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, java.util.AbstractCollection
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue, java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public MpscArrayQueue(int capacity) {
        super(capacity);
    }

    public boolean offerIfBelowThreshold(E e, int threshold) {
        long pIndex;
        if (null == e) {
            throw new NullPointerException();
        }
        long mask = this.mask;
        long capacity = mask + 1;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            long size = capacity - available;
            if (size >= threshold) {
                long cIndex = lvConsumerIndex();
                long size2 = pIndex - cIndex;
                if (size2 >= threshold) {
                    return false;
                }
                producerLimit = cIndex + capacity;
                soProducerLimit(producerLimit);
            }
        } while (!casProducerIndex(pIndex, pIndex + 1));
        long offset = UnsafeRefArrayAccess.calcCircularRefElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soRefElement(this.buffer, offset, e);
        return true;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean offer(E e) {
        long pIndex;
        if (null == e) {
            throw new NullPointerException();
        }
        long mask = this.mask;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            if (pIndex >= producerLimit) {
                long cIndex = lvConsumerIndex();
                producerLimit = cIndex + mask + 1;
                if (pIndex >= producerLimit) {
                    return false;
                }
                soProducerLimit(producerLimit);
            }
        } while (!casProducerIndex(pIndex, pIndex + 1));
        long offset = UnsafeRefArrayAccess.calcCircularRefElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soRefElement(this.buffer, offset, e);
        return true;
    }

    public final int failFastOffer(E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        long mask = this.mask;
        long capacity = mask + 1;
        long pIndex = lvProducerIndex();
        if (pIndex >= lvProducerLimit()) {
            long cIndex = lvConsumerIndex();
            long producerLimit = cIndex + capacity;
            if (pIndex >= producerLimit) {
                return 1;
            }
            soProducerLimit(producerLimit);
        }
        if (!casProducerIndex(pIndex, pIndex + 1)) {
            return -1;
        }
        long offset = UnsafeRefArrayAccess.calcCircularRefElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soRefElement(this.buffer, offset, e);
        return 0;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        long jLpConsumerIndex = lpConsumerIndex();
        long jCalcCircularRefElementOffset = UnsafeRefArrayAccess.calcCircularRefElementOffset(jLpConsumerIndex, this.mask);
        E[] eArr = this.buffer;
        Object objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jCalcCircularRefElementOffset);
        if (null == objLvRefElement) {
            if (jLpConsumerIndex != lvProducerIndex()) {
                do {
                    objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jCalcCircularRefElementOffset);
                } while (objLvRefElement == null);
            } else {
                return null;
            }
        }
        UnsafeRefArrayAccess.soRefElement(eArr, jCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 1);
        return (E) objLvRefElement;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E peek() {
        E[] eArr = this.buffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long jCalcCircularRefElementOffset = UnsafeRefArrayAccess.calcCircularRefElementOffset(jLpConsumerIndex, this.mask);
        Object objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jCalcCircularRefElementOffset);
        if (null == objLvRefElement) {
            if (jLpConsumerIndex != lvProducerIndex()) {
                do {
                    objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jCalcCircularRefElementOffset);
                } while (objLvRefElement == null);
            } else {
                return null;
            }
        }
        return (E) objLvRefElement;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean relaxedOffer(E e) {
        return offer(e);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPoll() {
        E[] eArr = this.buffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long jCalcCircularRefElementOffset = UnsafeRefArrayAccess.calcCircularRefElementOffset(jLpConsumerIndex, this.mask);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, jCalcCircularRefElementOffset);
        if (null == e) {
            return null;
        }
        UnsafeRefArrayAccess.soRefElement(eArr, jCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 1);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        return (E) UnsafeRefArrayAccess.lvRefElement(this.buffer, UnsafeRefArrayAccess.calcCircularRefElementOffset(lpConsumerIndex(), this.mask));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> consumer, int limit) {
        if (0 == consumer) {
            throw new IllegalArgumentException("c is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative: " + limit);
        }
        if (limit == 0) {
            return 0;
        }
        E[] buffer = this.buffer;
        long mask = this.mask;
        long cIndex = lpConsumerIndex();
        for (int i = 0; i < limit; i++) {
            long index = cIndex + i;
            long offset = UnsafeRefArrayAccess.calcCircularRefElementOffset(index, mask);
            Object objLvRefElement = UnsafeRefArrayAccess.lvRefElement(buffer, offset);
            if (null == objLvRefElement) {
                return i;
            }
            UnsafeRefArrayAccess.soRefElement(buffer, offset, null);
            soConsumerIndex(index + 1);
            consumer.accept(objLvRefElement);
        }
        return limit;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
        long pIndex;
        int actualLimit;
        if (null == s) {
            throw new IllegalArgumentException("supplier is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        long mask = this.mask;
        long capacity = mask + 1;
        long producerLimit = lvProducerLimit();
        do {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            if (available <= 0) {
                long cIndex = lvConsumerIndex();
                producerLimit = cIndex + capacity;
                available = producerLimit - pIndex;
                if (available <= 0) {
                    return 0;
                }
                soProducerLimit(producerLimit);
            }
            actualLimit = Math.min((int) available, limit);
        } while (!casProducerIndex(pIndex, pIndex + actualLimit));
        E[] buffer = this.buffer;
        for (int i = 0; i < actualLimit; i++) {
            long offset = UnsafeRefArrayAccess.calcCircularRefElementOffset(pIndex + i, mask);
            UnsafeRefArrayAccess.soRefElement(buffer, offset, s.get());
        }
        return actualLimit;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c) {
        return drain(c, capacity());
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s) {
        return MessagePassingQueueUtil.fillBounded(this, s);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.drain(this, c, w, exit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }
}

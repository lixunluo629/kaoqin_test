package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueueUtil;
import io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import redis.clients.jedis.Protocol;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/atomic/BaseMpscLinkedAtomicArrayQueue.class */
abstract class BaseMpscLinkedAtomicArrayQueue<E> extends BaseMpscLinkedAtomicArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {
    private static final Object JUMP;
    private static final Object BUFFER_CONSUMED;
    private static final int CONTINUE_TO_P_INDEX_CAS = 0;
    private static final int RETRY = 1;
    private static final int QUEUE_FULL = 2;
    private static final int QUEUE_RESIZE = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract long availableInQueue(long j, long j2);

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public abstract int capacity();

    protected abstract int getNextBufferSize(AtomicReferenceArray<E> atomicReferenceArray);

    protected abstract long getCurrentBufferCapacity(long j);

    static /* synthetic */ Object access$000() {
        return JUMP;
    }

    static /* synthetic */ Object access$100() {
        return BUFFER_CONSUMED;
    }

    static {
        $assertionsDisabled = !BaseMpscLinkedAtomicArrayQueue.class.desiredAssertionStatus();
        JUMP = new Object();
        BUFFER_CONSUMED = new Object();
    }

    public BaseMpscLinkedAtomicArrayQueue(int initialCapacity) {
        RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
        int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        long mask = (p2capacity - 1) << 1;
        AtomicReferenceArray<E> buffer = AtomicQueueUtil.allocateRefArray(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        soProducerLimit(mask);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int size() {
        long before;
        long currentProducerIndex;
        long after = lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = lvProducerIndex();
            after = lvConsumerIndex();
        } while (before != after);
        long size = (currentProducerIndex - after) >> 1;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean isEmpty() {
        return lvConsumerIndex() == lvProducerIndex();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return getClass().getName();
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean offer(E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        while (true) {
            long producerLimit = lvProducerLimit();
            long pIndex = lvProducerIndex();
            if ((pIndex & 1) != 1) {
                long mask = this.producerMask;
                AtomicReferenceArray<E> buffer = this.producerBuffer;
                if (producerLimit <= pIndex) {
                    int result = offerSlowPath(mask, pIndex, producerLimit);
                    switch (result) {
                        case 2:
                            return false;
                        case 3:
                            resize(mask, buffer, pIndex, e, null);
                            return true;
                    }
                }
                if (casProducerIndex(pIndex, pIndex + 2)) {
                    int offset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, mask);
                    AtomicQueueUtil.soRefElement(buffer, offset, e);
                    return true;
                }
            }
        }
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        int iModifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        Object objLvRefElement = AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
        if (objLvRefElement == null) {
            if (jLpConsumerIndex != lvProducerIndex()) {
                do {
                    objLvRefElement = AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
                } while (objLvRefElement == null);
            } else {
                return null;
            }
        }
        if (objLvRefElement == JUMP) {
            return newBufferPoll(nextBuffer(atomicReferenceArray, j), jLpConsumerIndex);
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 2);
        return (E) objLvRefElement;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E peek() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        int iModifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        Object objLvRefElement = AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
        if (objLvRefElement == null && jLpConsumerIndex != lvProducerIndex()) {
            do {
                objLvRefElement = AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
            } while (objLvRefElement == null);
        }
        if (objLvRefElement == JUMP) {
            return newBufferPeek(nextBuffer(atomicReferenceArray, j), jLpConsumerIndex);
        }
        return (E) objLvRefElement;
    }

    private int offerSlowPath(long mask, long pIndex, long producerLimit) {
        long cIndex = lvConsumerIndex();
        long bufferCapacity = getCurrentBufferCapacity(mask);
        if (cIndex + bufferCapacity > pIndex) {
            if (!casProducerLimit(producerLimit, cIndex + bufferCapacity)) {
                return 1;
            }
            return 0;
        }
        if (availableInQueue(pIndex, cIndex) <= 0) {
            return 2;
        }
        if (casProducerIndex(pIndex, pIndex + 1)) {
            return 3;
        }
        return 1;
    }

    private AtomicReferenceArray<E> nextBuffer(AtomicReferenceArray<E> buffer, long mask) {
        int offset = nextArrayOffset(mask);
        AtomicReferenceArray<E> nextBuffer = (AtomicReferenceArray) AtomicQueueUtil.lvRefElement(buffer, offset);
        this.consumerBuffer = nextBuffer;
        this.consumerMask = (AtomicQueueUtil.length(nextBuffer) - 2) << 1;
        AtomicQueueUtil.soRefElement(buffer, offset, BUFFER_CONSUMED);
        return nextBuffer;
    }

    private static int nextArrayOffset(long mask) {
        return AtomicQueueUtil.modifiedCalcCircularRefElementOffset(mask + 2, Long.MAX_VALUE);
    }

    private E newBufferPoll(AtomicReferenceArray<E> atomicReferenceArray, long j) {
        int iModifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
        if (e == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(j + 2);
        return e;
    }

    private E newBufferPeek(AtomicReferenceArray<E> atomicReferenceArray, long j) {
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, AtomicQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask));
        if (null == e) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public long currentProducerIndex() {
        return lvProducerIndex() / 2;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators
    public long currentConsumerIndex() {
        return lvConsumerIndex() / 2;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public boolean relaxedOffer(E e) {
        return offer(e);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPoll() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        int iModifiedCalcCircularRefElementOffset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset);
        if (e == null) {
            return null;
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(atomicReferenceArray, j), jLpConsumerIndex);
        }
        AtomicQueueUtil.soRefElement(atomicReferenceArray, iModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 2);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        AtomicReferenceArray<E> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        E e = (E) AtomicQueueUtil.lvRefElement(atomicReferenceArray, AtomicQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j));
        if (e == JUMP) {
            return newBufferPeek(nextBuffer(atomicReferenceArray, j), jLpConsumerIndex);
        }
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s) {
        long result = 0;
        int capacity = capacity();
        do {
            int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int) result;
            }
            result += filled;
        } while (result <= capacity);
        return (int) result;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
        if (null == s) {
            throw new IllegalArgumentException("supplier is null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit is negative:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        while (true) {
            long producerLimit = lvProducerLimit();
            long pIndex = lvProducerIndex();
            if ((pIndex & 1) != 1) {
                long mask = this.producerMask;
                AtomicReferenceArray<E> buffer = this.producerBuffer;
                long batchIndex = Math.min(producerLimit, pIndex + (2 * limit));
                if (pIndex >= producerLimit) {
                    int result = offerSlowPath(mask, pIndex, producerLimit);
                    switch (result) {
                        case 2:
                            return 0;
                        case 3:
                            resize(mask, buffer, pIndex, null, s);
                            return 1;
                    }
                }
                if (casProducerIndex(pIndex, batchIndex)) {
                    int claimedSlots = (int) ((batchIndex - pIndex) / 2);
                    for (int i = 0; i < claimedSlots; i++) {
                        int offset = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex + (2 * i), mask);
                        AtomicQueueUtil.soRefElement(buffer, offset, s.get());
                    }
                    return claimedSlots;
                }
            }
        }
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c) {
        return drain(c, capacity());
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
        return MessagePassingQueueUtil.drain(this, c, limit);
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
        MessagePassingQueueUtil.drain(this, c, wait, exit);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new WeakIterator(this.consumerBuffer, lvConsumerIndex(), lvProducerIndex());
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/atomic/BaseMpscLinkedAtomicArrayQueue$WeakIterator.class */
    private static class WeakIterator<E> implements Iterator<E> {
        private final long pIndex;
        private long nextIndex;
        private E nextElement;
        private AtomicReferenceArray<E> currentBuffer;
        private int mask;

        /*  JADX ERROR: Failed to decode insn: 0x0011: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        private E getNext() {
            /*
                r8 = this;
                r0 = r8
                long r0 = r0.nextIndex
                r1 = r8
                long r1 = r1.pIndex
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 >= 0) goto L82
                r0 = r8
                r1 = r0
                long r1 = r1.nextIndex
                // decode failed: arraycopy: source index -1 out of bounds for object array[8]
                r2 = 1
                long r1 = r1 + r2
                r0.nextIndex = r1
                r9 = r-1
                r-1 = r8
                java.util.concurrent.atomic.AtomicReferenceArray<E> r-1 = r-1.currentBuffer
                r0 = r9
                r1 = r8
                int r1 = r1.mask
                long r1 = (long) r1
                int r0 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.calcCircularRefElementOffset(r0, r1)
                io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r-1, r0)
                r11 = r-1
                r-1 = r11
                if (r-1 != 0) goto L30
                goto L0
                r-1 = r11
                java.lang.Object r0 = io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseMpscLinkedAtomicArrayQueue.access$000()
                if (r-1 == r0) goto L39
                r-1 = r11
                return r-1
                r-1 = r8
                int r-1 = r-1.mask
                r0 = 1
                int r-1 = r-1 + r0
                r12 = r-1
                r-1 = r8
                java.util.concurrent.atomic.AtomicReferenceArray<E> r-1 = r-1.currentBuffer
                r0 = r12
                long r0 = (long) r0
                int r0 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.calcRefElementOffset(r0)
                io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r-1, r0)
                r13 = r-1
                r-1 = r13
                java.lang.Object r0 = io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseMpscLinkedAtomicArrayQueue.access$100()
                if (r-1 == r0) goto L5d
                r-1 = r13
                if (r-1 != 0) goto L5f
                r-1 = 0
                return r-1
                r-1 = r8
                r0 = r13
                java.util.concurrent.atomic.AtomicReferenceArray r0 = (java.util.concurrent.atomic.AtomicReferenceArray) r0
                r-1.setBuffer(r0)
                r-1 = r8
                java.util.concurrent.atomic.AtomicReferenceArray<E> r-1 = r-1.currentBuffer
                r0 = r9
                r1 = r8
                int r1 = r1.mask
                long r1 = (long) r1
                int r0 = io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.calcCircularRefElementOffset(r0, r1)
                io.netty.util.internal.shaded.org.jctools.queues.atomic.AtomicQueueUtil.lvRefElement(r-1, r0)
                r11 = r-1
                r-1 = r11
                if (r-1 != 0) goto L80
                goto L0
                r-1 = r11
                return r-1
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.shaded.org.jctools.queues.atomic.BaseMpscLinkedAtomicArrayQueue.WeakIterator.getNext():java.lang.Object");
        }

        WeakIterator(AtomicReferenceArray<E> currentBuffer, long cIndex, long pIndex) {
            this.pIndex = pIndex >> 1;
            this.nextIndex = cIndex >> 1;
            setBuffer(currentBuffer);
            this.nextElement = getNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException(Protocol.SENTINEL_REMOVE);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextElement != null;
        }

        @Override // java.util.Iterator
        public E next() {
            E e = this.nextElement;
            if (e == null) {
                throw new NoSuchElementException();
            }
            this.nextElement = getNext();
            return e;
        }

        private void setBuffer(AtomicReferenceArray<E> buffer) {
            this.currentBuffer = buffer;
            this.mask = AtomicQueueUtil.length(buffer) - 2;
        }
    }

    private void resize(long oldMask, AtomicReferenceArray<E> oldBuffer, long pIndex, E e, MessagePassingQueue.Supplier<E> s) {
        if (!$assertionsDisabled && ((e == null || s != null) && e != null && s == null)) {
            throw new AssertionError();
        }
        int newBufferLength = getNextBufferSize(oldBuffer);
        try {
            AtomicReferenceArray<E> newBuffer = AtomicQueueUtil.allocateRefArray(newBufferLength);
            this.producerBuffer = newBuffer;
            int newMask = (newBufferLength - 2) << 1;
            this.producerMask = newMask;
            int offsetInOld = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, oldMask);
            int offsetInNew = AtomicQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, newMask);
            AtomicQueueUtil.soRefElement(newBuffer, offsetInNew, e == null ? s.get() : e);
            AtomicQueueUtil.soRefElement(oldBuffer, nextArrayOffset(oldMask), newBuffer);
            long cIndex = lvConsumerIndex();
            long availableInQueue = availableInQueue(pIndex, cIndex);
            RangeUtil.checkPositive(availableInQueue, "availableInQueue");
            soProducerLimit(pIndex + Math.min(newMask, availableInQueue));
            soProducerIndex(pIndex + 2);
            AtomicQueueUtil.soRefElement(oldBuffer, offsetInOld, JUMP);
        } catch (OutOfMemoryError oom) {
            if (!$assertionsDisabled && lvProducerIndex() != pIndex + 1) {
                throw new AssertionError();
            }
            soProducerIndex(pIndex);
            throw oom;
        }
    }
}

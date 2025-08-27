package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
import java.util.Iterator;
import java.util.NoSuchElementException;
import redis.clients.jedis.Protocol;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/BaseMpscLinkedArrayQueue.class */
abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {
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

    protected abstract int getNextBufferSize(E[] eArr);

    protected abstract long getCurrentBufferCapacity(long j);

    static /* synthetic */ Object access$000() {
        return JUMP;
    }

    static /* synthetic */ Object access$100() {
        return BUFFER_CONSUMED;
    }

    static {
        $assertionsDisabled = !BaseMpscLinkedArrayQueue.class.desiredAssertionStatus();
        JUMP = new Object();
        BUFFER_CONSUMED = new Object();
    }

    public BaseMpscLinkedArrayQueue(int i) {
        RangeUtil.checkGreaterThanOrEqual(i, 2, "initialCapacity");
        int iRoundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        long j = (iRoundToPowerOfTwo - 1) << 1;
        E[] eArr = (E[]) UnsafeRefArrayAccess.allocateRefArray(iRoundToPowerOfTwo + 1);
        this.producerBuffer = eArr;
        this.producerMask = j;
        this.consumerBuffer = eArr;
        this.consumerMask = j;
        soProducerLimit(j);
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
                E[] buffer = this.producerBuffer;
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
                    long offset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(pIndex, mask);
                    UnsafeRefArrayAccess.soRefElement(buffer, offset, e);
                    return true;
                }
            }
        }
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E poll() {
        E[] eArr = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        long jModifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        Object objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
        if (objLvRefElement == null) {
            if (jLpConsumerIndex != lvProducerIndex()) {
                do {
                    objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
                } while (objLvRefElement == null);
            } else {
                return null;
            }
        }
        if (objLvRefElement == JUMP) {
            return newBufferPoll(nextBuffer(eArr, j), jLpConsumerIndex);
        }
        UnsafeRefArrayAccess.soRefElement(eArr, jModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 2);
        return (E) objLvRefElement;
    }

    @Override // java.util.Queue, io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E peek() {
        E[] eArr = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        long jModifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        Object objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
        if (objLvRefElement == null && jLpConsumerIndex != lvProducerIndex()) {
            do {
                objLvRefElement = UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
            } while (objLvRefElement == null);
        }
        if (objLvRefElement == JUMP) {
            return newBufferPeek(nextBuffer(eArr, j), jLpConsumerIndex);
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

    private E[] nextBuffer(E[] eArr, long j) {
        long jNextArrayOffset = nextArrayOffset(j);
        E[] eArr2 = (E[]) ((Object[]) UnsafeRefArrayAccess.lvRefElement(eArr, jNextArrayOffset));
        this.consumerBuffer = eArr2;
        this.consumerMask = (LinkedArrayQueueUtil.length(eArr2) - 2) << 1;
        UnsafeRefArrayAccess.soRefElement(eArr, jNextArrayOffset, BUFFER_CONSUMED);
        return eArr2;
    }

    private static long nextArrayOffset(long mask) {
        return LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(mask + 2, Long.MAX_VALUE);
    }

    private E newBufferPoll(E[] eArr, long j) {
        long jModifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
        if (e == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        UnsafeRefArrayAccess.soRefElement(eArr, jModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(j + 2);
        return e;
    }

    private E newBufferPeek(E[] eArr, long j) {
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j, this.consumerMask));
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
        E[] eArr = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        long jModifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j);
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, jModifiedCalcCircularRefElementOffset);
        if (e == null) {
            return null;
        }
        if (e == JUMP) {
            return newBufferPoll(nextBuffer(eArr, j), jLpConsumerIndex);
        }
        UnsafeRefArrayAccess.soRefElement(eArr, jModifiedCalcCircularRefElementOffset, null);
        soConsumerIndex(jLpConsumerIndex + 2);
        return e;
    }

    @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
    public E relaxedPeek() {
        E[] eArr = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        long j = this.consumerMask;
        E e = (E) UnsafeRefArrayAccess.lvRefElement(eArr, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(jLpConsumerIndex, j));
        if (e == JUMP) {
            return newBufferPeek(nextBuffer(eArr, j), jLpConsumerIndex);
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
                E[] buffer = this.producerBuffer;
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
                        long offset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(pIndex + (2 * i), mask);
                        UnsafeRefArrayAccess.soRefElement(buffer, offset, s.get());
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

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/shaded/org/jctools/queues/BaseMpscLinkedArrayQueue$WeakIterator.class */
    private static class WeakIterator<E> implements Iterator<E> {
        private final long pIndex;
        private long nextIndex;
        private E nextElement;
        private E[] currentBuffer;
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
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
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
                if (r0 >= 0) goto L85
                r0 = r8
                r1 = r0
                long r1 = r1.nextIndex
                // decode failed: arraycopy: source index -1 out of bounds for object array[8]
                r2 = 1
                long r1 = r1 + r2
                r0.nextIndex = r1
                r9 = r-1
                r-1 = r8
                E[] r-1 = r-1.currentBuffer
                r0 = r9
                r1 = r8
                int r1 = r1.mask
                long r1 = (long) r1
                long r0 = io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.calcCircularRefElementOffset(r0, r1)
                io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r-1, r0)
                r11 = r-1
                r-1 = r11
                if (r-1 != 0) goto L30
                goto L0
                r-1 = r11
                java.lang.Object r0 = io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueue.access$000()
                if (r-1 == r0) goto L39
                r-1 = r11
                return r-1
                r-1 = r8
                int r-1 = r-1.mask
                r0 = 1
                int r-1 = r-1 + r0
                r12 = r-1
                r-1 = r8
                E[] r-1 = r-1.currentBuffer
                r0 = r12
                long r0 = (long) r0
                long r0 = io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.calcRefElementOffset(r0)
                io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r-1, r0)
                r13 = r-1
                r-1 = r13
                java.lang.Object r0 = io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueue.access$100()
                if (r-1 == r0) goto L5d
                r-1 = r13
                if (r-1 != 0) goto L5f
                r-1 = 0
                return r-1
                r-1 = r8
                r0 = r13
                java.lang.Object[] r0 = (java.lang.Object[]) r0
                java.lang.Object[] r0 = (java.lang.Object[]) r0
                r-1.setBuffer(r0)
                r-1 = r8
                E[] r-1 = r-1.currentBuffer
                r0 = r9
                r1 = r8
                int r1 = r1.mask
                long r1 = (long) r1
                long r0 = io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.calcCircularRefElementOffset(r0, r1)
                io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess.lvRefElement(r-1, r0)
                r11 = r-1
                r-1 = r11
                if (r-1 != 0) goto L83
                goto L0
                r-1 = r11
                return r-1
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueue.WeakIterator.getNext():java.lang.Object");
        }

        WeakIterator(E[] currentBuffer, long cIndex, long pIndex) {
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

        private void setBuffer(E[] buffer) {
            this.currentBuffer = buffer;
            this.mask = LinkedArrayQueueUtil.length(buffer) - 2;
        }
    }

    private void resize(long j, E[] eArr, long j2, E e, MessagePassingQueue.Supplier<E> supplier) {
        if (!$assertionsDisabled && ((e == null || supplier != null) && e != null && supplier == null)) {
            throw new AssertionError();
        }
        int nextBufferSize = getNextBufferSize(eArr);
        try {
            E[] eArr2 = (E[]) UnsafeRefArrayAccess.allocateRefArray(nextBufferSize);
            this.producerBuffer = eArr2;
            int i = (nextBufferSize - 2) << 1;
            this.producerMask = i;
            long jModifiedCalcCircularRefElementOffset = LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j2, j);
            UnsafeRefArrayAccess.soRefElement(eArr2, LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset(j2, i), e == null ? supplier.get() : e);
            UnsafeRefArrayAccess.soRefElement(eArr, nextArrayOffset(j), eArr2);
            long jAvailableInQueue = availableInQueue(j2, lvConsumerIndex());
            RangeUtil.checkPositive(jAvailableInQueue, "availableInQueue");
            soProducerLimit(j2 + Math.min(i, jAvailableInQueue));
            soProducerIndex(j2 + 2);
            UnsafeRefArrayAccess.soRefElement(eArr, jModifiedCalcCircularRefElementOffset, JUMP);
        } catch (OutOfMemoryError e2) {
            if (!$assertionsDisabled && lvProducerIndex() != j2 + 1) {
                throw new AssertionError();
            }
            soProducerIndex(j2);
            throw e2;
        }
    }
}

package io.netty.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler.class */
public abstract class Recycler<T> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Recycler.class);
    private static final Handle NOOP_HANDLE = new Handle() { // from class: io.netty.util.Recycler.1
        @Override // io.netty.util.internal.ObjectPool.Handle
        public void recycle(Object object) {
        }
    };
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
    private static final int OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
    private static final int DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD = 4096;
    private static final int DEFAULT_MAX_CAPACITY_PER_THREAD;
    private static final int INITIAL_CAPACITY;
    private static final int MAX_SHARED_CAPACITY_FACTOR;
    private static final int MAX_DELAYED_QUEUES_PER_THREAD;
    private static final int LINK_CAPACITY;
    private static final int RATIO;
    private static final int DELAYED_QUEUE_RATIO;
    private final int maxCapacityPerThread;
    private final int maxSharedCapacityFactor;
    private final int interval;
    private final int maxDelayedQueuesPerThread;
    private final int delayedQueueInterval;
    private final FastThreadLocal<Stack<T>> threadLocal;
    private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$Handle.class */
    public interface Handle<T> extends ObjectPool.Handle<T> {
    }

    protected abstract T newObject(Handle<T> handle);

    static {
        int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread", SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 4096));
        if (maxCapacityPerThread < 0) {
            maxCapacityPerThread = 4096;
        }
        DEFAULT_MAX_CAPACITY_PER_THREAD = maxCapacityPerThread;
        MAX_SHARED_CAPACITY_FACTOR = Math.max(2, SystemPropertyUtil.getInt("io.netty.recycler.maxSharedCapacityFactor", 2));
        MAX_DELAYED_QUEUES_PER_THREAD = Math.max(0, SystemPropertyUtil.getInt("io.netty.recycler.maxDelayedQueuesPerThread", NettyRuntime.availableProcessors() * 2));
        LINK_CAPACITY = MathUtil.safeFindNextPositivePowerOfTwo(Math.max(SystemPropertyUtil.getInt("io.netty.recycler.linkCapacity", 16), 16));
        RATIO = Math.max(0, SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
        DELAYED_QUEUE_RATIO = Math.max(0, SystemPropertyUtil.getInt("io.netty.recycler.delayedQueue.ratio", RATIO));
        if (logger.isDebugEnabled()) {
            if (DEFAULT_MAX_CAPACITY_PER_THREAD == 0) {
                logger.debug("-Dio.netty.recycler.maxCapacityPerThread: disabled");
                logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: disabled");
                logger.debug("-Dio.netty.recycler.linkCapacity: disabled");
                logger.debug("-Dio.netty.recycler.ratio: disabled");
                logger.debug("-Dio.netty.recycler.delayedQueue.ratio: disabled");
            } else {
                logger.debug("-Dio.netty.recycler.maxCapacityPerThread: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY_PER_THREAD));
                logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: {}", Integer.valueOf(MAX_SHARED_CAPACITY_FACTOR));
                logger.debug("-Dio.netty.recycler.linkCapacity: {}", Integer.valueOf(LINK_CAPACITY));
                logger.debug("-Dio.netty.recycler.ratio: {}", Integer.valueOf(RATIO));
                logger.debug("-Dio.netty.recycler.delayedQueue.ratio: {}", Integer.valueOf(DELAYED_QUEUE_RATIO));
            }
        }
        INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY_PER_THREAD, 256);
        DELAYED_RECYCLED = new FastThreadLocal<Map<Stack<?>, WeakOrderQueue>>() { // from class: io.netty.util.Recycler.3
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public Map<Stack<?>, WeakOrderQueue> initialValue() {
                return new WeakHashMap();
            }
        };
    }

    protected Recycler() {
        this(DEFAULT_MAX_CAPACITY_PER_THREAD);
    }

    protected Recycler(int maxCapacityPerThread) {
        this(maxCapacityPerThread, MAX_SHARED_CAPACITY_FACTOR);
    }

    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor) {
        this(maxCapacityPerThread, maxSharedCapacityFactor, RATIO, MAX_DELAYED_QUEUES_PER_THREAD);
    }

    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread) {
        this(maxCapacityPerThread, maxSharedCapacityFactor, ratio, maxDelayedQueuesPerThread, DELAYED_QUEUE_RATIO);
    }

    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread, int delayedQueueRatio) {
        this.threadLocal = new FastThreadLocal<Stack<T>>() { // from class: io.netty.util.Recycler.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public Stack<T> initialValue() {
                return new Stack<>(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacityPerThread, Recycler.this.maxSharedCapacityFactor, Recycler.this.interval, Recycler.this.maxDelayedQueuesPerThread, Recycler.this.delayedQueueInterval);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public void onRemoval(Stack<T> value) {
                if (value.threadRef.get() == Thread.currentThread() && Recycler.DELAYED_RECYCLED.isSet()) {
                    ((Map) Recycler.DELAYED_RECYCLED.get()).remove(value);
                }
            }
        };
        this.interval = Math.max(0, ratio);
        this.delayedQueueInterval = Math.max(0, delayedQueueRatio);
        if (maxCapacityPerThread <= 0) {
            this.maxCapacityPerThread = 0;
            this.maxSharedCapacityFactor = 1;
            this.maxDelayedQueuesPerThread = 0;
        } else {
            this.maxCapacityPerThread = maxCapacityPerThread;
            this.maxSharedCapacityFactor = Math.max(1, maxSharedCapacityFactor);
            this.maxDelayedQueuesPerThread = Math.max(0, maxDelayedQueuesPerThread);
        }
    }

    public final T get() {
        if (this.maxCapacityPerThread == 0) {
            return newObject(NOOP_HANDLE);
        }
        Stack<T> stack = this.threadLocal.get();
        DefaultHandle<T> defaultHandlePop = stack.pop();
        if (defaultHandlePop == null) {
            defaultHandlePop = stack.newHandle();
            defaultHandlePop.value = newObject(defaultHandlePop);
        }
        return (T) defaultHandlePop.value;
    }

    @Deprecated
    public final boolean recycle(T o, Handle<T> handle) {
        if (handle == NOOP_HANDLE) {
            return false;
        }
        DefaultHandle<T> h = (DefaultHandle) handle;
        if (h.stack.parent != this) {
            return false;
        }
        h.recycle(o);
        return true;
    }

    final int threadLocalCapacity() {
        return this.threadLocal.get().elements.length;
    }

    final int threadLocalSize() {
        return this.threadLocal.get().size;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$DefaultHandle.class */
    private static final class DefaultHandle<T> implements Handle<T> {
        int lastRecycledId;
        int recycleId;
        boolean hasBeenRecycled;
        Stack<?> stack;
        Object value;

        DefaultHandle(Stack<?> stack) {
            this.stack = stack;
        }

        @Override // io.netty.util.internal.ObjectPool.Handle
        public void recycle(Object object) {
            if (object != this.value) {
                throw new IllegalArgumentException("object does not belong to handle");
            }
            Stack<?> stack = this.stack;
            if (this.lastRecycledId != this.recycleId || stack == null) {
                throw new IllegalStateException("recycled already");
            }
            stack.push(this);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$WeakOrderQueue.class */
    private static final class WeakOrderQueue extends WeakReference<Thread> {
        static final WeakOrderQueue DUMMY;
        private final Head head;
        private Link tail;
        private WeakOrderQueue next;
        private final int id;
        private final int interval;
        private int handleRecycleCount;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Recycler.class.desiredAssertionStatus();
            DUMMY = new WeakOrderQueue();
        }

        /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$WeakOrderQueue$Link.class */
        static final class Link extends AtomicInteger {
            final DefaultHandle<?>[] elements = new DefaultHandle[Recycler.LINK_CAPACITY];
            int readIndex;
            Link next;

            Link() {
            }
        }

        /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$WeakOrderQueue$Head.class */
        private static final class Head {
            private final AtomicInteger availableSharedCapacity;
            Link link;

            Head(AtomicInteger availableSharedCapacity) {
                this.availableSharedCapacity = availableSharedCapacity;
            }

            void reclaimAllSpaceAndUnlink() {
                Link head = this.link;
                this.link = null;
                int reclaimSpace = 0;
                while (head != null) {
                    reclaimSpace += Recycler.LINK_CAPACITY;
                    Link next = head.next;
                    head.next = null;
                    head = next;
                }
                if (reclaimSpace > 0) {
                    reclaimSpace(reclaimSpace);
                }
            }

            private void reclaimSpace(int space) {
                this.availableSharedCapacity.addAndGet(space);
            }

            void relink(Link link) {
                reclaimSpace(Recycler.LINK_CAPACITY);
                this.link = link;
            }

            Link newLink() {
                if (reserveSpaceForLink(this.availableSharedCapacity)) {
                    return new Link();
                }
                return null;
            }

            static boolean reserveSpaceForLink(AtomicInteger availableSharedCapacity) {
                int available;
                do {
                    available = availableSharedCapacity.get();
                    if (available < Recycler.LINK_CAPACITY) {
                        return false;
                    }
                } while (!availableSharedCapacity.compareAndSet(available, available - Recycler.LINK_CAPACITY));
                return true;
            }
        }

        private WeakOrderQueue() {
            super(null);
            this.id = Recycler.ID_GENERATOR.getAndIncrement();
            this.head = new Head(null);
            this.interval = 0;
        }

        private WeakOrderQueue(Stack<?> stack, Thread thread) {
            super(thread);
            this.id = Recycler.ID_GENERATOR.getAndIncrement();
            this.tail = new Link();
            this.head = new Head(stack.availableSharedCapacity);
            this.head.link = this.tail;
            this.interval = ((Stack) stack).delayedQueueInterval;
            this.handleRecycleCount = this.interval;
        }

        static WeakOrderQueue newQueue(Stack<?> stack, Thread thread) {
            if (!Head.reserveSpaceForLink(stack.availableSharedCapacity)) {
                return null;
            }
            WeakOrderQueue queue = new WeakOrderQueue(stack, thread);
            stack.setHead(queue);
            return queue;
        }

        WeakOrderQueue getNext() {
            return this.next;
        }

        void setNext(WeakOrderQueue next) {
            if (!$assertionsDisabled && next == this) {
                throw new AssertionError();
            }
            this.next = next;
        }

        void reclaimAllSpaceAndUnlink() {
            this.head.reclaimAllSpaceAndUnlink();
            this.next = null;
        }

        void add(DefaultHandle<?> handle) {
            handle.lastRecycledId = this.id;
            if (this.handleRecycleCount < this.interval) {
                this.handleRecycleCount++;
                return;
            }
            this.handleRecycleCount = 0;
            Link tail = this.tail;
            int i = tail.get();
            int writeIndex = i;
            if (i == Recycler.LINK_CAPACITY) {
                Link link = this.head.newLink();
                if (link == null) {
                    return;
                }
                tail.next = link;
                tail = link;
                this.tail = link;
                writeIndex = tail.get();
            }
            tail.elements[writeIndex] = handle;
            handle.stack = null;
            tail.lazySet(writeIndex + 1);
        }

        boolean hasFinalData() {
            return this.tail.readIndex != this.tail.get();
        }

        boolean transfer(Stack<?> dst) {
            Link head = this.head.link;
            if (head != null) {
                if (head.readIndex == Recycler.LINK_CAPACITY) {
                    if (head.next == null) {
                        return false;
                    }
                    head = head.next;
                    this.head.relink(head);
                }
                int srcStart = head.readIndex;
                int srcEnd = head.get();
                int srcSize = srcEnd - srcStart;
                if (srcSize == 0) {
                    return false;
                }
                int dstSize = dst.size;
                int expectedCapacity = dstSize + srcSize;
                if (expectedCapacity > dst.elements.length) {
                    int actualCapacity = dst.increaseCapacity(expectedCapacity);
                    srcEnd = Math.min((srcStart + actualCapacity) - dstSize, srcEnd);
                }
                if (srcStart != srcEnd) {
                    DefaultHandle<?>[] srcElems = head.elements;
                    DefaultHandle[] dstElems = dst.elements;
                    int newDstSize = dstSize;
                    for (int i = srcStart; i < srcEnd; i++) {
                        DefaultHandle<?> element = srcElems[i];
                        if (element.recycleId == 0) {
                            element.recycleId = element.lastRecycledId;
                        } else if (element.recycleId != element.lastRecycledId) {
                            throw new IllegalStateException("recycled already");
                        }
                        srcElems[i] = null;
                        if (!dst.dropHandle(element)) {
                            element.stack = dst;
                            int i2 = newDstSize;
                            newDstSize++;
                            dstElems[i2] = element;
                        }
                    }
                    if (srcEnd == Recycler.LINK_CAPACITY && head.next != null) {
                        this.head.relink(head.next);
                    }
                    head.readIndex = srcEnd;
                    if (dst.size == newDstSize) {
                        return false;
                    }
                    dst.size = newDstSize;
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Recycler$Stack.class */
    private static final class Stack<T> {
        final Recycler<T> parent;
        final WeakReference<Thread> threadRef;
        final AtomicInteger availableSharedCapacity;
        private final int maxDelayedQueues;
        private final int maxCapacity;
        private final int interval;
        private final int delayedQueueInterval;
        DefaultHandle<?>[] elements;
        int size;
        private int handleRecycleCount;
        private WeakOrderQueue cursor;
        private WeakOrderQueue prev;
        private volatile WeakOrderQueue head;

        Stack(Recycler<T> parent, Thread thread, int maxCapacity, int maxSharedCapacityFactor, int interval, int maxDelayedQueues, int delayedQueueInterval) {
            this.parent = parent;
            this.threadRef = new WeakReference<>(thread);
            this.maxCapacity = maxCapacity;
            this.availableSharedCapacity = new AtomicInteger(Math.max(maxCapacity / maxSharedCapacityFactor, Recycler.LINK_CAPACITY));
            this.elements = new DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
            this.interval = interval;
            this.delayedQueueInterval = delayedQueueInterval;
            this.handleRecycleCount = interval;
            this.maxDelayedQueues = maxDelayedQueues;
        }

        synchronized void setHead(WeakOrderQueue queue) {
            queue.setNext(this.head);
            this.head = queue;
        }

        int increaseCapacity(int expectedCapacity) {
            int newCapacity = this.elements.length;
            int maxCapacity = this.maxCapacity;
            do {
                newCapacity <<= 1;
                if (newCapacity >= expectedCapacity) {
                    break;
                }
            } while (newCapacity < maxCapacity);
            int newCapacity2 = Math.min(newCapacity, maxCapacity);
            if (newCapacity2 != this.elements.length) {
                this.elements = (DefaultHandle[]) Arrays.copyOf(this.elements, newCapacity2);
            }
            return newCapacity2;
        }

        DefaultHandle<T> pop() {
            int size = this.size;
            if (size == 0) {
                if (!scavenge()) {
                    return null;
                }
                size = this.size;
                if (size <= 0) {
                    return null;
                }
            }
            int size2 = size - 1;
            DefaultHandle ret = this.elements[size2];
            this.elements[size2] = null;
            this.size = size2;
            if (ret.lastRecycledId != ret.recycleId) {
                throw new IllegalStateException("recycled multiple times");
            }
            ret.recycleId = 0;
            ret.lastRecycledId = 0;
            return ret;
        }

        private boolean scavenge() {
            if (scavengeSome()) {
                return true;
            }
            this.prev = null;
            this.cursor = this.head;
            return false;
        }

        private boolean scavengeSome() {
            WeakOrderQueue prev;
            WeakOrderQueue cursor = this.cursor;
            if (cursor == null) {
                prev = null;
                cursor = this.head;
                if (cursor == null) {
                    return false;
                }
            } else {
                prev = this.prev;
            }
            boolean success = false;
            while (true) {
                if (cursor.transfer(this)) {
                    success = true;
                    break;
                }
                WeakOrderQueue next = cursor.getNext();
                if (cursor.get() == null) {
                    if (cursor.hasFinalData()) {
                        while (cursor.transfer(this)) {
                            success = true;
                        }
                    }
                    if (prev != null) {
                        cursor.reclaimAllSpaceAndUnlink();
                        prev.setNext(next);
                    }
                } else {
                    prev = cursor;
                }
                cursor = next;
                if (cursor == null || success) {
                    break;
                }
            }
            this.prev = prev;
            this.cursor = cursor;
            return success;
        }

        void push(DefaultHandle<?> item) {
            Thread currentThread = Thread.currentThread();
            if (this.threadRef.get() == currentThread) {
                pushNow(item);
            } else {
                pushLater(item, currentThread);
            }
        }

        private void pushNow(DefaultHandle<?> item) {
            if ((item.recycleId | item.lastRecycledId) == 0) {
                int i = Recycler.OWN_THREAD_ID;
                item.lastRecycledId = i;
                item.recycleId = i;
                int size = this.size;
                if (size >= this.maxCapacity || dropHandle(item)) {
                    return;
                }
                if (size == this.elements.length) {
                    this.elements = (DefaultHandle[]) Arrays.copyOf(this.elements, Math.min(size << 1, this.maxCapacity));
                }
                this.elements[size] = item;
                this.size = size + 1;
                return;
            }
            throw new IllegalStateException("recycled already");
        }

        private void pushLater(DefaultHandle<?> item, Thread thread) {
            if (this.maxDelayedQueues != 0) {
                Map<Stack<?>, WeakOrderQueue> delayedRecycled = (Map) Recycler.DELAYED_RECYCLED.get();
                WeakOrderQueue queue = delayedRecycled.get(this);
                if (queue == null) {
                    if (delayedRecycled.size() >= this.maxDelayedQueues) {
                        delayedRecycled.put(this, WeakOrderQueue.DUMMY);
                        return;
                    }
                    WeakOrderQueue weakOrderQueueNewWeakOrderQueue = newWeakOrderQueue(thread);
                    queue = weakOrderQueueNewWeakOrderQueue;
                    if (weakOrderQueueNewWeakOrderQueue == null) {
                        return;
                    } else {
                        delayedRecycled.put(this, queue);
                    }
                } else if (queue == WeakOrderQueue.DUMMY) {
                    return;
                }
                queue.add(item);
            }
        }

        private WeakOrderQueue newWeakOrderQueue(Thread thread) {
            return WeakOrderQueue.newQueue(this, thread);
        }

        boolean dropHandle(DefaultHandle<?> handle) {
            if (!handle.hasBeenRecycled) {
                if (this.handleRecycleCount < this.interval) {
                    this.handleRecycleCount++;
                    return true;
                }
                this.handleRecycleCount = 0;
                handle.hasBeenRecycled = true;
                return false;
            }
            return false;
        }

        DefaultHandle<T> newHandle() {
            return new DefaultHandle<>(this);
        }
    }
}

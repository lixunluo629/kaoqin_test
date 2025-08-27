package io.netty.channel.kqueue;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.IovArray;
import io.netty.util.IntSupplier;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/kqueue/KQueueEventLoop.class */
final class KQueueEventLoop extends SingleThreadEventLoop {
    private static final InternalLogger logger;
    private static final AtomicIntegerFieldUpdater<KQueueEventLoop> WAKEN_UP_UPDATER;
    private static final int KQUEUE_WAKE_UP_IDENT = 0;
    private final boolean allowGrowing;
    private final FileDescriptor kqueueFd;
    private final KQueueEventArray changeList;
    private final KQueueEventArray eventList;
    private final SelectStrategy selectStrategy;
    private final IovArray iovArray;
    private final IntSupplier selectNowSupplier;
    private final IntObjectMap<AbstractKQueueChannel> channels;
    private volatile int wakenUp;
    private volatile int ioRatio;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !KQueueEventLoop.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) KQueueEventLoop.class);
        WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(KQueueEventLoop.class, "wakenUp");
        KQueue.ensureAvailability();
    }

    KQueueEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory queueFactory) {
        super(parent, executor, false, newTaskQueue(queueFactory), newTaskQueue(queueFactory), rejectedExecutionHandler);
        this.iovArray = new IovArray();
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.kqueue.KQueueEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return KQueueEventLoop.this.kqueueWaitNow();
            }
        };
        this.channels = new IntObjectHashMap(4096);
        this.ioRatio = 50;
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "strategy");
        this.kqueueFd = Native.newKQueue();
        if (maxEvents == 0) {
            this.allowGrowing = true;
            maxEvents = 4096;
        } else {
            this.allowGrowing = false;
        }
        this.changeList = new KQueueEventArray(maxEvents);
        this.eventList = new KQueueEventArray(maxEvents);
        int result = Native.keventAddUserEvent(this.kqueueFd.intValue(), 0);
        if (result < 0) {
            cleanup();
            throw new IllegalStateException("kevent failed to add user event with errno: " + (-result));
        }
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    void add(AbstractKQueueChannel ch2) {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        AbstractKQueueChannel old = this.channels.put(ch2.fd().intValue(), (int) ch2);
        if (!$assertionsDisabled && old != null && old.isOpen()) {
            throw new AssertionError();
        }
    }

    void evSet(AbstractKQueueChannel ch2, short filter, short flags, int fflags) {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        this.changeList.evSet(ch2, filter, flags, fflags);
    }

    void remove(AbstractKQueueChannel ch2) throws Exception {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch2.fd().intValue();
        AbstractKQueueChannel old = this.channels.remove(fd);
        if (old != null && old != ch2) {
            this.channels.put(fd, (int) old);
            if (!$assertionsDisabled && ch2.isOpen()) {
                throw new AssertionError();
            }
            return;
        }
        if (ch2.isOpen()) {
            ch2.unregisterFilters();
        }
    }

    IovArray cleanArray() {
        this.iovArray.clear();
        return this.iovArray;
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void wakeup(boolean inEventLoop) {
        if (!inEventLoop && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            wakeup();
        }
    }

    private void wakeup() {
        Native.keventTriggerUserEvent(this.kqueueFd.intValue(), 0);
    }

    private int kqueueWait(boolean oldWakeup) throws IOException {
        if (oldWakeup && hasTasks()) {
            return kqueueWaitNow();
        }
        long totalDelay = delayNanos(System.nanoTime());
        int delaySeconds = (int) Math.min(totalDelay / 1000000000, 2147483647L);
        return kqueueWait(delaySeconds, (int) Math.min(totalDelay - (delaySeconds * 1000000000), 2147483647L));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int kqueueWaitNow() throws IOException {
        return kqueueWait(0, 0);
    }

    private int kqueueWait(int timeoutSec, int timeoutNs) throws IOException {
        int numEvents = Native.keventWait(this.kqueueFd.intValue(), this.changeList, this.eventList, timeoutSec, timeoutNs);
        this.changeList.clear();
        return numEvents;
    }

    private void processReady(int ready) {
        for (int i = 0; i < ready; i++) {
            short filter = this.eventList.filter(i);
            short flags = this.eventList.flags(i);
            int fd = this.eventList.fd(i);
            if (filter == Native.EVFILT_USER || (flags & Native.EV_ERROR) != 0) {
                if (!$assertionsDisabled && filter == Native.EVFILT_USER && (filter != Native.EVFILT_USER || fd != 0)) {
                    throw new AssertionError();
                }
            } else {
                AbstractKQueueChannel channel = this.channels.get(fd);
                if (channel == null) {
                    logger.warn("events[{}]=[{}, {}] had no channel!", Integer.valueOf(i), Integer.valueOf(this.eventList.fd(i)), Short.valueOf(filter));
                } else {
                    AbstractKQueueChannel.AbstractKQueueUnsafe unsafe = (AbstractKQueueChannel.AbstractKQueueUnsafe) channel.unsafe();
                    if (filter == Native.EVFILT_WRITE) {
                        unsafe.writeReady();
                    } else if (filter == Native.EVFILT_READ) {
                        unsafe.readReady(this.eventList.data(i));
                    } else if (filter == Native.EVFILT_SOCK && (this.eventList.fflags(i) & Native.NOTE_RDHUP) != 0) {
                        unsafe.readEOF();
                    }
                    if ((flags & Native.EV_EOF) != 0) {
                        unsafe.readEOF();
                    }
                }
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:56|2|3|12|(5:14|(2:52|16)|17|18|22)(5:23|24|(2:54|26)|27|28)|33|(1:37)|38|50|41|62|(3:60|43|(3:61|45|49)(1:65))(1:64)|63) */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f5, code lost:
    
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00f6, code lost:
    
        handleLoopException(r7);
     */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() throws java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 254
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.KQueueEventLoop.run():void");
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return newTaskQueue0(maxPendingTasks);
    }

    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxPendingTasks);
    }

    public int getIoRatio() {
        return this.ioRatio;
    }

    public void setIoRatio(int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = ioRatio;
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return this.channels.size();
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() {
        try {
            try {
                this.kqueueFd.close();
            } catch (IOException e) {
                logger.warn("Failed to close the kqueue fd.", (Throwable) e);
            }
        } finally {
            this.changeList.free();
            this.eventList.free();
        }
    }

    private void closeAll() {
        try {
            kqueueWaitNow();
        } catch (IOException e) {
        }
        AbstractKQueueChannel[] localChannels = (AbstractKQueueChannel[]) this.channels.values().toArray(new AbstractKQueueChannel[0]);
        for (AbstractKQueueChannel ch2 : localChannels) {
            ch2.unsafe().close(ch2.unsafe().voidPromise());
        }
    }

    private static void handleLoopException(Throwable t) throws InterruptedException {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }
}

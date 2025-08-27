package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.epoll.AbstractEpollChannel;
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
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/EpollEventLoop.class */
class EpollEventLoop extends SingleThreadEventLoop {
    private static final InternalLogger logger;
    private final FileDescriptor epollFd;
    private final FileDescriptor eventFd;
    private final FileDescriptor timerFd;
    private final IntObjectMap<AbstractEpollChannel> channels;
    private final boolean allowGrowing;
    private final EpollEventArray events;
    private IovArray iovArray;
    private NativeDatagramPacketArray datagramPacketArray;
    private final SelectStrategy selectStrategy;
    private final IntSupplier selectNowSupplier;
    private static final long AWAKE = -1;
    private static final long NONE = Long.MAX_VALUE;
    private final AtomicLong nextWakeupNanos;
    private boolean pendingWakeup;
    private volatile int ioRatio;
    private static final long MAX_SCHEDULED_TIMERFD_NS = 999999999;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EpollEventLoop.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) EpollEventLoop.class);
        Epoll.ensureAvailability();
    }

    EpollEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory queueFactory) {
        super(parent, executor, false, newTaskQueue(queueFactory), newTaskQueue(queueFactory), rejectedExecutionHandler);
        this.channels = new IntObjectHashMap(4096);
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.epoll.EpollEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return EpollEventLoop.this.epollWaitNow();
            }
        };
        this.nextWakeupNanos = new AtomicLong(-1L);
        this.ioRatio = 50;
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "strategy");
        if (maxEvents == 0) {
            this.allowGrowing = true;
            this.events = new EpollEventArray(4096);
        } else {
            this.allowGrowing = false;
            this.events = new EpollEventArray(maxEvents);
        }
        FileDescriptor epollFd = null;
        FileDescriptor eventFd = null;
        FileDescriptor timerFd = null;
        try {
            FileDescriptor fileDescriptorNewEpollCreate = Native.newEpollCreate();
            epollFd = fileDescriptorNewEpollCreate;
            this.epollFd = fileDescriptorNewEpollCreate;
            FileDescriptor fileDescriptorNewEventFd = Native.newEventFd();
            eventFd = fileDescriptorNewEventFd;
            this.eventFd = fileDescriptorNewEventFd;
            try {
                Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
                FileDescriptor fileDescriptorNewTimerFd = Native.newTimerFd();
                timerFd = fileDescriptorNewTimerFd;
                this.timerFd = fileDescriptorNewTimerFd;
                try {
                    Native.epollCtlAdd(epollFd.intValue(), timerFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
                    if (1 == 0) {
                        if (epollFd != null) {
                            try {
                                epollFd.close();
                            } catch (Exception e) {
                            }
                        }
                        if (eventFd != null) {
                            try {
                                eventFd.close();
                            } catch (Exception e2) {
                            }
                        }
                        if (timerFd != null) {
                            try {
                                timerFd.close();
                            } catch (Exception e3) {
                            }
                        }
                    }
                } catch (IOException e4) {
                    throw new IllegalStateException("Unable to add timerFd filedescriptor to epoll", e4);
                }
            } catch (IOException e5) {
                throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", e5);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                if (epollFd != null) {
                    try {
                        epollFd.close();
                    } catch (Exception e6) {
                    }
                }
                if (eventFd != null) {
                    try {
                        eventFd.close();
                    } catch (Exception e7) {
                    }
                }
                if (timerFd != null) {
                    try {
                        timerFd.close();
                    } catch (Exception e8) {
                    }
                }
            }
            throw th;
        }
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    IovArray cleanIovArray() {
        if (this.iovArray == null) {
            this.iovArray = new IovArray();
        } else {
            this.iovArray.clear();
        }
        return this.iovArray;
    }

    NativeDatagramPacketArray cleanDatagramPacketArray() {
        if (this.datagramPacketArray == null) {
            this.datagramPacketArray = new NativeDatagramPacketArray();
        } else {
            this.datagramPacketArray.clear();
        }
        return this.datagramPacketArray;
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void wakeup(boolean inEventLoop) {
        if (!inEventLoop && this.nextWakeupNanos.getAndSet(-1L) != -1) {
            Native.eventFdWrite(this.eventFd.intValue(), 1L);
        }
    }

    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    protected boolean beforeScheduledTaskSubmitted(long deadlineNanos) {
        return deadlineNanos < this.nextWakeupNanos.get();
    }

    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    protected boolean afterScheduledTaskSubmitted(long deadlineNanos) {
        return deadlineNanos < this.nextWakeupNanos.get();
    }

    void add(AbstractEpollChannel ch2) throws IOException {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch2.socket.intValue();
        Native.epollCtlAdd(this.epollFd.intValue(), fd, ch2.flags);
        AbstractEpollChannel old = this.channels.put(fd, (int) ch2);
        if (!$assertionsDisabled && old != null && old.isOpen()) {
            throw new AssertionError();
        }
    }

    void modify(AbstractEpollChannel ch2) throws IOException {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        Native.epollCtlMod(this.epollFd.intValue(), ch2.socket.intValue(), ch2.flags);
    }

    void remove(AbstractEpollChannel ch2) throws IOException {
        if (!$assertionsDisabled && !inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch2.socket.intValue();
        AbstractEpollChannel old = this.channels.remove(fd);
        if (old != null && old != ch2) {
            this.channels.put(fd, (int) old);
            if (!$assertionsDisabled && ch2.isOpen()) {
                throw new AssertionError();
            }
            return;
        }
        if (ch2.isOpen()) {
            Native.epollCtlDel(this.epollFd.intValue(), fd);
        }
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

    private int epollWait(long deadlineNanos) throws IOException {
        if (deadlineNanos == Long.MAX_VALUE) {
            return Native.epollWait(this.epollFd, this.events, this.timerFd, Integer.MAX_VALUE, 0);
        }
        long totalDelay = deadlineToDelayNanos(deadlineNanos);
        int delaySeconds = (int) Math.min(totalDelay / 1000000000, 2147483647L);
        int delayNanos = (int) Math.min(totalDelay - (delaySeconds * 1000000000), MAX_SCHEDULED_TIMERFD_NS);
        return Native.epollWait(this.epollFd, this.events, this.timerFd, delaySeconds, delayNanos);
    }

    private int epollWaitNoTimerChange() throws IOException {
        return Native.epollWait(this.epollFd, this.events, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int epollWaitNow() throws IOException {
        return Native.epollWait(this.epollFd, this.events, true);
    }

    private int epollBusyWait() throws IOException {
        return Native.epollBusyWait(this.epollFd, this.events);
    }

    private int epollWaitTimeboxed() throws IOException {
        return Native.epollWait(this.epollFd, this.events, 1000);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0004 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0067 A[Catch: Throwable -> 0x01a4, PHI: r9
  0x0067: PHI (r9v3 'strategy' int) = (r9v2 'strategy' int), (r9v7 'strategy' int) binds: [B:8:0x003f, B:13:0x0061] A[DONT_GENERATE, DONT_INLINE], TryCatch #3 {Throwable -> 0x01a4, blocks: (B:3:0x0004, B:4:0x0017, B:6:0x0033, B:7:0x003b, B:9:0x0042, B:12:0x004e, B:15:0x0067, B:18:0x007b, B:19:0x0084, B:23:0x0092, B:24:0x009a, B:26:0x00a5, B:28:0x00b2, B:30:0x00c3, B:33:0x00ce, B:35:0x00db, B:39:0x00f3, B:37:0x00ec, B:40:0x00f4, B:44:0x0105, B:48:0x0116, B:68:0x0188, B:70:0x018f, B:72:0x019a, B:56:0x012e, B:57:0x0133, B:60:0x0143, B:61:0x0146, B:63:0x0162, B:64:0x0165, B:65:0x017e, B:67:0x0182, B:51:0x0120, B:52:0x0126), top: B:91:0x0004, inners: #0, #2, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x018f A[Catch: Throwable -> 0x01a4, TryCatch #3 {Throwable -> 0x01a4, blocks: (B:3:0x0004, B:4:0x0017, B:6:0x0033, B:7:0x003b, B:9:0x0042, B:12:0x004e, B:15:0x0067, B:18:0x007b, B:19:0x0084, B:23:0x0092, B:24:0x009a, B:26:0x00a5, B:28:0x00b2, B:30:0x00c3, B:33:0x00ce, B:35:0x00db, B:39:0x00f3, B:37:0x00ec, B:40:0x00f4, B:44:0x0105, B:48:0x0116, B:68:0x0188, B:70:0x018f, B:72:0x019a, B:56:0x012e, B:57:0x0133, B:60:0x0143, B:61:0x0146, B:63:0x0162, B:64:0x0165, B:65:0x017e, B:67:0x0182, B:51:0x0120, B:52:0x0126), top: B:91:0x0004, inners: #0, #2, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01b1 A[SYNTHETIC] */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() throws java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 460
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.EpollEventLoop.run():void");
    }

    void handleLoopException(Throwable t) throws InterruptedException {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }

    private void closeAll() {
        AbstractEpollChannel[] localChannels = (AbstractEpollChannel[]) this.channels.values().toArray(new AbstractEpollChannel[0]);
        for (AbstractEpollChannel ch2 : localChannels) {
            ch2.unsafe().close(ch2.unsafe().voidPromise());
        }
    }

    private boolean processReady(EpollEventArray events, int ready) {
        boolean timerFired = false;
        for (int i = 0; i < ready; i++) {
            int fd = events.fd(i);
            if (fd == this.eventFd.intValue()) {
                this.pendingWakeup = false;
            } else if (fd == this.timerFd.intValue()) {
                timerFired = true;
            } else {
                long ev = events.events(i);
                AbstractEpollChannel ch2 = this.channels.get(fd);
                if (ch2 != null) {
                    AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe) ch2.unsafe();
                    if ((ev & (Native.EPOLLERR | Native.EPOLLOUT)) != 0) {
                        unsafe.epollOutReady();
                    }
                    if ((ev & (Native.EPOLLERR | Native.EPOLLIN)) != 0) {
                        unsafe.epollInReady();
                    }
                    if ((ev & Native.EPOLLRDHUP) != 0) {
                        unsafe.epollRdHupReady();
                    }
                } else {
                    try {
                        Native.epollCtlDel(this.epollFd.intValue(), fd);
                    } catch (IOException e) {
                    }
                }
            }
        }
        return timerFired;
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() {
        int count;
        while (this.pendingWakeup) {
            try {
                try {
                    count = epollWaitTimeboxed();
                } catch (IOException e) {
                }
                if (count == 0) {
                    break;
                }
                int i = 0;
                while (true) {
                    if (i >= count) {
                        break;
                    }
                    if (this.events.fd(i) != this.eventFd.intValue()) {
                        i++;
                    } else {
                        this.pendingWakeup = false;
                        break;
                    }
                }
            } finally {
                if (this.iovArray != null) {
                    this.iovArray.release();
                    this.iovArray = null;
                }
                if (this.datagramPacketArray != null) {
                    this.datagramPacketArray.release();
                    this.datagramPacketArray = null;
                }
                this.events.free();
            }
        }
        try {
            this.eventFd.close();
        } catch (IOException e2) {
            logger.warn("Failed to close the event fd.", (Throwable) e2);
        }
        try {
            this.timerFd.close();
        } catch (IOException e3) {
            logger.warn("Failed to close the timer fd.", (Throwable) e3);
        }
        try {
            this.epollFd.close();
        } catch (IOException e4) {
            logger.warn("Failed to close the epoll fd.", (Throwable) e4);
        }
    }
}

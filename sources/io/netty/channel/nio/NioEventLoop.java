package io.netty.channel.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopException;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.util.IntSupplier;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReflectionUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.tomcat.jni.Time;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/nio/NioEventLoop.class */
public final class NioEventLoop extends SingleThreadEventLoop {
    private static final int CLEANUP_INTERVAL = 256;
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    private final IntSupplier selectNowSupplier;
    private Selector selector;
    private Selector unwrappedSelector;
    private SelectedSelectionKeySet selectedKeys;
    private final SelectorProvider provider;
    private static final long AWAKE = -1;
    private static final long NONE = Long.MAX_VALUE;
    private final AtomicLong nextWakeupNanos;
    private final SelectStrategy selectStrategy;
    private volatile int ioRatio;
    private int cancelledKeys;
    private boolean needsToSelectAgain;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NioEventLoop.class);
    private static final boolean DISABLE_KEY_SET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);

    static {
        String bugLevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
        if (bugLevel == null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: io.netty.channel.nio.NioEventLoop.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        System.setProperty("sun.nio.ch.bugLevel", "");
                        return null;
                    }
                });
            } catch (SecurityException e) {
                logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", (Throwable) e);
            }
        }
        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (selectorAutoRebuildThreshold < 3) {
            selectorAutoRebuildThreshold = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEY_SET_OPTIMIZATION));
            logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
        }
    }

    NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory queueFactory) throws IOException {
        super(parent, executor, false, newTaskQueue(queueFactory), newTaskQueue(queueFactory), rejectedExecutionHandler);
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.nio.NioEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return NioEventLoop.this.selectNow();
            }
        };
        this.nextWakeupNanos = new AtomicLong(-1L);
        this.ioRatio = 50;
        this.provider = (SelectorProvider) ObjectUtil.checkNotNull(selectorProvider, "selectorProvider");
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "selectStrategy");
        SelectorTuple selectorTuple = openSelector();
        this.selector = selectorTuple.selector;
        this.unwrappedSelector = selectorTuple.unwrappedSelector;
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/nio/NioEventLoop$SelectorTuple.class */
    private static final class SelectorTuple {
        final Selector unwrappedSelector;
        final Selector selector;

        SelectorTuple(Selector unwrappedSelector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = unwrappedSelector;
        }

        SelectorTuple(Selector unwrappedSelector, Selector selector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = selector;
        }
    }

    private SelectorTuple openSelector() throws IOException {
        try {
            final Selector unwrappedSelector = this.provider.openSelector();
            if (DISABLE_KEY_SET_OPTIMIZATION) {
                return new SelectorTuple(unwrappedSelector);
            }
            Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.channel.nio.NioEventLoop.3
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        return Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (!(maybeSelectorImplClass instanceof Class) || !((Class) maybeSelectorImplClass).isAssignableFrom(unwrappedSelector.getClass())) {
                if (maybeSelectorImplClass instanceof Throwable) {
                    Throwable t = (Throwable) maybeSelectorImplClass;
                    logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, t);
                }
                return new SelectorTuple(unwrappedSelector);
            }
            final Class<?> selectorImplClass = (Class) maybeSelectorImplClass;
            final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
            Object maybeException = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.channel.nio.NioEventLoop.4
                @Override // java.security.PrivilegedAction
                public Object run() throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
                    try {
                        Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                        Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
                        if (PlatformDependent.javaVersion() >= 9 && PlatformDependent.hasUnsafe()) {
                            long selectedKeysFieldOffset = PlatformDependent.objectFieldOffset(selectedKeysField);
                            long publicSelectedKeysFieldOffset = PlatformDependent.objectFieldOffset(publicSelectedKeysField);
                            if (selectedKeysFieldOffset != -1 && publicSelectedKeysFieldOffset != -1) {
                                PlatformDependent.putObject(unwrappedSelector, selectedKeysFieldOffset, selectedKeySet);
                                PlatformDependent.putObject(unwrappedSelector, publicSelectedKeysFieldOffset, selectedKeySet);
                                return null;
                            }
                        }
                        Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
                        if (cause != null) {
                            return cause;
                        }
                        Throwable cause2 = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
                        if (cause2 != null) {
                            return cause2;
                        }
                        selectedKeysField.set(unwrappedSelector, selectedKeySet);
                        publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
                        return null;
                    } catch (IllegalAccessException e) {
                        return e;
                    } catch (NoSuchFieldException e2) {
                        return e2;
                    }
                }
            });
            if (maybeException instanceof Exception) {
                this.selectedKeys = null;
                Exception e = (Exception) maybeException;
                logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, e);
                return new SelectorTuple(unwrappedSelector);
            }
            this.selectedKeys = selectedKeySet;
            logger.trace("instrumented a special java.util.Set into: {}", unwrappedSelector);
            return new SelectorTuple(unwrappedSelector, new SelectedSelectionKeySetSelector(unwrappedSelector, selectedKeySet));
        } catch (IOException e2) {
            throw new ChannelException("failed to open a new selector", e2);
        }
    }

    public SelectorProvider selectorProvider() {
        return this.provider;
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return newTaskQueue0(maxPendingTasks);
    }

    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxPendingTasks);
    }

    public void register(final SelectableChannel ch2, final int interestOps, final NioTask<?> task) throws ClosedChannelException {
        ObjectUtil.checkNotNull(ch2, "ch");
        if (interestOps == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if ((interestOps & (ch2.validOps() ^ (-1))) != 0) {
            throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch2.validOps() + ')');
        }
        ObjectUtil.checkNotNull(task, "task");
        if (isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        if (inEventLoop()) {
            register0(ch2, interestOps, task);
            return;
        }
        try {
            submit(new Runnable() { // from class: io.netty.channel.nio.NioEventLoop.5
                @Override // java.lang.Runnable
                public void run() throws ClosedChannelException {
                    NioEventLoop.this.register0(ch2, interestOps, task);
                }
            }).sync2();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void register0(SelectableChannel ch2, int interestOps, NioTask<?> task) throws ClosedChannelException {
        try {
            ch2.register(this.unwrappedSelector, interestOps, task);
        } catch (Exception e) {
            throw new EventLoopException("failed to register a channel", e);
        }
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

    public void rebuildSelector() throws ClosedChannelException {
        if (!inEventLoop()) {
            execute(new Runnable() { // from class: io.netty.channel.nio.NioEventLoop.6
                @Override // java.lang.Runnable
                public void run() throws ClosedChannelException {
                    NioEventLoop.this.rebuildSelector0();
                }
            });
        } else {
            rebuildSelector0();
        }
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return this.selector.keys().size() - this.cancelledKeys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rebuildSelector0() throws ClosedChannelException {
        Selector oldSelector = this.selector;
        if (oldSelector == null) {
            return;
        }
        try {
            SelectorTuple newSelectorTuple = openSelector();
            int nChannels = 0;
            for (SelectionKey key : oldSelector.keys()) {
                Object a = key.attachment();
                try {
                } catch (Exception e) {
                    logger.warn("Failed to re-register a Channel to the new Selector.", (Throwable) e);
                    if (a instanceof AbstractNioChannel) {
                        AbstractNioChannel ch2 = (AbstractNioChannel) a;
                        ch2.unsafe().close(ch2.unsafe().voidPromise());
                    } else {
                        NioTask<SelectableChannel> task = (NioTask) a;
                        invokeChannelUnregistered(task, key, e);
                    }
                }
                if (key.isValid() && key.channel().keyFor(newSelectorTuple.unwrappedSelector) == null) {
                    int interestOps = key.interestOps();
                    key.cancel();
                    SelectionKey newKey = key.channel().register(newSelectorTuple.unwrappedSelector, interestOps, a);
                    if (a instanceof AbstractNioChannel) {
                        ((AbstractNioChannel) a).selectionKey = newKey;
                    }
                    nChannels++;
                }
            }
            this.selector = newSelectorTuple.selector;
            this.unwrappedSelector = newSelectorTuple.unwrappedSelector;
            try {
                oldSelector.close();
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to close the old Selector.", t);
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
            }
        } catch (Exception e2) {
            logger.warn("Failed to create a new Selector.", (Throwable) e2);
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:24:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x018c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0002 A[SYNTHETIC] */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() throws java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 419
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.nio.NioEventLoop.run():void");
    }

    private boolean unexpectedSelectorWakeup(int selectCnt) throws ClosedChannelException {
        if (Thread.interrupted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
                return true;
            }
            return true;
        }
        if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
            logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", Integer.valueOf(selectCnt), this.selector);
            rebuildSelector();
            return true;
        }
        return false;
    }

    private static void handleLoopException(Throwable t) throws InterruptedException {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }

    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            processSelectedKeysOptimized();
        } else {
            processSelectedKeysPlain(this.selector.selectedKeys());
        }
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() throws IOException {
        try {
            this.selector.close();
        } catch (IOException e) {
            logger.warn("Failed to close a selector.", (Throwable) e);
        }
    }

    void cancel(SelectionKey key) {
        key.cancel();
        this.cancelledKeys++;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }

    private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
        if (selectedKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> i = selectedKeys.iterator();
        while (true) {
            SelectionKey k = i.next();
            Object a = k.attachment();
            i.remove();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel) a);
            } else {
                NioTask<SelectableChannel> task = (NioTask) a;
                processSelectedKey(k, task);
            }
            if (i.hasNext()) {
                if (this.needsToSelectAgain) {
                    selectAgain();
                    Set<SelectionKey> selectedKeys2 = this.selector.selectedKeys();
                    if (!selectedKeys2.isEmpty()) {
                        i = selectedKeys2.iterator();
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
        }
    }

    private void processSelectedKeysOptimized() {
        int i = 0;
        while (i < this.selectedKeys.size) {
            SelectionKey k = this.selectedKeys.keys[i];
            this.selectedKeys.keys[i] = null;
            Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel) a);
            } else {
                NioTask<SelectableChannel> task = (NioTask) a;
                processSelectedKey(k, task);
            }
            if (this.needsToSelectAgain) {
                this.selectedKeys.reset(i + 1);
                selectAgain();
                i = -1;
            }
            i++;
        }
    }

    private void processSelectedKey(SelectionKey k, AbstractNioChannel ch2) {
        AbstractNioChannel.NioUnsafe unsafe = ch2.unsafe();
        if (!k.isValid()) {
            try {
                EventLoop eventLoop = ch2.eventLoop();
                if (eventLoop == this) {
                    unsafe.close(unsafe.voidPromise());
                    return;
                }
                return;
            } catch (Throwable th) {
                return;
            }
        }
        try {
            int readyOps = k.readyOps();
            if ((readyOps & 8) != 0) {
                int ops = k.interestOps();
                k.interestOps(ops & (-9));
                unsafe.finishConnect();
            }
            if ((readyOps & 4) != 0) {
                ch2.unsafe().forceFlush();
            }
            if ((readyOps & 17) != 0 || readyOps == 0) {
                unsafe.read();
            }
        } catch (CancelledKeyException e) {
            unsafe.close(unsafe.voidPromise());
        }
    }

    private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
        int state = 0;
        try {
            try {
                task.channelReady(k.channel(), k);
                state = 1;
                switch (1) {
                    case 0:
                        k.cancel();
                        invokeChannelUnregistered(task, k, null);
                        return;
                    case 1:
                        if (!k.isValid()) {
                            invokeChannelUnregistered(task, k, null);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                k.cancel();
                invokeChannelUnregistered(task, k, e);
                state = 2;
                switch (2) {
                    case 0:
                        k.cancel();
                        invokeChannelUnregistered(task, k, null);
                        return;
                    case 1:
                        if (!k.isValid()) {
                            invokeChannelUnregistered(task, k, null);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        } catch (Throwable th) {
            switch (state) {
                case 0:
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    break;
                case 1:
                    if (!k.isValid()) {
                        invokeChannelUnregistered(task, k, null);
                        break;
                    }
                    break;
            }
            throw th;
        }
    }

    private void closeAll() {
        selectAgain();
        Set<SelectionKey> keys = this.selector.keys();
        Collection<AbstractNioChannel> channels = new ArrayList<>(keys.size());
        for (SelectionKey k : keys) {
            Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                channels.add((AbstractNioChannel) a);
            } else {
                k.cancel();
                NioTask<SelectableChannel> task = (NioTask) a;
                invokeChannelUnregistered(task, k, null);
            }
        }
        for (AbstractNioChannel ch2 : channels) {
            ch2.unsafe().close(ch2.unsafe().voidPromise());
        }
    }

    private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
        try {
            task.channelUnregistered(k.channel(), cause);
        } catch (Exception e) {
            logger.warn("Unexpected exception while running NioTask.channelUnregistered()", (Throwable) e);
        }
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void wakeup(boolean inEventLoop) {
        if (!inEventLoop && this.nextWakeupNanos.getAndSet(-1L) != -1) {
            this.selector.wakeup();
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

    Selector unwrappedSelector() {
        return this.unwrappedSelector;
    }

    int selectNow() throws IOException {
        return this.selector.selectNow();
    }

    private int select(long deadlineNanos) throws IOException {
        if (deadlineNanos == Long.MAX_VALUE) {
            return this.selector.select();
        }
        long timeoutMillis = deadlineToDelayNanos(deadlineNanos + 995000) / Time.APR_USEC_PER_SEC;
        return timeoutMillis <= 0 ? this.selector.selectNow() : this.selector.select(timeoutMillis);
    }

    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        } catch (Throwable t) {
            logger.warn("Failed to update SelectionKeys.", t);
        }
    }
}

package io.netty.channel.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/FixedChannelPool.class */
public class FixedChannelPool extends SimpleChannelPool {
    private final EventExecutor executor;
    private final long acquireTimeoutNanos;
    private final Runnable timeoutTask;
    private final Queue<AcquireTask> pendingAcquireQueue;
    private final int maxConnections;
    private final int maxPendingAcquires;
    private final AtomicInteger acquiredChannelCount;
    private int pendingAcquireCount;
    private boolean closed;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/FixedChannelPool$AcquireTimeoutAction.class */
    public enum AcquireTimeoutAction {
        NEW,
        FAIL
    }

    static /* synthetic */ int access$806(FixedChannelPool x0) {
        int i = x0.pendingAcquireCount - 1;
        x0.pendingAcquireCount = i;
        return i;
    }

    static {
        $assertionsDisabled = !FixedChannelPool.class.desiredAssertionStatus();
    }

    public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, int maxConnections) {
        this(bootstrap, handler, maxConnections, Integer.MAX_VALUE);
    }

    public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, int maxConnections, int maxPendingAcquires) {
        this(bootstrap, handler, ChannelHealthChecker.ACTIVE, null, -1L, maxConnections, maxPendingAcquires);
    }

    public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires) {
        this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, true);
    }

    public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck) {
        this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, releaseHealthCheck, true);
    }

    public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck, boolean lastRecentUsed) {
        super(bootstrap, handler, healthCheck, releaseHealthCheck, lastRecentUsed);
        this.pendingAcquireQueue = new ArrayDeque();
        this.acquiredChannelCount = new AtomicInteger();
        if (maxConnections < 1) {
            throw new IllegalArgumentException("maxConnections: " + maxConnections + " (expected: >= 1)");
        }
        if (maxPendingAcquires < 1) {
            throw new IllegalArgumentException("maxPendingAcquires: " + maxPendingAcquires + " (expected: >= 1)");
        }
        if (action == null && acquireTimeoutMillis == -1) {
            this.timeoutTask = null;
            this.acquireTimeoutNanos = -1L;
        } else {
            if (action == null && acquireTimeoutMillis != -1) {
                throw new NullPointerException(ParameterMethodNameResolver.DEFAULT_PARAM_NAME);
            }
            if (action != null && acquireTimeoutMillis < 0) {
                throw new IllegalArgumentException("acquireTimeoutMillis: " + acquireTimeoutMillis + " (expected: >= 0)");
            }
            this.acquireTimeoutNanos = TimeUnit.MILLISECONDS.toNanos(acquireTimeoutMillis);
            switch (action) {
                case FAIL:
                    this.timeoutTask = new TimeoutTask() { // from class: io.netty.channel.pool.FixedChannelPool.1
                        @Override // io.netty.channel.pool.FixedChannelPool.TimeoutTask
                        public void onTimeout(AcquireTask task) {
                            task.promise.setFailure(new TimeoutException("Acquire operation took longer then configured maximum time") { // from class: io.netty.channel.pool.FixedChannelPool.1.1
                                @Override // java.lang.Throwable
                                public Throwable fillInStackTrace() {
                                    return this;
                                }
                            });
                        }
                    };
                    break;
                case NEW:
                    this.timeoutTask = new TimeoutTask() { // from class: io.netty.channel.pool.FixedChannelPool.2
                        @Override // io.netty.channel.pool.FixedChannelPool.TimeoutTask
                        public void onTimeout(AcquireTask task) {
                            task.acquired();
                            FixedChannelPool.super.acquire(task.promise);
                        }
                    };
                    break;
                default:
                    throw new Error();
            }
        }
        this.executor = bootstrap.config().group().next();
        this.maxConnections = maxConnections;
        this.maxPendingAcquires = maxPendingAcquires;
    }

    public int acquiredChannelCount() {
        return this.acquiredChannelCount.get();
    }

    @Override // io.netty.channel.pool.SimpleChannelPool, io.netty.channel.pool.ChannelPool
    public Future<Channel> acquire(final Promise<Channel> promise) {
        try {
            if (this.executor.inEventLoop()) {
                acquire0(promise);
            } else {
                this.executor.execute(new Runnable() { // from class: io.netty.channel.pool.FixedChannelPool.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FixedChannelPool.this.acquire0(promise);
                    }
                });
            }
        } catch (Throwable cause) {
            promise.setFailure(cause);
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acquire0(Promise<Channel> promise) {
        if (!$assertionsDisabled && !this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        if (this.closed) {
            promise.setFailure(new IllegalStateException("FixedChannelPool was closed"));
            return;
        }
        if (this.acquiredChannelCount.get() < this.maxConnections) {
            if (!$assertionsDisabled && this.acquiredChannelCount.get() < 0) {
                throw new AssertionError();
            }
            Promise<Channel> p = this.executor.newPromise();
            AcquireListener l = new AcquireListener(promise);
            l.acquired();
            p.addListener2((GenericFutureListener<? extends Future<? super Channel>>) l);
            super.acquire(p);
            return;
        }
        if (this.pendingAcquireCount >= this.maxPendingAcquires) {
            tooManyOutstanding(promise);
        } else {
            AcquireTask task = new AcquireTask(promise);
            if (this.pendingAcquireQueue.offer(task)) {
                this.pendingAcquireCount++;
                if (this.timeoutTask != null) {
                    task.timeoutFuture = this.executor.schedule(this.timeoutTask, this.acquireTimeoutNanos, TimeUnit.NANOSECONDS);
                }
            } else {
                tooManyOutstanding(promise);
            }
        }
        if (!$assertionsDisabled && this.pendingAcquireCount <= 0) {
            throw new AssertionError();
        }
    }

    private void tooManyOutstanding(Promise<?> promise) {
        promise.setFailure(new IllegalStateException("Too many outstanding acquire operations"));
    }

    @Override // io.netty.channel.pool.SimpleChannelPool, io.netty.channel.pool.ChannelPool
    public Future<Void> release(final Channel channel, final Promise<Void> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        Promise<Void> p = this.executor.newPromise();
        super.release(channel, p.addListener2(new FutureListener<Void>() { // from class: io.netty.channel.pool.FixedChannelPool.4
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !FixedChannelPool.class.desiredAssertionStatus();
            }

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Void> future) throws Exception {
                if (!$assertionsDisabled && !FixedChannelPool.this.executor.inEventLoop()) {
                    throw new AssertionError();
                }
                if (FixedChannelPool.this.closed) {
                    channel.close();
                    promise.setFailure(new IllegalStateException("FixedChannelPool was closed"));
                } else if (future.isSuccess()) {
                    FixedChannelPool.this.decrementAndRunTaskQueue();
                    promise.setSuccess(null);
                } else {
                    Throwable cause = future.cause();
                    if (!(cause instanceof IllegalArgumentException)) {
                        FixedChannelPool.this.decrementAndRunTaskQueue();
                    }
                    promise.setFailure(future.cause());
                }
            }
        }));
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementAndRunTaskQueue() {
        int currentCount = this.acquiredChannelCount.decrementAndGet();
        if (!$assertionsDisabled && currentCount < 0) {
            throw new AssertionError();
        }
        runTaskQueue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runTaskQueue() {
        AcquireTask task;
        while (this.acquiredChannelCount.get() < this.maxConnections && (task = this.pendingAcquireQueue.poll()) != null) {
            ScheduledFuture<?> timeoutFuture = task.timeoutFuture;
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
            }
            this.pendingAcquireCount--;
            task.acquired();
            super.acquire(task.promise);
        }
        if (!$assertionsDisabled && this.pendingAcquireCount < 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.acquiredChannelCount.get() < 0) {
            throw new AssertionError();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/FixedChannelPool$AcquireTask.class */
    private final class AcquireTask extends AcquireListener {
        final Promise<Channel> promise;
        final long expireNanoTime;
        ScheduledFuture<?> timeoutFuture;

        AcquireTask(Promise<Channel> promise) {
            super(promise);
            this.expireNanoTime = System.nanoTime() + FixedChannelPool.this.acquireTimeoutNanos;
            this.promise = FixedChannelPool.this.executor.newPromise().addListener2((GenericFutureListener) this);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/FixedChannelPool$TimeoutTask.class */
    private abstract class TimeoutTask implements Runnable {
        static final /* synthetic */ boolean $assertionsDisabled;

        public abstract void onTimeout(AcquireTask acquireTask);

        private TimeoutTask() {
        }

        static {
            $assertionsDisabled = !FixedChannelPool.class.desiredAssertionStatus();
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (!$assertionsDisabled && !FixedChannelPool.this.executor.inEventLoop()) {
                throw new AssertionError();
            }
            long nanoTime = System.nanoTime();
            while (true) {
                AcquireTask task = (AcquireTask) FixedChannelPool.this.pendingAcquireQueue.peek();
                if (task != null && nanoTime - task.expireNanoTime >= 0) {
                    FixedChannelPool.this.pendingAcquireQueue.remove();
                    FixedChannelPool.access$806(FixedChannelPool.this);
                    onTimeout(task);
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/FixedChannelPool$AcquireListener.class */
    private class AcquireListener implements FutureListener<Channel> {
        private final Promise<Channel> originalPromise;
        protected boolean acquired;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FixedChannelPool.class.desiredAssertionStatus();
        }

        AcquireListener(Promise<Channel> originalPromise) {
            this.originalPromise = originalPromise;
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(Future<Channel> future) throws Exception {
            if (!$assertionsDisabled && !FixedChannelPool.this.executor.inEventLoop()) {
                throw new AssertionError();
            }
            if (FixedChannelPool.this.closed) {
                if (future.isSuccess()) {
                    future.getNow().close();
                }
                this.originalPromise.setFailure(new IllegalStateException("FixedChannelPool was closed"));
            } else {
                if (future.isSuccess()) {
                    this.originalPromise.setSuccess(future.getNow());
                    return;
                }
                if (this.acquired) {
                    FixedChannelPool.this.decrementAndRunTaskQueue();
                } else {
                    FixedChannelPool.this.runTaskQueue();
                }
                this.originalPromise.setFailure(future.cause());
            }
        }

        public void acquired() {
            if (!this.acquired) {
                FixedChannelPool.this.acquiredChannelCount.incrementAndGet();
                this.acquired = true;
            }
        }
    }

    @Override // io.netty.channel.pool.SimpleChannelPool, io.netty.channel.pool.ChannelPool, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            closeAsync().await2();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override // io.netty.channel.pool.SimpleChannelPool
    public Future<Void> closeAsync() {
        if (this.executor.inEventLoop()) {
            return close0();
        }
        final Promise<Void> closeComplete = this.executor.newPromise();
        this.executor.execute(new Runnable() { // from class: io.netty.channel.pool.FixedChannelPool.5
            @Override // java.lang.Runnable
            public void run() {
                FixedChannelPool.this.close0().addListener2(new FutureListener<Void>() { // from class: io.netty.channel.pool.FixedChannelPool.5.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Void> f) throws Exception {
                        if (f.isSuccess()) {
                            closeComplete.setSuccess(null);
                        } else {
                            closeComplete.setFailure(f.cause());
                        }
                    }
                });
            }
        });
        return closeComplete;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Future<Void> close0() {
        if (!$assertionsDisabled && !this.executor.inEventLoop()) {
            throw new AssertionError();
        }
        if (!this.closed) {
            this.closed = true;
            while (true) {
                AcquireTask task = this.pendingAcquireQueue.poll();
                if (task != null) {
                    ScheduledFuture<?> f = task.timeoutFuture;
                    if (f != null) {
                        f.cancel(false);
                    }
                    task.promise.setFailure(new ClosedChannelException());
                } else {
                    this.acquiredChannelCount.set(0);
                    this.pendingAcquireCount = 0;
                    return GlobalEventExecutor.INSTANCE.submit((Callable) new Callable<Void>() { // from class: io.netty.channel.pool.FixedChannelPool.6
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Void call() throws Exception {
                            FixedChannelPool.super.close();
                            return null;
                        }
                    });
                }
            }
        } else {
            return GlobalEventExecutor.INSTANCE.newSucceededFuture(null);
        }
    }
}

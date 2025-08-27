package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import org.apache.tomcat.jni.Time;
import org.springframework.cache.interceptor.CacheOperationExpressionEvaluator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/DefaultPromise.class */
public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultPromise.class);
    private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
    private static final int MAX_LISTENER_STACK_DEPTH = Math.min(8, SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
    private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, CacheOperationExpressionEvaluator.RESULT_VARIABLE);
    private static final Object SUCCESS = new Object();
    private static final Object UNCANCELLABLE = new Object();
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(ThrowableUtil.unknownStackTrace(new CancellationException(), DefaultPromise.class, "cancel(...)"));
    private static final StackTraceElement[] CANCELLATION_STACK = CANCELLATION_CAUSE_HOLDER.cause.getStackTrace();
    private volatile Object result;
    private final EventExecutor executor;
    private Object listeners;
    private short waiters;
    private boolean notifyingListeners;

    public DefaultPromise(EventExecutor executor) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
    }

    protected DefaultPromise() {
        this.executor = null;
    }

    public Promise<V> setSuccess(V result) {
        if (setSuccess0(result)) {
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }

    public boolean trySuccess(V result) {
        return setSuccess0(result);
    }

    public Promise<V> setFailure(Throwable cause) {
        if (setFailure0(cause)) {
            return this;
        }
        throw new IllegalStateException("complete already: " + this, cause);
    }

    public boolean tryFailure(Throwable cause) {
        return setFailure0(cause);
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean setUncancellable() {
        if (RESULT_UPDATER.compareAndSet(this, null, UNCANCELLABLE)) {
            return true;
        }
        Object result = this.result;
        return (isDone0(result) && isCancelled0(result)) ? false : true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        Object result = this.result;
        return (result == null || result == UNCANCELLABLE || (result instanceof CauseHolder)) ? false : true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return this.result == null;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/DefaultPromise$LeanCancellationException.class */
    private static final class LeanCancellationException extends CancellationException {
        private static final long serialVersionUID = 2794674970981187807L;

        private LeanCancellationException() {
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            setStackTrace(DefaultPromise.CANCELLATION_STACK);
            return this;
        }

        @Override // java.lang.Throwable
        public String toString() {
            return CancellationException.class.getName();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return cause0(this.result);
    }

    private Throwable cause0(Object result) {
        if (!(result instanceof CauseHolder)) {
            return null;
        }
        if (result == CANCELLATION_CAUSE_HOLDER) {
            CancellationException ce = new LeanCancellationException();
            if (RESULT_UPDATER.compareAndSet(this, CANCELLATION_CAUSE_HOLDER, new CauseHolder(ce))) {
                return ce;
            }
            result = this.result;
        }
        return ((CauseHolder) result).cause;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: addListener */
    public Promise<V> addListener2(GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            addListener0(listener);
        }
        if (isDone()) {
            notifyListeners();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: addListeners */
    public Promise<V> addListeners2(GenericFutureListener<? extends Future<? super V>>... listeners) {
        GenericFutureListener<? extends Future<? super V>> listener;
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            int length = listeners.length;
            for (int i = 0; i < length && (listener = listeners[i]) != null; i++) {
                addListener0(listener);
            }
        }
        if (isDone()) {
            notifyListeners();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: removeListener */
    public Promise<V> removeListener2(GenericFutureListener<? extends Future<? super V>> listener) {
        ObjectUtil.checkNotNull(listener, "listener");
        synchronized (this) {
            removeListener0(listener);
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: removeListeners */
    public Promise<V> removeListeners2(GenericFutureListener<? extends Future<? super V>>... listeners) {
        GenericFutureListener<? extends Future<? super V>> listener;
        ObjectUtil.checkNotNull(listeners, "listeners");
        synchronized (this) {
            int length = listeners.length;
            for (int i = 0; i < length && (listener = listeners[i]) != null; i++) {
                removeListener0(listener);
            }
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: await */
    public Promise<V> await2() throws InterruptedException {
        if (isDone()) {
            return this;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        checkDeadLock();
        synchronized (this) {
            while (!isDone()) {
                incWaiters();
                try {
                    wait();
                    decWaiters();
                } catch (Throwable th) {
                    decWaiters();
                    throw th;
                }
            }
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: awaitUninterruptibly */
    public Promise<V> awaitUninterruptibly2() {
        if (isDone()) {
            return this;
        }
        checkDeadLock();
        boolean interrupted = false;
        synchronized (this) {
            while (!isDone()) {
                incWaiters();
                try {
                    wait();
                    decWaiters();
                } catch (InterruptedException e) {
                    interrupted = true;
                    decWaiters();
                } catch (Throwable th) {
                    decWaiters();
                    throw th;
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return await0(unit.toNanos(timeout), true);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) throws InterruptedException {
        return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        try {
            return await0(unit.toNanos(timeout), false);
        } catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        try {
            return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        } catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    @Override // io.netty.util.concurrent.Future
    public V getNow() {
        V v = (V) this.result;
        if ((v instanceof CauseHolder) || v == SUCCESS || v == UNCANCELLABLE) {
            return null;
        }
        return v;
    }

    @Override // io.netty.util.concurrent.AbstractFuture, java.util.concurrent.Future
    public V get() throws ExecutionException, InterruptedException {
        Object obj = this.result;
        if (!isDone0(obj)) {
            await2();
            obj = this.result;
        }
        if (obj == SUCCESS || obj == UNCANCELLABLE) {
            return null;
        }
        Throwable thCause0 = cause0(obj);
        if (thCause0 == null) {
            return (V) obj;
        }
        if (thCause0 instanceof CancellationException) {
            throw ((CancellationException) thCause0);
        }
        throw new ExecutionException(thCause0);
    }

    @Override // io.netty.util.concurrent.AbstractFuture, java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        Object obj = this.result;
        if (!isDone0(obj)) {
            if (!await(j, timeUnit)) {
                throw new TimeoutException();
            }
            obj = this.result;
        }
        if (obj == SUCCESS || obj == UNCANCELLABLE) {
            return null;
        }
        Throwable thCause0 = cause0(obj);
        if (thCause0 == null) {
            return (V) obj;
        }
        if (thCause0 instanceof CancellationException) {
            throw ((CancellationException) thCause0);
        }
        throw new ExecutionException(thCause0);
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (RESULT_UPDATER.compareAndSet(this, null, CANCELLATION_CAUSE_HOLDER)) {
            if (checkNotifyWaiters()) {
                notifyListeners();
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return isCancelled0(this.result);
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return isDone0(this.result);
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: sync */
    public Promise<V> sync2() throws InterruptedException {
        await2();
        rethrowIfFailed();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    /* renamed from: syncUninterruptibly */
    public Promise<V> syncUninterruptibly2() {
        awaitUninterruptibly2();
        rethrowIfFailed();
        return this;
    }

    public String toString() {
        return toStringBuilder().toString();
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(hashCode()));
        Object result = this.result;
        if (result == SUCCESS) {
            buf.append("(success)");
        } else if (result == UNCANCELLABLE) {
            buf.append("(uncancellable)");
        } else if (result instanceof CauseHolder) {
            buf.append("(failure: ").append(((CauseHolder) result).cause).append(')');
        } else if (result != null) {
            buf.append("(success: ").append(result).append(')');
        } else {
            buf.append("(incomplete)");
        }
        return buf;
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    protected void checkDeadLock() {
        EventExecutor e = executor();
        if (e != null && e.inEventLoop()) {
            throw new BlockingOperationException(toString());
        }
    }

    protected static void notifyListener(EventExecutor eventExecutor, Future<?> future, GenericFutureListener<?> listener) {
        notifyListenerWithStackOverFlowProtection((EventExecutor) ObjectUtil.checkNotNull(eventExecutor, "eventExecutor"), (Future) ObjectUtil.checkNotNull(future, "future"), (GenericFutureListener) ObjectUtil.checkNotNull(listener, "listener"));
    }

    private void notifyListeners() {
        InternalThreadLocalMap threadLocals;
        int stackDepth;
        EventExecutor executor = executor();
        if (executor.inEventLoop() && (stackDepth = (threadLocals = InternalThreadLocalMap.get()).futureListenerStackDepth()) < MAX_LISTENER_STACK_DEPTH) {
            threadLocals.setFutureListenerStackDepth(stackDepth + 1);
            try {
                notifyListenersNow();
                threadLocals.setFutureListenerStackDepth(stackDepth);
                return;
            } catch (Throwable th) {
                threadLocals.setFutureListenerStackDepth(stackDepth);
                throw th;
            }
        }
        safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultPromise.this.notifyListenersNow();
            }
        });
    }

    private static void notifyListenerWithStackOverFlowProtection(EventExecutor executor, final Future<?> future, final GenericFutureListener<?> listener) {
        InternalThreadLocalMap threadLocals;
        int stackDepth;
        if (executor.inEventLoop() && (stackDepth = (threadLocals = InternalThreadLocalMap.get()).futureListenerStackDepth()) < MAX_LISTENER_STACK_DEPTH) {
            threadLocals.setFutureListenerStackDepth(stackDepth + 1);
            try {
                notifyListener0(future, listener);
                threadLocals.setFutureListenerStackDepth(stackDepth);
                return;
            } catch (Throwable th) {
                threadLocals.setFutureListenerStackDepth(stackDepth);
                throw th;
            }
        }
        safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.2
            @Override // java.lang.Runnable
            public void run() {
                DefaultPromise.notifyListener0(future, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenersNow() {
        synchronized (this) {
            if (this.notifyingListeners || this.listeners == null) {
                return;
            }
            this.notifyingListeners = true;
            Object listeners = this.listeners;
            this.listeners = null;
            while (true) {
                if (listeners instanceof DefaultFutureListeners) {
                    notifyListeners0((DefaultFutureListeners) listeners);
                } else {
                    notifyListener0(this, (GenericFutureListener) listeners);
                }
                synchronized (this) {
                    if (this.listeners == null) {
                        this.notifyingListeners = false;
                        return;
                    } else {
                        listeners = this.listeners;
                        this.listeners = null;
                    }
                }
            }
        }
    }

    private void notifyListeners0(DefaultFutureListeners listeners) {
        GenericFutureListener<?>[] a = listeners.listeners();
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            notifyListener0(this, a[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyListener0(Future future, GenericFutureListener l) {
        try {
            l.operationComplete(future);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
            }
        }
    }

    private void addListener0(GenericFutureListener<? extends Future<? super V>> listener) {
        if (this.listeners == null) {
            this.listeners = listener;
        } else if (this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners) this.listeners).add(listener);
        } else {
            this.listeners = new DefaultFutureListeners((GenericFutureListener) this.listeners, listener);
        }
    }

    private void removeListener0(GenericFutureListener<? extends Future<? super V>> listener) {
        if (this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners) this.listeners).remove(listener);
        } else if (this.listeners == listener) {
            this.listeners = null;
        }
    }

    private boolean setSuccess0(V result) {
        return setValue0(result == null ? SUCCESS : result);
    }

    private boolean setFailure0(Throwable cause) {
        return setValue0(new CauseHolder((Throwable) ObjectUtil.checkNotNull(cause, "cause")));
    }

    private boolean setValue0(Object objResult) {
        if (RESULT_UPDATER.compareAndSet(this, null, objResult) || RESULT_UPDATER.compareAndSet(this, UNCANCELLABLE, objResult)) {
            if (checkNotifyWaiters()) {
                notifyListeners();
                return true;
            }
            return true;
        }
        return false;
    }

    private synchronized boolean checkNotifyWaiters() {
        if (this.waiters > 0) {
            notifyAll();
        }
        return this.listeners != null;
    }

    private void incWaiters() {
        if (this.waiters == Short.MAX_VALUE) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        this.waiters = (short) (this.waiters + 1);
    }

    private void decWaiters() {
        this.waiters = (short) (this.waiters - 1);
    }

    private void rethrowIfFailed() {
        Throwable cause = cause();
        if (cause == null) {
            return;
        }
        PlatformDependent.throwException(cause);
    }

    /* JADX WARN: Finally extract failed */
    private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
        if (isDone()) {
            return true;
        }
        if (timeoutNanos <= 0) {
            return isDone();
        }
        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        checkDeadLock();
        long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;
        do {
            try {
                synchronized (this) {
                    if (isDone()) {
                        return true;
                    }
                    incWaiters();
                    try {
                        try {
                            wait(waitTime / Time.APR_USEC_PER_SEC, (int) (waitTime % Time.APR_USEC_PER_SEC));
                            decWaiters();
                        } catch (Throwable th) {
                            decWaiters();
                            throw th;
                        }
                    } catch (InterruptedException e) {
                        if (interruptable) {
                            throw e;
                        }
                        interrupted = true;
                        decWaiters();
                    }
                    if (isDone()) {
                        if (interrupted) {
                            Thread.currentThread().interrupt();
                        }
                        return true;
                    }
                    waitTime = timeoutNanos - (System.nanoTime() - startTime);
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        } while (waitTime > 0);
        boolean zIsDone = isDone();
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return zIsDone;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void notifyProgressiveListeners(final long progress, final long total) {
        Object listeners = progressiveListeners();
        if (listeners == null) {
            return;
        }
        final ProgressiveFuture<V> self = (ProgressiveFuture) this;
        EventExecutor executor = executor();
        if (executor.inEventLoop()) {
            if (listeners instanceof GenericProgressiveFutureListener[]) {
                notifyProgressiveListeners0(self, (GenericProgressiveFutureListener[]) listeners, progress, total);
                return;
            } else {
                notifyProgressiveListener0(self, (GenericProgressiveFutureListener) listeners, progress, total);
                return;
            }
        }
        if (listeners instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener[]) listeners;
            safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.3
                @Override // java.lang.Runnable
                public void run() {
                    DefaultPromise.notifyProgressiveListeners0(self, array, progress, total);
                }
            });
        } else {
            final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener) listeners;
            safeExecute(executor, new Runnable() { // from class: io.netty.util.concurrent.DefaultPromise.4
                @Override // java.lang.Runnable
                public void run() {
                    DefaultPromise.notifyProgressiveListener0(self, l, progress, total);
                }
            });
        }
    }

    private synchronized Object progressiveListeners() {
        Object listeners = this.listeners;
        if (listeners == null) {
            return null;
        }
        if (listeners instanceof DefaultFutureListeners) {
            DefaultFutureListeners dfl = (DefaultFutureListeners) listeners;
            int progressiveSize = dfl.progressiveSize();
            switch (progressiveSize) {
                case 0:
                    return null;
                case 1:
                    for (GenericFutureListener<?> l : dfl.listeners()) {
                        if (l instanceof GenericProgressiveFutureListener) {
                            return l;
                        }
                    }
                    return null;
                default:
                    GenericFutureListener<?>[] array = dfl.listeners();
                    GenericProgressiveFutureListener<?>[] copy = new GenericProgressiveFutureListener[progressiveSize];
                    int i = 0;
                    int j = 0;
                    while (j < progressiveSize) {
                        GenericFutureListener<?> l2 = array[i];
                        if (l2 instanceof GenericProgressiveFutureListener) {
                            int i2 = j;
                            j++;
                            copy[i2] = (GenericProgressiveFutureListener) l2;
                        }
                        i++;
                    }
                    return copy;
            }
        }
        if (listeners instanceof GenericProgressiveFutureListener) {
            return listeners;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total) {
        GenericProgressiveFutureListener<?> l;
        int length = listeners.length;
        for (int i = 0; i < length && (l = listeners[i]) != null; i++) {
            notifyProgressiveListener0(future, l, progress, total);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener l, long progress, long total) {
        try {
            l.operationProgressed(future, progress, total);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
            }
        }
    }

    private static boolean isCancelled0(Object result) {
        return (result instanceof CauseHolder) && (((CauseHolder) result).cause instanceof CancellationException);
    }

    private static boolean isDone0(Object result) {
        return (result == null || result == UNCANCELLABLE) ? false : true;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/DefaultPromise$CauseHolder.class */
    private static final class CauseHolder {
        final Throwable cause;

        CauseHolder(Throwable cause) {
            this.cause = cause;
        }
    }

    private static void safeExecute(EventExecutor executor, Runnable task) {
        try {
            executor.execute(task);
        } catch (Throwable t) {
            rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
        }
    }
}

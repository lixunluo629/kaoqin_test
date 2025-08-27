package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures.class */
public final class Futures {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() { // from class: com.google.common.util.concurrent.Futures.4
        @Override // com.google.common.util.concurrent.AsyncFunction
        public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
            return input;
        }
    };
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>() { // from class: com.google.common.util.concurrent.Futures.7
        @Override // com.google.common.base.Function
        public Boolean apply(Constructor<?> input) {
            return Boolean.valueOf(Arrays.asList(input.getParameterTypes()).contains(String.class));
        }
    }).reverse();

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$FutureCombiner.class */
    private interface FutureCombiner<V, C> {
        C combine(List<Optional<V>> list);
    }

    private Futures() {
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
        return new MappingCheckedFuture((ListenableFuture) Preconditions.checkNotNull(future), mapper);
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateFuture.class */
    private static abstract class ImmediateFuture<V> implements ListenableFuture<V> {
        private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

        @Override // java.util.concurrent.Future
        public abstract V get() throws ExecutionException;

        private ImmediateFuture() {
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable listener, Executor executor) {
            Preconditions.checkNotNull(listener, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            try {
                executor.execute(listener);
            } catch (RuntimeException e) {
                Logger logger = log;
                Level level = Level.SEVERE;
                String strValueOf = String.valueOf(String.valueOf(listener));
                String strValueOf2 = String.valueOf(String.valueOf(executor));
                logger.log(level, new StringBuilder(57 + strValueOf.length() + strValueOf2.length()).append("RuntimeException while executing runnable ").append(strValueOf).append(" with executor ").append(strValueOf2).toString(), (Throwable) e);
            }
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override // java.util.concurrent.Future
        public V get(long timeout, TimeUnit unit) throws ExecutionException {
            Preconditions.checkNotNull(unit);
            return get();
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return false;
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return true;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateSuccessfulFuture.class */
    private static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {

        @Nullable
        private final V value;

        ImmediateSuccessfulFuture(@Nullable V value) {
            super();
            this.value = value;
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public V get() {
            return this.value;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateSuccessfulCheckedFuture.class */
    private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {

        @Nullable
        private final V value;

        ImmediateSuccessfulCheckedFuture(@Nullable V value) {
            super();
            this.value = value;
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public V get() {
            return this.value;
        }

        @Override // com.google.common.util.concurrent.CheckedFuture
        public V checkedGet() {
            return this.value;
        }

        @Override // com.google.common.util.concurrent.CheckedFuture
        public V checkedGet(long timeout, TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            return this.value;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateFailedFuture.class */
    private static class ImmediateFailedFuture<V> extends ImmediateFuture<V> {
        private final Throwable thrown;

        ImmediateFailedFuture(Throwable thrown) {
            super();
            this.thrown = thrown;
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateCancelledFuture.class */
    private static class ImmediateCancelledFuture<V> extends ImmediateFuture<V> {
        private final CancellationException thrown;

        ImmediateCancelledFuture() {
            super();
            this.thrown = new CancellationException("Immediate cancelled future.");
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public boolean isCancelled() {
            return true;
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public V get() {
            throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ImmediateFailedCheckedFuture.class */
    private static class ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
        private final X thrown;

        ImmediateFailedCheckedFuture(X thrown) {
            super();
            this.thrown = thrown;
        }

        @Override // com.google.common.util.concurrent.Futures.ImmediateFuture, java.util.concurrent.Future
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: X extends java.lang.Exception */
        @Override // com.google.common.util.concurrent.CheckedFuture
        public V checkedGet() throws Exception {
            throw this.thrown;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: X extends java.lang.Exception */
        @Override // com.google.common.util.concurrent.CheckedFuture
        public V checkedGet(long timeout, TimeUnit unit) throws Exception {
            Preconditions.checkNotNull(unit);
            throw this.thrown;
        }
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
        return new ImmediateSuccessfulFuture(value);
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
        return new ImmediateSuccessfulCheckedFuture(value);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFailedFuture(throwable);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateCancelledFuture();
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFailedCheckedFuture(exception);
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback) {
        return withFallback(input, fallback, MoreExecutors.directExecutor());
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback, Executor executor) {
        Preconditions.checkNotNull(fallback);
        return new FallbackFuture(input, fallback, executor);
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$FallbackFuture.class */
    private static class FallbackFuture<V> extends AbstractFuture<V> {
        private volatile ListenableFuture<? extends V> running;

        FallbackFuture(ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, Executor executor) {
            this.running = input;
            Futures.addCallback(this.running, new FutureCallback<V>() { // from class: com.google.common.util.concurrent.Futures.FallbackFuture.1
                @Override // com.google.common.util.concurrent.FutureCallback
                public void onSuccess(V value) {
                    FallbackFuture.this.set(value);
                }

                @Override // com.google.common.util.concurrent.FutureCallback
                public void onFailure(Throwable t) {
                    if (FallbackFuture.this.isCancelled()) {
                        return;
                    }
                    try {
                        FallbackFuture.this.running = fallback.create(t);
                        if (FallbackFuture.this.isCancelled()) {
                            FallbackFuture.this.running.cancel(FallbackFuture.this.wasInterrupted());
                        } else {
                            Futures.addCallback(FallbackFuture.this.running, new FutureCallback<V>() { // from class: com.google.common.util.concurrent.Futures.FallbackFuture.1.1
                                @Override // com.google.common.util.concurrent.FutureCallback
                                public void onSuccess(V value) {
                                    FallbackFuture.this.set(value);
                                }

                                @Override // com.google.common.util.concurrent.FutureCallback
                                public void onFailure(Throwable t2) {
                                    if (FallbackFuture.this.running.isCancelled()) {
                                        FallbackFuture.this.cancel(false);
                                    } else {
                                        FallbackFuture.this.setException(t2);
                                    }
                                }
                            }, MoreExecutors.directExecutor());
                        }
                    } catch (Throwable e) {
                        FallbackFuture.this.setException(e);
                    }
                }
            }, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                this.running.cancel(mayInterruptIfRunning);
                return true;
            }
            return false;
        }
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        ChainingListenableFuture<I, O> output = new ChainingListenableFuture<>(function, input);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(executor);
        ChainingListenableFuture<I, O> output = new ChainingListenableFuture<>(function, input);
        input.addListener(rejectionPropagatingRunnable(output, output, executor), MoreExecutors.directExecutor());
        return output;
    }

    private static Runnable rejectionPropagatingRunnable(final AbstractFuture<?> outputFuture, final Runnable delegateTask, final Executor delegateExecutor) {
        return new Runnable() { // from class: com.google.common.util.concurrent.Futures.1
            @Override // java.lang.Runnable
            public void run() {
                final AtomicBoolean thrownFromDelegate = new AtomicBoolean(true);
                try {
                    delegateExecutor.execute(new Runnable() { // from class: com.google.common.util.concurrent.Futures.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            thrownFromDelegate.set(false);
                            delegateTask.run();
                        }
                    });
                } catch (RejectedExecutionException e) {
                    if (thrownFromDelegate.get()) {
                        outputFuture.setException(e);
                    }
                }
            }
        };
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        ChainingListenableFuture<I, O> output = new ChainingListenableFuture<>(asAsyncFunction(function), input);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        return transform(input, asAsyncFunction(function), executor);
    }

    private static <I, O> AsyncFunction<I, O> asAsyncFunction(final Function<? super I, ? extends O> function) {
        return new AsyncFunction<I, O>() { // from class: com.google.common.util.concurrent.Futures.2
            @Override // com.google.common.util.concurrent.AsyncFunction
            public ListenableFuture<O> apply(I input) {
                return Futures.immediateFuture(function.apply(input));
            }
        };
    }

    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>() { // from class: com.google.common.util.concurrent.Futures.3
            @Override // java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                return input.cancel(mayInterruptIfRunning);
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return input.isCancelled();
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return input.isDone();
            }

            @Override // java.util.concurrent.Future
            public O get() throws ExecutionException, InterruptedException {
                return applyTransformation(input.get());
            }

            @Override // java.util.concurrent.Future
            public O get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return applyTransformation(input.get(timeout, unit));
            }

            private O applyTransformation(I i) throws ExecutionException {
                try {
                    return (O) function.apply(i);
                } catch (Throwable th) {
                    throw new ExecutionException(th);
                }
            }
        };
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$ChainingListenableFuture.class */
    private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable {
        private AsyncFunction<? super I, ? extends O> function;
        private ListenableFuture<? extends I> inputFuture;
        private volatile ListenableFuture<? extends O> outputFuture;

        private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
            this.function = (AsyncFunction) Preconditions.checkNotNull(function);
            this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                cancel(this.inputFuture, mayInterruptIfRunning);
                cancel(this.outputFuture, mayInterruptIfRunning);
                return true;
            }
            return false;
        }

        private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
            if (future != null) {
                future.cancel(mayInterruptIfRunning);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    try {
                        try {
                            final ListenableFuture<? extends O> outputFuture = (ListenableFuture) Preconditions.checkNotNull(this.function.apply(Uninterruptibles.getUninterruptibly(this.inputFuture)), "AsyncFunction may not return null.");
                            this.outputFuture = outputFuture;
                            if (!isCancelled()) {
                                outputFuture.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.ChainingListenableFuture.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        try {
                                            try {
                                                ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(outputFuture));
                                                ChainingListenableFuture.this.outputFuture = null;
                                            } catch (CancellationException e) {
                                                ChainingListenableFuture.this.cancel(false);
                                                ChainingListenableFuture.this.outputFuture = null;
                                            } catch (ExecutionException e2) {
                                                ChainingListenableFuture.this.setException(e2.getCause());
                                                ChainingListenableFuture.this.outputFuture = null;
                                            }
                                        } catch (Throwable th) {
                                            ChainingListenableFuture.this.outputFuture = null;
                                            throw th;
                                        }
                                    }
                                }, MoreExecutors.directExecutor());
                                this.function = null;
                                this.inputFuture = null;
                            } else {
                                outputFuture.cancel(wasInterrupted());
                                this.outputFuture = null;
                                this.function = null;
                                this.inputFuture = null;
                            }
                        } catch (CancellationException e) {
                            cancel(false);
                            this.function = null;
                            this.inputFuture = null;
                        } catch (ExecutionException e2) {
                            setException(e2.getCause());
                            this.function = null;
                            this.inputFuture = null;
                        }
                    } catch (UndeclaredThrowableException e3) {
                        setException(e3.getCause());
                        this.function = null;
                        this.inputFuture = null;
                    }
                } catch (Throwable th) {
                    this.function = null;
                    this.inputFuture = null;
                    throw th;
                }
            } catch (Throwable t) {
                setException(t);
                this.function = null;
                this.inputFuture = null;
            }
        }
    }

    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return transform(nested, DEREFERENCER);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
        return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.directExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.directExecutor());
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$WrappedCombiner.class */
    private static final class WrappedCombiner<T> implements Callable<T> {
        final Callable<T> delegate;
        CombinerFuture<T> outputFuture;

        WrappedCombiner(Callable<T> delegate) {
            this.delegate = (Callable) Preconditions.checkNotNull(delegate);
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            try {
                return this.delegate.call();
            } catch (CancellationException e) {
                this.outputFuture.cancel(false);
                return null;
            } catch (ExecutionException e2) {
                this.outputFuture.setException(e2.getCause());
                return null;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$CombinerFuture.class */
    private static final class CombinerFuture<V> extends ListenableFutureTask<V> {
        ImmutableList<ListenableFuture<?>> futures;

        CombinerFuture(Callable<V> callable, ImmutableList<ListenableFuture<?>> futures) {
            super(callable);
            this.futures = futures;
        }

        @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            ImmutableList<ListenableFuture<?>> futures = this.futures;
            if (super.cancel(mayInterruptIfRunning)) {
                Iterator i$ = futures.iterator();
                while (i$.hasNext()) {
                    ListenableFuture<?> future = (ListenableFuture) i$.next();
                    future.cancel(mayInterruptIfRunning);
                }
                return true;
            }
            return false;
        }

        @Override // com.google.common.util.concurrent.ListenableFutureTask, java.util.concurrent.FutureTask
        protected void done() {
            super.done();
            this.futures = null;
        }

        @Override // java.util.concurrent.FutureTask
        protected void setException(Throwable t) {
            super.setException(t);
        }
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        return new NonCancellationPropagatingFuture(future);
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$NonCancellationPropagatingFuture.class */
    private static class NonCancellationPropagatingFuture<V> extends AbstractFuture<V> {
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            Preconditions.checkNotNull(delegate);
            Futures.addCallback(delegate, new FutureCallback<V>() { // from class: com.google.common.util.concurrent.Futures.NonCancellationPropagatingFuture.1
                @Override // com.google.common.util.concurrent.FutureCallback
                public void onSuccess(V result) {
                    NonCancellationPropagatingFuture.this.set(result);
                }

                @Override // com.google.common.util.concurrent.FutureCallback
                public void onFailure(Throwable t) {
                    if (delegate.isCancelled()) {
                        NonCancellationPropagatingFuture.this.cancel(false);
                    } else {
                        NonCancellationPropagatingFuture.this.setException(t);
                    }
                }
            }, MoreExecutors.directExecutor());
        }
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
        return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.directExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.directExecutor());
    }

    @Beta
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ConcurrentLinkedQueue<AsyncSettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
        ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
        SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
        for (final ListenableFuture<? extends T> future : futures) {
            AsyncSettableFuture<T> delegate = AsyncSettableFuture.create();
            delegates.add(delegate);
            future.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.5
                @Override // java.lang.Runnable
                public void run() {
                    ((AsyncSettableFuture) delegates.remove()).setFuture(future);
                }
            }, executor);
            listBuilder.add((ImmutableList.Builder<ListenableFuture<T>>) delegate);
        }
        return listBuilder.build();
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.directExecutor());
    }

    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        Runnable callbackListener = new Runnable() { // from class: com.google.common.util.concurrent.Futures.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    callback.onSuccess(Uninterruptibles.getUninterruptibly(future));
                } catch (Error e) {
                    callback.onFailure(e);
                } catch (RuntimeException e2) {
                    callback.onFailure(e2);
                } catch (ExecutionException e3) {
                    callback.onFailure(e3.getCause());
                }
            }
        };
        future.addListener(callbackListener, executor);
    }

    public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        } catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }

    public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(unit);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        } catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), exceptionClass);
            throw new AssertionError();
        } catch (TimeoutException e3) {
            throw newWithCause(exceptionClass, e3);
        }
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws Exception {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        }
        throw newWithCause(exceptionClass, cause);
    }

    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return (V) Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        throw new UncheckedExecutionException(cause);
    }

    private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
        List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
        for (Constructor<X> constructor : preferringStrings(constructors)) {
            X x = (X) newFromConstructor(constructor, cause);
            if (x != null) {
                if (x.getCause() == null) {
                    x.initCause(cause);
                }
                return x;
            }
        }
        String strValueOf = String.valueOf(String.valueOf(exceptionClass));
        throw new IllegalArgumentException(new StringBuilder(82 + strValueOf.length()).append("No appropriate constructor for exception of type ").append(strValueOf).append(" in response to chained exception").toString(), cause);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> list) {
        return (List<Constructor<X>>) WITH_STRING_PARAM_FIRST.sortedCopy(list);
    }

    @Nullable
    private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (paramType.equals(String.class)) {
                params[i] = cause.toString();
            } else if (paramType.equals(Throwable.class)) {
                params[i] = cause;
            } else {
                return null;
            }
        }
        try {
            return constructor.newInstance(params);
        } catch (IllegalAccessException e) {
            return null;
        } catch (IllegalArgumentException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (InvocationTargetException e4) {
            return null;
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$CombinedFuture.class */
    private static class CombinedFuture<V, C> extends AbstractFuture<C> {
        private static final Logger logger = Logger.getLogger(CombinedFuture.class.getName());
        ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
        final boolean allMustSucceed;
        final AtomicInteger remaining;
        FutureCombiner<V, C> combiner;
        List<Optional<V>> values;
        final Object seenExceptionsLock = new Object();
        Set<Throwable> seenExceptions;

        CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, FutureCombiner<V, C> combiner) {
            this.futures = futures;
            this.allMustSucceed = allMustSucceed;
            this.remaining = new AtomicInteger(futures.size());
            this.combiner = combiner;
            this.values = Lists.newArrayListWithCapacity(futures.size());
            init(listenerExecutor);
        }

        protected void init(Executor listenerExecutor) {
            addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.CombinedFuture.1
                @Override // java.lang.Runnable
                public void run() {
                    if (CombinedFuture.this.isCancelled()) {
                        Iterator i$ = CombinedFuture.this.futures.iterator();
                        while (i$.hasNext()) {
                            ListenableFuture<?> future = (ListenableFuture) i$.next();
                            future.cancel(CombinedFuture.this.wasInterrupted());
                        }
                    }
                    CombinedFuture.this.futures = null;
                    CombinedFuture.this.values = null;
                    CombinedFuture.this.combiner = null;
                }
            }, MoreExecutors.directExecutor());
            if (this.futures.isEmpty()) {
                set(this.combiner.combine(ImmutableList.of()));
                return;
            }
            for (int i = 0; i < this.futures.size(); i++) {
                this.values.add(null);
            }
            int i2 = 0;
            Iterator i$ = this.futures.iterator();
            while (i$.hasNext()) {
                final ListenableFuture<? extends V> listenable = (ListenableFuture) i$.next();
                final int index = i2;
                i2++;
                listenable.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.CombinedFuture.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CombinedFuture.this.setOneValue(index, listenable);
                    }
                }, listenerExecutor);
            }
        }

        private void setExceptionAndMaybeLog(Throwable throwable) {
            boolean visibleFromOutputFuture = false;
            boolean firstTimeSeeingThisException = true;
            if (this.allMustSucceed) {
                visibleFromOutputFuture = super.setException(throwable);
                synchronized (this.seenExceptionsLock) {
                    if (this.seenExceptions == null) {
                        this.seenExceptions = Sets.newHashSet();
                    }
                    firstTimeSeeingThisException = this.seenExceptions.add(throwable);
                }
            }
            if ((throwable instanceof Error) || (this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException)) {
                logger.log(Level.SEVERE, "input future failed.", throwable);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setOneValue(int index, Future<? extends V> future) {
            List<Optional<V>> localValues = this.values;
            if (isDone() || localValues == null) {
                Preconditions.checkState(this.allMustSucceed || isCancelled(), "Future was done before all dependencies completed");
            }
            try {
                try {
                    try {
                        Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
                        Object uninterruptibly = Uninterruptibles.getUninterruptibly(future);
                        if (localValues != null) {
                            localValues.set(index, Optional.fromNullable(uninterruptibly));
                        }
                        int newRemaining = this.remaining.decrementAndGet();
                        Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
                        if (newRemaining == 0) {
                            FutureCombiner<V, C> localCombiner = this.combiner;
                            if (localCombiner == null || localValues == null) {
                                Preconditions.checkState(isDone());
                            } else {
                                set(localCombiner.combine(localValues));
                            }
                        }
                    } catch (CancellationException e) {
                        if (this.allMustSucceed) {
                            cancel(false);
                        }
                        int newRemaining2 = this.remaining.decrementAndGet();
                        Preconditions.checkState(newRemaining2 >= 0, "Less than 0 remaining futures");
                        if (newRemaining2 == 0) {
                            FutureCombiner<V, C> localCombiner2 = this.combiner;
                            if (localCombiner2 == null || localValues == null) {
                                Preconditions.checkState(isDone());
                            } else {
                                set(localCombiner2.combine(localValues));
                            }
                        }
                    } catch (ExecutionException e2) {
                        setExceptionAndMaybeLog(e2.getCause());
                        int newRemaining3 = this.remaining.decrementAndGet();
                        Preconditions.checkState(newRemaining3 >= 0, "Less than 0 remaining futures");
                        if (newRemaining3 == 0) {
                            FutureCombiner<V, C> localCombiner3 = this.combiner;
                            if (localCombiner3 == null || localValues == null) {
                                Preconditions.checkState(isDone());
                            } else {
                                set(localCombiner3.combine(localValues));
                            }
                        }
                    }
                } catch (Throwable t) {
                    setExceptionAndMaybeLog(t);
                    int newRemaining4 = this.remaining.decrementAndGet();
                    Preconditions.checkState(newRemaining4 >= 0, "Less than 0 remaining futures");
                    if (newRemaining4 == 0) {
                        FutureCombiner<V, C> localCombiner4 = this.combiner;
                        if (localCombiner4 == null || localValues == null) {
                            Preconditions.checkState(isDone());
                        } else {
                            set(localCombiner4.combine(localValues));
                        }
                    }
                }
            } catch (Throwable th) {
                int newRemaining5 = this.remaining.decrementAndGet();
                Preconditions.checkState(newRemaining5 >= 0, "Less than 0 remaining futures");
                if (newRemaining5 == 0) {
                    FutureCombiner<V, C> localCombiner5 = this.combiner;
                    if (localCombiner5 == null || localValues == null) {
                        Preconditions.checkState(isDone());
                    } else {
                        set(localCombiner5.combine(localValues));
                    }
                }
                throw th;
            }
        }
    }

    private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
        return new CombinedFuture(futures, allMustSucceed, listenerExecutor, new FutureCombiner<V, List<V>>() { // from class: com.google.common.util.concurrent.Futures.8
            @Override // com.google.common.util.concurrent.Futures.FutureCombiner
            public List<V> combine(List<Optional<V>> values) {
                List<V> result = Lists.newArrayList();
                Iterator i$ = values.iterator();
                while (i$.hasNext()) {
                    Optional<V> element = i$.next();
                    result.add(element != null ? element.orNull() : null);
                }
                return Collections.unmodifiableList(result);
            }
        });
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Futures$MappingCheckedFuture.class */
    private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
        final Function<? super Exception, X> mapper;

        MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
            super(delegate);
            this.mapper = (Function) Preconditions.checkNotNull(mapper);
        }

        @Override // com.google.common.util.concurrent.AbstractCheckedFuture
        protected X mapException(Exception e) {
            return this.mapper.apply(e);
        }
    }
}

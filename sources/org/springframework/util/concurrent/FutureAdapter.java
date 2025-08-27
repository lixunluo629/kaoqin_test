package org.springframework.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.util.Assert;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/FutureAdapter.class */
public abstract class FutureAdapter<T, S> implements Future<T> {
    private final Future<S> adaptee;
    private Object result = null;
    private State state = State.NEW;
    private final Object mutex = new Object();

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/concurrent/FutureAdapter$State.class */
    private enum State {
        NEW,
        SUCCESS,
        FAILURE
    }

    protected abstract T adapt(S s) throws ExecutionException;

    protected FutureAdapter(Future<S> adaptee) {
        Assert.notNull(adaptee, "Delegate must not be null");
        this.adaptee = adaptee;
    }

    protected Future<S> getAdaptee() {
        return this.adaptee;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.adaptee.cancel(mayInterruptIfRunning);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.adaptee.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.adaptee.isDone();
    }

    @Override // java.util.concurrent.Future
    public T get() throws ExecutionException, InterruptedException {
        return adaptInternal(this.adaptee.get());
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return adaptInternal(this.adaptee.get(timeout, unit));
    }

    final T adaptInternal(S s) throws ExecutionException {
        synchronized (this.mutex) {
            switch (this.state) {
                case SUCCESS:
                    return (T) this.result;
                case FAILURE:
                    throw ((ExecutionException) this.result);
                case NEW:
                    try {
                        try {
                            T tAdapt = adapt(s);
                            this.result = tAdapt;
                            this.state = State.SUCCESS;
                            return tAdapt;
                        } catch (ExecutionException e) {
                            this.result = e;
                            this.state = State.FAILURE;
                            throw e;
                        }
                    } catch (Throwable th) {
                        ExecutionException executionException = new ExecutionException(th);
                        this.result = executionException;
                        this.state = State.FAILURE;
                        throw executionException;
                    }
                default:
                    throw new IllegalStateException();
            }
        }
    }
}

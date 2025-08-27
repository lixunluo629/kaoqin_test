package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/AbstractIdleService.class */
public abstract class AbstractIdleService implements Service {
    private final Supplier<String> threadNameSupplier = new Supplier<String>() { // from class: com.google.common.util.concurrent.AbstractIdleService.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.base.Supplier
        public String get() {
            String strValueOf = String.valueOf(String.valueOf(AbstractIdleService.this.serviceName()));
            String strValueOf2 = String.valueOf(String.valueOf(AbstractIdleService.this.state()));
            return new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(SymbolConstants.SPACE_SYMBOL).append(strValueOf2).toString();
        }
    };
    private final Service delegate = new AbstractService() { // from class: com.google.common.util.concurrent.AbstractIdleService.2
        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), (Supplier<String>) AbstractIdleService.this.threadNameSupplier).execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractIdleService.2.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AbstractIdleService.this.startUp();
                        notifyStarted();
                    } catch (Throwable t) {
                        notifyFailed(t);
                        throw Throwables.propagate(t);
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), (Supplier<String>) AbstractIdleService.this.threadNameSupplier).execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractIdleService.2.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AbstractIdleService.this.shutDown();
                        notifyStopped();
                    } catch (Throwable t) {
                        notifyFailed(t);
                        throw Throwables.propagate(t);
                    }
                }
            });
        }
    };

    protected abstract void startUp() throws Exception;

    protected abstract void shutDown() throws Exception;

    protected AbstractIdleService() {
    }

    protected Executor executor() {
        return new Executor() { // from class: com.google.common.util.concurrent.AbstractIdleService.3
            @Override // java.util.concurrent.Executor
            public void execute(Runnable command) {
                MoreExecutors.newThread((String) AbstractIdleService.this.threadNameSupplier.get(), command).start();
            }
        };
    }

    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(serviceName()));
        String strValueOf2 = String.valueOf(String.valueOf(state()));
        return new StringBuilder(3 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" [").append(strValueOf2).append("]").toString();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }
}

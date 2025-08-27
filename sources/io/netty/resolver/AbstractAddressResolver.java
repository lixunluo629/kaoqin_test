package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.net.SocketAddress;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Collections;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/AbstractAddressResolver.class */
public abstract class AbstractAddressResolver<T extends SocketAddress> implements AddressResolver<T> {
    private final EventExecutor executor;
    private final TypeParameterMatcher matcher;

    protected abstract boolean doIsResolved(T t);

    protected abstract void doResolve(T t, Promise<T> promise) throws Exception;

    protected abstract void doResolveAll(T t, Promise<List<T>> promise) throws Exception;

    protected AbstractAddressResolver(EventExecutor executor) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.find(this, AbstractAddressResolver.class, "T");
    }

    protected AbstractAddressResolver(EventExecutor executor, Class<? extends T> addressType) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.get(addressType);
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    @Override // io.netty.resolver.AddressResolver
    public boolean isSupported(SocketAddress address) {
        return this.matcher.match(address);
    }

    @Override // io.netty.resolver.AddressResolver
    public final boolean isResolved(SocketAddress address) {
        if (!isSupported(address)) {
            throw new UnsupportedAddressTypeException();
        }
        return doIsResolved(address);
    }

    @Override // io.netty.resolver.AddressResolver
    public final Future<T> resolve(SocketAddress address) {
        if (!isSupported((SocketAddress) ObjectUtil.checkNotNull(address, "address"))) {
            return executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (isResolved(address)) {
            return this.executor.newSucceededFuture(address);
        }
        try {
            Promise<T> promise = executor().newPromise();
            doResolve(address, promise);
            return promise;
        } catch (Exception e) {
            return executor().newFailedFuture(e);
        }
    }

    @Override // io.netty.resolver.AddressResolver
    public final Future<T> resolve(SocketAddress address, Promise<T> promise) {
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isSupported(address)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (isResolved(address)) {
            return promise.setSuccess(address);
        }
        try {
            doResolve(address, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.resolver.AddressResolver
    public final Future<List<T>> resolveAll(SocketAddress address) {
        if (!isSupported((SocketAddress) ObjectUtil.checkNotNull(address, "address"))) {
            return executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (isResolved(address)) {
            return this.executor.newSucceededFuture(Collections.singletonList(address));
        }
        try {
            Promise<List<T>> promise = executor().newPromise();
            doResolveAll(address, promise);
            return promise;
        } catch (Exception e) {
            return executor().newFailedFuture(e);
        }
    }

    @Override // io.netty.resolver.AddressResolver
    public final Future<List<T>> resolveAll(SocketAddress address, Promise<List<T>> promise) {
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isSupported(address)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (isResolved(address)) {
            return promise.setSuccess(Collections.singletonList(address));
        }
        try {
            doResolveAll(address, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.resolver.AddressResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}

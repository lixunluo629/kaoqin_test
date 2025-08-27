package io.netty.resolver;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.io.Closeable;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/NameResolver.class */
public interface NameResolver<T> extends Closeable {
    Future<T> resolve(String str);

    Future<T> resolve(String str, Promise<T> promise);

    Future<List<T>> resolveAll(String str);

    Future<List<T>> resolveAll(String str, Promise<List<T>> promise);

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}

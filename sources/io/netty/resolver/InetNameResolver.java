package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/InetNameResolver.class */
public abstract class InetNameResolver extends SimpleNameResolver<InetAddress> {
    private volatile AddressResolver<InetSocketAddress> addressResolver;

    protected InetNameResolver(EventExecutor executor) {
        super(executor);
    }

    public AddressResolver<InetSocketAddress> asAddressResolver() {
        AddressResolver<InetSocketAddress> result = this.addressResolver;
        if (result == null) {
            synchronized (this) {
                result = this.addressResolver;
                if (result == null) {
                    InetSocketAddressResolver inetSocketAddressResolver = new InetSocketAddressResolver(executor(), this);
                    result = inetSocketAddressResolver;
                    this.addressResolver = inetSocketAddressResolver;
                }
            }
        }
        return result;
    }
}

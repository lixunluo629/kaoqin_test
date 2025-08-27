package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/AuthoritativeDnsServerCache.class */
public interface AuthoritativeDnsServerCache {
    DnsServerAddressStream get(String str);

    void cache(String str, InetSocketAddress inetSocketAddress, long j, EventLoop eventLoop);

    void clear();

    boolean clear(String str);
}

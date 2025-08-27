package io.netty.resolver.dns;

import java.net.InetAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsCacheEntry.class */
public interface DnsCacheEntry {
    InetAddress address();

    Throwable cause();
}

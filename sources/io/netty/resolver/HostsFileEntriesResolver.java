package io.netty.resolver;

import java.net.InetAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/HostsFileEntriesResolver.class */
public interface HostsFileEntriesResolver {
    public static final HostsFileEntriesResolver DEFAULT = new DefaultHostsFileEntriesResolver();

    InetAddress address(String str, ResolvedAddressTypes resolvedAddressTypes);
}

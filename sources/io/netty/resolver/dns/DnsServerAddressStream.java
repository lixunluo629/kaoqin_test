package io.netty.resolver.dns;

import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsServerAddressStream.class */
public interface DnsServerAddressStream {
    InetSocketAddress next();

    int size();

    DnsServerAddressStream duplicate();
}

package io.netty.resolver.dns;

import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/SingletonDnsServerAddressStreamProvider.class */
public final class SingletonDnsServerAddressStreamProvider extends UniSequentialDnsServerAddressStreamProvider {
    public SingletonDnsServerAddressStreamProvider(InetSocketAddress address) {
        super(DnsServerAddresses.singleton(address));
    }
}

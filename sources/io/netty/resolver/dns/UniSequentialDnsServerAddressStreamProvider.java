package io.netty.resolver.dns;

import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/UniSequentialDnsServerAddressStreamProvider.class */
abstract class UniSequentialDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private final DnsServerAddresses addresses;

    UniSequentialDnsServerAddressStreamProvider(DnsServerAddresses addresses) {
        this.addresses = (DnsServerAddresses) ObjectUtil.checkNotNull(addresses, "addresses");
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    public final DnsServerAddressStream nameServerAddressStream(String hostname) {
        return this.addresses.stream();
    }
}

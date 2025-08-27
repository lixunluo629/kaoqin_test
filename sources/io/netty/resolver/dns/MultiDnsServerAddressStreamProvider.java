package io.netty.resolver.dns;

import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/MultiDnsServerAddressStreamProvider.class */
public final class MultiDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private final DnsServerAddressStreamProvider[] providers;

    public MultiDnsServerAddressStreamProvider(List<DnsServerAddressStreamProvider> providers) {
        this.providers = (DnsServerAddressStreamProvider[]) providers.toArray(new DnsServerAddressStreamProvider[0]);
    }

    public MultiDnsServerAddressStreamProvider(DnsServerAddressStreamProvider... providers) {
        this.providers = (DnsServerAddressStreamProvider[]) providers.clone();
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    public DnsServerAddressStream nameServerAddressStream(String hostname) {
        for (DnsServerAddressStreamProvider provider : this.providers) {
            DnsServerAddressStream stream = provider.nameServerAddressStream(hostname);
            if (stream != null) {
                return stream;
            }
        }
        return null;
    }
}

package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.util.concurrent.Promise;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsAddressResolveContext.class */
final class DnsAddressResolveContext extends DnsResolveContext<InetAddress> {
    private final DnsCache resolveCache;
    private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
    private final boolean completeEarlyIfPossible;

    DnsAddressResolveContext(DnsNameResolver parent, Promise<?> originalPromise, String hostname, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache, boolean completeEarlyIfPossible) {
        super(parent, originalPromise, hostname, 1, parent.resolveRecordTypes(), additionals, nameServerAddrs);
        this.resolveCache = resolveCache;
        this.authoritativeDnsServerCache = authoritativeDnsServerCache;
        this.completeEarlyIfPossible = completeEarlyIfPossible;
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsResolveContext<InetAddress> newResolverContext(DnsNameResolver parent, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
        return new DnsAddressResolveContext(parent, originalPromise, hostname, additionals, nameServerAddrs, this.resolveCache, this.authoritativeDnsServerCache, this.completeEarlyIfPossible);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public InetAddress convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop) {
        return DnsAddressDecoder.decodeAddress(record, hostname, this.parent.isDecodeIdn());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    List<InetAddress> filterResults(List<InetAddress> unfiltered) {
        Collections.sort(unfiltered, PreferredAddressTypeComparator.comparator(this.parent.preferredAddressType()));
        return unfiltered;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public boolean isCompleteEarly(InetAddress resolved) {
        return this.completeEarlyIfPossible && this.parent.preferredAddressType().addressType() == resolved.getClass();
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    boolean isDuplicateAllowed() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public void cache(String hostname, DnsRecord[] additionals, DnsRecord result, InetAddress convertedResult) {
        this.resolveCache.cache(hostname, additionals, convertedResult, result.timeToLive(), this.parent.f7ch.eventLoop());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause) {
        this.resolveCache.cache(hostname, additionals, cause, this.parent.f7ch.eventLoop());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    void doSearchDomainQuery(String hostname, Promise<List<InetAddress>> nextPromise) {
        if (!DnsNameResolver.doResolveAllCached(hostname, this.additionals, nextPromise, this.resolveCache, this.parent.resolvedInternetProtocolFamiliesUnsafe())) {
            super.doSearchDomainQuery(hostname, nextPromise);
        }
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsCache resolveCache() {
        return this.resolveCache;
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }
}

package io.netty.resolver.dns;

import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.concurrent.Promise;
import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DatagramDnsQueryContext.class */
final class DatagramDnsQueryContext extends DnsQueryContext {
    DatagramDnsQueryContext(DnsNameResolver parent, InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise) {
        super(parent, nameServerAddr, question, additionals, promise);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected DnsQuery newQuery(int id) {
        return new DatagramDnsQuery(null, nameServerAddr(), id);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected Channel channel() {
        return parent().f7ch;
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected String protocol() {
        return RtspHeaders.Values.UDP;
    }
}

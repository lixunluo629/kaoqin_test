package io.netty.resolver.dns;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import java.net.InetSocketAddress;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsQueryLifecycleObserver.class */
public interface DnsQueryLifecycleObserver {
    void queryWritten(InetSocketAddress inetSocketAddress, ChannelFuture channelFuture);

    void queryCancelled(int i);

    DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> list);

    DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion dnsQuestion);

    DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode dnsResponseCode);

    void queryFailed(Throwable th);

    void querySucceed();
}

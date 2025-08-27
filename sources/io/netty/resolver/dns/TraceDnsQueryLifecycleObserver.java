package io.netty.resolver.dns;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import java.net.InetSocketAddress;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/TraceDnsQueryLifecycleObserver.class */
final class TraceDnsQueryLifecycleObserver implements DnsQueryLifecycleObserver {
    private final InternalLogger logger;
    private final InternalLogLevel level;
    private final DnsQuestion question;
    private InetSocketAddress dnsServerAddress;

    TraceDnsQueryLifecycleObserver(DnsQuestion question, InternalLogger logger, InternalLogLevel level) {
        this.question = (DnsQuestion) ObjectUtil.checkNotNull(question, "question");
        this.logger = (InternalLogger) ObjectUtil.checkNotNull(logger, "logger");
        this.level = (InternalLogLevel) ObjectUtil.checkNotNull(level, "level");
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {
        this.dnsServerAddress = dnsServerAddress;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryCancelled(int queriesRemaining) {
        if (this.dnsServerAddress != null) {
            this.logger.log(this.level, "from {} : {} cancelled with {} queries remaining", this.dnsServerAddress, this.question, Integer.valueOf(queriesRemaining));
        } else {
            this.logger.log(this.level, "{} query never written and cancelled with {} queries remaining", this.question, Integer.valueOf(queriesRemaining));
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
        this.logger.log(this.level, "from {} : {} redirected", this.dnsServerAddress, this.question);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
        this.logger.log(this.level, "from {} : {} CNAME question {}", this.dnsServerAddress, this.question, cnameQuestion);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
        this.logger.log(this.level, "from {} : {} no answer {}", this.dnsServerAddress, this.question, code);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryFailed(Throwable cause) {
        if (this.dnsServerAddress != null) {
            this.logger.log(this.level, "from {} : {} failure", this.dnsServerAddress, this.question, cause);
        } else {
            this.logger.log(this.level, "{} query never written and failed", this.question, cause);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void querySucceed() {
    }
}

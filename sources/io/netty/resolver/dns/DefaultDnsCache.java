package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DefaultDnsCache.class */
public class DefaultDnsCache implements DnsCache {
    private final Cache<DefaultDnsCacheEntry> resolveCache;
    private final int minTtl;
    private final int maxTtl;
    private final int negativeTtl;

    public DefaultDnsCache() {
        this(0, Cache.MAX_SUPPORTED_TTL_SECS, 0);
    }

    public DefaultDnsCache(int minTtl, int maxTtl, int negativeTtl) {
        this.resolveCache = new Cache<DefaultDnsCacheEntry>() { // from class: io.netty.resolver.dns.DefaultDnsCache.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean shouldReplaceAll(DefaultDnsCacheEntry entry) {
                return entry.cause() != null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean equals(DefaultDnsCacheEntry entry, DefaultDnsCacheEntry otherEntry) {
                if (entry.address() != null) {
                    return entry.address().equals(otherEntry.address());
                }
                if (otherEntry.address() != null) {
                    return false;
                }
                return entry.cause().equals(otherEntry.cause());
            }
        };
        this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
        this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl"));
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
        }
        this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
    }

    public int minTtl() {
        return this.minTtl;
    }

    public int maxTtl() {
        return this.maxTtl;
    }

    public int negativeTtl() {
        return this.negativeTtl;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public void clear() {
        this.resolveCache.clear();
    }

    @Override // io.netty.resolver.dns.DnsCache
    public boolean clear(String hostname) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        return this.resolveCache.clear(appendDot(hostname));
    }

    private static boolean emptyAdditionals(DnsRecord[] additionals) {
        return additionals == null || additionals.length == 0;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        if (!emptyAdditionals(additionals)) {
            return Collections.emptyList();
        }
        return this.resolveCache.get(appendDot(hostname));
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, InetAddress address, long originalTtl, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(loop, "loop");
        DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, address);
        if (this.maxTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.resolveCache.cache(appendDot(hostname), e, Math.max(this.minTtl, (int) Math.min(this.maxTtl, originalTtl)), loop);
        return e;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, Throwable cause, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(cause, "cause");
        ObjectUtil.checkNotNull(loop, "loop");
        DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, cause);
        if (this.negativeTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.resolveCache.cache(appendDot(hostname), e, this.negativeTtl, loop);
        return e;
    }

    public String toString() {
        return "DefaultDnsCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", negativeTtl=" + this.negativeTtl + ", cached resolved hostname=" + this.resolveCache.size() + ')';
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DefaultDnsCache$DefaultDnsCacheEntry.class */
    private static final class DefaultDnsCacheEntry implements DnsCacheEntry {
        private final String hostname;
        private final InetAddress address;
        private final Throwable cause;

        DefaultDnsCacheEntry(String hostname, InetAddress address) {
            this.hostname = hostname;
            this.address = address;
            this.cause = null;
        }

        DefaultDnsCacheEntry(String hostname, Throwable cause) {
            this.hostname = hostname;
            this.cause = cause;
            this.address = null;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public InetAddress address() {
            return this.address;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public Throwable cause() {
            return this.cause;
        }

        String hostname() {
            return this.hostname;
        }

        public String toString() {
            if (this.cause != null) {
                return this.hostname + '/' + this.cause;
            }
            return this.address.toString();
        }
    }

    private static String appendDot(String hostname) {
        return StringUtil.endsWith(hostname, '.') ? hostname : hostname + '.';
    }
}

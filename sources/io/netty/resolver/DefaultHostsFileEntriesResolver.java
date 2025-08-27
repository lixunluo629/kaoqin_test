package io.netty.resolver;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/DefaultHostsFileEntriesResolver.class */
public final class DefaultHostsFileEntriesResolver implements HostsFileEntriesResolver {
    private final Map<String, Inet4Address> inet4Entries;
    private final Map<String, Inet6Address> inet6Entries;

    public DefaultHostsFileEntriesResolver() {
        this(parseEntries());
    }

    DefaultHostsFileEntriesResolver(HostsFileEntries entries) {
        this.inet4Entries = entries.inet4Entries();
        this.inet6Entries = entries.inet6Entries();
    }

    @Override // io.netty.resolver.HostsFileEntriesResolver
    public InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes) {
        String normalized = normalize(inetHost);
        switch (resolvedAddressTypes) {
            case IPV4_ONLY:
                return this.inet4Entries.get(normalized);
            case IPV6_ONLY:
                return this.inet6Entries.get(normalized);
            case IPV4_PREFERRED:
                Inet4Address inet4Address = this.inet4Entries.get(normalized);
                return inet4Address != null ? inet4Address : this.inet6Entries.get(normalized);
            case IPV6_PREFERRED:
                Inet6Address inet6Address = this.inet6Entries.get(normalized);
                return inet6Address != null ? inet6Address : this.inet4Entries.get(normalized);
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
    }

    String normalize(String inetHost) {
        return inetHost.toLowerCase(Locale.ENGLISH);
    }

    private static HostsFileEntries parseEntries() {
        return PlatformDependent.isWindows() ? HostsFileParser.parseSilently(Charset.defaultCharset(), CharsetUtil.UTF_16, CharsetUtil.UTF_8) : HostsFileParser.parseSilently();
    }
}

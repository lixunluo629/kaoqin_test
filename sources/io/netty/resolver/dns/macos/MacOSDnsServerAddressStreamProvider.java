package io.netty.resolver.dns.macos;

import io.netty.resolver.dns.DnsServerAddressStream;
import io.netty.resolver.dns.DnsServerAddressStreamProvider;
import io.netty.resolver.dns.DnsServerAddressStreamProviders;
import io.netty.resolver.dns.DnsServerAddresses;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/macos/MacOSDnsServerAddressStreamProvider.class */
public final class MacOSDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private static final Throwable UNAVAILABILITY_CAUSE;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) MacOSDnsServerAddressStreamProvider.class);
    private static final long REFRESH_INTERVAL = TimeUnit.SECONDS.toNanos(10);
    private volatile Map<String, DnsServerAddresses> currentMappings = retrieveCurrentMappings();
    private final AtomicLong lastRefresh = new AtomicLong(System.nanoTime());

    private static native DnsResolver[] resolvers();

    static {
        Throwable cause = null;
        try {
            loadNativeLibrary();
        } catch (Throwable error) {
            cause = error;
        }
        UNAVAILABILITY_CAUSE = cause;
    }

    private static void loadNativeLibrary() throws IOException {
        String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        if (!name.startsWith("mac")) {
            throw new IllegalStateException("Only supported on MacOS");
        }
        String sharedLibName = "netty_resolver_dns_native_macos_" + PlatformDependent.normalizedArch();
        ClassLoader cl = PlatformDependent.getClassLoader(MacOSDnsServerAddressStreamProvider.class);
        try {
            NativeLibraryLoader.load(sharedLibName, cl);
        } catch (UnsatisfiedLinkError e1) {
            try {
                NativeLibraryLoader.load("netty_resolver_dns_native_macos", cl);
                logger.debug("Failed to load {}", sharedLibName, e1);
            } catch (UnsatisfiedLinkError e2) {
                ThrowableUtil.addSuppressed(e1, e2);
                throw e1;
            }
        }
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw ((Error) new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }

    public MacOSDnsServerAddressStreamProvider() {
        ensureAvailability();
    }

    private static Map<String, DnsServerAddresses> retrieveCurrentMappings() {
        InetSocketAddress[] nameservers;
        DnsResolver[] resolvers = resolvers();
        if (resolvers == null || resolvers.length == 0) {
            return Collections.emptyMap();
        }
        Map<String, DnsServerAddresses> resolverMap = new HashMap<>(resolvers.length);
        for (DnsResolver resolver : resolvers) {
            if (!"mdns".equalsIgnoreCase(resolver.options()) && (nameservers = resolver.nameservers()) != null && nameservers.length != 0) {
                String domain = resolver.domain();
                if (domain == null) {
                    domain = "";
                }
                InetSocketAddress[] servers = resolver.nameservers();
                for (int a = 0; a < servers.length; a++) {
                    InetSocketAddress address = servers[a];
                    if (address.getPort() == 0) {
                        int port = resolver.port();
                        if (port == 0) {
                            port = 53;
                        }
                        servers[a] = new InetSocketAddress(address.getAddress(), port);
                    }
                }
                resolverMap.put(domain, DnsServerAddresses.sequential(servers));
            }
        }
        return resolverMap;
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    public DnsServerAddressStream nameServerAddressStream(String hostname) {
        long last = this.lastRefresh.get();
        Map<String, DnsServerAddresses> resolverMap = this.currentMappings;
        if (System.nanoTime() - last > REFRESH_INTERVAL && this.lastRefresh.compareAndSet(last, System.nanoTime())) {
            Map<String, DnsServerAddresses> mapRetrieveCurrentMappings = retrieveCurrentMappings();
            this.currentMappings = mapRetrieveCurrentMappings;
            resolverMap = mapRetrieveCurrentMappings;
        }
        while (true) {
            int i = hostname.indexOf(46, 1);
            if (i < 0 || i == hostname.length() - 1) {
                break;
            }
            DnsServerAddresses addresses = resolverMap.get(hostname);
            if (addresses != null) {
                return addresses.stream();
            }
            hostname = hostname.substring(i + 1);
        }
        DnsServerAddresses addresses2 = resolverMap.get("");
        if (addresses2 == null) {
            return DnsServerAddressStreamProviders.unixDefault().nameServerAddressStream(hostname);
        }
        return addresses2.stream();
    }
}

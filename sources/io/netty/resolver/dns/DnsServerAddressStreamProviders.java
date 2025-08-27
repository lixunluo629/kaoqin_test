package io.netty.resolver.dns;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsServerAddressStreamProviders.class */
public final class DnsServerAddressStreamProviders {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance((Class<?>) DnsServerAddressStreamProviders.class);
    private static final Constructor<? extends DnsServerAddressStreamProvider> STREAM_PROVIDER_CONSTRUCTOR;

    static {
        Constructor<? extends DnsServerAddressStreamProvider> constructor = null;
        if (PlatformDependent.isOsx()) {
            try {
                Object maybeProvider = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.resolver.dns.DnsServerAddressStreamProviders.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            return Class.forName("io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider", true, DnsServerAddressStreamProviders.class.getClassLoader());
                        } catch (Throwable cause) {
                            return cause;
                        }
                    }
                });
                if (maybeProvider instanceof Class) {
                    Class<? extends DnsServerAddressStreamProvider> providerClass = (Class) maybeProvider;
                    Method method = providerClass.getMethod("ensureAvailability", new Class[0]);
                    method.invoke(null, new Object[0]);
                    constructor = providerClass.getConstructor(new Class[0]);
                    constructor.newInstance(new Object[0]);
                } else if (!(maybeProvider instanceof ClassNotFoundException)) {
                    throw ((Throwable) maybeProvider);
                }
            } catch (Throwable cause) {
                LOGGER.debug("Unable to use MacOSDnsServerAddressStreamProvider, fallback to system defaults", cause);
                constructor = null;
            }
        }
        STREAM_PROVIDER_CONSTRUCTOR = constructor;
    }

    private DnsServerAddressStreamProviders() {
    }

    public static DnsServerAddressStreamProvider platformDefault() {
        if (STREAM_PROVIDER_CONSTRUCTOR != null) {
            try {
                return STREAM_PROVIDER_CONSTRUCTOR.newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
        return unixDefault();
    }

    public static DnsServerAddressStreamProvider unixDefault() {
        return DefaultProviderHolder.DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsServerAddressStreamProviders$DefaultProviderHolder.class */
    private static final class DefaultProviderHolder {
        private static final long REFRESH_INTERVAL = TimeUnit.MINUTES.toNanos(5);
        static final DnsServerAddressStreamProvider DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER = new DnsServerAddressStreamProvider() { // from class: io.netty.resolver.dns.DnsServerAddressStreamProviders.DefaultProviderHolder.1
            private volatile DnsServerAddressStreamProvider currentProvider = provider();
            private final AtomicLong lastRefresh = new AtomicLong(System.nanoTime());

            @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
            public DnsServerAddressStream nameServerAddressStream(String hostname) {
                long last = this.lastRefresh.get();
                DnsServerAddressStreamProvider current = this.currentProvider;
                if (System.nanoTime() - last > DefaultProviderHolder.REFRESH_INTERVAL && this.lastRefresh.compareAndSet(last, System.nanoTime())) {
                    DnsServerAddressStreamProvider dnsServerAddressStreamProviderProvider = provider();
                    this.currentProvider = dnsServerAddressStreamProviderProvider;
                    current = dnsServerAddressStreamProviderProvider;
                }
                return current.nameServerAddressStream(hostname);
            }

            private DnsServerAddressStreamProvider provider() {
                return PlatformDependent.isWindows() ? DefaultDnsServerAddressStreamProvider.INSTANCE : UnixResolverDnsServerAddressStreamProvider.parseSilently();
            }
        };

        private DefaultProviderHolder() {
        }
    }
}

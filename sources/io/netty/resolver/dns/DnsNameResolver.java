package io.netty.resolver.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.TcpDnsQueryEncoder;
import io.netty.handler.codec.dns.TcpDnsResponseDecoder;
import io.netty.resolver.HostsFileEntriesResolver;
import io.netty.resolver.InetNameResolver;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.util.NetUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import java.net.IDN;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsNameResolver.class */
public class DnsNameResolver extends InetNameResolver {
    private static final InternalLogger logger;
    private static final String LOCALHOST = "localhost";
    private static final InetAddress LOCALHOST_ADDRESS;
    private static final DnsRecord[] EMPTY_ADDITIONALS;
    private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
    static final String[] DEFAULT_SEARCH_DOMAINS;
    private static final UnixResolverOptions DEFAULT_OPTIONS;
    private static final DatagramDnsResponseDecoder DATAGRAM_DECODER;
    private static final DatagramDnsQueryEncoder DATAGRAM_ENCODER;
    private static final TcpDnsQueryEncoder TCP_ENCODER;
    final Future<Channel> channelFuture;

    /* renamed from: ch, reason: collision with root package name */
    final Channel f7ch;
    private final Comparator<InetSocketAddress> nameServerComparator;
    final DnsQueryContextManager queryContextManager;
    private final DnsCache resolveCache;
    private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
    private final DnsCnameCache cnameCache;
    private final FastThreadLocal<DnsServerAddressStream> nameServerAddrStream;
    private final long queryTimeoutMillis;
    private final int maxQueriesPerResolve;
    private final ResolvedAddressTypes resolvedAddressTypes;
    private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
    private final boolean recursionDesired;
    private final int maxPayloadSize;
    private final boolean optResourceEnabled;
    private final HostsFileEntriesResolver hostsFileEntriesResolver;
    private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
    private final String[] searchDomains;
    private final int ndots;
    private final boolean supportsAAAARecords;
    private final boolean supportsARecords;
    private final InternetProtocolFamily preferredAddressType;
    private final DnsRecordType[] resolveRecordTypes;
    private final boolean decodeIdn;
    private final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory;
    private final boolean completeOncePreferredResolved;
    private final ChannelFactory<? extends SocketChannel> socketChannelFactory;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        String[] searchDomains;
        UnixResolverOptions options;
        List<String> etcResolverSearchDomains;
        $assertionsDisabled = !DnsNameResolver.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) DnsNameResolver.class);
        EMPTY_ADDITIONALS = new DnsRecord[0];
        IPV4_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[]{DnsRecordType.A};
        IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[]{InternetProtocolFamily.IPv4};
        IPV4_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[]{DnsRecordType.A, DnsRecordType.AAAA};
        IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[]{InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6};
        IPV6_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[]{DnsRecordType.AAAA};
        IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[]{InternetProtocolFamily.IPv6};
        IPV6_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[]{DnsRecordType.AAAA, DnsRecordType.A};
        IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[]{InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4};
        if (NetUtil.isIpV4StackPreferred() || !anyInterfaceSupportsIpV6()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        } else if (NetUtil.isIpV6AddressesPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST6;
        } else {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        }
        try {
            if (PlatformDependent.isWindows()) {
                etcResolverSearchDomains = getSearchDomainsHack();
            } else {
                etcResolverSearchDomains = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverSearchDomains();
            }
            List<String> list = etcResolverSearchDomains;
            searchDomains = (String[]) list.toArray(new String[0]);
        } catch (Exception e) {
            searchDomains = EmptyArrays.EMPTY_STRINGS;
        }
        DEFAULT_SEARCH_DOMAINS = searchDomains;
        try {
            options = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverOptions();
        } catch (Exception e2) {
            options = UnixResolverOptions.newBuilder().build();
        }
        DEFAULT_OPTIONS = options;
        DATAGRAM_DECODER = new DatagramDnsResponseDecoder() { // from class: io.netty.resolver.dns.DnsNameResolver.1
            @Override // io.netty.handler.codec.dns.DatagramDnsResponseDecoder
            protected DnsResponse decodeResponse(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                DnsResponse response = super.decodeResponse(ctx, packet);
                if (((ByteBuf) packet.content()).isReadable()) {
                    response.setTruncated(true);
                    if (DnsNameResolver.logger.isDebugEnabled()) {
                        DnsNameResolver.logger.debug("{} RECEIVED: UDP truncated packet received, consider adjusting maxPayloadSize for the {}.", ctx.channel(), StringUtil.simpleClassName((Class<?>) DnsNameResolver.class));
                    }
                }
                return response;
            }
        };
        DATAGRAM_ENCODER = new DatagramDnsQueryEncoder();
        TCP_ENCODER = new TcpDnsQueryEncoder();
    }

    private static boolean anyInterfaceSupportsIpV6() throws SocketException {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if ((inetAddress instanceof Inet6Address) && !inetAddress.isAnyLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SocketException e) {
            logger.debug("Unable to detect if any interface supports IPv6, assuming IPv4-only", (Throwable) e);
            return false;
        }
    }

    private static List<String> getSearchDomainsHack() throws Exception {
        if (PlatformDependent.javaVersion() < 9) {
            Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
            Method open = configClass.getMethod("open", new Class[0]);
            Method nameservers = configClass.getMethod("searchlist", new Class[0]);
            Object instance = open.invoke(null, new Object[0]);
            return (List) nameservers.invoke(instance, new Object[0]);
        }
        return Collections.emptyList();
    }

    @Deprecated
    public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, DnsCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn) {
        this(eventLoop, channelFactory, resolveCache, new AuthoritativeDnsServerCacheAdapter(authoritativeDnsServerCache), dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, searchDomains, ndots, decodeIdn);
    }

    @Deprecated
    public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn) {
        this(eventLoop, channelFactory, null, resolveCache, NoopDnsCnameCache.INSTANCE, authoritativeDnsServerCache, dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, searchDomains, ndots, decodeIdn, false);
    }

    DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, ChannelFactory<? extends SocketChannel> socketChannelFactory, final DnsCache resolveCache, final DnsCnameCache cnameCache, final AuthoritativeDnsServerCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn, boolean completeOncePreferredResolved) {
        DnsQueryLifecycleObserverFactory traceDnsQueryLifeCycleObserverFactory;
        super(eventLoop);
        this.queryContextManager = new DnsQueryContextManager();
        this.nameServerAddrStream = new FastThreadLocal<DnsServerAddressStream>() { // from class: io.netty.resolver.dns.DnsNameResolver.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public DnsServerAddressStream initialValue() {
                return DnsNameResolver.this.dnsServerAddressStreamProvider.nameServerAddressStream("");
            }
        };
        this.queryTimeoutMillis = queryTimeoutMillis > 0 ? queryTimeoutMillis : TimeUnit.SECONDS.toMillis(DEFAULT_OPTIONS.timeout());
        this.resolvedAddressTypes = resolvedAddressTypes != null ? resolvedAddressTypes : DEFAULT_RESOLVE_ADDRESS_TYPES;
        this.recursionDesired = recursionDesired;
        this.maxQueriesPerResolve = maxQueriesPerResolve > 0 ? maxQueriesPerResolve : DEFAULT_OPTIONS.attempts();
        this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
        this.optResourceEnabled = optResourceEnabled;
        this.hostsFileEntriesResolver = (HostsFileEntriesResolver) ObjectUtil.checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver");
        this.dnsServerAddressStreamProvider = (DnsServerAddressStreamProvider) ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
        this.resolveCache = (DnsCache) ObjectUtil.checkNotNull(resolveCache, "resolveCache");
        this.cnameCache = (DnsCnameCache) ObjectUtil.checkNotNull(cnameCache, "cnameCache");
        if (traceEnabled) {
            traceDnsQueryLifeCycleObserverFactory = dnsQueryLifecycleObserverFactory instanceof NoopDnsQueryLifecycleObserverFactory ? new TraceDnsQueryLifeCycleObserverFactory() : new BiDnsQueryLifecycleObserverFactory(new TraceDnsQueryLifeCycleObserverFactory(), dnsQueryLifecycleObserverFactory);
        } else {
            traceDnsQueryLifeCycleObserverFactory = (DnsQueryLifecycleObserverFactory) ObjectUtil.checkNotNull(dnsQueryLifecycleObserverFactory, "dnsQueryLifecycleObserverFactory");
        }
        this.dnsQueryLifecycleObserverFactory = traceDnsQueryLifeCycleObserverFactory;
        this.searchDomains = searchDomains != null ? (String[]) searchDomains.clone() : DEFAULT_SEARCH_DOMAINS;
        this.ndots = ndots >= 0 ? ndots : DEFAULT_OPTIONS.ndots();
        this.decodeIdn = decodeIdn;
        this.completeOncePreferredResolved = completeOncePreferredResolved;
        this.socketChannelFactory = socketChannelFactory;
        switch (this.resolvedAddressTypes) {
            case IPV4_ONLY:
                this.supportsAAAARecords = false;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV4_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV4_PREFERRED:
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV6_ONLY:
                this.supportsAAAARecords = true;
                this.supportsARecords = false;
                this.resolveRecordTypes = IPV6_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV6_PREFERRED:
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                break;
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
        this.preferredAddressType = preferredAddressType(this.resolvedAddressTypes);
        this.authoritativeDnsServerCache = (AuthoritativeDnsServerCache) ObjectUtil.checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache");
        this.nameServerComparator = new NameServerComparator(this.preferredAddressType.addressType());
        Bootstrap b = new Bootstrap();
        b.group(executor());
        b.channelFactory((ChannelFactory) channelFactory);
        b.option(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, true);
        final DnsResponseHandler responseHandler = new DnsResponseHandler(executor().newPromise());
        b.handler(new ChannelInitializer<DatagramChannel>() { // from class: io.netty.resolver.dns.DnsNameResolver.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.channel.ChannelInitializer
            public void initChannel(DatagramChannel ch2) {
                ch2.pipeline().addLast(DnsNameResolver.DATAGRAM_ENCODER, DnsNameResolver.DATAGRAM_DECODER, responseHandler);
            }
        });
        this.channelFuture = responseHandler.channelActivePromise;
        ChannelFuture future = b.register();
        Throwable cause = future.cause();
        if (cause != null) {
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new IllegalStateException("Unable to create / register Channel", cause);
        }
        this.f7ch = future.channel();
        this.f7ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(maxPayloadSize));
        this.f7ch.closeFuture().addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsNameResolver.4
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future2) {
                resolveCache.clear();
                cnameCache.clear();
                authoritativeDnsServerCache.clear();
            }
        });
    }

    static InternetProtocolFamily preferredAddressType(ResolvedAddressTypes resolvedAddressTypes) {
        switch (resolvedAddressTypes) {
            case IPV4_ONLY:
            case IPV4_PREFERRED:
                return InternetProtocolFamily.IPv4;
            case IPV6_ONLY:
            case IPV6_PREFERRED:
                return InternetProtocolFamily.IPv6;
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
    }

    InetSocketAddress newRedirectServerAddress(InetAddress server) {
        return new InetSocketAddress(server, 53);
    }

    final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory() {
        return this.dnsQueryLifecycleObserverFactory;
    }

    protected DnsServerAddressStream newRedirectDnsServerStream(String hostname, List<InetSocketAddress> nameservers) {
        DnsServerAddressStream cached = authoritativeDnsServerCache().get(hostname);
        if (cached == null || cached.size() == 0) {
            Collections.sort(nameservers, this.nameServerComparator);
            return new SequentialDnsServerAddressStream(nameservers, 0);
        }
        return cached;
    }

    public DnsCache resolveCache() {
        return this.resolveCache;
    }

    DnsCnameCache cnameCache() {
        return this.cnameCache;
    }

    public AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }

    public long queryTimeoutMillis() {
        return this.queryTimeoutMillis;
    }

    public ResolvedAddressTypes resolvedAddressTypes() {
        return this.resolvedAddressTypes;
    }

    InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
        return this.resolvedInternetProtocolFamilies;
    }

    final String[] searchDomains() {
        return this.searchDomains;
    }

    final int ndots() {
        return this.ndots;
    }

    final boolean supportsAAAARecords() {
        return this.supportsAAAARecords;
    }

    final boolean supportsARecords() {
        return this.supportsARecords;
    }

    final InternetProtocolFamily preferredAddressType() {
        return this.preferredAddressType;
    }

    final DnsRecordType[] resolveRecordTypes() {
        return this.resolveRecordTypes;
    }

    final boolean isDecodeIdn() {
        return this.decodeIdn;
    }

    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }

    public int maxQueriesPerResolve() {
        return this.maxQueriesPerResolve;
    }

    public int maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public boolean isOptResourceEnabled() {
        return this.optResourceEnabled;
    }

    public HostsFileEntriesResolver hostsFileEntriesResolver() {
        return this.hostsFileEntriesResolver;
    }

    @Override // io.netty.resolver.SimpleNameResolver, io.netty.resolver.NameResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f7ch.isOpen()) {
            this.f7ch.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.resolver.SimpleNameResolver
    public EventLoop executor() {
        return (EventLoop) super.executor();
    }

    private InetAddress resolveHostsFileEntry(String hostname) {
        if (this.hostsFileEntriesResolver == null) {
            return null;
        }
        InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
        if (address == null && PlatformDependent.isWindows() && "localhost".equalsIgnoreCase(hostname)) {
            return LOCALHOST_ADDRESS;
        }
        return address;
    }

    public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals) {
        return resolve(inetHost, additionals, executor().newPromise());
    }

    public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals, Promise<InetAddress> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            doResolve(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals) {
        return resolveAll(inetHost, additionals, executor().newPromise());
    }

    public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals, Promise<List<InetAddress>> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolve(String inetHost, Promise<InetAddress> promise) throws Exception {
        doResolve(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question) {
        return resolveAll(question, EMPTY_ADDITIONALS, executor().newPromise());
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals) {
        return resolveAll(question, additionals, executor().newPromise());
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals, Promise<List<DnsRecord>> promise) {
        DnsRecord[] additionalsArray = toArray(additionals, true);
        return resolveAll(question, additionalsArray, promise);
    }

    private Future<List<DnsRecord>> resolveAll(DnsQuestion question, DnsRecord[] additionals, Promise<List<DnsRecord>> promise) {
        InetAddress hostsFileEntry;
        ObjectUtil.checkNotNull(question, "question");
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecordType type = question.type();
        String hostname = question.name();
        if ((type == DnsRecordType.A || type == DnsRecordType.AAAA) && (hostsFileEntry = resolveHostsFileEntry(hostname)) != null) {
            ByteBuf content = null;
            if (hostsFileEntry instanceof Inet4Address) {
                if (type == DnsRecordType.A) {
                    content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                }
            } else if ((hostsFileEntry instanceof Inet6Address) && type == DnsRecordType.AAAA) {
                content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
            }
            if (content != null) {
                trySuccess(promise, Collections.singletonList(new DefaultDnsRawRecord(hostname, type, 86400L, content)));
                return promise;
            }
        }
        DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        new DnsRecordResolveContext(this, promise, question, additionals, nameServerAddrs).resolve(promise);
        return promise;
    }

    private static DnsRecord[] toArray(Iterable<DnsRecord> additionals, boolean validateType) {
        ObjectUtil.checkNotNull(additionals, "additionals");
        if (additionals instanceof Collection) {
            Collection<DnsRecord> records = (Collection) additionals;
            Iterator<DnsRecord> it = additionals.iterator();
            while (it.hasNext()) {
                validateAdditional(it.next(), validateType);
            }
            return (DnsRecord[]) records.toArray(new DnsRecord[records.size()]);
        }
        Iterator<DnsRecord> additionalsIt = additionals.iterator();
        if (!additionalsIt.hasNext()) {
            return EMPTY_ADDITIONALS;
        }
        List<DnsRecord> records2 = new ArrayList<>();
        do {
            DnsRecord r = additionalsIt.next();
            validateAdditional(r, validateType);
            records2.add(r);
        } while (additionalsIt.hasNext());
        return (DnsRecord[]) records2.toArray(new DnsRecord[records2.size()]);
    }

    private static void validateAdditional(DnsRecord record, boolean validateType) {
        ObjectUtil.checkNotNull(record, "record");
        if (validateType && (record instanceof DnsRawRecord)) {
            throw new IllegalArgumentException("DnsRawRecord implementations not allowed: " + record);
        }
    }

    private InetAddress loopbackAddress() {
        return preferredAddressType().localhost();
    }

    protected void doResolve(String inetHost, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(loopbackAddress());
            return;
        }
        byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess(InetAddress.getByAddress(bytes));
            return;
        }
        String hostname = hostname(inetHost);
        InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(hostsFileEntry);
        } else if (!doResolveCached(hostname, additionals, promise, resolveCache)) {
            doResolveUncached(hostname, additionals, promise, resolveCache, true);
        }
    }

    private boolean doResolveCached(String hostname, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) {
        List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        Throwable cause = cachedEntries.get(0).cause();
        if (cause == null) {
            int numEntries = cachedEntries.size();
            for (InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
                for (int i = 0; i < numEntries; i++) {
                    DnsCacheEntry e = cachedEntries.get(i);
                    if (f.addressType().isInstance(e.address())) {
                        trySuccess(promise, e.address());
                        return true;
                    }
                }
            }
            return false;
        }
        tryFailure(promise, cause);
        return true;
    }

    static <T> void trySuccess(Promise<T> promise, T result) {
        if (!promise.trySuccess(result)) {
            logger.trace("Failed to notify success ({}) to a promise: {}", result, promise);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void tryFailure(Promise<?> promise, Throwable cause) {
        if (!promise.tryFailure(cause)) {
            logger.trace("Failed to notify failure to a promise: {}", promise, cause);
        }
    }

    private void doResolveUncached(String hostname, DnsRecord[] additionals, final Promise<InetAddress> promise, DnsCache resolveCache, boolean completeEarlyIfPossible) {
        Promise<List<InetAddress>> allPromise = executor().newPromise();
        doResolveAllUncached(hostname, additionals, promise, allPromise, resolveCache, true);
        allPromise.addListener2((GenericFutureListener<? extends Future<? super List<InetAddress>>>) new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.dns.DnsNameResolver.5
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) {
                if (!future.isSuccess()) {
                    DnsNameResolver.tryFailure(promise, future.cause());
                } else {
                    DnsNameResolver.trySuccess(promise, future.getNow().get(0));
                }
            }
        });
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolveAll(String inetHost, Promise<List<InetAddress>> promise) throws Exception {
        doResolveAll(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
    }

    protected void doResolveAll(String inetHost, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(Collections.singletonList(loopbackAddress()));
            return;
        }
        byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess(Collections.singletonList(InetAddress.getByAddress(bytes)));
            return;
        }
        String hostname = hostname(inetHost);
        InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(Collections.singletonList(hostsFileEntry));
        } else if (!doResolveAllCached(hostname, additionals, promise, resolveCache, this.resolvedInternetProtocolFamilies)) {
            doResolveAllUncached(hostname, additionals, promise, promise, resolveCache, this.completeOncePreferredResolved);
        }
    }

    static boolean doResolveAllCached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache, InternetProtocolFamily[] resolvedInternetProtocolFamilies) {
        List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        Throwable cause = cachedEntries.get(0).cause();
        if (cause == null) {
            List<InetAddress> result = null;
            int numEntries = cachedEntries.size();
            for (InternetProtocolFamily f : resolvedInternetProtocolFamilies) {
                for (int i = 0; i < numEntries; i++) {
                    DnsCacheEntry e = cachedEntries.get(i);
                    if (f.addressType().isInstance(e.address())) {
                        if (result == null) {
                            result = new ArrayList<>(numEntries);
                        }
                        result.add(e.address());
                    }
                }
            }
            if (result != null) {
                trySuccess(promise, result);
                return true;
            }
            return false;
        }
        tryFailure(promise, cause);
        return true;
    }

    private void doResolveAllUncached(final String hostname, final DnsRecord[] additionals, final Promise<?> originalPromise, final Promise<List<InetAddress>> promise, final DnsCache resolveCache, final boolean completeEarlyIfPossible) {
        EventExecutor executor = executor();
        if (executor.inEventLoop()) {
            doResolveAllUncached0(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
        } else {
            executor.execute(new Runnable() { // from class: io.netty.resolver.dns.DnsNameResolver.6
                @Override // java.lang.Runnable
                public void run() {
                    DnsNameResolver.this.doResolveAllUncached0(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doResolveAllUncached0(String hostname, DnsRecord[] additionals, Promise<?> originalPromise, Promise<List<InetAddress>> promise, DnsCache resolveCache, boolean completeEarlyIfPossible) {
        if (!$assertionsDisabled && !executor().inEventLoop()) {
            throw new AssertionError();
        }
        DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        new DnsAddressResolveContext(this, originalPromise, hostname, additionals, nameServerAddrs, resolveCache, this.authoritativeDnsServerCache, completeEarlyIfPossible).resolve(promise);
    }

    private static String hostname(String inetHost) {
        String hostname = IDN.toASCII(inetHost);
        if (StringUtil.endsWith(inetHost, '.') && !StringUtil.endsWith(hostname, '.')) {
            hostname = hostname + ".";
        }
        return hostname;
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question) {
        return query(nextNameServerAddress(), question);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Iterable<DnsRecord> additionals) {
        return query(nextNameServerAddress(), question, additionals);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return query(nextNameServerAddress(), question, Collections.emptyList(), promise);
    }

    private InetSocketAddress nextNameServerAddress() {
        return this.nameServerAddrStream.get().next();
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question) {
        return query0(nameServerAddr, question, EMPTY_ADDITIONALS, true, this.f7ch.newPromise(), this.f7ch.eventLoop().newPromise());
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals) {
        return query0(nameServerAddr, question, toArray(additionals, false), true, this.f7ch.newPromise(), this.f7ch.eventLoop().newPromise());
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return query0(nameServerAddr, question, EMPTY_ADDITIONALS, true, this.f7ch.newPromise(), promise);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return query0(nameServerAddr, question, toArray(additionals, false), true, this.f7ch.newPromise(), promise);
    }

    public static boolean isTransportOrTimeoutError(Throwable cause) {
        return cause != null && (cause.getCause() instanceof DnsNameResolverException);
    }

    public static boolean isTimeoutError(Throwable cause) {
        return cause != null && (cause.getCause() instanceof DnsNameResolverTimeoutException);
    }

    final void flushQueries() {
        this.f7ch.flush();
    }

    final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, boolean flush, ChannelPromise writePromise, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        if (!$assertionsDisabled && writePromise.isVoid()) {
            throw new AssertionError();
        }
        Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise = cast((Promise) ObjectUtil.checkNotNull(promise, "promise"));
        try {
            new DatagramDnsQueryContext(this, nameServerAddr, question, additionals, castPromise).query(flush, writePromise);
            return castPromise;
        } catch (Exception e) {
            return castPromise.setFailure(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(Promise<?> promise) {
        return promise;
    }

    final DnsServerAddressStream newNameServerAddressStream(String hostname) {
        return this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsNameResolver$DnsResponseHandler.class */
    private final class DnsResponseHandler extends ChannelInboundHandlerAdapter {
        private final Promise<Channel> channelActivePromise;

        DnsResponseHandler(Promise<Channel> channelActivePromise) {
            this.channelActivePromise = channelActivePromise;
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            final DatagramDnsResponse res = (DatagramDnsResponse) msg;
            final int queryId = res.id();
            if (DnsNameResolver.logger.isDebugEnabled()) {
                DnsNameResolver.logger.debug("{} RECEIVED: UDP [{}: {}], {}", DnsNameResolver.this.f7ch, Integer.valueOf(queryId), res.sender(), res);
            }
            final DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
            if (qCtx == null) {
                DnsNameResolver.logger.debug("Received a DNS response with an unknown ID: UDP [{}: {}]", DnsNameResolver.this.f7ch, Integer.valueOf(queryId));
                res.release();
            } else {
                if (!res.isTruncated() || DnsNameResolver.this.socketChannelFactory == null) {
                    qCtx.finish(res);
                    return;
                }
                Bootstrap bs = new Bootstrap();
                bs.option(ChannelOption.SO_REUSEADDR, true).group(DnsNameResolver.this.executor()).channelFactory(DnsNameResolver.this.socketChannelFactory).handler(DnsNameResolver.TCP_ENCODER);
                bs.connect(res.sender()).addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsNameResolver.DnsResponseHandler.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future) {
                        if (!future.isSuccess()) {
                            if (DnsNameResolver.logger.isDebugEnabled()) {
                                DnsNameResolver.logger.debug("Unable to fallback to TCP [{}]", Integer.valueOf(queryId), future.cause());
                            }
                            qCtx.finish(res);
                            return;
                        }
                        final Channel channel = future.channel();
                        Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise = channel.eventLoop().newPromise();
                        final TcpDnsQueryContext tcpCtx = new TcpDnsQueryContext(DnsNameResolver.this, channel, (InetSocketAddress) channel.remoteAddress(), qCtx.question(), DnsNameResolver.EMPTY_ADDITIONALS, promise);
                        channel.pipeline().addLast(new TcpDnsResponseDecoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() { // from class: io.netty.resolver.dns.DnsNameResolver.DnsResponseHandler.1.1
                            @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
                            public void channelRead(ChannelHandlerContext ctx2, Object msg2) {
                                Channel channel2 = ctx2.channel();
                                DnsResponse response = (DnsResponse) msg2;
                                int queryId2 = response.id();
                                if (DnsNameResolver.logger.isDebugEnabled()) {
                                    DnsNameResolver.logger.debug("{} RECEIVED: TCP [{}: {}], {}", channel2, Integer.valueOf(queryId2), channel2.remoteAddress(), response);
                                }
                                DnsQueryContext foundCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId2);
                                if (foundCtx == tcpCtx) {
                                    tcpCtx.finish(new AddressedEnvelopeAdapter((InetSocketAddress) ctx2.channel().remoteAddress(), (InetSocketAddress) ctx2.channel().localAddress(), response));
                                    return;
                                }
                                response.release();
                                tcpCtx.tryFailure("Received TCP DNS response with unexpected ID", null, false);
                                DnsNameResolver.logger.debug("Received a DNS response with an unexpected ID: TCP [{}: {}]", channel2, Integer.valueOf(queryId2));
                            }

                            @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
                            public void exceptionCaught(ChannelHandlerContext ctx2, Throwable cause) {
                                if (tcpCtx.tryFailure("TCP fallback error", cause, false) && DnsNameResolver.logger.isDebugEnabled()) {
                                    DnsNameResolver.logger.debug("{} Error during processing response: TCP [{}: {}]", ctx2.channel(), Integer.valueOf(queryId), ctx2.channel().remoteAddress(), cause);
                                }
                            }
                        });
                        promise.addListener2(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() { // from class: io.netty.resolver.dns.DnsNameResolver.DnsResponseHandler.1.2
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future2) {
                                channel.close();
                                if (future2.isSuccess()) {
                                    qCtx.finish(future2.getNow());
                                    res.release();
                                } else {
                                    qCtx.finish(res);
                                }
                            }
                        });
                        tcpCtx.query(true, future.channel().newPromise());
                    }
                });
            }
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.channelActivePromise.setSuccess(ctx.channel());
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            if (cause instanceof CorruptedFrameException) {
                DnsNameResolver.logger.debug("Unable to decode DNS response: UDP [{}]", ctx.channel(), cause);
            } else {
                DnsNameResolver.logger.warn("Unexpected exception: UDP [{}]", ctx.channel(), cause);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsNameResolver$AddressedEnvelopeAdapter.class */
    private static final class AddressedEnvelopeAdapter implements AddressedEnvelope<DnsResponse, InetSocketAddress> {
        private final InetSocketAddress sender;
        private final InetSocketAddress recipient;
        private final DnsResponse response;

        AddressedEnvelopeAdapter(InetSocketAddress sender, InetSocketAddress recipient, DnsResponse response) {
            this.sender = sender;
            this.recipient = recipient;
            this.response = response;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.channel.AddressedEnvelope
        public DnsResponse content() {
            return this.response;
        }

        @Override // io.netty.channel.AddressedEnvelope
        public InetSocketAddress sender() {
            return this.sender;
        }

        @Override // io.netty.channel.AddressedEnvelope
        public InetSocketAddress recipient() {
            return this.recipient;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> retain() {
            this.response.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> retain(int increment) {
            this.response.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> touch() {
            this.response.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> touch(Object hint) {
            this.response.touch(hint);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.response.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.response.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.response.release(decrement);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AddressedEnvelope)) {
                return false;
            }
            AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope) obj;
            if (sender() == null) {
                if (that.sender() != null) {
                    return false;
                }
            } else if (!sender().equals(that.sender())) {
                return false;
            }
            if (recipient() == null) {
                if (that.recipient() != null) {
                    return false;
                }
            } else if (!recipient().equals(that.recipient())) {
                return false;
            }
            return this.response.equals(obj);
        }

        public int hashCode() {
            int hashCode = this.response.hashCode();
            if (sender() != null) {
                hashCode = (hashCode * 31) + sender().hashCode();
            }
            if (recipient() != null) {
                hashCode = (hashCode * 31) + recipient().hashCode();
            }
            return hashCode;
        }
    }
}

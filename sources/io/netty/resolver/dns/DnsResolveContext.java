package io.netty.resolver.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SuppressJava6Requirement;
import io.netty.util.internal.ThrowableUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext.class */
abstract class DnsResolveContext<T> {
    private static final RuntimeException NXDOMAIN_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NAME_SERVERS_EXHAUSTED_EXCEPTION;
    final DnsNameResolver parent;
    private final Promise<?> originalPromise;
    private final DnsServerAddressStream nameServerAddrs;
    private final String hostname;
    private final int dnsClass;
    private final DnsRecordType[] expectedTypes;
    private final int maxAllowedQueries;
    final DnsRecord[] additionals;
    private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress = Collections.newSetFromMap(new IdentityHashMap());
    private List<T> finalResult;
    private int allowedQueries;
    private boolean triedCNAME;
    private boolean completeEarly;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract DnsResolveContext<T> newResolverContext(DnsNameResolver dnsNameResolver, Promise<?> promise, String str, int i, DnsRecordType[] dnsRecordTypeArr, DnsRecord[] dnsRecordArr, DnsServerAddressStream dnsServerAddressStream);

    abstract T convertRecord(DnsRecord dnsRecord, String str, DnsRecord[] dnsRecordArr, EventLoop eventLoop);

    abstract List<T> filterResults(List<T> list);

    abstract boolean isCompleteEarly(T t);

    abstract boolean isDuplicateAllowed();

    abstract void cache(String str, DnsRecord[] dnsRecordArr, DnsRecord dnsRecord, T t);

    abstract void cache(String str, DnsRecord[] dnsRecordArr, UnknownHostException unknownHostException);

    static {
        $assertionsDisabled = !DnsResolveContext.class.desiredAssertionStatus();
        NXDOMAIN_QUERY_FAILED_EXCEPTION = (RuntimeException) ThrowableUtil.unknownStackTrace(DnsResolveContextException.newStatic("No answer found and NXDOMAIN response code returned"), DnsResolveContext.class, "onResponse(..)");
        CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION = (RuntimeException) ThrowableUtil.unknownStackTrace(DnsResolveContextException.newStatic("No matching CNAME record found"), DnsResolveContext.class, "onResponseCNAME(..)");
        NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION = (RuntimeException) ThrowableUtil.unknownStackTrace(DnsResolveContextException.newStatic("No matching record type found"), DnsResolveContext.class, "onResponseAorAAAA(..)");
        UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION = (RuntimeException) ThrowableUtil.unknownStackTrace(new RuntimeException("Response type was unrecognized"), DnsResolveContext.class, "onResponse(..)");
        NAME_SERVERS_EXHAUSTED_EXCEPTION = (RuntimeException) ThrowableUtil.unknownStackTrace(DnsResolveContextException.newStatic("No name servers returned an answer"), DnsResolveContext.class, "tryToFinishResolve(..)");
    }

    DnsResolveContext(DnsNameResolver parent, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
        if (!$assertionsDisabled && expectedTypes.length <= 0) {
            throw new AssertionError();
        }
        this.parent = parent;
        this.originalPromise = originalPromise;
        this.hostname = hostname;
        this.dnsClass = dnsClass;
        this.expectedTypes = expectedTypes;
        this.additionals = additionals;
        this.nameServerAddrs = (DnsServerAddressStream) ObjectUtil.checkNotNull(nameServerAddrs, "nameServerAddrs");
        this.maxAllowedQueries = parent.maxQueriesPerResolve();
        this.allowedQueries = this.maxAllowedQueries;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$DnsResolveContextException.class */
    static final class DnsResolveContextException extends RuntimeException {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DnsResolveContext.class.desiredAssertionStatus();
        }

        private DnsResolveContextException(String message) {
            super(message);
        }

        @SuppressJava6Requirement(reason = "uses Java 7+ Exception.<init>(String, Throwable, boolean, boolean) but is guarded by version checks")
        private DnsResolveContextException(String message, boolean shared) {
            super(message, null, false, true);
            if (!$assertionsDisabled && !shared) {
                throw new AssertionError();
            }
        }

        static DnsResolveContextException newStatic(String message) {
            if (PlatformDependent.javaVersion() >= 7) {
                return new DnsResolveContextException(message, true);
            }
            return new DnsResolveContextException(message);
        }
    }

    DnsCache resolveCache() {
        return this.parent.resolveCache();
    }

    DnsCnameCache cnameCache() {
        return this.parent.cnameCache();
    }

    AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.parent.authoritativeDnsServerCache();
    }

    void resolve(final Promise<List<T>> promise) {
        final String[] searchDomains = this.parent.searchDomains();
        if (searchDomains.length == 0 || this.parent.ndots() == 0 || StringUtil.endsWith(this.hostname, '.')) {
            internalResolve(this.hostname, promise);
            return;
        }
        final boolean startWithoutSearchDomain = hasNDots();
        String initialHostname = startWithoutSearchDomain ? this.hostname : this.hostname + '.' + searchDomains[0];
        final int initialSearchDomainIdx = startWithoutSearchDomain ? 0 : 1;
        Promise<List<T>> searchDomainPromise = this.parent.executor().newPromise();
        searchDomainPromise.addListener2((GenericFutureListener<? extends Future<? super List<T>>>) new FutureListener<List<T>>() { // from class: io.netty.resolver.dns.DnsResolveContext.1
            private int searchDomainIdx;

            {
                this.searchDomainIdx = initialSearchDomainIdx;
            }

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<T>> future) {
                Throwable cause = future.cause();
                if (cause == null) {
                    promise.trySuccess(future.getNow());
                    return;
                }
                if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
                    promise.tryFailure(new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
                    return;
                }
                if (this.searchDomainIdx >= searchDomains.length) {
                    if (!startWithoutSearchDomain) {
                        DnsResolveContext.this.internalResolve(DnsResolveContext.this.hostname, promise);
                        return;
                    } else {
                        promise.tryFailure(new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
                        return;
                    }
                }
                Promise<List<T>> newPromise = DnsResolveContext.this.parent.executor().newPromise();
                newPromise.addListener2((GenericFutureListener<? extends Future<? super List<T>>>) this);
                DnsResolveContext dnsResolveContext = DnsResolveContext.this;
                StringBuilder sbAppend = new StringBuilder().append(DnsResolveContext.this.hostname).append('.');
                String[] strArr = searchDomains;
                int i = this.searchDomainIdx;
                this.searchDomainIdx = i + 1;
                dnsResolveContext.doSearchDomainQuery(sbAppend.append(strArr[i]).toString(), newPromise);
            }
        });
        doSearchDomainQuery(initialHostname, searchDomainPromise);
    }

    private boolean hasNDots() {
        int dots = 0;
        for (int idx = this.hostname.length() - 1; idx >= 0; idx--) {
            if (this.hostname.charAt(idx) == '.') {
                dots++;
                if (dots >= this.parent.ndots()) {
                    return true;
                }
            }
        }
        return false;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$SearchDomainUnknownHostException.class */
    private static final class SearchDomainUnknownHostException extends UnknownHostException {
        private static final long serialVersionUID = -8573510133644997085L;

        SearchDomainUnknownHostException(Throwable cause, String originalHostname) {
            super("Search domain query failed. Original hostname: '" + originalHostname + "' " + cause.getMessage());
            setStackTrace(cause.getStackTrace());
            initCause(cause.getCause());
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }

    void doSearchDomainQuery(String hostname, Promise<List<T>> nextPromise) {
        DnsResolveContext<T> nextContext = newResolverContext(this.parent, this.originalPromise, hostname, this.dnsClass, this.expectedTypes, this.additionals, this.nameServerAddrs);
        nextContext.internalResolve(hostname, nextPromise);
    }

    private static String hostnameWithDot(String name) {
        if (StringUtil.endsWith(name, '.')) {
            return name;
        }
        return name + '.';
    }

    private String cnameResolveFromCache(String name) {
        DnsCnameCache cnameCache = cnameCache();
        String first = cnameCache.get(hostnameWithDot(name));
        if (first == null) {
            return name;
        }
        String second = cnameCache.get(hostnameWithDot(first));
        if (second == null) {
            return first;
        }
        if (first.equals(second)) {
            return first;
        }
        return cnameResolveFromCacheLoop(cnameCache, first, second);
    }

    private String cnameResolveFromCacheLoop(DnsCnameCache cnameCache, String first, String mapping) {
        String name;
        Set<String> cnames = new HashSet<>(4);
        cnames.add(first);
        cnames.add(mapping);
        do {
            name = mapping;
            String str = cnameCache.get(hostnameWithDot(name));
            mapping = str;
            if (str == null) {
                break;
            }
        } while (cnames.add(mapping));
        return name;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalResolve(String name, Promise<List<T>> promise) {
        String name2 = cnameResolveFromCache(name);
        try {
            DnsServerAddressStream nameServerAddressStream = getNameServers(name2);
            int end = this.expectedTypes.length - 1;
            for (int i = 0; i < end; i++) {
                if (!query(name2, this.expectedTypes[i], nameServerAddressStream.duplicate(), false, promise)) {
                    return;
                }
            }
            query(name2, this.expectedTypes[end], nameServerAddressStream, false, promise);
            this.parent.flushQueries();
        } finally {
            this.parent.flushQueries();
        }
    }

    private DnsServerAddressStream getNameServersFromCache(String hostname) {
        DnsServerAddressStream entries;
        int len = hostname.length();
        if (len == 0) {
            return null;
        }
        if (hostname.charAt(len - 1) != '.') {
            hostname = hostname + ".";
        }
        int idx = hostname.indexOf(46);
        if (idx == hostname.length() - 1) {
            return null;
        }
        do {
            hostname = hostname.substring(idx + 1);
            int idx2 = hostname.indexOf(46);
            if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                return null;
            }
            idx = idx2;
            entries = authoritativeDnsServerCache().get(hostname);
        } while (entries == null);
        return entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, boolean flush, final Promise<List<T>> promise, Throwable cause) {
        if (this.completeEarly || nameServerAddrStreamIndex >= nameServerAddrStream.size() || this.allowedQueries == 0 || this.originalPromise.isCancelled() || promise.isCancelled()) {
            tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
            return;
        }
        this.allowedQueries--;
        InetSocketAddress nameServerAddr = nameServerAddrStream.next();
        if (nameServerAddr.isUnresolved()) {
            queryUnresolvedNameServer(nameServerAddr, nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
            return;
        }
        ChannelPromise writePromise = this.parent.f7ch.newPromise();
        Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> queryPromise = this.parent.f7ch.eventLoop().newPromise();
        Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.query0(nameServerAddr, question, this.additionals, flush, writePromise, queryPromise);
        this.queriesInProgress.add(f);
        queryLifecycleObserver.queryWritten(nameServerAddr, writePromise);
        f.addListener2(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() { // from class: io.netty.resolver.dns.DnsResolveContext.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                DnsResolveContext.this.queriesInProgress.remove(future);
                if (promise.isDone() || future.isCancelled()) {
                    queryLifecycleObserver.queryCancelled(DnsResolveContext.this.allowedQueries);
                    AddressedEnvelope<DnsResponse, InetSocketAddress> result = future.getNow();
                    if (result != null) {
                        result.release();
                        return;
                    }
                    return;
                }
                Throwable queryCause = future.cause();
                try {
                    if (queryCause == null) {
                        DnsResolveContext.this.onResponse(nameServerAddrStream, nameServerAddrStreamIndex, question, future.getNow(), queryLifecycleObserver, promise);
                    } else {
                        queryLifecycleObserver.queryFailed(queryCause);
                        DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, DnsResolveContext.this.newDnsQueryLifecycleObserver(question), true, promise, queryCause);
                    }
                } finally {
                    DnsResolveContext.this.tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, NoopDnsQueryLifecycleObserver.INSTANCE, promise, queryCause);
                }
            }
        });
    }

    private void queryUnresolvedNameServer(final InetSocketAddress nameServerAddr, final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, final Throwable cause) {
        String nameServerName = PlatformDependent.javaVersion() >= 7 ? nameServerAddr.getHostString() : nameServerAddr.getHostName();
        if (!$assertionsDisabled && nameServerName == null) {
            throw new AssertionError();
        }
        final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> resolveFuture = this.parent.executor().newSucceededFuture(null);
        this.queriesInProgress.add(resolveFuture);
        Promise<List<T>> promiseNewPromise = this.parent.executor().newPromise();
        promiseNewPromise.addListener2((GenericFutureListener<? extends Future<? super List<T>>>) new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.dns.DnsResolveContext.3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) {
                DnsResolveContext.this.queriesInProgress.remove(resolveFuture);
                if (!future.isSuccess()) {
                    DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, true, promise, cause);
                    return;
                }
                List<InetAddress> resolvedAddresses = future.getNow();
                DnsServerAddressStream addressStream = new CombinedDnsServerAddressStream(nameServerAddr, resolvedAddresses, nameServerAddrStream);
                DnsResolveContext.this.query(addressStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, true, promise, cause);
            }
        });
        if (!DnsNameResolver.doResolveAllCached(nameServerName, this.additionals, promiseNewPromise, resolveCache(), this.parent.resolvedInternetProtocolFamiliesUnsafe())) {
            final AuthoritativeDnsServerCache authoritativeDnsServerCache = authoritativeDnsServerCache();
            new DnsAddressResolveContext(this.parent, this.originalPromise, nameServerName, this.additionals, this.parent.newNameServerAddressStream(nameServerName), resolveCache(), new AuthoritativeDnsServerCache() { // from class: io.netty.resolver.dns.DnsResolveContext.4
                @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
                public DnsServerAddressStream get(String hostname) {
                    return null;
                }

                @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
                public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop) {
                    authoritativeDnsServerCache.cache(hostname, address, originalTtl, loop);
                }

                @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
                public void clear() {
                    authoritativeDnsServerCache.clear();
                }

                @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
                public boolean clear(String hostname) {
                    return authoritativeDnsServerCache.clear(hostname);
                }
            }, false).resolve(promiseNewPromise);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResponse(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        try {
            DnsResponse res = envelope.content();
            DnsResponseCode code = res.code();
            if (code != DnsResponseCode.NOERROR) {
                if (code != DnsResponseCode.NXDOMAIN) {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver.queryNoAnswer(code), true, promise, null);
                } else {
                    queryLifecycleObserver.queryFailed(NXDOMAIN_QUERY_FAILED_EXCEPTION);
                    if (!res.isAuthoritativeAnswer()) {
                        query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, newDnsQueryLifecycleObserver(question), true, promise, null);
                    }
                }
                ReferenceCountUtil.safeRelease(envelope);
                return;
            }
            if (handleRedirect(question, envelope, queryLifecycleObserver, promise)) {
                return;
            }
            DnsRecordType type = question.type();
            if (type == DnsRecordType.CNAME) {
                onResponseCNAME(question, buildAliasMap(envelope.content(), cnameCache(), this.parent.executor()), queryLifecycleObserver, promise);
                ReferenceCountUtil.safeRelease(envelope);
                return;
            }
            for (DnsRecordType expectedType : this.expectedTypes) {
                if (type == expectedType) {
                    onExpectedResponse(question, envelope, queryLifecycleObserver, promise);
                    ReferenceCountUtil.safeRelease(envelope);
                    return;
                }
            }
            queryLifecycleObserver.queryFailed(UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION);
            ReferenceCountUtil.safeRelease(envelope);
        } finally {
            ReferenceCountUtil.safeRelease(envelope);
        }
    }

    private boolean handleRedirect(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        AuthoritativeNameServerList serverNames;
        DnsResponse res = envelope.content();
        if (res.count(DnsSection.ANSWER) == 0 && (serverNames = extractAuthoritativeNameServers(question.name(), res)) != null) {
            int additionalCount = res.count(DnsSection.ADDITIONAL);
            AuthoritativeDnsServerCache authoritativeDnsServerCache = authoritativeDnsServerCache();
            for (int i = 0; i < additionalCount; i++) {
                DnsRecord r = res.recordAt(DnsSection.ADDITIONAL, i);
                if ((r.type() != DnsRecordType.A || this.parent.supportsARecords()) && (r.type() != DnsRecordType.AAAA || this.parent.supportsAAAARecords())) {
                    serverNames.handleWithAdditional(this.parent, r, authoritativeDnsServerCache);
                }
            }
            serverNames.handleWithoutAdditionals(this.parent, resolveCache(), authoritativeDnsServerCache);
            List<InetSocketAddress> addresses = serverNames.addressList();
            DnsServerAddressStream serverStream = this.parent.newRedirectDnsServerStream(question.name(), addresses);
            if (serverStream != null) {
                query(serverStream, 0, question, queryLifecycleObserver.queryRedirected(new DnsAddressStreamList(serverStream)), true, promise, null);
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$DnsAddressStreamList.class */
    private static final class DnsAddressStreamList extends AbstractList<InetSocketAddress> {
        private final DnsServerAddressStream duplicate;
        private List<InetSocketAddress> addresses;

        DnsAddressStreamList(DnsServerAddressStream stream) {
            this.duplicate = stream.duplicate();
        }

        @Override // java.util.AbstractList, java.util.List
        public InetSocketAddress get(int index) {
            if (this.addresses == null) {
                DnsServerAddressStream stream = this.duplicate.duplicate();
                this.addresses = new ArrayList(size());
                for (int i = 0; i < stream.size(); i++) {
                    this.addresses.add(stream.next());
                }
            }
            return this.addresses.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.duplicate.size();
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<InetSocketAddress> iterator() {
            return new Iterator<InetSocketAddress>() { // from class: io.netty.resolver.dns.DnsResolveContext.DnsAddressStreamList.1
                private final DnsServerAddressStream stream;
                private int i;

                {
                    this.stream = DnsAddressStreamList.this.duplicate.duplicate();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.i < this.stream.size();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public InetSocketAddress next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.i++;
                    return this.stream.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static AuthoritativeNameServerList extractAuthoritativeNameServers(String questionName, DnsResponse res) {
        int authorityCount = res.count(DnsSection.AUTHORITY);
        if (authorityCount == 0) {
            return null;
        }
        AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
        for (int i = 0; i < authorityCount; i++) {
            serverNames.add(res.recordAt(DnsSection.AUTHORITY, i));
        }
        if (serverNames.isEmpty()) {
            return null;
        }
        return serverNames;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void onExpectedResponse(io.netty.handler.codec.dns.DnsQuestion r7, io.netty.channel.AddressedEnvelope<io.netty.handler.codec.dns.DnsResponse, java.net.InetSocketAddress> r8, io.netty.resolver.dns.DnsQueryLifecycleObserver r9, io.netty.util.concurrent.Promise<java.util.List<T>> r10) {
        /*
            Method dump skipped, instructions count: 457
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.DnsResolveContext.onExpectedResponse(io.netty.handler.codec.dns.DnsQuestion, io.netty.channel.AddressedEnvelope, io.netty.resolver.dns.DnsQueryLifecycleObserver, io.netty.util.concurrent.Promise):void");
    }

    private void onResponseCNAME(DnsQuestion question, Map<String, String> cnames, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        String next;
        String resolved = question.name().toLowerCase(Locale.US);
        boolean found = false;
        while (!cnames.isEmpty() && (next = cnames.remove(resolved)) != null) {
            found = true;
            resolved = next;
        }
        if (found) {
            followCname(question, resolved, queryLifecycleObserver, promise);
        } else {
            queryLifecycleObserver.queryFailed(CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION);
        }
    }

    private static Map<String, String> buildAliasMap(DnsResponse response, DnsCnameCache cache, EventLoop loop) {
        int answerCount = response.count(DnsSection.ANSWER);
        Map<String, String> cnames = null;
        for (int i = 0; i < answerCount; i++) {
            DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
            DnsRecordType type = r.type();
            if (type == DnsRecordType.CNAME && (r instanceof DnsRawRecord)) {
                ByteBuf recordContent = ((ByteBufHolder) r).content();
                String domainName = decodeDomainName(recordContent);
                if (domainName != null) {
                    if (cnames == null) {
                        cnames = new HashMap<>(Math.min(8, answerCount));
                    }
                    String name = r.name().toLowerCase(Locale.US);
                    String mapping = domainName.toLowerCase(Locale.US);
                    String nameWithDot = hostnameWithDot(name);
                    String mappingWithDot = hostnameWithDot(mapping);
                    if (!nameWithDot.equalsIgnoreCase(mappingWithDot)) {
                        cache.cache(nameWithDot, mappingWithDot, r.timeToLive(), loop);
                        cnames.put(name, mapping);
                    }
                }
            }
        }
        return cnames != null ? cnames : Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToFinishResolve(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise, Throwable cause) {
        if (!this.completeEarly && !this.queriesInProgress.isEmpty()) {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
            return;
        }
        if (this.finalResult == null) {
            if (nameServerAddrStreamIndex < nameServerAddrStream.size()) {
                if (queryLifecycleObserver == NoopDnsQueryLifecycleObserver.INSTANCE) {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, newDnsQueryLifecycleObserver(question), true, promise, cause);
                    return;
                } else {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, true, promise, cause);
                    return;
                }
            }
            queryLifecycleObserver.queryFailed(NAME_SERVERS_EXHAUSTED_EXCEPTION);
            if (cause == null && !this.triedCNAME) {
                this.triedCNAME = true;
                query(this.hostname, DnsRecordType.CNAME, getNameServers(this.hostname), true, promise);
                return;
            }
        } else {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
        }
        finishResolve(promise, cause);
    }

    private void finishResolve(Promise<List<T>> promise, Throwable cause) {
        if (!this.completeEarly && !this.queriesInProgress.isEmpty()) {
            Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = this.queriesInProgress.iterator();
            while (i.hasNext()) {
                Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = i.next();
                i.remove();
                f.cancel(false);
            }
        }
        if (this.finalResult != null) {
            if (!promise.isDone()) {
                DnsNameResolver.trySuccess(promise, filterResults(this.finalResult));
                return;
            }
            return;
        }
        int tries = this.maxAllowedQueries - this.allowedQueries;
        StringBuilder buf = new StringBuilder(64);
        buf.append("failed to resolve '").append(this.hostname).append('\'');
        if (tries > 1) {
            if (tries < this.maxAllowedQueries) {
                buf.append(" after ").append(tries).append(" queries ");
            } else {
                buf.append(". Exceeded max queries per resolve ").append(this.maxAllowedQueries).append(' ');
            }
        }
        UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
        if (cause == null) {
            cache(this.hostname, this.additionals, unknownHostException);
        } else {
            unknownHostException.initCause(cause);
        }
        promise.tryFailure(unknownHostException);
    }

    static String decodeDomainName(ByteBuf in) {
        in.markReaderIndex();
        try {
            return DefaultDnsRecordDecoder.decodeName(in);
        } catch (CorruptedFrameException e) {
            return null;
        } finally {
            in.resetReaderIndex();
        }
    }

    private DnsServerAddressStream getNameServers(String hostname) {
        DnsServerAddressStream stream = getNameServersFromCache(hostname);
        return stream == null ? this.parent.newNameServerAddressStream(hostname) : stream;
    }

    private void followCname(DnsQuestion question, String cname, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        String cname2 = cnameResolveFromCache(cname);
        DnsServerAddressStream stream = getNameServers(cname2);
        try {
            DnsQuestion cnameQuestion = new DefaultDnsQuestion(cname2, question.type(), this.dnsClass);
            query(stream, 0, cnameQuestion, queryLifecycleObserver.queryCNAMEd(cnameQuestion), true, promise, null);
        } catch (Throwable cause) {
            queryLifecycleObserver.queryFailed(cause);
            PlatformDependent.throwException(cause);
        }
    }

    private boolean query(String hostname, DnsRecordType type, DnsServerAddressStream dnsServerAddressStream, boolean flush, Promise<List<T>> promise) {
        try {
            DnsQuestion question = new DefaultDnsQuestion(hostname, type, this.dnsClass);
            query(dnsServerAddressStream, 0, question, newDnsQueryLifecycleObserver(question), flush, promise, null);
            return true;
        } catch (Throwable cause) {
            promise.tryFailure(new IllegalArgumentException("Unable to create DNS Question for: [" + hostname + ", " + type + ']', cause));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
        return this.parent.dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$CombinedDnsServerAddressStream.class */
    private final class CombinedDnsServerAddressStream implements DnsServerAddressStream {
        private final InetSocketAddress replaced;
        private final DnsServerAddressStream originalStream;
        private final List<InetAddress> resolvedAddresses;
        private Iterator<InetAddress> resolved;

        CombinedDnsServerAddressStream(InetSocketAddress replaced, List<InetAddress> resolvedAddresses, DnsServerAddressStream originalStream) {
            this.replaced = replaced;
            this.resolvedAddresses = resolvedAddresses;
            this.originalStream = originalStream;
            this.resolved = resolvedAddresses.iterator();
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public InetSocketAddress next() {
            if (this.resolved.hasNext()) {
                return nextResolved0();
            }
            InetSocketAddress address = this.originalStream.next();
            if (address.equals(this.replaced)) {
                this.resolved = this.resolvedAddresses.iterator();
                return nextResolved0();
            }
            return address;
        }

        private InetSocketAddress nextResolved0() {
            return DnsResolveContext.this.parent.newRedirectServerAddress(this.resolved.next());
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public int size() {
            return (this.originalStream.size() + this.resolvedAddresses.size()) - 1;
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public DnsServerAddressStream duplicate() {
            return new CombinedDnsServerAddressStream(this.replaced, this.resolvedAddresses, this.originalStream.duplicate());
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$AuthoritativeNameServerList.class */
    private static final class AuthoritativeNameServerList {
        private final String questionName;
        private AuthoritativeNameServer head;
        private int nameServerCount;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DnsResolveContext.class.desiredAssertionStatus();
        }

        AuthoritativeNameServerList(String questionName) {
            this.questionName = questionName.toLowerCase(Locale.US);
        }

        void add(DnsRecord r) {
            if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord) || this.questionName.length() < r.name().length()) {
                return;
            }
            String recordName = r.name().toLowerCase(Locale.US);
            int dots = 0;
            int a = recordName.length() - 1;
            int b = this.questionName.length() - 1;
            while (a >= 0) {
                char c = recordName.charAt(a);
                if (this.questionName.charAt(b) != c) {
                    return;
                }
                if (c == '.') {
                    dots++;
                }
                a--;
                b--;
            }
            if (this.head != null && this.head.dots > dots) {
                return;
            }
            ByteBuf recordContent = ((ByteBufHolder) r).content();
            String domainName = DnsResolveContext.decodeDomainName(recordContent);
            if (domainName == null) {
                return;
            }
            if (this.head == null || this.head.dots < dots) {
                this.nameServerCount = 1;
                this.head = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
            } else if (this.head.dots == dots) {
                AuthoritativeNameServer authoritativeNameServer = this.head;
                while (true) {
                    AuthoritativeNameServer serverName = authoritativeNameServer;
                    if (serverName.next != null) {
                        authoritativeNameServer = serverName.next;
                    } else {
                        serverName.next = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
                        this.nameServerCount++;
                        return;
                    }
                }
            }
        }

        void handleWithAdditional(DnsNameResolver parent, DnsRecord r, AuthoritativeDnsServerCache authoritativeCache) {
            AuthoritativeNameServer serverName = this.head;
            String nsName = r.name();
            InetAddress resolved = DnsAddressDecoder.decodeAddress(r, nsName, parent.isDecodeIdn());
            if (resolved == null) {
                return;
            }
            while (serverName != null) {
                if (!serverName.nsName.equalsIgnoreCase(nsName)) {
                    serverName = serverName.next;
                } else {
                    if (serverName.address != null) {
                        while (serverName.next != null && serverName.next.isCopy) {
                            serverName = serverName.next;
                        }
                        AuthoritativeNameServer server = new AuthoritativeNameServer(serverName);
                        server.next = serverName.next;
                        serverName.next = server;
                        serverName = server;
                        this.nameServerCount++;
                    }
                    serverName.update(parent.newRedirectServerAddress(resolved), r.timeToLive());
                    cache(serverName, authoritativeCache, parent.executor());
                    return;
                }
            }
        }

        void handleWithoutAdditionals(DnsNameResolver parent, DnsCache cache, AuthoritativeDnsServerCache authoritativeCache) {
            InetAddress address;
            AuthoritativeNameServer authoritativeNameServer = this.head;
            while (true) {
                AuthoritativeNameServer serverName = authoritativeNameServer;
                if (serverName == null) {
                    return;
                }
                if (serverName.address == null) {
                    cacheUnresolved(serverName, authoritativeCache, parent.executor());
                    List<? extends DnsCacheEntry> entries = cache.get(serverName.nsName, null);
                    if (entries != null && !entries.isEmpty() && (address = entries.get(0).address()) != null) {
                        serverName.update(parent.newRedirectServerAddress(address));
                        for (int i = 1; i < entries.size(); i++) {
                            InetAddress address2 = entries.get(i).address();
                            if (!$assertionsDisabled && address2 == null) {
                                throw new AssertionError("Cache returned a cached failure, should never return anything else");
                            }
                            AuthoritativeNameServer server = new AuthoritativeNameServer(serverName);
                            server.next = serverName.next;
                            serverName.next = server;
                            serverName = server;
                            serverName.update(parent.newRedirectServerAddress(address2));
                            this.nameServerCount++;
                        }
                    }
                }
                authoritativeNameServer = serverName.next;
            }
        }

        private static void cacheUnresolved(AuthoritativeNameServer server, AuthoritativeDnsServerCache authoritativeCache, EventLoop loop) {
            server.address = InetSocketAddress.createUnresolved(server.nsName, 53);
            cache(server, authoritativeCache, loop);
        }

        private static void cache(AuthoritativeNameServer server, AuthoritativeDnsServerCache cache, EventLoop loop) {
            if (!server.isRootServer()) {
                cache.cache(server.domainName, server.address, server.ttl, loop);
            }
        }

        boolean isEmpty() {
            return this.nameServerCount == 0;
        }

        List<InetSocketAddress> addressList() {
            List<InetSocketAddress> addressList = new ArrayList<>(this.nameServerCount);
            AuthoritativeNameServer authoritativeNameServer = this.head;
            while (true) {
                AuthoritativeNameServer server = authoritativeNameServer;
                if (server == null) {
                    return addressList;
                }
                if (server.address != null) {
                    addressList.add(server.address);
                }
                authoritativeNameServer = server.next;
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsResolveContext$AuthoritativeNameServer.class */
    private static final class AuthoritativeNameServer {
        private final int dots;
        private final String domainName;
        final boolean isCopy = false;
        final String nsName;
        private long ttl;
        private InetSocketAddress address;
        AuthoritativeNameServer next;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DnsResolveContext.class.desiredAssertionStatus();
        }

        AuthoritativeNameServer(int dots, long ttl, String domainName, String nsName) {
            this.dots = dots;
            this.ttl = ttl;
            this.nsName = nsName;
            this.domainName = domainName;
        }

        AuthoritativeNameServer(AuthoritativeNameServer server) {
            this.dots = server.dots;
            this.ttl = server.ttl;
            this.nsName = server.nsName;
            this.domainName = server.domainName;
        }

        boolean isRootServer() {
            return this.dots == 1;
        }

        void update(InetSocketAddress address, long ttl) {
            if (!$assertionsDisabled && this.address != null && !this.address.isUnresolved()) {
                throw new AssertionError();
            }
            this.address = address;
            this.ttl = Math.min(this.ttl, ttl);
        }

        void update(InetSocketAddress address) {
            update(address, Long.MAX_VALUE);
        }
    }
}

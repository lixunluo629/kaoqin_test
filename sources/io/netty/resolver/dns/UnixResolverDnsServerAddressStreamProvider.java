package io.netty.resolver.dns;

import io.netty.resolver.dns.UnixResolverOptions;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/UnixResolverDnsServerAddressStreamProvider.class */
public final class UnixResolverDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) UnixResolverDnsServerAddressStreamProvider.class);
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final String RES_OPTIONS = System.getenv("RES_OPTIONS");
    private static final String ETC_RESOLV_CONF_FILE = "/etc/resolv.conf";
    private static final String ETC_RESOLVER_DIR = "/etc/resolver";
    private static final String NAMESERVER_ROW_LABEL = "nameserver";
    private static final String SORTLIST_ROW_LABEL = "sortlist";
    private static final String OPTIONS_ROW_LABEL = "options ";
    private static final String OPTIONS_ROTATE_FLAG = "rotate";
    private static final String DOMAIN_ROW_LABEL = "domain";
    private static final String SEARCH_ROW_LABEL = "search";
    private static final String PORT_ROW_LABEL = "port";
    private final DnsServerAddresses defaultNameServerAddresses;
    private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;

    static DnsServerAddressStreamProvider parseSilently() {
        try {
            UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider(ETC_RESOLV_CONF_FILE, ETC_RESOLVER_DIR);
            return nameServerCache.mayOverrideNameServers() ? nameServerCache : DefaultDnsServerAddressStreamProvider.INSTANCE;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("failed to parse {} and/or {}", ETC_RESOLV_CONF_FILE, ETC_RESOLVER_DIR, e);
            }
            return DefaultDnsServerAddressStreamProvider.INSTANCE;
        }
    }

    public UnixResolverDnsServerAddressStreamProvider(File etcResolvConf, File... etcResolverFiles) throws IOException {
        Map<String, DnsServerAddresses> etcResolvConfMap = parse((File) ObjectUtil.checkNotNull(etcResolvConf, "etcResolvConf"));
        boolean useEtcResolverFiles = (etcResolverFiles == null || etcResolverFiles.length == 0) ? false : true;
        this.domainToNameServerStreamMap = useEtcResolverFiles ? parse(etcResolverFiles) : etcResolvConfMap;
        DnsServerAddresses defaultNameServerAddresses = etcResolvConfMap.get(etcResolvConf.getName());
        if (defaultNameServerAddresses == null) {
            Collection<DnsServerAddresses> values = etcResolvConfMap.values();
            if (values.isEmpty()) {
                throw new IllegalArgumentException(etcResolvConf + " didn't provide any name servers");
            }
            this.defaultNameServerAddresses = values.iterator().next();
        } else {
            this.defaultNameServerAddresses = defaultNameServerAddresses;
        }
        if (useEtcResolverFiles) {
            this.domainToNameServerStreamMap.putAll(etcResolvConfMap);
        }
    }

    public UnixResolverDnsServerAddressStreamProvider(String etcResolvConf, String etcResolverDir) throws IOException {
        this(etcResolvConf == null ? null : new File(etcResolvConf), etcResolverDir == null ? null : new File(etcResolverDir).listFiles());
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001d, code lost:
    
        return r4.defaultNameServerAddresses.stream();
     */
    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.resolver.dns.DnsServerAddressStream nameServerAddressStream(java.lang.String r5) {
        /*
            r4 = this;
        L0:
            r0 = r5
            r1 = 46
            r2 = 1
            int r0 = r0.indexOf(r1, r2)
            r6 = r0
            r0 = r6
            if (r0 < 0) goto L16
            r0 = r6
            r1 = r5
            int r1 = r1.length()
            r2 = 1
            int r1 = r1 - r2
            if (r0 != r1) goto L1e
        L16:
            r0 = r4
            io.netty.resolver.dns.DnsServerAddresses r0 = r0.defaultNameServerAddresses
            io.netty.resolver.dns.DnsServerAddressStream r0 = r0.stream()
            return r0
        L1e:
            r0 = r4
            java.util.Map<java.lang.String, io.netty.resolver.dns.DnsServerAddresses> r0 = r0.domainToNameServerStreamMap
            r1 = r5
            java.lang.Object r0 = r0.get(r1)
            io.netty.resolver.dns.DnsServerAddresses r0 = (io.netty.resolver.dns.DnsServerAddresses) r0
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L35
            r0 = r7
            io.netty.resolver.dns.DnsServerAddressStream r0 = r0.stream()
            return r0
        L35:
            r0 = r5
            r1 = r6
            r2 = 1
            int r1 = r1 + r2
            java.lang.String r0 = r0.substring(r1)
            r5 = r0
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.nameServerAddressStream(java.lang.String):io.netty.resolver.dns.DnsServerAddressStream");
    }

    private boolean mayOverrideNameServers() {
        return (this.domainToNameServerStreamMap.isEmpty() && this.defaultNameServerAddresses.stream().next() == null) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x015e, code lost:
    
        throw new java.lang.IllegalArgumentException("error parsing label nameserver in file " + r0 + ". value: " + r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.util.Map<java.lang.String, io.netty.resolver.dns.DnsServerAddresses> parse(java.io.File... r5) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 784
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.parse(java.io.File[]):java.util.Map");
    }

    private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, List<InetSocketAddress> addresses, boolean rotate) {
        DnsServerAddresses dnsServerAddressesSequential;
        if (rotate) {
            dnsServerAddressesSequential = DnsServerAddresses.rotational(addresses);
        } else {
            dnsServerAddressesSequential = DnsServerAddresses.sequential(addresses);
        }
        DnsServerAddresses addrs = dnsServerAddressesSequential;
        putIfAbsent(domainToNameServerStreamMap, domainName, addrs);
    }

    private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, DnsServerAddresses addresses) {
        DnsServerAddresses existingAddresses = domainToNameServerStreamMap.put(domainName, addresses);
        if (existingAddresses != null) {
            domainToNameServerStreamMap.put(domainName, existingAddresses);
            if (logger.isDebugEnabled()) {
                logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", domainName, existingAddresses, addresses);
            }
        }
    }

    static UnixResolverOptions parseEtcResolverOptions() throws IOException {
        return parseEtcResolverOptions(new File(ETC_RESOLV_CONF_FILE));
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x002c, code lost:
    
        parseResOptions(r0.substring(io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.OPTIONS_ROW_LABEL.length()), r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static io.netty.resolver.dns.UnixResolverOptions parseEtcResolverOptions(java.io.File r4) throws java.io.IOException {
        /*
            io.netty.resolver.dns.UnixResolverOptions$Builder r0 = io.netty.resolver.dns.UnixResolverOptions.newBuilder()
            r5 = r0
            java.io.FileReader r0 = new java.io.FileReader
            r1 = r0
            r2 = r4
            r1.<init>(r2)
            r6 = r0
            r0 = 0
            r7 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L4f
            r1 = r0
            r2 = r6
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L4f
            r7 = r0
        L18:
            r0 = r7
            java.lang.String r0 = r0.readLine()     // Catch: java.lang.Throwable -> L4f
            r1 = r0
            r8 = r1
            if (r0 == 0) goto L3d
            r0 = r8
            java.lang.String r1 = "options "
            boolean r0 = r0.startsWith(r1)     // Catch: java.lang.Throwable -> L4f
            if (r0 == 0) goto L18
            r0 = r8
            java.lang.String r1 = "options "
            int r1 = r1.length()     // Catch: java.lang.Throwable -> L4f
            java.lang.String r0 = r0.substring(r1)     // Catch: java.lang.Throwable -> L4f
            r1 = r5
            parseResOptions(r0, r1)     // Catch: java.lang.Throwable -> L4f
            goto L3d
        L3d:
            r0 = r7
            if (r0 != 0) goto L48
            r0 = r6
            r0.close()
            goto L63
        L48:
            r0 = r7
            r0.close()
            goto L63
        L4f:
            r9 = move-exception
            r0 = r7
            if (r0 != 0) goto L5c
            r0 = r6
            r0.close()
            goto L60
        L5c:
            r0 = r7
            r0.close()
        L60:
            r0 = r9
            throw r0
        L63:
            java.lang.String r0 = io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.RES_OPTIONS
            if (r0 == 0) goto L70
            java.lang.String r0 = io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.RES_OPTIONS
            r1 = r5
            parseResOptions(r0, r1)
        L70:
            r0 = r5
            io.netty.resolver.dns.UnixResolverOptions r0 = r0.build()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.parseEtcResolverOptions(java.io.File):io.netty.resolver.dns.UnixResolverOptions");
    }

    private static void parseResOptions(String line, UnixResolverOptions.Builder builder) {
        String[] opts = WHITESPACE_PATTERN.split(line);
        for (String opt : opts) {
            try {
                if (opt.startsWith("ndots:")) {
                    builder.setNdots(parseResIntOption(opt, "ndots:"));
                } else if (opt.startsWith("attempts:")) {
                    builder.setAttempts(parseResIntOption(opt, "attempts:"));
                } else if (opt.startsWith("timeout:")) {
                    builder.setTimeout(parseResIntOption(opt, "timeout:"));
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private static int parseResIntOption(String opt, String fullLabel) {
        String optValue = opt.substring(fullLabel.length());
        return Integer.parseInt(optValue);
    }

    static List<String> parseEtcResolverSearchDomains() throws IOException {
        return parseEtcResolverSearchDomains(new File(ETC_RESOLV_CONF_FILE));
    }

    static List<String> parseEtcResolverSearchDomains(File etcResolvConf) throws IOException {
        String localDomain = null;
        List<String> searchDomains = new ArrayList<>();
        FileReader fr = new FileReader(etcResolvConf);
        BufferedReader br = null;
        try {
            br = new BufferedReader(fr);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (localDomain == null && line.startsWith("domain")) {
                    int i = StringUtil.indexOfNonWhiteSpace(line, "domain".length());
                    if (i >= 0) {
                        localDomain = line.substring(i);
                    }
                } else if (line.startsWith(SEARCH_ROW_LABEL)) {
                    int i2 = StringUtil.indexOfNonWhiteSpace(line, SEARCH_ROW_LABEL.length());
                    if (i2 >= 0) {
                        String[] domains = WHITESPACE_PATTERN.split(line.substring(i2));
                        Collections.addAll(searchDomains, domains);
                    }
                }
            }
            if (br == null) {
                fr.close();
            } else {
                br.close();
            }
            return (localDomain == null || !searchDomains.isEmpty()) ? searchDomains : Collections.singletonList(localDomain);
        } catch (Throwable th) {
            if (br == null) {
                fr.close();
            } else {
                br.close();
            }
            throw th;
        }
    }
}

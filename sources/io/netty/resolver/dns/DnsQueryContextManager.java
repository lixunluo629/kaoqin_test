package io.netty.resolver.dns;

import io.netty.util.NetUtil;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.internal.PlatformDependent;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsQueryContextManager.class */
final class DnsQueryContextManager {
    final Map<InetSocketAddress, IntObjectMap<DnsQueryContext>> map = new HashMap();

    DnsQueryContextManager() {
    }

    int add(DnsQueryContext qCtx) {
        int i;
        IntObjectMap<DnsQueryContext> contexts = getOrCreateContextMap(qCtx.nameServerAddr());
        int id = PlatformDependent.threadLocalRandom().nextInt(65535) + 1;
        int tries = 0;
        synchronized (contexts) {
            while (contexts.containsKey(id)) {
                id = (id + 1) & 65535;
                tries++;
                if (tries >= 131070) {
                    throw new IllegalStateException("query ID space exhausted: " + qCtx.question());
                }
            }
            contexts.put(id, (int) qCtx);
            i = id;
        }
        return i;
    }

    DnsQueryContext get(InetSocketAddress nameServerAddr, int id) {
        DnsQueryContext qCtx;
        IntObjectMap<DnsQueryContext> contexts = getContextMap(nameServerAddr);
        if (contexts != null) {
            synchronized (contexts) {
                qCtx = contexts.get(id);
            }
        } else {
            qCtx = null;
        }
        return qCtx;
    }

    DnsQueryContext remove(InetSocketAddress nameServerAddr, int id) {
        DnsQueryContext dnsQueryContextRemove;
        IntObjectMap<DnsQueryContext> contexts = getContextMap(nameServerAddr);
        if (contexts == null) {
            return null;
        }
        synchronized (contexts) {
            dnsQueryContextRemove = contexts.remove(id);
        }
        return dnsQueryContextRemove;
    }

    private IntObjectMap<DnsQueryContext> getContextMap(InetSocketAddress nameServerAddr) {
        IntObjectMap<DnsQueryContext> intObjectMap;
        synchronized (this.map) {
            intObjectMap = this.map.get(nameServerAddr);
        }
        return intObjectMap;
    }

    private IntObjectMap<DnsQueryContext> getOrCreateContextMap(InetSocketAddress nameServerAddr) {
        synchronized (this.map) {
            IntObjectMap<DnsQueryContext> contexts = this.map.get(nameServerAddr);
            if (contexts != null) {
                return contexts;
            }
            IntObjectMap<DnsQueryContext> newContexts = new IntObjectHashMap<>();
            InetAddress a = nameServerAddr.getAddress();
            int port = nameServerAddr.getPort();
            this.map.put(nameServerAddr, newContexts);
            if (a instanceof Inet4Address) {
                Inet4Address a4 = (Inet4Address) a;
                if (a4.isLoopbackAddress()) {
                    this.map.put(new InetSocketAddress(NetUtil.LOCALHOST6, port), newContexts);
                } else {
                    this.map.put(new InetSocketAddress(toCompactAddress(a4), port), newContexts);
                }
            } else if (a instanceof Inet6Address) {
                Inet6Address a6 = (Inet6Address) a;
                if (a6.isLoopbackAddress()) {
                    this.map.put(new InetSocketAddress(NetUtil.LOCALHOST4, port), newContexts);
                } else if (a6.isIPv4CompatibleAddress()) {
                    this.map.put(new InetSocketAddress(toIPv4Address(a6), port), newContexts);
                }
            }
            return newContexts;
        }
    }

    private static Inet6Address toCompactAddress(Inet4Address a4) {
        byte[] b4 = a4.getAddress();
        byte[] b6 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, b4[0], b4[1], b4[2], b4[3]};
        try {
            return (Inet6Address) InetAddress.getByAddress(b6);
        } catch (UnknownHostException e) {
            throw new Error(e);
        }
    }

    private static Inet4Address toIPv4Address(Inet6Address a6) {
        byte[] b6 = a6.getAddress();
        byte[] b4 = {b6[12], b6[13], b6[14], b6[15]};
        try {
            return (Inet4Address) InetAddress.getByAddress(b4);
        } catch (UnknownHostException e) {
            throw new Error(e);
        }
    }
}

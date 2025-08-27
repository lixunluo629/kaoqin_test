package org.apache.commons.httpclient.protocol;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/Protocol.class */
public class Protocol {
    private static final Map PROTOCOLS = Collections.synchronizedMap(new HashMap());
    private String scheme;
    private ProtocolSocketFactory socketFactory;
    private int defaultPort;
    private boolean secure;

    public static void registerProtocol(String id, Protocol protocol) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("protocol is null");
        }
        PROTOCOLS.put(id, protocol);
    }

    public static void unregisterProtocol(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        PROTOCOLS.remove(id);
    }

    public static Protocol getProtocol(String id) throws IllegalStateException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        Protocol protocol = (Protocol) PROTOCOLS.get(id);
        if (protocol == null) {
            protocol = lazyRegisterProtocol(id);
        }
        return protocol;
    }

    private static Protocol lazyRegisterProtocol(String id) throws IllegalStateException {
        if ("http".equals(id)) {
            Protocol http = new Protocol("http", DefaultProtocolSocketFactory.getSocketFactory(), 80);
            registerProtocol("http", http);
            return http;
        }
        if ("https".equals(id)) {
            Protocol https = new Protocol("https", (SecureProtocolSocketFactory) SSLProtocolSocketFactory.getSocketFactory(), 443);
            registerProtocol("https", https);
            return https;
        }
        throw new IllegalStateException(new StringBuffer().append("unsupported protocol: '").append(id).append("'").toString());
    }

    public Protocol(String scheme, ProtocolSocketFactory factory, int defaultPort) {
        if (scheme == null) {
            throw new IllegalArgumentException("scheme is null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("socketFactory is null");
        }
        if (defaultPort <= 0) {
            throw new IllegalArgumentException(new StringBuffer().append("port is invalid: ").append(defaultPort).toString());
        }
        this.scheme = scheme;
        this.socketFactory = factory;
        this.defaultPort = defaultPort;
        this.secure = factory instanceof SecureProtocolSocketFactory;
    }

    public Protocol(String scheme, SecureProtocolSocketFactory factory, int defaultPort) {
        this(scheme, (ProtocolSocketFactory) factory, defaultPort);
    }

    public int getDefaultPort() {
        return this.defaultPort;
    }

    public ProtocolSocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public String getScheme() {
        return this.scheme;
    }

    public boolean isSecure() {
        return this.secure;
    }

    public int resolvePort(int port) {
        return port <= 0 ? getDefaultPort() : port;
    }

    public String toString() {
        return new StringBuffer().append(this.scheme).append(":").append(this.defaultPort).toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Protocol) {
            Protocol p = (Protocol) obj;
            return this.defaultPort == p.getDefaultPort() && this.scheme.equalsIgnoreCase(p.getScheme()) && this.secure == p.isSecure() && this.socketFactory.equals(p.getSocketFactory());
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.defaultPort);
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(hash, this.scheme.toLowerCase()), this.secure), this.socketFactory);
    }
}

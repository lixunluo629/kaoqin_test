package org.apache.commons.httpclient;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpHost.class */
public class HttpHost implements Cloneable {
    private String hostname;
    private int port;
    private Protocol protocol;

    public HttpHost(String hostname, int port, Protocol protocol) {
        this.hostname = null;
        this.port = -1;
        this.protocol = null;
        if (hostname == null) {
            throw new IllegalArgumentException("Host name may not be null");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("Protocol may not be null");
        }
        this.hostname = hostname;
        this.protocol = protocol;
        if (port >= 0) {
            this.port = port;
        } else {
            this.port = this.protocol.getDefaultPort();
        }
    }

    public HttpHost(String hostname, int port) {
        this(hostname, port, Protocol.getProtocol("http"));
    }

    public HttpHost(String hostname) {
        this(hostname, -1, Protocol.getProtocol("http"));
    }

    public HttpHost(URI uri) throws URIException {
        this(uri.getHost(), uri.getPort(), Protocol.getProtocol(uri.getScheme()));
    }

    public HttpHost(HttpHost httphost) {
        this.hostname = null;
        this.port = -1;
        this.protocol = null;
        init(httphost);
    }

    private void init(HttpHost httphost) {
        this.hostname = httphost.hostname;
        this.port = httphost.port;
        this.protocol = httphost.protocol;
    }

    public Object clone() throws CloneNotSupportedException {
        HttpHost copy = (HttpHost) super.clone();
        copy.init(this);
        return copy;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public String toURI() {
        StringBuffer buffer = new StringBuffer(50);
        buffer.append(this.protocol.getScheme());
        buffer.append("://");
        buffer.append(this.hostname);
        if (this.port != this.protocol.getDefaultPort()) {
            buffer.append(':');
            buffer.append(this.port);
        }
        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(50);
        buffer.append(toURI());
        return buffer.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof HttpHost) {
            if (o == this) {
                return true;
            }
            HttpHost that = (HttpHost) o;
            if (!this.hostname.equalsIgnoreCase(that.hostname) || this.port != that.port || !this.protocol.equals(that.protocol)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.hostname);
        return LangUtils.hashCode(LangUtils.hashCode(hash, this.port), this.protocol);
    }
}

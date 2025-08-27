package org.apache.commons.httpclient;

import java.net.InetAddress;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HostConfiguration.class */
public class HostConfiguration implements Cloneable {
    public static final HostConfiguration ANY_HOST_CONFIGURATION = new HostConfiguration();
    private HttpHost host = null;
    private ProxyHost proxyHost = null;
    private InetAddress localAddress = null;
    private HostParams params = new HostParams();

    public HostConfiguration() {
    }

    public HostConfiguration(HostConfiguration hostConfiguration) {
        init(hostConfiguration);
    }

    private void init(HostConfiguration hostConfiguration) {
        synchronized (hostConfiguration) {
            try {
                if (hostConfiguration.host != null) {
                    this.host = (HttpHost) hostConfiguration.host.clone();
                } else {
                    this.host = null;
                }
                if (hostConfiguration.proxyHost != null) {
                    this.proxyHost = (ProxyHost) hostConfiguration.proxyHost.clone();
                } else {
                    this.proxyHost = null;
                }
                this.localAddress = hostConfiguration.getLocalAddress();
                this.params = (HostParams) hostConfiguration.getParams().clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalArgumentException("Host configuration could not be cloned");
            }
        }
    }

    public Object clone() {
        try {
            HostConfiguration copy = (HostConfiguration) super.clone();
            copy.init(this);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Host configuration could not be cloned");
        }
    }

    public synchronized String toString() {
        boolean appendComma = false;
        StringBuffer b = new StringBuffer(50);
        b.append("HostConfiguration[");
        if (this.host != null) {
            appendComma = true;
            b.append("host=").append(this.host);
        }
        if (this.proxyHost != null) {
            if (appendComma) {
                b.append(", ");
            } else {
                appendComma = true;
            }
            b.append("proxyHost=").append(this.proxyHost);
        }
        if (this.localAddress != null) {
            if (appendComma) {
                b.append(", ");
            } else {
                appendComma = true;
            }
            b.append("localAddress=").append(this.localAddress);
            if (appendComma) {
                b.append(", ");
            }
            b.append("params=").append(this.params);
        }
        b.append("]");
        return b.toString();
    }

    public synchronized boolean hostEquals(HttpConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection may not be null");
        }
        if (this.host == null || !this.host.getHostName().equalsIgnoreCase(connection.getHost()) || this.host.getPort() != connection.getPort() || !this.host.getProtocol().equals(connection.getProtocol())) {
            return false;
        }
        if (this.localAddress != null) {
            if (!this.localAddress.equals(connection.getLocalAddress())) {
                return false;
            }
            return true;
        }
        if (connection.getLocalAddress() != null) {
            return false;
        }
        return true;
    }

    public synchronized boolean proxyEquals(HttpConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection may not be null");
        }
        return this.proxyHost != null ? this.proxyHost.getHostName().equalsIgnoreCase(connection.getProxyHost()) && this.proxyHost.getPort() == connection.getProxyPort() : connection.getProxyHost() == null;
    }

    public synchronized boolean isHostSet() {
        return this.host != null;
    }

    public synchronized void setHost(HttpHost host) {
        this.host = host;
    }

    public synchronized void setHost(String host, int port, String protocol) {
        this.host = new HttpHost(host, port, Protocol.getProtocol(protocol));
    }

    public synchronized void setHost(String host, String virtualHost, int port, Protocol protocol) {
        setHost(host, port, protocol);
        this.params.setVirtualHost(virtualHost);
    }

    public synchronized void setHost(String host, int port, Protocol protocol) {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("protocol must not be null");
        }
        this.host = new HttpHost(host, port, protocol);
    }

    public synchronized void setHost(String host, int port) {
        setHost(host, port, Protocol.getProtocol("http"));
    }

    public synchronized void setHost(String host) throws IllegalStateException {
        Protocol defaultProtocol = Protocol.getProtocol("http");
        setHost(host, defaultProtocol.getDefaultPort(), defaultProtocol);
    }

    public synchronized void setHost(URI uri) {
        try {
            setHost(uri.getHost(), uri.getPort(), uri.getScheme());
        } catch (URIException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public synchronized String getHostURL() {
        if (this.host == null) {
            throw new IllegalStateException("Host must be set to create a host URL");
        }
        return this.host.toURI();
    }

    public synchronized String getHost() {
        if (this.host != null) {
            return this.host.getHostName();
        }
        return null;
    }

    public synchronized String getVirtualHost() {
        return this.params.getVirtualHost();
    }

    public synchronized int getPort() {
        if (this.host != null) {
            return this.host.getPort();
        }
        return -1;
    }

    public synchronized Protocol getProtocol() {
        if (this.host != null) {
            return this.host.getProtocol();
        }
        return null;
    }

    public synchronized boolean isProxySet() {
        return this.proxyHost != null;
    }

    public synchronized void setProxyHost(ProxyHost proxyHost) {
        this.proxyHost = proxyHost;
    }

    public synchronized void setProxy(String proxyHost, int proxyPort) {
        this.proxyHost = new ProxyHost(proxyHost, proxyPort);
    }

    public synchronized String getProxyHost() {
        if (this.proxyHost != null) {
            return this.proxyHost.getHostName();
        }
        return null;
    }

    public synchronized int getProxyPort() {
        if (this.proxyHost != null) {
            return this.proxyHost.getPort();
        }
        return -1;
    }

    public synchronized void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public synchronized InetAddress getLocalAddress() {
        return this.localAddress;
    }

    public HostParams getParams() {
        return this.params;
    }

    public void setParams(HostParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    public synchronized boolean equals(Object o) {
        if (o instanceof HostConfiguration) {
            if (o == this) {
                return true;
            }
            HostConfiguration that = (HostConfiguration) o;
            return LangUtils.equals(this.host, that.host) && LangUtils.equals(this.proxyHost, that.proxyHost) && LangUtils.equals(this.localAddress, that.localAddress);
        }
        return false;
    }

    public synchronized int hashCode() {
        int hash = LangUtils.hashCode(17, this.host);
        return LangUtils.hashCode(LangUtils.hashCode(hash, this.proxyHost), this.localAddress);
    }
}

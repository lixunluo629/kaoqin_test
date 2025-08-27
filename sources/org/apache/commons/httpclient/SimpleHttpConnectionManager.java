package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/SimpleHttpConnectionManager.class */
public class SimpleHttpConnectionManager implements HttpConnectionManager {
    private static final Log LOG;
    private static final String MISUSE_MESSAGE = "SimpleHttpConnectionManager being used incorrectly.  Be sure that HttpMethod.releaseConnection() is always called and that only one thread and/or method is using this connection manager at a time.";
    protected HttpConnection httpConnection;
    private HttpConnectionManagerParams params;
    private long idleStartTime;
    private volatile boolean inUse;
    private boolean alwaysClose;
    static Class class$org$apache$commons$httpclient$SimpleHttpConnectionManager;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$SimpleHttpConnectionManager == null) {
            clsClass$ = class$("org.apache.commons.httpclient.SimpleHttpConnectionManager");
            class$org$apache$commons$httpclient$SimpleHttpConnectionManager = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$SimpleHttpConnectionManager;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    static void finishLastResponse(HttpConnection conn) throws IOException {
        InputStream lastResponse = conn.getLastResponseInputStream();
        if (lastResponse != null) {
            conn.setLastResponseInputStream(null);
            try {
                lastResponse.close();
            } catch (IOException e) {
                conn.close();
            }
        }
    }

    public SimpleHttpConnectionManager(boolean alwaysClose) {
        this.params = new HttpConnectionManagerParams();
        this.idleStartTime = Long.MAX_VALUE;
        this.inUse = false;
        this.alwaysClose = false;
        this.alwaysClose = alwaysClose;
    }

    public SimpleHttpConnectionManager() {
        this.params = new HttpConnectionManagerParams();
        this.idleStartTime = Long.MAX_VALUE;
        this.inUse = false;
        this.alwaysClose = false;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnection(HostConfiguration hostConfiguration) {
        return getConnection(hostConfiguration, 0L);
    }

    public boolean isConnectionStaleCheckingEnabled() {
        return this.params.isStaleCheckingEnabled();
    }

    public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
        this.params.setStaleCheckingEnabled(connectionStaleCheckingEnabled);
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws IllegalStateException, IOException {
        if (this.httpConnection == null) {
            this.httpConnection = new HttpConnection(hostConfiguration);
            this.httpConnection.setHttpConnectionManager(this);
            this.httpConnection.getParams().setDefaults(this.params);
        } else if (!hostConfiguration.hostEquals(this.httpConnection) || !hostConfiguration.proxyEquals(this.httpConnection)) {
            if (this.httpConnection.isOpen()) {
                this.httpConnection.close();
            }
            this.httpConnection.setHost(hostConfiguration.getHost());
            this.httpConnection.setPort(hostConfiguration.getPort());
            this.httpConnection.setProtocol(hostConfiguration.getProtocol());
            this.httpConnection.setLocalAddress(hostConfiguration.getLocalAddress());
            this.httpConnection.setProxyHost(hostConfiguration.getProxyHost());
            this.httpConnection.setProxyPort(hostConfiguration.getProxyPort());
        } else {
            finishLastResponse(this.httpConnection);
        }
        this.idleStartTime = Long.MAX_VALUE;
        if (this.inUse) {
            LOG.warn(MISUSE_MESSAGE);
        }
        this.inUse = true;
        return this.httpConnection;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) {
        return getConnectionWithTimeout(hostConfiguration, timeout);
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void releaseConnection(HttpConnection conn) throws IOException {
        if (conn != this.httpConnection) {
            throw new IllegalStateException("Unexpected release of an unknown connection.");
        }
        if (this.alwaysClose) {
            this.httpConnection.close();
        } else {
            finishLastResponse(this.httpConnection);
        }
        this.inUse = false;
        this.idleStartTime = System.currentTimeMillis();
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnectionManagerParams getParams() {
        return this.params;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void setParams(HttpConnectionManagerParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void closeIdleConnections(long idleTimeout) throws IOException {
        long maxIdleTime = System.currentTimeMillis() - idleTimeout;
        if (this.idleStartTime <= maxIdleTime) {
            this.httpConnection.close();
        }
    }

    public void shutdown() throws IOException {
        this.httpConnection.close();
    }
}

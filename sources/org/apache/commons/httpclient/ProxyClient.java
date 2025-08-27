package org.apache.commons.httpclient;

import java.io.IOException;
import java.net.Socket;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ProxyClient.class */
public class ProxyClient {
    private HttpState state;
    private HttpClientParams params;
    private HostConfiguration hostConfiguration;

    /* renamed from: org.apache.commons.httpclient.ProxyClient$1, reason: invalid class name */
    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ProxyClient$1.class */
    static class AnonymousClass1 {
    }

    public ProxyClient() {
        this(new HttpClientParams());
    }

    public ProxyClient(HttpClientParams params) {
        this.state = new HttpState();
        this.params = null;
        this.hostConfiguration = new HostConfiguration();
        if (params == null) {
            throw new IllegalArgumentException("Params may not be null");
        }
        this.params = params;
    }

    public synchronized HttpState getState() {
        return this.state;
    }

    public synchronized void setState(HttpState state) {
        this.state = state;
    }

    public synchronized HostConfiguration getHostConfiguration() {
        return this.hostConfiguration;
    }

    public synchronized void setHostConfiguration(HostConfiguration hostConfiguration) {
        this.hostConfiguration = hostConfiguration;
    }

    public synchronized HttpClientParams getParams() {
        return this.params;
    }

    public synchronized void setParams(HttpClientParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    public ConnectResponse connect() throws IOException {
        HostConfiguration hostconf = getHostConfiguration();
        if (hostconf.getProxyHost() == null) {
            throw new IllegalStateException("proxy host must be configured");
        }
        if (hostconf.getHost() == null) {
            throw new IllegalStateException("destination host must be configured");
        }
        if (hostconf.getProtocol().isSecure()) {
            throw new IllegalStateException("secure protocol socket factory may not be used");
        }
        ConnectMethod method = new ConnectMethod(getHostConfiguration());
        method.getParams().setDefaults(getParams());
        DummyConnectionManager connectionManager = new DummyConnectionManager();
        connectionManager.setConnectionParams(getParams());
        HttpMethodDirector director = new HttpMethodDirector(connectionManager, hostconf, getParams(), getState());
        director.executeMethod(method);
        ConnectResponse response = new ConnectResponse(null);
        response.setConnectMethod(method);
        if (method.getStatusCode() != 200) {
            connectionManager.getConnection().close();
        } else {
            response.setSocket(connectionManager.getConnection().getSocket());
        }
        return response;
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ProxyClient$ConnectResponse.class */
    public static class ConnectResponse {
        private ConnectMethod connectMethod;
        private Socket socket;

        ConnectResponse(AnonymousClass1 x0) {
            this();
        }

        private ConnectResponse() {
        }

        public ConnectMethod getConnectMethod() {
            return this.connectMethod;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setConnectMethod(ConnectMethod connectMethod) {
            this.connectMethod = connectMethod;
        }

        public Socket getSocket() {
            return this.socket;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSocket(Socket socket) {
            this.socket = socket;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ProxyClient$DummyConnectionManager.class */
    static class DummyConnectionManager implements HttpConnectionManager {
        private HttpConnection httpConnection;
        private HttpParams connectionParams;

        DummyConnectionManager() {
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public void closeIdleConnections(long idleTimeout) {
        }

        public HttpConnection getConnection() {
            return this.httpConnection;
        }

        public void setConnectionParams(HttpParams httpParams) {
            this.connectionParams = httpParams;
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) {
            this.httpConnection = new HttpConnection(hostConfiguration);
            this.httpConnection.setHttpConnectionManager(this);
            this.httpConnection.getParams().setDefaults(this.connectionParams);
            return this.httpConnection;
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) throws HttpException {
            return getConnectionWithTimeout(hostConfiguration, timeout);
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public HttpConnection getConnection(HostConfiguration hostConfiguration) {
            return getConnectionWithTimeout(hostConfiguration, -1L);
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public void releaseConnection(HttpConnection conn) {
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public HttpConnectionManagerParams getParams() {
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnectionManager
        public void setParams(HttpConnectionManagerParams params) {
        }
    }
}

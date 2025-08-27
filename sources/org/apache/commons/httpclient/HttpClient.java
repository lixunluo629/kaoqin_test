package org.apache.commons.httpclient;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpClient.class */
public class HttpClient {
    private static final Log LOG;
    private HttpConnectionManager httpConnectionManager;
    private HttpState state;
    private HttpClientParams params;
    private HostConfiguration hostConfiguration;
    static Class class$org$apache$commons$httpclient$HttpClient;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpClient == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpClient");
            class$org$apache$commons$httpclient$HttpClient = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpClient;
        }
        LOG = LogFactory.getLog(clsClass$);
        if (LOG.isDebugEnabled()) {
            try {
                LOG.debug(new StringBuffer().append("Java version: ").append(System.getProperty("java.version")).toString());
                LOG.debug(new StringBuffer().append("Java vendor: ").append(System.getProperty("java.vendor")).toString());
                LOG.debug(new StringBuffer().append("Java class path: ").append(System.getProperty("java.class.path")).toString());
                LOG.debug(new StringBuffer().append("Operating system name: ").append(System.getProperty("os.name")).toString());
                LOG.debug(new StringBuffer().append("Operating system architecture: ").append(System.getProperty("os.arch")).toString());
                LOG.debug(new StringBuffer().append("Operating system version: ").append(System.getProperty("os.version")).toString());
                Provider[] providers = Security.getProviders();
                for (Provider provider : providers) {
                    LOG.debug(new StringBuffer().append(provider.getName()).append(SymbolConstants.SPACE_SYMBOL).append(provider.getVersion()).append(": ").append(provider.getInfo()).toString());
                }
            } catch (SecurityException e) {
            }
        }
    }

    public HttpClient() {
        this(new HttpClientParams());
    }

    public HttpClient(HttpClientParams params) {
        this.state = new HttpState();
        this.params = null;
        this.hostConfiguration = new HostConfiguration();
        if (params == null) {
            throw new IllegalArgumentException("Params may not be null");
        }
        this.params = params;
        this.httpConnectionManager = null;
        Class clazz = params.getConnectionManagerClass();
        if (clazz != null) {
            try {
                this.httpConnectionManager = (HttpConnectionManager) clazz.newInstance();
            } catch (Exception e) {
                LOG.warn("Error instantiating connection manager class, defaulting to SimpleHttpConnectionManager", e);
            }
        }
        if (this.httpConnectionManager == null) {
            this.httpConnectionManager = new SimpleHttpConnectionManager();
        }
        if (this.httpConnectionManager != null) {
            this.httpConnectionManager.getParams().setDefaults(this.params);
        }
    }

    public HttpClient(HttpClientParams params, HttpConnectionManager httpConnectionManager) {
        this.state = new HttpState();
        this.params = null;
        this.hostConfiguration = new HostConfiguration();
        if (httpConnectionManager == null) {
            throw new IllegalArgumentException("httpConnectionManager cannot be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("Params may not be null");
        }
        this.params = params;
        this.httpConnectionManager = httpConnectionManager;
        this.httpConnectionManager.getParams().setDefaults(this.params);
    }

    public HttpClient(HttpConnectionManager httpConnectionManager) {
        this(new HttpClientParams(), httpConnectionManager);
    }

    public synchronized HttpState getState() {
        return this.state;
    }

    public synchronized void setState(HttpState state) {
        this.state = state;
    }

    public synchronized void setStrictMode(boolean strictMode) {
        if (strictMode) {
            this.params.makeStrict();
        } else {
            this.params.makeLenient();
        }
    }

    public synchronized boolean isStrictMode() {
        return false;
    }

    public synchronized void setTimeout(int newTimeoutInMilliseconds) {
        this.params.setSoTimeout(newTimeoutInMilliseconds);
    }

    public synchronized void setHttpConnectionFactoryTimeout(long timeout) {
        this.params.setConnectionManagerTimeout(timeout);
    }

    public synchronized void setConnectionTimeout(int newTimeoutInMilliseconds) {
        this.httpConnectionManager.getParams().setConnectionTimeout(newTimeoutInMilliseconds);
    }

    public int executeMethod(HttpMethod method) throws IOException {
        LOG.trace("enter HttpClient.executeMethod(HttpMethod)");
        return executeMethod(null, method, null);
    }

    public int executeMethod(HostConfiguration hostConfiguration, HttpMethod method) throws IOException {
        LOG.trace("enter HttpClient.executeMethod(HostConfiguration,HttpMethod)");
        return executeMethod(hostConfiguration, method, null);
    }

    public int executeMethod(HostConfiguration hostconfig, HttpMethod method, HttpState state) throws IOException {
        LOG.trace("enter HttpClient.executeMethod(HostConfiguration,HttpMethod,HttpState)");
        if (method == null) {
            throw new IllegalArgumentException("HttpMethod parameter may not be null");
        }
        HostConfiguration defaulthostconfig = getHostConfiguration();
        if (hostconfig == null) {
            hostconfig = defaulthostconfig;
        }
        URI uri = method.getURI();
        if (hostconfig == defaulthostconfig || uri.isAbsoluteURI()) {
            hostconfig = (HostConfiguration) hostconfig.clone();
            if (uri.isAbsoluteURI()) {
                hostconfig.setHost(uri);
            }
        }
        HttpMethodDirector methodDirector = new HttpMethodDirector(getHttpConnectionManager(), hostconfig, this.params, state == null ? getState() : state);
        methodDirector.executeMethod(method);
        return method.getStatusCode();
    }

    public String getHost() {
        return this.hostConfiguration.getHost();
    }

    public int getPort() {
        return this.hostConfiguration.getPort();
    }

    public synchronized HostConfiguration getHostConfiguration() {
        return this.hostConfiguration;
    }

    public synchronized void setHostConfiguration(HostConfiguration hostConfiguration) {
        this.hostConfiguration = hostConfiguration;
    }

    public synchronized HttpConnectionManager getHttpConnectionManager() {
        return this.httpConnectionManager;
    }

    public synchronized void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
        if (this.httpConnectionManager != null) {
            this.httpConnectionManager.getParams().setDefaults(this.params);
        }
    }

    public HttpClientParams getParams() {
        return this.params;
    }

    public void setParams(HttpClientParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }
}

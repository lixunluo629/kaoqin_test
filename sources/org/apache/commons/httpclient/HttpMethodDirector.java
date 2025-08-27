package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.auth.AuthChallengeException;
import org.apache.commons.httpclient.auth.AuthChallengeParser;
import org.apache.commons.httpclient.auth.AuthChallengeProcessor;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.AuthState;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.MalformedChallengeException;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpMethodDirector.class */
class HttpMethodDirector {
    public static final String WWW_AUTH_CHALLENGE = "WWW-Authenticate";
    public static final String WWW_AUTH_RESP = "Authorization";
    public static final String PROXY_AUTH_CHALLENGE = "Proxy-Authenticate";
    public static final String PROXY_AUTH_RESP = "Proxy-Authorization";
    private static final Log LOG;
    private ConnectMethod connectMethod;
    private HttpState state;
    private HostConfiguration hostConfiguration;
    private HttpConnectionManager connectionManager;
    private HttpClientParams params;
    private HttpConnection conn;
    private AuthChallengeProcessor authProcessor;
    static Class class$org$apache$commons$httpclient$HttpMethodDirector;
    private boolean releaseConnection = false;
    private Set redirectLocations = null;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpMethodDirector == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpMethodDirector");
            class$org$apache$commons$httpclient$HttpMethodDirector = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpMethodDirector;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public HttpMethodDirector(HttpConnectionManager connectionManager, HostConfiguration hostConfiguration, HttpClientParams params, HttpState state) {
        this.authProcessor = null;
        this.connectionManager = connectionManager;
        this.hostConfiguration = hostConfiguration;
        this.params = params;
        this.state = state;
        this.authProcessor = new AuthChallengeProcessor(this.params);
    }

    public void executeMethod(HttpMethod method) throws IOException {
        boolean z;
        InputStream responseBodyAsStream;
        if (method == null) {
            throw new IllegalArgumentException("Method may not be null");
        }
        this.hostConfiguration.getParams().setDefaults(this.params);
        method.getParams().setDefaults(this.hostConfiguration.getParams());
        Collection defaults = (Collection) this.hostConfiguration.getParams().getParameter(HostParams.DEFAULT_HEADERS);
        if (defaults != null) {
            Iterator i = defaults.iterator();
            while (i.hasNext()) {
                method.addRequestHeader((Header) i.next());
            }
        }
        try {
            int maxRedirects = this.params.getIntParameter(HttpClientParams.MAX_REDIRECTS, 100);
            int redirectCount = 0;
            while (true) {
                if (this.conn != null && !this.hostConfiguration.hostEquals(this.conn)) {
                    this.conn.setLocked(false);
                    this.conn.releaseConnection();
                    this.conn = null;
                }
                if (this.conn == null) {
                    this.conn = this.connectionManager.getConnectionWithTimeout(this.hostConfiguration, this.params.getConnectionManagerTimeout());
                    this.conn.setLocked(true);
                    if (this.params.isAuthenticationPreemptive() || this.state.isAuthenticationPreemptive()) {
                        LOG.debug("Preemptively sending default basic credentials");
                        method.getHostAuthState().setPreemptive();
                        method.getHostAuthState().setAuthAttempted(true);
                        if (this.conn.isProxied() && !this.conn.isSecure()) {
                            method.getProxyAuthState().setPreemptive();
                            method.getProxyAuthState().setAuthAttempted(true);
                        }
                    }
                }
                authenticate(method);
                executeWithRetry(method);
                if (this.connectMethod != null) {
                    fakeResponse(method);
                    break;
                }
                boolean retry = false;
                if (isRedirectNeeded(method) && processRedirectResponse(method)) {
                    retry = true;
                    redirectCount++;
                    if (redirectCount >= maxRedirects) {
                        LOG.error("Narrowly avoided an infinite loop in execute");
                        throw new RedirectException(new StringBuffer().append("Maximum redirects (").append(maxRedirects).append(") exceeded").toString());
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(new StringBuffer().append("Execute redirect ").append(redirectCount).append(" of ").append(maxRedirects).toString());
                    }
                }
                if (isAuthenticationNeeded(method) && processAuthenticationResponse(method)) {
                    LOG.debug("Retry authentication");
                    retry = true;
                }
                if (!retry) {
                    break;
                } else if (method.getResponseBodyAsStream() != null) {
                    method.getResponseBodyAsStream().close();
                }
            }
            if (!z) {
                if (responseBodyAsStream != null) {
                    return;
                }
            }
        } finally {
            if (this.conn != null) {
                this.conn.setLocked(false);
            }
            if ((this.releaseConnection || method.getResponseBodyAsStream() == null) && this.conn != null) {
                this.conn.releaseConnection();
            }
        }
    }

    private void authenticate(HttpMethod method) {
        try {
            if (this.conn.isProxied() && !this.conn.isSecure()) {
                authenticateProxy(method);
            }
            authenticateHost(method);
        } catch (AuthenticationException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private boolean cleanAuthHeaders(HttpMethod method, String name) {
        Header[] authheaders = method.getRequestHeaders(name);
        boolean clean = true;
        for (Header authheader : authheaders) {
            if (authheader.isAutogenerated()) {
                method.removeRequestHeader(authheader);
            } else {
                clean = false;
            }
        }
        return clean;
    }

    private void authenticateHost(HttpMethod method) throws AuthenticationException {
        AuthState authstate;
        AuthScheme authscheme;
        if (!cleanAuthHeaders(method, "Authorization") || (authscheme = (authstate = method.getHostAuthState()).getAuthScheme()) == null) {
            return;
        }
        if (authstate.isAuthRequested() || !authscheme.isConnectionBased()) {
            String host = method.getParams().getVirtualHost();
            if (host == null) {
                host = this.conn.getHost();
            }
            int port = this.conn.getPort();
            AuthScope authscope = new AuthScope(host, port, authscheme.getRealm(), authscheme.getSchemeName());
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Authenticating with ").append(authscope).toString());
            }
            Credentials credentials = this.state.getCredentials(authscope);
            if (credentials != null) {
                String authstring = authscheme.authenticate(credentials, method);
                if (authstring != null) {
                    method.addRequestHeader(new Header("Authorization", authstring, true));
                    return;
                }
                return;
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Required credentials not available for ").append(authscope).toString());
                if (method.getHostAuthState().isPreemptive()) {
                    LOG.warn("Preemptive authentication requested but no default credentials available");
                }
            }
        }
    }

    private void authenticateProxy(HttpMethod method) throws AuthenticationException {
        AuthState authstate;
        AuthScheme authscheme;
        if (!cleanAuthHeaders(method, "Proxy-Authorization") || (authscheme = (authstate = method.getProxyAuthState()).getAuthScheme()) == null) {
            return;
        }
        if (authstate.isAuthRequested() || !authscheme.isConnectionBased()) {
            AuthScope authscope = new AuthScope(this.conn.getProxyHost(), this.conn.getProxyPort(), authscheme.getRealm(), authscheme.getSchemeName());
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Authenticating with ").append(authscope).toString());
            }
            Credentials credentials = this.state.getProxyCredentials(authscope);
            if (credentials != null) {
                String authstring = authscheme.authenticate(credentials, method);
                if (authstring != null) {
                    method.addRequestHeader(new Header("Proxy-Authorization", authstring, true));
                    return;
                }
                return;
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Required proxy credentials not available for ").append(authscope).toString());
                if (method.getProxyAuthState().isPreemptive()) {
                    LOG.warn("Preemptive authentication requested but no default proxy credentials available");
                }
            }
        }
    }

    private void applyConnectionParams(HttpMethod method) throws IllegalStateException, IOException {
        int timeout = 0;
        Object param = method.getParams().getParameter("http.socket.timeout");
        if (param == null) {
            param = this.conn.getParams().getParameter("http.socket.timeout");
        }
        if (param != null) {
            timeout = ((Integer) param).intValue();
        }
        this.conn.setSocketTimeout(timeout);
    }

    private void executeWithRetry(HttpMethod method) throws IOException {
        MethodRetryHandler handler;
        int execCount = 0;
        while (true) {
            try {
                execCount++;
                try {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(new StringBuffer().append("Attempt number ").append(execCount).append(" to process request").toString());
                    }
                    if (this.conn.getParams().isStaleCheckingEnabled()) {
                        this.conn.closeIfStale();
                    }
                    if (!this.conn.isOpen()) {
                        this.conn.open();
                        if (this.conn.isProxied() && this.conn.isSecure() && !(method instanceof ConnectMethod) && !executeConnect()) {
                            return;
                        }
                    }
                    applyConnectionParams(method);
                    method.execute(this.state, this.conn);
                    return;
                } catch (HttpException e) {
                    throw e;
                } catch (IOException e2) {
                    LOG.debug("Closing the connection.");
                    this.conn.close();
                    if ((method instanceof HttpMethodBase) && (handler = ((HttpMethodBase) method).getMethodRetryHandler()) != null && !handler.retryMethod(method, this.conn, new HttpRecoverableException(e2.getMessage()), execCount, method.isRequestSent())) {
                        LOG.debug("Method retry handler returned false. Automatic recovery will not be attempted");
                        throw e2;
                    }
                    HttpMethodRetryHandler handler2 = (HttpMethodRetryHandler) method.getParams().getParameter(HttpMethodParams.RETRY_HANDLER);
                    if (handler2 == null) {
                        handler2 = new DefaultHttpMethodRetryHandler();
                    }
                    if (!handler2.retryMethod(method, e2, execCount)) {
                        LOG.debug("Method retry handler returned false. Automatic recovery will not be attempted");
                        throw e2;
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info(new StringBuffer().append("I/O exception (").append(e2.getClass().getName()).append(") caught when processing request: ").append(e2.getMessage()).toString());
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(e2.getMessage(), e2);
                    }
                    LOG.info("Retrying request");
                }
            } catch (IOException e3) {
                if (this.conn.isOpen()) {
                    LOG.debug("Closing the connection.");
                    this.conn.close();
                }
                this.releaseConnection = true;
                throw e3;
            } catch (RuntimeException e4) {
                if (this.conn.isOpen()) {
                    LOG.debug("Closing the connection.");
                    this.conn.close();
                }
                this.releaseConnection = true;
                throw e4;
            }
        }
    }

    private boolean executeConnect() throws IllegalStateException, IOException, IllegalArgumentException {
        int code;
        this.connectMethod = new ConnectMethod(this.hostConfiguration);
        this.connectMethod.getParams().setDefaults(this.hostConfiguration.getParams());
        while (true) {
            if (!this.conn.isOpen()) {
                this.conn.open();
            }
            if (this.params.isAuthenticationPreemptive() || this.state.isAuthenticationPreemptive()) {
                LOG.debug("Preemptively sending default basic credentials");
                this.connectMethod.getProxyAuthState().setPreemptive();
                this.connectMethod.getProxyAuthState().setAuthAttempted(true);
            }
            try {
                authenticateProxy(this.connectMethod);
            } catch (AuthenticationException e) {
                LOG.error(e.getMessage(), e);
            }
            applyConnectionParams(this.connectMethod);
            this.connectMethod.execute(this.state, this.conn);
            code = this.connectMethod.getStatusCode();
            boolean retry = false;
            AuthState authstate = this.connectMethod.getProxyAuthState();
            authstate.setAuthRequested(code == 407);
            if (authstate.isAuthRequested() && processAuthenticationResponse(this.connectMethod)) {
                retry = true;
            }
            if (!retry) {
                break;
            }
            if (this.connectMethod.getResponseBodyAsStream() != null) {
                this.connectMethod.getResponseBodyAsStream().close();
            }
        }
        if (code >= 200 && code < 300) {
            this.conn.tunnelCreated();
            this.connectMethod = null;
            return true;
        }
        this.conn.close();
        return false;
    }

    private void fakeResponse(HttpMethod method) throws IOException {
        LOG.debug("CONNECT failed, fake the response for the original method");
        if (method instanceof HttpMethodBase) {
            ((HttpMethodBase) method).fakeResponse(this.connectMethod.getStatusLine(), this.connectMethod.getResponseHeaderGroup(), this.connectMethod.getResponseBodyAsStream());
            method.getProxyAuthState().setAuthScheme(this.connectMethod.getProxyAuthState().getAuthScheme());
            this.connectMethod = null;
        } else {
            this.releaseConnection = true;
            LOG.warn("Unable to fake response on method as it is not derived from HttpMethodBase.");
        }
    }

    private boolean processRedirectResponse(HttpMethod method) throws RedirectException {
        Header locationHeader = method.getResponseHeader("location");
        if (locationHeader == null) {
            LOG.error(new StringBuffer().append("Received redirect response ").append(method.getStatusCode()).append(" but no location header").toString());
            return false;
        }
        String location = locationHeader.getValue();
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Redirect requested to location '").append(location).append("'").toString());
        }
        try {
            URI currentUri = new URI(this.conn.getProtocol().getScheme(), (String) null, this.conn.getHost(), this.conn.getPort(), method.getPath());
            String charset = method.getParams().getUriCharset();
            URI redirectUri = new URI(location, true, charset);
            if (redirectUri.isRelativeURI()) {
                if (this.params.isParameterTrue(HttpClientParams.REJECT_RELATIVE_REDIRECT)) {
                    LOG.warn(new StringBuffer().append("Relative redirect location '").append(location).append("' not allowed").toString());
                    return false;
                }
                LOG.debug("Redirect URI is not absolute - parsing as relative");
                redirectUri = new URI(currentUri, redirectUri);
            } else {
                method.getParams().setDefaults(this.params);
            }
            method.setURI(redirectUri);
            this.hostConfiguration.setHost(redirectUri);
            if (this.params.isParameterFalse(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS)) {
                if (this.redirectLocations == null) {
                    this.redirectLocations = new HashSet();
                }
                this.redirectLocations.add(currentUri);
                try {
                    if (redirectUri.hasQuery()) {
                        redirectUri.setQuery(null);
                    }
                    if (this.redirectLocations.contains(redirectUri)) {
                        throw new CircularRedirectException(new StringBuffer().append("Circular redirect to '").append(redirectUri).append("'").toString());
                    }
                } catch (URIException e) {
                    return false;
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Redirecting from '").append(currentUri.getEscapedURI()).append("' to '").append(redirectUri.getEscapedURI()).toString());
            }
            method.getHostAuthState().invalidate();
            return true;
        } catch (URIException ex) {
            throw new InvalidRedirectLocationException(new StringBuffer().append("Invalid redirect location: ").append(location).toString(), location, ex);
        }
    }

    private boolean processAuthenticationResponse(HttpMethod method) {
        LOG.trace("enter HttpMethodBase.processAuthenticationResponse(HttpState, HttpConnection)");
        try {
            switch (method.getStatusCode()) {
                case 401:
                    return processWWWAuthChallenge(method);
                case 407:
                    return processProxyAuthChallenge(method);
                default:
                    return false;
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
                return false;
            }
            return false;
        }
    }

    private boolean processWWWAuthChallenge(HttpMethod method) throws MalformedChallengeException, AuthenticationException {
        AuthState authstate = method.getHostAuthState();
        Map challenges = AuthChallengeParser.parseChallenges(method.getResponseHeaders("WWW-Authenticate"));
        if (challenges.isEmpty()) {
            LOG.debug("Authentication challenge(s) not found");
            return false;
        }
        AuthScheme authscheme = null;
        try {
            authscheme = this.authProcessor.processChallenge(authstate, challenges);
        } catch (AuthChallengeException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(e.getMessage());
            }
        }
        if (authscheme == null) {
            return false;
        }
        String host = method.getParams().getVirtualHost();
        if (host == null) {
            host = this.conn.getHost();
        }
        int port = this.conn.getPort();
        AuthScope authscope = new AuthScope(host, port, authscheme.getRealm(), authscheme.getSchemeName());
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Authentication scope: ").append(authscope).toString());
        }
        if (authstate.isAuthAttempted() && authscheme.isComplete()) {
            if (promptForCredentials(authscheme, method.getParams(), authscope) == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(new StringBuffer().append("Failure authenticating with ").append(authscope).toString());
                    return false;
                }
                return false;
            }
            return true;
        }
        authstate.setAuthAttempted(true);
        Credentials credentials = this.state.getCredentials(authscope);
        if (credentials == null) {
            credentials = promptForCredentials(authscheme, method.getParams(), authscope);
        }
        if (credentials == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info(new StringBuffer().append("No credentials available for ").append(authscope).toString());
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean processProxyAuthChallenge(HttpMethod method) throws MalformedChallengeException, AuthenticationException {
        AuthState authstate = method.getProxyAuthState();
        Map proxyChallenges = AuthChallengeParser.parseChallenges(method.getResponseHeaders("Proxy-Authenticate"));
        if (proxyChallenges.isEmpty()) {
            LOG.debug("Proxy authentication challenge(s) not found");
            return false;
        }
        AuthScheme authscheme = null;
        try {
            authscheme = this.authProcessor.processChallenge(authstate, proxyChallenges);
        } catch (AuthChallengeException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(e.getMessage());
            }
        }
        if (authscheme == null) {
            return false;
        }
        AuthScope authscope = new AuthScope(this.conn.getProxyHost(), this.conn.getProxyPort(), authscheme.getRealm(), authscheme.getSchemeName());
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Proxy authentication scope: ").append(authscope).toString());
        }
        if (authstate.isAuthAttempted() && authscheme.isComplete()) {
            if (promptForProxyCredentials(authscheme, method.getParams(), authscope) == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(new StringBuffer().append("Failure authenticating with ").append(authscope).toString());
                    return false;
                }
                return false;
            }
            return true;
        }
        authstate.setAuthAttempted(true);
        Credentials credentials = this.state.getProxyCredentials(authscope);
        if (credentials == null) {
            credentials = promptForProxyCredentials(authscheme, method.getParams(), authscope);
        }
        if (credentials == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info(new StringBuffer().append("No credentials available for ").append(authscope).toString());
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean isRedirectNeeded(HttpMethod method) {
        switch (method.getStatusCode()) {
            case 301:
            case 302:
            case 303:
            case 307:
                LOG.debug("Redirect required");
                if (method.getFollowRedirects()) {
                    return true;
                }
                return false;
            case 304:
            case 305:
            case 306:
            default:
                return false;
        }
    }

    private boolean isAuthenticationNeeded(HttpMethod method) {
        method.getHostAuthState().setAuthRequested(method.getStatusCode() == 401);
        method.getProxyAuthState().setAuthRequested(method.getStatusCode() == 407);
        if (method.getHostAuthState().isAuthRequested() || method.getProxyAuthState().isAuthRequested()) {
            LOG.debug("Authorization required");
            if (method.getDoAuthentication()) {
                return true;
            }
            LOG.info("Authentication requested but doAuthentication is disabled");
            return false;
        }
        return false;
    }

    private Credentials promptForCredentials(AuthScheme authScheme, HttpParams params, AuthScope authscope) {
        LOG.debug("Credentials required");
        Credentials creds = null;
        CredentialsProvider credProvider = (CredentialsProvider) params.getParameter(CredentialsProvider.PROVIDER);
        if (credProvider != null) {
            try {
                creds = credProvider.getCredentials(authScheme, authscope.getHost(), authscope.getPort(), false);
            } catch (CredentialsNotAvailableException e) {
                LOG.warn(e.getMessage());
            }
            if (creds != null) {
                this.state.setCredentials(authscope, creds);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append(authscope).append(" new credentials given").toString());
                }
            }
        } else {
            LOG.debug("Credentials provider not available");
        }
        return creds;
    }

    private Credentials promptForProxyCredentials(AuthScheme authScheme, HttpParams params, AuthScope authscope) {
        LOG.debug("Proxy credentials required");
        Credentials creds = null;
        CredentialsProvider credProvider = (CredentialsProvider) params.getParameter(CredentialsProvider.PROVIDER);
        if (credProvider != null) {
            try {
                creds = credProvider.getCredentials(authScheme, authscope.getHost(), authscope.getPort(), true);
            } catch (CredentialsNotAvailableException e) {
                LOG.warn(e.getMessage());
            }
            if (creds != null) {
                this.state.setProxyCredentials(authscope, creds);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append(authscope).append(" new credentials given").toString());
                }
            }
        } else {
            LOG.debug("Proxy credentials provider not available");
        }
        return creds;
    }

    public HostConfiguration getHostConfiguration() {
        return this.hostConfiguration;
    }

    public HttpState getState() {
        return this.state;
    }

    public HttpConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public HttpParams getParams() {
        return this.params;
    }
}

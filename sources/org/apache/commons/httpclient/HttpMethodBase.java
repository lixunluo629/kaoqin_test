package org.apache.commons.httpclient;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Collection;
import org.apache.commons.httpclient.auth.AuthState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.cookie.CookieVersionSupport;
import org.apache.commons.httpclient.cookie.MalformedCookieException;
import org.apache.commons.httpclient.cookie.RFC2109Spec;
import org.apache.commons.httpclient.cookie.RFC2965Spec;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.util.ExceptionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpMethodBase.class */
public abstract class HttpMethodBase implements HttpMethod {
    private static final Log LOG;
    private MethodRetryHandler methodRetryHandler;
    private static final int RESPONSE_WAIT_TIME_MS = 3000;
    private static final int DEFAULT_INITIAL_BUFFER_SIZE = 4096;
    static Class class$org$apache$commons$httpclient$HttpMethodBase;
    private HeaderGroup requestHeaders = new HeaderGroup();
    protected StatusLine statusLine = null;
    private HeaderGroup responseHeaders = new HeaderGroup();
    private HeaderGroup responseTrailerHeaders = new HeaderGroup();
    private String path = null;
    private String queryString = null;
    private InputStream responseStream = null;
    private HttpConnection responseConnection = null;
    private byte[] responseBody = null;
    private boolean followRedirects = false;
    private boolean doAuthentication = true;
    private HttpMethodParams params = new HttpMethodParams();
    private AuthState hostAuthState = new AuthState();
    private AuthState proxyAuthState = new AuthState();
    private boolean used = false;
    private int recoverableExceptionCount = 0;
    private HttpHost httphost = null;
    private boolean connectionCloseForced = false;
    protected HttpVersion effectiveVersion = null;
    private volatile boolean aborted = false;
    private boolean requestSent = false;
    private CookieSpec cookiespec = null;

    @Override // org.apache.commons.httpclient.HttpMethod
    public abstract String getName();

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpMethodBase == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpMethodBase");
            class$org$apache$commons$httpclient$HttpMethodBase = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpMethodBase;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public HttpMethodBase() {
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x00a3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public HttpMethodBase(java.lang.String r8) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.httpclient.HttpMethodBase.<init>(java.lang.String):void");
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public URI getURI() throws URIException {
        StringBuffer buffer = new StringBuffer();
        if (this.httphost != null) {
            buffer.append(this.httphost.getProtocol().getScheme());
            buffer.append("://");
            buffer.append(this.httphost.getHostName());
            int port = this.httphost.getPort();
            if (port != -1 && port != this.httphost.getProtocol().getDefaultPort()) {
                buffer.append(":");
                buffer.append(port);
            }
        }
        buffer.append(this.path);
        if (this.queryString != null) {
            buffer.append('?');
            buffer.append(this.queryString);
        }
        String charset = getParams().getUriCharset();
        return new URI(buffer.toString(), true, charset);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setURI(URI uri) throws URIException {
        if (uri.isAbsoluteURI()) {
            this.httphost = new HttpHost(uri);
        }
        setPath(uri.getPath() == null ? "/" : uri.getEscapedPath());
        setQueryString(uri.getEscapedQuery());
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public void setHttp11(boolean http11) {
        if (http11) {
            this.params.setVersion(HttpVersion.HTTP_1_1);
        } else {
            this.params.setVersion(HttpVersion.HTTP_1_0);
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean getDoAuthentication() {
        return this.doAuthentication;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setDoAuthentication(boolean doAuthentication) {
        this.doAuthentication = doAuthentication;
    }

    public boolean isHttp11() {
        return this.params.getVersion().equals(HttpVersion.HTTP_1_1);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setPath(String path) {
        this.path = path;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void addRequestHeader(Header header) {
        LOG.trace("HttpMethodBase.addRequestHeader(Header)");
        if (header == null) {
            LOG.debug("null header value ignored");
        } else {
            getRequestHeaderGroup().addHeader(header);
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void addResponseFooter(Header footer) {
        getResponseTrailerHeaderGroup().addHeader(footer);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public String getPath() {
        return (this.path == null || this.path.equals("")) ? "/" : this.path;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setQueryString(NameValuePair[] params) {
        LOG.trace("enter HttpMethodBase.setQueryString(NameValuePair[])");
        this.queryString = EncodingUtil.formUrlEncode(params, "UTF-8");
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public String getQueryString() {
        return this.queryString;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setRequestHeader(String headerName, String headerValue) {
        Header header = new Header(headerName, headerValue);
        setRequestHeader(header);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setRequestHeader(Header header) {
        Header[] headers = getRequestHeaderGroup().getHeaders(header.getName());
        for (Header header2 : headers) {
            getRequestHeaderGroup().removeHeader(header2);
        }
        getRequestHeaderGroup().addHeader(header);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header getRequestHeader(String headerName) {
        if (headerName == null) {
            return null;
        }
        return getRequestHeaderGroup().getCondensedHeader(headerName);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header[] getRequestHeaders() {
        return getRequestHeaderGroup().getAllHeaders();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header[] getRequestHeaders(String headerName) {
        return getRequestHeaderGroup().getHeaders(headerName);
    }

    protected HeaderGroup getRequestHeaderGroup() {
        return this.requestHeaders;
    }

    protected HeaderGroup getResponseTrailerHeaderGroup() {
        return this.responseTrailerHeaders;
    }

    protected HeaderGroup getResponseHeaderGroup() {
        return this.responseHeaders;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header[] getResponseHeaders(String headerName) {
        return getResponseHeaderGroup().getHeaders(headerName);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public int getStatusCode() {
        return this.statusLine.getStatusCode();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public StatusLine getStatusLine() {
        return this.statusLine;
    }

    private boolean responseAvailable() {
        return (this.responseBody == null && this.responseStream == null) ? false : true;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header[] getResponseHeaders() {
        return getResponseHeaderGroup().getAllHeaders();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header getResponseHeader(String headerName) {
        if (headerName == null) {
            return null;
        }
        return getResponseHeaderGroup().getCondensedHeader(headerName);
    }

    public long getResponseContentLength() {
        Header[] headers = getResponseHeaderGroup().getHeaders("Content-Length");
        if (headers.length == 0) {
            return -1L;
        }
        if (headers.length > 1) {
            LOG.warn("Multiple content-length headers detected");
        }
        for (int i = headers.length - 1; i >= 0; i--) {
            Header header = headers[i];
            try {
                return Long.parseLong(header.getValue());
            } catch (NumberFormatException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(new StringBuffer().append("Invalid content-length value: ").append(e.getMessage()).toString());
                }
            }
        }
        return -1L;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public byte[] getResponseBody() throws IOException {
        InputStream instream;
        if (this.responseBody == null && (instream = getResponseBodyAsStream()) != null) {
            long contentLength = getResponseContentLength();
            if (contentLength > 2147483647L) {
                throw new IOException(new StringBuffer().append("Content too large to be buffered: ").append(contentLength).append(" bytes").toString());
            }
            int limit = getParams().getIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, 1048576);
            if (contentLength == -1 || contentLength > limit) {
                LOG.warn("Going to buffer response body of large or unknown size. Using getResponseBodyAsStream instead is recommended.");
            }
            LOG.debug("Buffering response body");
            ByteArrayOutputStream outstream = new ByteArrayOutputStream(contentLength > 0 ? (int) contentLength : 4096);
            byte[] buffer = new byte[4096];
            while (true) {
                int len = instream.read(buffer);
                if (len <= 0) {
                    break;
                }
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            setResponseStream(null);
            this.responseBody = outstream.toByteArray();
        }
        return this.responseBody;
    }

    public byte[] getResponseBody(int maxlen) throws IOException {
        InputStream instream;
        if (maxlen < 0) {
            throw new IllegalArgumentException("maxlen must be positive");
        }
        if (this.responseBody == null && (instream = getResponseBodyAsStream()) != null) {
            long contentLength = getResponseContentLength();
            if (contentLength != -1 && contentLength > maxlen) {
                throw new HttpContentTooLargeException(new StringBuffer().append("Content-Length is ").append(contentLength).toString(), maxlen);
            }
            LOG.debug("Buffering response body");
            ByteArrayOutputStream rawdata = new ByteArrayOutputStream(contentLength > 0 ? (int) contentLength : 4096);
            byte[] buffer = new byte[2048];
            int pos = 0;
            do {
                int len = instream.read(buffer, 0, Math.min(buffer.length, maxlen - pos));
                if (len == -1) {
                    break;
                }
                rawdata.write(buffer, 0, len);
                pos += len;
            } while (pos < maxlen);
            setResponseStream(null);
            if (pos == maxlen && instream.read() != -1) {
                throw new HttpContentTooLargeException(new StringBuffer().append("Content-Length not known but larger than ").append(maxlen).toString(), maxlen);
            }
            this.responseBody = rawdata.toByteArray();
        }
        return this.responseBody;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public InputStream getResponseBodyAsStream() throws IOException {
        if (this.responseStream != null) {
            return this.responseStream;
        }
        if (this.responseBody != null) {
            InputStream byteResponseStream = new ByteArrayInputStream(this.responseBody);
            LOG.debug("re-creating response stream from byte array");
            return byteResponseStream;
        }
        return null;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public String getResponseBodyAsString() throws IOException {
        byte[] rawdata = null;
        if (responseAvailable()) {
            rawdata = getResponseBody();
        }
        if (rawdata != null) {
            return EncodingUtil.getString(rawdata, getResponseCharSet());
        }
        return null;
    }

    public String getResponseBodyAsString(int maxlen) throws IOException {
        if (maxlen < 0) {
            throw new IllegalArgumentException("maxlen must be positive");
        }
        byte[] rawdata = null;
        if (responseAvailable()) {
            rawdata = getResponseBody(maxlen);
        }
        if (rawdata != null) {
            return EncodingUtil.getString(rawdata, getResponseCharSet());
        }
        return null;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header[] getResponseFooters() {
        return getResponseTrailerHeaderGroup().getAllHeaders();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public Header getResponseFooter(String footerName) {
        if (footerName == null) {
            return null;
        }
        return getResponseTrailerHeaderGroup().getCondensedHeader(footerName);
    }

    protected void setResponseStream(InputStream responseStream) {
        this.responseStream = responseStream;
    }

    protected InputStream getResponseStream() {
        return this.responseStream;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public String getStatusText() {
        return this.statusLine.getReasonPhrase();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setStrictMode(boolean strictMode) {
        if (strictMode) {
            this.params.makeStrict();
        } else {
            this.params.makeLenient();
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean isStrictMode() {
        return false;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void addRequestHeader(String headerName, String headerValue) {
        addRequestHeader(new Header(headerName, headerValue));
    }

    protected boolean isConnectionCloseForced() {
        return this.connectionCloseForced;
    }

    protected void setConnectionCloseForced(boolean b) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Force-close connection: ").append(b).toString());
        }
        this.connectionCloseForced = b;
    }

    protected boolean shouldCloseConnection(HttpConnection conn) {
        if (isConnectionCloseForced()) {
            LOG.debug("Should force-close connection.");
            return true;
        }
        Header connectionHeader = null;
        if (!conn.isTransparent()) {
            connectionHeader = this.responseHeaders.getFirstHeader("proxy-connection");
        }
        if (connectionHeader == null) {
            connectionHeader = this.responseHeaders.getFirstHeader("connection");
        }
        if (connectionHeader == null) {
            connectionHeader = this.requestHeaders.getFirstHeader("connection");
        }
        if (connectionHeader != null) {
            if (connectionHeader.getValue().equalsIgnoreCase("close")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append("Should close connection in response to directive: ").append(connectionHeader.getValue()).toString());
                    return true;
                }
                return true;
            }
            if (connectionHeader.getValue().equalsIgnoreCase("keep-alive")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(new StringBuffer().append("Should NOT close connection in response to directive: ").append(connectionHeader.getValue()).toString());
                    return false;
                }
                return false;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Unknown directive: ").append(connectionHeader.toExternalForm()).toString());
            }
        }
        LOG.debug("Resorting to protocol version default close connection policy");
        if (this.effectiveVersion.greaterEquals(HttpVersion.HTTP_1_1)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Should NOT close connection, using ").append(this.effectiveVersion.toString()).toString());
            }
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Should close connection, using ").append(this.effectiveVersion.toString()).toString());
        }
        return this.effectiveVersion.lessEquals(HttpVersion.HTTP_1_0);
    }

    private void checkExecuteConditions(HttpState state, HttpConnection conn) throws HttpException {
        if (state == null) {
            throw new IllegalArgumentException("HttpState parameter may not be null");
        }
        if (conn == null) {
            throw new IllegalArgumentException("HttpConnection parameter may not be null");
        }
        if (this.aborted) {
            throw new IllegalStateException("Method has been aborted");
        }
        if (!validate()) {
            throw new ProtocolException("HttpMethodBase object not valid");
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public int execute(HttpState state, HttpConnection conn) throws IllegalStateException, IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.execute(HttpState, HttpConnection)");
        this.responseConnection = conn;
        checkExecuteConditions(state, conn);
        this.statusLine = null;
        this.connectionCloseForced = false;
        conn.setLastResponseInputStream(null);
        if (this.effectiveVersion == null) {
            this.effectiveVersion = this.params.getVersion();
        }
        writeRequest(state, conn);
        this.requestSent = true;
        readResponse(state, conn);
        this.used = true;
        return this.statusLine.getStatusCode();
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void abort() throws IOException {
        if (this.aborted) {
            return;
        }
        this.aborted = true;
        HttpConnection conn = this.responseConnection;
        if (conn != null) {
            conn.close();
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean hasBeenUsed() {
        return this.used;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void recycle() {
        LOG.trace("enter HttpMethodBase.recycle()");
        releaseConnection();
        this.path = null;
        this.followRedirects = false;
        this.doAuthentication = true;
        this.queryString = null;
        getRequestHeaderGroup().clear();
        getResponseHeaderGroup().clear();
        getResponseTrailerHeaderGroup().clear();
        this.statusLine = null;
        this.effectiveVersion = null;
        this.aborted = false;
        this.used = false;
        this.params = new HttpMethodParams();
        this.responseBody = null;
        this.recoverableExceptionCount = 0;
        this.connectionCloseForced = false;
        this.hostAuthState.invalidate();
        this.proxyAuthState.invalidate();
        this.cookiespec = null;
        this.requestSent = false;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void releaseConnection() {
        try {
            if (this.responseStream != null) {
                try {
                    this.responseStream.close();
                } catch (IOException e) {
                }
            }
        } finally {
            ensureConnectionRelease();
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void removeRequestHeader(String headerName) {
        Header[] headers = getRequestHeaderGroup().getHeaders(headerName);
        for (Header header : headers) {
            getRequestHeaderGroup().removeHeader(header);
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void removeRequestHeader(Header header) {
        if (header == null) {
            return;
        }
        getRequestHeaderGroup().removeHeader(header);
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean validate() {
        return true;
    }

    private CookieSpec getCookieSpec(HttpState state) {
        if (this.cookiespec == null) {
            int i = state.getCookiePolicy();
            if (i == -1) {
                this.cookiespec = CookiePolicy.getCookieSpec(this.params.getCookiePolicy());
            } else {
                this.cookiespec = CookiePolicy.getSpecByPolicy(i);
            }
            this.cookiespec.setValidDateFormats((Collection) this.params.getParameter(HttpMethodParams.DATE_PATTERNS));
        }
        return this.cookiespec;
    }

    protected void addCookieRequestHeader(HttpState state, HttpConnection conn) throws IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.addCookieRequestHeader(HttpState, HttpConnection)");
        Header[] cookieheaders = getRequestHeaderGroup().getHeaders("Cookie");
        for (Header cookieheader : cookieheaders) {
            if (cookieheader.isAutogenerated()) {
                getRequestHeaderGroup().removeHeader(cookieheader);
            }
        }
        CookieSpec matcher = getCookieSpec(state);
        String host = this.params.getVirtualHost();
        if (host == null) {
            host = conn.getHost();
        }
        Cookie[] cookies = matcher.match(host, conn.getPort(), getPath(), conn.isSecure(), state.getCookies());
        if (cookies != null && cookies.length > 0) {
            if (getParams().isParameterTrue(HttpMethodParams.SINGLE_COOKIE_HEADER)) {
                String s = matcher.formatCookies(cookies);
                getRequestHeaderGroup().addHeader(new Header("Cookie", s, true));
            } else {
                for (Cookie cookie : cookies) {
                    String s2 = matcher.formatCookie(cookie);
                    getRequestHeaderGroup().addHeader(new Header("Cookie", s2, true));
                }
            }
            if (matcher instanceof CookieVersionSupport) {
                CookieVersionSupport versupport = (CookieVersionSupport) matcher;
                int ver = versupport.getVersion();
                boolean needVersionHeader = false;
                for (Cookie cookie2 : cookies) {
                    if (ver != cookie2.getVersion()) {
                        needVersionHeader = true;
                    }
                }
                if (needVersionHeader) {
                    getRequestHeaderGroup().addHeader(versupport.getVersionHeader());
                }
            }
        }
    }

    protected void addHostRequestHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter HttpMethodBase.addHostRequestHeader(HttpState, HttpConnection)");
        String host = this.params.getVirtualHost();
        if (host != null) {
            LOG.debug(new StringBuffer().append("Using virtual host name: ").append(host).toString());
        } else {
            host = conn.getHost();
        }
        int port = conn.getPort();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding Host request header");
        }
        if (conn.getProtocol().getDefaultPort() != port) {
            host = new StringBuffer().append(host).append(":").append(port).toString();
        }
        setRequestHeader("Host", host);
    }

    protected void addProxyConnectionHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter HttpMethodBase.addProxyConnectionHeader(HttpState, HttpConnection)");
        if (!conn.isTransparent() && getRequestHeader("Proxy-Connection") == null) {
            addRequestHeader("Proxy-Connection", "Keep-Alive");
        }
    }

    protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.addRequestHeaders(HttpState, HttpConnection)");
        addUserAgentRequestHeader(state, conn);
        addHostRequestHeader(state, conn);
        addCookieRequestHeader(state, conn);
        addProxyConnectionHeader(state, conn);
    }

    protected void addUserAgentRequestHeader(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter HttpMethodBase.addUserAgentRequestHeaders(HttpState, HttpConnection)");
        if (getRequestHeader("User-Agent") == null) {
            String agent = (String) getParams().getParameter(HttpMethodParams.USER_AGENT);
            if (agent == null) {
                agent = "Jakarta Commons-HttpClient";
            }
            setRequestHeader("User-Agent", agent);
        }
    }

    protected void checkNotUsed() throws IllegalStateException {
        if (this.used) {
            throw new IllegalStateException("Already used.");
        }
    }

    protected void checkUsed() throws IllegalStateException {
        if (!this.used) {
            throw new IllegalStateException("Not Used.");
        }
    }

    protected static String generateRequestLine(HttpConnection connection, String name, String requestPath, String query, String version) {
        LOG.trace("enter HttpMethodBase.generateRequestLine(HttpConnection, String, String, String, String)");
        StringBuffer buf = new StringBuffer();
        buf.append(name);
        buf.append(SymbolConstants.SPACE_SYMBOL);
        if (!connection.isTransparent()) {
            Protocol protocol = connection.getProtocol();
            buf.append(protocol.getScheme().toLowerCase());
            buf.append("://");
            buf.append(connection.getHost());
            if (connection.getPort() != -1 && connection.getPort() != protocol.getDefaultPort()) {
                buf.append(":");
                buf.append(connection.getPort());
            }
        }
        if (requestPath == null) {
            buf.append("/");
        } else {
            if (!connection.isTransparent() && !requestPath.startsWith("/")) {
                buf.append("/");
            }
            buf.append(requestPath);
        }
        if (query != null) {
            if (query.indexOf("?") != 0) {
                buf.append("?");
            }
            buf.append(query);
        }
        buf.append(SymbolConstants.SPACE_SYMBOL);
        buf.append(version);
        buf.append("\r\n");
        return buf.toString();
    }

    protected void processResponseBody(HttpState state, HttpConnection conn) {
    }

    protected void processResponseHeaders(HttpState state, HttpConnection conn) throws IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.processResponseHeaders(HttpState, HttpConnection)");
        CookieSpec parser = getCookieSpec(state);
        Header[] headers = getResponseHeaderGroup().getHeaders(RFC2109Spec.SET_COOKIE_KEY);
        processCookieHeaders(parser, headers, state, conn);
        if (parser instanceof CookieVersionSupport) {
            CookieVersionSupport versupport = (CookieVersionSupport) parser;
            if (versupport.getVersion() > 0) {
                Header[] headers2 = getResponseHeaderGroup().getHeaders(RFC2965Spec.SET_COOKIE2_KEY);
                processCookieHeaders(parser, headers2, state, conn);
            }
        }
    }

    protected void processCookieHeaders(CookieSpec parser, Header[] headers, HttpState state, HttpConnection conn) throws IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.processCookieHeaders(Header[], HttpState, HttpConnection)");
        String host = this.params.getVirtualHost();
        if (host == null) {
            host = conn.getHost();
        }
        for (Header header : headers) {
            Cookie[] cookies = null;
            try {
                cookies = parser.parse(host, conn.getPort(), getPath(), conn.isSecure(), header);
            } catch (MalformedCookieException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(new StringBuffer().append("Invalid cookie header: \"").append(header.getValue()).append("\". ").append(e.getMessage()).toString());
                }
            }
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    try {
                        parser.validate(host, conn.getPort(), getPath(), conn.isSecure(), cookie);
                        state.addCookie(cookie);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(new StringBuffer().append("Cookie accepted: \"").append(parser.formatCookie(cookie)).append(SymbolConstants.QUOTES_SYMBOL).toString());
                        }
                    } catch (MalformedCookieException e2) {
                        if (LOG.isWarnEnabled()) {
                            LOG.warn(new StringBuffer().append("Cookie rejected: \"").append(parser.formatCookie(cookie)).append("\". ").append(e2.getMessage()).toString());
                        }
                    }
                }
            }
        }
    }

    protected void processStatusLine(HttpState state, HttpConnection conn) {
    }

    protected void readResponse(HttpState state, HttpConnection conn) throws IllegalStateException, IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.readResponse(HttpState, HttpConnection)");
        while (this.statusLine == null) {
            readStatusLine(state, conn);
            processStatusLine(state, conn);
            readResponseHeaders(state, conn);
            processResponseHeaders(state, conn);
            int status = this.statusLine.getStatusCode();
            if (status >= 100 && status < 200) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(new StringBuffer().append("Discarding unexpected response: ").append(this.statusLine.toString()).toString());
                }
                this.statusLine = null;
            }
        }
        readResponseBody(state, conn);
        processResponseBody(state, conn);
    }

    protected void readResponseBody(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter HttpMethodBase.readResponseBody(HttpState, HttpConnection)");
        InputStream stream = readResponseBody(conn);
        if (stream == null) {
            responseBodyConsumed();
        } else {
            conn.setLastResponseInputStream(stream);
            setResponseStream(stream);
        }
    }

    private InputStream readResponseBody(HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter HttpMethodBase.readResponseBody(HttpConnection)");
        this.responseBody = null;
        InputStream is = conn.getResponseInputStream();
        if (Wire.CONTENT_WIRE.enabled()) {
            is = new WireLogInputStream(is, Wire.CONTENT_WIRE);
        }
        boolean canHaveBody = canResponseHaveBody(this.statusLine.getStatusCode());
        InputStream result = null;
        Header transferEncodingHeader = this.responseHeaders.getFirstHeader("Transfer-Encoding");
        if (transferEncodingHeader != null) {
            String transferEncoding = transferEncodingHeader.getValue();
            if (!"chunked".equalsIgnoreCase(transferEncoding) && !"identity".equalsIgnoreCase(transferEncoding) && LOG.isWarnEnabled()) {
                LOG.warn(new StringBuffer().append("Unsupported transfer encoding: ").append(transferEncoding).toString());
            }
            HeaderElement[] encodings = transferEncodingHeader.getElements();
            int len = encodings.length;
            if (len > 0 && "chunked".equalsIgnoreCase(encodings[len - 1].getName())) {
                if (conn.isResponseAvailable(conn.getParams().getSoTimeout())) {
                    result = new ChunkedInputStream(is, this);
                } else {
                    if (getParams().isParameterTrue(HttpMethodParams.STRICT_TRANSFER_ENCODING)) {
                        throw new ProtocolException("Chunk-encoded body declared but not sent");
                    }
                    LOG.warn("Chunk-encoded body missing");
                }
            } else {
                LOG.info("Response content is not chunk-encoded");
                setConnectionCloseForced(true);
                result = is;
            }
        } else {
            long expectedLength = getResponseContentLength();
            if (expectedLength == -1) {
                if (canHaveBody && this.effectiveVersion.greaterEquals(HttpVersion.HTTP_1_1)) {
                    Header connectionHeader = this.responseHeaders.getFirstHeader("Connection");
                    String connectionDirective = null;
                    if (connectionHeader != null) {
                        connectionDirective = connectionHeader.getValue();
                    }
                    if (!"close".equalsIgnoreCase(connectionDirective)) {
                        LOG.info("Response content length is not known");
                        setConnectionCloseForced(true);
                    }
                }
                result = is;
            } else {
                result = new ContentLengthInputStream(is, expectedLength);
            }
        }
        if (!canHaveBody) {
            result = null;
        }
        if (result != null) {
            result = new AutoCloseInputStream(result, new ResponseConsumedWatcher(this) { // from class: org.apache.commons.httpclient.HttpMethodBase.1
                private final HttpMethodBase this$0;

                {
                    this.this$0 = this;
                }

                @Override // org.apache.commons.httpclient.ResponseConsumedWatcher
                public void responseConsumed() throws IOException {
                    this.this$0.responseBodyConsumed();
                }
            });
        }
        return result;
    }

    protected void readResponseHeaders(HttpState state, HttpConnection conn) throws IOException {
        LOG.trace("enter HttpMethodBase.readResponseHeaders(HttpState,HttpConnection)");
        getResponseHeaderGroup().clear();
        Header[] headers = HttpParser.parseHeaders(conn.getResponseInputStream(), getParams().getHttpElementCharset());
        getResponseHeaderGroup().setHeaders(headers);
    }

    protected void readStatusLine(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter HttpMethodBase.readStatusLine(HttpState, HttpConnection)");
        int maxGarbageLines = getParams().getIntParameter(HttpMethodParams.STATUS_LINE_GARBAGE_LIMIT, Integer.MAX_VALUE);
        int count = 0;
        while (true) {
            String s = conn.readLine(getParams().getHttpElementCharset());
            if (s == null && count == 0) {
                throw new NoHttpResponseException(new StringBuffer().append("The server ").append(conn.getHost()).append(" failed to respond").toString());
            }
            if (Wire.HEADER_WIRE.enabled()) {
                Wire.HEADER_WIRE.input(new StringBuffer().append(s).append("\r\n").toString());
            }
            if (s == null || !StatusLine.startsWithHTTP(s)) {
                if (s == null || count >= maxGarbageLines) {
                    break;
                } else {
                    count++;
                }
            } else {
                this.statusLine = new StatusLine(s);
                String versionStr = this.statusLine.getHttpVersion();
                if (getParams().isParameterFalse(HttpMethodParams.UNAMBIGUOUS_STATUS_LINE) && versionStr.equals("HTTP")) {
                    getParams().setVersion(HttpVersion.HTTP_1_0);
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(new StringBuffer().append("Ambiguous status line (HTTP protocol version missing):").append(this.statusLine.toString()).toString());
                        return;
                    }
                    return;
                }
                this.effectiveVersion = HttpVersion.parse(versionStr);
                return;
            }
        }
        throw new ProtocolException(new StringBuffer().append("The server ").append(conn.getHost()).append(" failed to respond with a valid HTTP response").toString());
    }

    protected void writeRequest(HttpState state, HttpConnection conn) throws IllegalStateException, IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.writeRequest(HttpState, HttpConnection)");
        writeRequestLine(state, conn);
        writeRequestHeaders(state, conn);
        conn.writeLine();
        if (Wire.HEADER_WIRE.enabled()) {
            Wire.HEADER_WIRE.output("\r\n");
        }
        HttpVersion ver = getParams().getVersion();
        Header expectheader = getRequestHeader("Expect");
        String expectvalue = null;
        if (expectheader != null) {
            expectvalue = expectheader.getValue();
        }
        if (expectvalue != null && expectvalue.compareToIgnoreCase("100-continue") == 0) {
            if (ver.greaterEquals(HttpVersion.HTTP_1_1)) {
                conn.flushRequestOutputStream();
                int readTimeout = conn.getParams().getSoTimeout();
                try {
                    try {
                        conn.setSocketTimeout(3000);
                        readStatusLine(state, conn);
                        processStatusLine(state, conn);
                        readResponseHeaders(state, conn);
                        processResponseHeaders(state, conn);
                        if (this.statusLine.getStatusCode() == 100) {
                            this.statusLine = null;
                            LOG.debug("OK to continue received");
                            conn.setSocketTimeout(readTimeout);
                        } else {
                            conn.setSocketTimeout(readTimeout);
                            return;
                        }
                    } catch (InterruptedIOException e) {
                        if (!ExceptionUtil.isSocketTimeoutException(e)) {
                            throw e;
                        }
                        removeRequestHeader("Expect");
                        LOG.info("100 (continue) read timeout. Resume sending the request");
                        conn.setSocketTimeout(readTimeout);
                    }
                } catch (Throwable th) {
                    conn.setSocketTimeout(readTimeout);
                    throw th;
                }
            } else {
                removeRequestHeader("Expect");
                LOG.info("'Expect: 100-continue' handshake is only supported by HTTP/1.1 or higher");
            }
        }
        writeRequestBody(state, conn);
        conn.flushRequestOutputStream();
    }

    protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException {
        return true;
    }

    protected void writeRequestHeaders(HttpState state, HttpConnection conn) throws IllegalStateException, IOException, IllegalArgumentException {
        LOG.trace("enter HttpMethodBase.writeRequestHeaders(HttpState,HttpConnection)");
        addRequestHeaders(state, conn);
        String charset = getParams().getHttpElementCharset();
        Header[] headers = getRequestHeaders();
        for (Header header : headers) {
            String s = header.toExternalForm();
            if (Wire.HEADER_WIRE.enabled()) {
                Wire.HEADER_WIRE.output(s);
            }
            conn.print(s, charset);
        }
    }

    protected void writeRequestLine(HttpState state, HttpConnection conn) throws IllegalStateException, IOException {
        LOG.trace("enter HttpMethodBase.writeRequestLine(HttpState, HttpConnection)");
        String requestLine = getRequestLine(conn);
        if (Wire.HEADER_WIRE.enabled()) {
            Wire.HEADER_WIRE.output(requestLine);
        }
        conn.print(requestLine, getParams().getHttpElementCharset());
    }

    private String getRequestLine(HttpConnection conn) {
        return generateRequestLine(conn, getName(), getPath(), getQueryString(), this.effectiveVersion.toString());
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public HttpMethodParams getParams() {
        return this.params;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public void setParams(HttpMethodParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    public HttpVersion getEffectiveVersion() {
        return this.effectiveVersion;
    }

    private static boolean canResponseHaveBody(int status) {
        LOG.trace("enter HttpMethodBase.canResponseHaveBody(int)");
        boolean result = true;
        if ((status >= 100 && status <= 199) || status == 204 || status == 304) {
            result = false;
        }
        return result;
    }

    public String getProxyAuthenticationRealm() {
        return this.proxyAuthState.getRealm();
    }

    public String getAuthenticationRealm() {
        return this.hostAuthState.getRealm();
    }

    protected String getContentCharSet(Header contentheader) {
        NameValuePair param;
        LOG.trace("enter getContentCharSet( Header contentheader )");
        String charset = null;
        if (contentheader != null) {
            HeaderElement[] values = contentheader.getElements();
            if (values.length == 1 && (param = values[0].getParameterByName("charset")) != null) {
                charset = param.getValue();
            }
        }
        if (charset == null) {
            charset = getParams().getContentCharset();
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Default charset used: ").append(charset).toString());
            }
        }
        return charset;
    }

    public String getRequestCharSet() {
        return getContentCharSet(getRequestHeader("Content-Type"));
    }

    public String getResponseCharSet() {
        return getContentCharSet(getResponseHeader("Content-Type"));
    }

    public int getRecoverableExceptionCount() {
        return this.recoverableExceptionCount;
    }

    protected void responseBodyConsumed() throws IOException {
        this.responseStream = null;
        if (this.responseConnection != null) {
            this.responseConnection.setLastResponseInputStream(null);
            if (shouldCloseConnection(this.responseConnection)) {
                this.responseConnection.close();
            } else {
                try {
                    if (this.responseConnection.isResponseAvailable()) {
                        boolean logExtraInput = getParams().isParameterTrue(HttpMethodParams.WARN_EXTRA_INPUT);
                        if (logExtraInput) {
                            LOG.warn("Extra response data detected - closing connection");
                        }
                        this.responseConnection.close();
                    }
                } catch (IOException e) {
                    LOG.warn(e.getMessage());
                    this.responseConnection.close();
                }
            }
        }
        this.connectionCloseForced = false;
        ensureConnectionRelease();
    }

    private void ensureConnectionRelease() {
        if (this.responseConnection != null) {
            this.responseConnection.releaseConnection();
            this.responseConnection = null;
        }
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public HostConfiguration getHostConfiguration() {
        HostConfiguration hostconfig = new HostConfiguration();
        hostconfig.setHost(this.httphost);
        return hostconfig;
    }

    public void setHostConfiguration(HostConfiguration hostconfig) {
        if (hostconfig != null) {
            this.httphost = new HttpHost(hostconfig.getHost(), hostconfig.getPort(), hostconfig.getProtocol());
        } else {
            this.httphost = null;
        }
    }

    public MethodRetryHandler getMethodRetryHandler() {
        return this.methodRetryHandler;
    }

    public void setMethodRetryHandler(MethodRetryHandler handler) {
        this.methodRetryHandler = handler;
    }

    void fakeResponse(StatusLine statusline, HeaderGroup responseheaders, InputStream responseStream) {
        this.used = true;
        this.statusLine = statusline;
        this.responseHeaders = responseheaders;
        this.responseBody = null;
        this.responseStream = responseStream;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public AuthState getHostAuthState() {
        return this.hostAuthState;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public AuthState getProxyAuthState() {
        return this.proxyAuthState;
    }

    public boolean isAborted() {
        return this.aborted;
    }

    @Override // org.apache.commons.httpclient.HttpMethod
    public boolean isRequestSent() {
        return this.requestSent;
    }
}

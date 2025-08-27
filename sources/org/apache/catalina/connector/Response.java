package org.apache.catalina.connector;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Session;
import org.apache.catalina.security.SecurityUtil;
import org.apache.catalina.util.SessionConfig;
import org.apache.coyote.ActionCode;
import org.apache.coyote.ajp.Constants;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.UEncoder;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.MediaTypeCache;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.security.Escape;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/connector/Response.class */
public class Response implements HttpServletResponse {
    private static final Log log = LogFactory.getLog((Class<?>) Response.class);
    protected static final StringManager sm = StringManager.getManager((Class<?>) Response.class);
    private static final MediaTypeCache MEDIA_TYPE_CACHE = new MediaTypeCache(100);
    private static final boolean ENFORCE_ENCODING_IN_GET_WRITER = Boolean.parseBoolean(System.getProperty("org.apache.catalina.connector.Response.ENFORCE_ENCODING_IN_GET_WRITER", "true"));
    protected org.apache.coyote.Response coyoteResponse;
    protected OutputBuffer outputBuffer;
    protected CoyoteOutputStream outputStream;
    protected CoyoteWriter writer;
    protected SimpleDateFormat format = null;
    protected boolean appCommitted = false;
    protected boolean included = false;
    private boolean isCharacterEncodingSet = false;
    protected boolean usingOutputStream = false;
    protected boolean usingWriter = false;
    protected final UEncoder urlEncoder = new UEncoder(UEncoder.SafeCharsSet.WITH_SLASH);
    protected final CharChunk redirectURLCC = new CharChunk();
    private final List<Cookie> cookies = new ArrayList();
    private HttpServletResponse applicationResponse = null;
    protected Request request = null;
    protected ResponseFacade facade = null;

    public void setConnector(Connector connector) {
        if ("AJP/1.3".equals(connector.getProtocol())) {
            this.outputBuffer = new OutputBuffer(Constants.MAX_SEND_SIZE);
        } else {
            this.outputBuffer = new OutputBuffer();
        }
        this.outputStream = new CoyoteOutputStream(this.outputBuffer);
        this.writer = new CoyoteWriter(this.outputBuffer);
    }

    public void setCoyoteResponse(org.apache.coyote.Response coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
        this.outputBuffer.setResponse(coyoteResponse);
    }

    public org.apache.coyote.Response getCoyoteResponse() {
        return this.coyoteResponse;
    }

    public Context getContext() {
        return this.request.getContext();
    }

    public void recycle() {
        this.cookies.clear();
        this.outputBuffer.recycle();
        this.usingOutputStream = false;
        this.usingWriter = false;
        this.appCommitted = false;
        this.included = false;
        this.isCharacterEncodingSet = false;
        this.applicationResponse = null;
        if (Globals.IS_SECURITY_ENABLED || Connector.RECYCLE_FACADES) {
            if (this.facade != null) {
                this.facade.clear();
                this.facade = null;
            }
            if (this.outputStream != null) {
                this.outputStream.clear();
                this.outputStream = null;
            }
            if (this.writer != null) {
                this.writer.clear();
                this.writer = null;
                return;
            }
            return;
        }
        this.writer.recycle();
    }

    public List<Cookie> getCookies() {
        return this.cookies;
    }

    public long getContentWritten() {
        return this.outputBuffer.getContentWritten();
    }

    public long getBytesWritten(boolean flush) {
        if (flush) {
            try {
                this.outputBuffer.flush();
            } catch (IOException e) {
            }
        }
        return getCoyoteResponse().getBytesWritten(flush);
    }

    public void setAppCommitted(boolean appCommitted) {
        this.appCommitted = appCommitted;
    }

    public boolean isAppCommitted() {
        return this.appCommitted || isCommitted() || isSuspended() || (getContentLength() > 0 && getContentWritten() >= ((long) getContentLength()));
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        if (this.facade == null) {
            this.facade = new ResponseFacade(this);
        }
        if (this.applicationResponse == null) {
            this.applicationResponse = this.facade;
        }
        return this.applicationResponse;
    }

    public void setResponse(HttpServletResponse applicationResponse) {
        ServletResponse r;
        ServletResponse response = applicationResponse;
        while (true) {
            r = response;
            if (!(r instanceof HttpServletResponseWrapper)) {
                break;
            } else {
                response = ((HttpServletResponseWrapper) r).getResponse();
            }
        }
        if (r != this.facade) {
            throw new IllegalArgumentException(sm.getString("response.illegalWrap"));
        }
        this.applicationResponse = applicationResponse;
    }

    public void setSuspended(boolean suspended) {
        this.outputBuffer.setSuspended(suspended);
    }

    public boolean isSuspended() {
        return this.outputBuffer.isSuspended();
    }

    public boolean isClosed() {
        return this.outputBuffer.isClosed();
    }

    public boolean setError() {
        return getCoyoteResponse().setError();
    }

    public boolean isError() {
        return getCoyoteResponse().isError();
    }

    public boolean isErrorReportRequired() {
        return getCoyoteResponse().isErrorReportRequired();
    }

    public boolean setErrorReported() {
        return getCoyoteResponse().setErrorReported();
    }

    public void finishResponse() throws IOException {
        this.outputBuffer.close();
    }

    public int getContentLength() {
        return getCoyoteResponse().getContentLength();
    }

    @Override // javax.servlet.ServletResponse
    public String getContentType() {
        return getCoyoteResponse().getContentType();
    }

    public PrintWriter getReporter() throws IOException {
        if (this.outputBuffer.isNew()) {
            this.outputBuffer.checkConverter();
            if (this.writer == null) {
                this.writer = new CoyoteWriter(this.outputBuffer);
            }
            return this.writer;
        }
        return null;
    }

    @Override // javax.servlet.ServletResponse
    public void flushBuffer() throws IOException {
        this.outputBuffer.flush();
    }

    @Override // javax.servlet.ServletResponse
    public int getBufferSize() {
        return this.outputBuffer.getBufferSize();
    }

    @Override // javax.servlet.ServletResponse
    public String getCharacterEncoding() {
        String charset = getCoyoteResponse().getCharacterEncoding();
        if (charset != null) {
            return charset;
        }
        Context context = getContext();
        String result = null;
        if (context != null) {
            result = context.getResponseCharacterEncoding();
        }
        if (result == null) {
            result = org.apache.coyote.Constants.DEFAULT_BODY_CHARSET.name();
        }
        return result;
    }

    @Override // javax.servlet.ServletResponse
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.usingWriter) {
            throw new IllegalStateException(sm.getString("coyoteResponse.getOutputStream.ise"));
        }
        this.usingOutputStream = true;
        if (this.outputStream == null) {
            this.outputStream = new CoyoteOutputStream(this.outputBuffer);
        }
        return this.outputStream;
    }

    @Override // javax.servlet.ServletResponse
    public Locale getLocale() {
        return getCoyoteResponse().getLocale();
    }

    @Override // javax.servlet.ServletResponse
    public PrintWriter getWriter() throws IOException {
        if (this.usingOutputStream) {
            throw new IllegalStateException(sm.getString("coyoteResponse.getWriter.ise"));
        }
        if (ENFORCE_ENCODING_IN_GET_WRITER) {
            setCharacterEncoding(getCharacterEncoding());
        }
        this.usingWriter = true;
        this.outputBuffer.checkConverter();
        if (this.writer == null) {
            this.writer = new CoyoteWriter(this.outputBuffer);
        }
        return this.writer;
    }

    @Override // javax.servlet.ServletResponse
    public boolean isCommitted() {
        return getCoyoteResponse().isCommitted();
    }

    @Override // javax.servlet.ServletResponse
    public void reset() throws IllegalStateException {
        if (this.included) {
            return;
        }
        getCoyoteResponse().reset();
        this.outputBuffer.reset();
        this.usingOutputStream = false;
        this.usingWriter = false;
        this.isCharacterEncodingSet = false;
    }

    @Override // javax.servlet.ServletResponse
    public void resetBuffer() {
        resetBuffer(false);
    }

    public void resetBuffer(boolean resetWriterStreamFlags) {
        if (isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.resetBuffer.ise"));
        }
        this.outputBuffer.reset(resetWriterStreamFlags);
        if (resetWriterStreamFlags) {
            this.usingOutputStream = false;
            this.usingWriter = false;
            this.isCharacterEncodingSet = false;
        }
    }

    @Override // javax.servlet.ServletResponse
    public void setBufferSize(int size) {
        if (isCommitted() || !this.outputBuffer.isNew()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.setBufferSize.ise"));
        }
        this.outputBuffer.setBufferSize(size);
    }

    @Override // javax.servlet.ServletResponse
    public void setContentLength(int length) {
        setContentLengthLong(length);
    }

    @Override // javax.servlet.ServletResponse
    public void setContentLengthLong(long length) {
        if (isCommitted() || this.included) {
            return;
        }
        getCoyoteResponse().setContentLength(length);
    }

    @Override // javax.servlet.ServletResponse
    public void setContentType(String type) {
        if (isCommitted() || this.included) {
            return;
        }
        if (type == null) {
            getCoyoteResponse().setContentType(null);
            return;
        }
        String[] m = MEDIA_TYPE_CACHE.parse(type);
        if (m == null) {
            getCoyoteResponse().setContentTypeNoCharset(type);
            return;
        }
        getCoyoteResponse().setContentTypeNoCharset(m[0]);
        if (m[1] != null && !this.usingWriter) {
            try {
                getCoyoteResponse().setCharacterEncoding(m[1]);
            } catch (IllegalArgumentException e) {
                log.warn(sm.getString("coyoteResponse.encoding.invalid", m[1]), e);
            }
            this.isCharacterEncodingSet = true;
        }
    }

    @Override // javax.servlet.ServletResponse
    public void setCharacterEncoding(String characterEncoding) {
        if (isCommitted() || this.included || this.usingWriter) {
            return;
        }
        try {
            getCoyoteResponse().setCharacterEncoding(characterEncoding);
            this.isCharacterEncodingSet = true;
        } catch (IllegalArgumentException e) {
            log.warn(sm.getString("coyoteResponse.encoding.invalid", characterEncoding), e);
        }
    }

    @Override // javax.servlet.ServletResponse
    public void setLocale(Locale locale) {
        Context context;
        String charset;
        if (isCommitted() || this.included) {
            return;
        }
        getCoyoteResponse().setLocale(locale);
        if (!this.usingWriter && !this.isCharacterEncodingSet && (context = getContext()) != null && (charset = context.getCharset(locale)) != null) {
            try {
                getCoyoteResponse().setCharacterEncoding(charset);
            } catch (IllegalArgumentException e) {
                log.warn(sm.getString("coyoteResponse.encoding.invalid", charset), e);
            }
        }
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String getHeader(String name) {
        return getCoyoteResponse().getMimeHeaders().getHeader(name);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public Collection<String> getHeaderNames() {
        MimeHeaders headers = getCoyoteResponse().getMimeHeaders();
        int n = headers.size();
        List<String> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add(headers.getName(i).toString());
        }
        return result;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public Collection<String> getHeaders(String name) {
        Enumeration<String> enumeration = getCoyoteResponse().getMimeHeaders().values(name);
        Vector<String> result = new Vector<>();
        while (enumeration.hasMoreElements()) {
            result.addElement(enumeration.nextElement());
        }
        return result;
    }

    public String getMessage() {
        return getCoyoteResponse().getMessage();
    }

    @Override // javax.servlet.http.HttpServletResponse
    public int getStatus() {
        return getCoyoteResponse().getStatus();
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addCookie(Cookie cookie) {
        if (this.included || isCommitted()) {
            return;
        }
        this.cookies.add(cookie);
        String header = generateCookieString(cookie);
        addHeader("Set-Cookie", header, getContext().getCookieProcessor().getCharset());
    }

    public void addSessionCookieInternal(Cookie cookie) {
        if (isCommitted()) {
            return;
        }
        String name = cookie.getName();
        String startsWith = name + SymbolConstants.EQUAL_SYMBOL;
        String header = generateCookieString(cookie);
        boolean set = false;
        MimeHeaders headers = getCoyoteResponse().getMimeHeaders();
        int n = headers.size();
        for (int i = 0; i < n; i++) {
            if (headers.getName(i).toString().equals("Set-Cookie") && headers.getValue(i).toString().startsWith(startsWith)) {
                headers.getValue(i).setString(header);
                set = true;
            }
        }
        if (!set) {
            addHeader("Set-Cookie", header);
        }
    }

    public String generateCookieString(final Cookie cookie) {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: org.apache.catalina.connector.Response.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return Response.this.getContext().getCookieProcessor().generateHeader(cookie);
                }
            });
        }
        return getContext().getCookieProcessor().generateHeader(cookie);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addDateHeader(String name, long value) {
        if (name == null || name.length() == 0 || isCommitted() || this.included) {
            return;
        }
        if (this.format == null) {
            this.format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            this.format.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        addHeader(name, FastHttpDateFormat.formatDate(value, this.format));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addHeader(String name, String value) {
        addHeader(name, value, null);
    }

    private void addHeader(String name, String value, Charset charset) {
        if (name == null || name.length() == 0 || value == null || isCommitted() || this.included) {
            return;
        }
        char cc = name.charAt(0);
        if ((cc == 'C' || cc == 'c') && checkSpecialHeader(name, value)) {
            return;
        }
        getCoyoteResponse().addHeader(name, value, charset);
    }

    private boolean checkSpecialHeader(String name, String value) {
        if (name.equalsIgnoreCase("Content-Type")) {
            setContentType(value);
            return true;
        }
        return false;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addIntHeader(String name, int value) {
        if (name == null || name.length() == 0 || isCommitted() || this.included) {
            return;
        }
        addHeader(name, "" + value);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public boolean containsHeader(String name) {
        char cc = name.charAt(0);
        if (cc == 'C' || cc == 'c') {
            if (name.equalsIgnoreCase("Content-Type")) {
                return getCoyoteResponse().getContentType() != null;
            }
            if (name.equalsIgnoreCase("Content-Length")) {
                return getCoyoteResponse().getContentLengthLong() != -1;
            }
        }
        return getCoyoteResponse().containsHeader(name);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeRedirectURL(String url) {
        if (isEncodeable(toAbsolute(url))) {
            return toEncoded(url, this.request.getSessionInternal().getIdInternal());
        }
        return url;
    }

    @Override // javax.servlet.http.HttpServletResponse
    @Deprecated
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeURL(String url) {
        try {
            String absolute = toAbsolute(url);
            if (isEncodeable(absolute)) {
                if (url.equalsIgnoreCase("")) {
                    url = absolute;
                } else if (url.equals(absolute) && !hasPath(url)) {
                    url = url + '/';
                }
                return toEncoded(url, this.request.getSessionInternal().getIdInternal());
            }
            return url;
        } catch (IllegalArgumentException e) {
            return url;
        }
    }

    @Override // javax.servlet.http.HttpServletResponse
    @Deprecated
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    public void sendAcknowledgement() throws IOException {
        if (isCommitted() || this.included) {
            return;
        }
        getCoyoteResponse().action(ActionCode.ACK, null);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendError(int status) throws IOException {
        sendError(status, null);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendError(int status, String message) throws IOException {
        if (isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.sendError.ise"));
        }
        if (this.included) {
            return;
        }
        setError();
        getCoyoteResponse().setStatus(status);
        getCoyoteResponse().setMessage(message);
        resetBuffer();
        setSuspended(true);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendRedirect(String location) throws IOException {
        sendRedirect(location, 302);
    }

    public void sendRedirect(String location, int status) throws IOException {
        String locationUri;
        if (isCommitted()) {
            throw new IllegalStateException(sm.getString("coyoteResponse.sendRedirect.ise"));
        }
        if (this.included) {
            return;
        }
        resetBuffer(true);
        try {
            if (getRequest().getCoyoteRequest().getSupportsRelativeRedirects() && getContext().getUseRelativeRedirects()) {
                locationUri = location;
            } else {
                locationUri = toAbsolute(location);
            }
            setStatus(status);
            setHeader("Location", locationUri);
            if (getContext().getSendRedirectBody()) {
                PrintWriter writer = getWriter();
                writer.print(sm.getString("coyoteResponse.sendRedirect.note", Escape.htmlElementContent(locationUri)));
                flushBuffer();
            }
        } catch (IllegalArgumentException e) {
            log.warn(sm.getString("response.sendRedirectFail", location), e);
            setStatus(404);
        }
        setSuspended(true);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setDateHeader(String name, long value) {
        if (name == null || name.length() == 0 || isCommitted() || this.included) {
            return;
        }
        if (this.format == null) {
            this.format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            this.format.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        setHeader(name, FastHttpDateFormat.formatDate(value, this.format));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setHeader(String name, String value) {
        if (name == null || name.length() == 0 || value == null || isCommitted() || this.included) {
            return;
        }
        char cc = name.charAt(0);
        if ((cc == 'C' || cc == 'c') && checkSpecialHeader(name, value)) {
            return;
        }
        getCoyoteResponse().setHeader(name, value);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setIntHeader(String name, int value) {
        if (name == null || name.length() == 0 || isCommitted() || this.included) {
            return;
        }
        setHeader(name, "" + value);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setStatus(int status) {
        setStatus(status, null);
    }

    @Override // javax.servlet.http.HttpServletResponse
    @Deprecated
    public void setStatus(int status, String message) {
        if (isCommitted() || this.included) {
            return;
        }
        getCoyoteResponse().setStatus(status);
        getCoyoteResponse().setMessage(message);
    }

    protected boolean isEncodeable(final String location) {
        final Request hreq;
        final Session session;
        if (location == null || location.startsWith("#") || (session = (hreq = this.request).getSessionInternal(false)) == null || hreq.isRequestedSessionIdFromCookie() || !hreq.getServletContext().getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL)) {
            return false;
        }
        if (SecurityUtil.isPackageProtectionEnabled()) {
            return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: org.apache.catalina.connector.Response.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Boolean run() {
                    return Boolean.valueOf(Response.this.doIsEncodeable(hreq, session, location));
                }
            })).booleanValue();
        }
        return doIsEncodeable(hreq, session, location);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean doIsEncodeable(Request hreq, Session session, String location) {
        try {
            URL url = new URL(location);
            if (!hreq.getScheme().equalsIgnoreCase(url.getProtocol()) || !hreq.getServerName().equalsIgnoreCase(url.getHost())) {
                return false;
            }
            int serverPort = hreq.getServerPort();
            if (serverPort == -1) {
                if ("https".equals(hreq.getScheme())) {
                    serverPort = 443;
                } else {
                    serverPort = 80;
                }
            }
            int urlPort = url.getPort();
            if (urlPort == -1) {
                if ("https".equals(url.getProtocol())) {
                    urlPort = 443;
                } else {
                    urlPort = 80;
                }
            }
            if (serverPort != urlPort) {
                return false;
            }
            String contextPath = getContext().getPath();
            if (contextPath != null) {
                String file = url.getFile();
                if (!file.startsWith(contextPath)) {
                    return false;
                }
                String tok = ScriptUtils.DEFAULT_STATEMENT_SEPARATOR + SessionConfig.getSessionUriParamName(this.request.getContext()) + SymbolConstants.EQUAL_SYMBOL + session.getIdInternal();
                if (file.indexOf(tok, contextPath.length()) >= 0) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    protected String toAbsolute(String location) {
        CharChunk encodedURI;
        if (location == null) {
            return location;
        }
        boolean leadingSlash = location.startsWith("/");
        if (location.startsWith("//")) {
            this.redirectURLCC.recycle();
            String scheme = this.request.getScheme();
            try {
                this.redirectURLCC.append(scheme, 0, scheme.length());
                this.redirectURLCC.append(':');
                this.redirectURLCC.append(location, 0, location.length());
                return this.redirectURLCC.toString();
            } catch (IOException e) {
                IllegalArgumentException iae = new IllegalArgumentException(location);
                iae.initCause(e);
                throw iae;
            }
        }
        if (leadingSlash || !UriUtil.hasScheme(location)) {
            this.redirectURLCC.recycle();
            String scheme2 = this.request.getScheme();
            String name = this.request.getServerName();
            int port = this.request.getServerPort();
            try {
                this.redirectURLCC.append(scheme2, 0, scheme2.length());
                this.redirectURLCC.append("://", 0, 3);
                this.redirectURLCC.append(name, 0, name.length());
                if ((scheme2.equals("http") && port != 80) || (scheme2.equals("https") && port != 443)) {
                    this.redirectURLCC.append(':');
                    String portS = port + "";
                    this.redirectURLCC.append(portS, 0, portS.length());
                }
                if (!leadingSlash) {
                    final String relativePath = this.request.getDecodedRequestURI();
                    final int pos = relativePath.lastIndexOf(47);
                    if (SecurityUtil.isPackageProtectionEnabled()) {
                        try {
                            encodedURI = (CharChunk) AccessController.doPrivileged(new PrivilegedExceptionAction<CharChunk>() { // from class: org.apache.catalina.connector.Response.3
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public CharChunk run() throws IOException {
                                    return Response.this.urlEncoder.encodeURL(relativePath, 0, pos);
                                }
                            });
                        } catch (PrivilegedActionException pae) {
                            IllegalArgumentException iae2 = new IllegalArgumentException(location);
                            iae2.initCause(pae.getException());
                            throw iae2;
                        }
                    } else {
                        encodedURI = this.urlEncoder.encodeURL(relativePath, 0, pos);
                    }
                    this.redirectURLCC.append(encodedURI);
                    encodedURI.recycle();
                    this.redirectURLCC.append('/');
                }
                this.redirectURLCC.append(location, 0, location.length());
                normalize(this.redirectURLCC);
                return this.redirectURLCC.toString();
            } catch (IOException e2) {
                IllegalArgumentException iae3 = new IllegalArgumentException(location);
                iae3.initCause(e2);
                throw iae3;
            }
        }
        return location;
    }

    private void normalize(CharChunk cc) {
        int truncate = cc.indexOf('?');
        if (truncate == -1) {
            truncate = cc.indexOf('#');
        }
        char[] truncateCC = null;
        if (truncate > -1) {
            truncateCC = Arrays.copyOfRange(cc.getBuffer(), cc.getStart() + truncate, cc.getEnd());
            cc.setEnd(cc.getStart() + truncate);
        }
        if (cc.endsWith("/.") || cc.endsWith("/..")) {
            try {
                cc.append('/');
            } catch (IOException e) {
                throw new IllegalArgumentException(cc.toString(), e);
            }
        }
        char[] c = cc.getChars();
        int start = cc.getStart();
        int end = cc.getEnd();
        int startIndex = 0;
        for (int i = 0; i < 3; i++) {
            startIndex = cc.indexOf('/', startIndex + 1);
        }
        int index = startIndex;
        while (true) {
            index = cc.indexOf("/./", 0, 3, index);
            if (index < 0) {
                break;
            }
            copyChars(c, start + index, start + index + 2, ((end - start) - index) - 2);
            end -= 2;
            cc.setEnd(end);
        }
        int i2 = startIndex;
        while (true) {
            int index2 = i2;
            int index3 = cc.indexOf("/../", 0, 4, index2);
            if (index3 >= 0) {
                if (index3 == startIndex) {
                    throw new IllegalArgumentException();
                }
                int index22 = -1;
                for (int pos = (start + index3) - 1; pos >= 0 && index22 < 0; pos--) {
                    if (c[pos] == '/') {
                        index22 = pos;
                    }
                }
                copyChars(c, start + index22, start + index3 + 3, ((end - start) - index3) - 3);
                end = ((end + index22) - index3) - 3;
                cc.setEnd(end);
                i2 = index22;
            } else {
                if (truncateCC != null) {
                    try {
                        cc.append(truncateCC, 0, truncateCC.length);
                        return;
                    } catch (IOException ioe) {
                        throw new IllegalArgumentException(ioe);
                    }
                }
                return;
            }
        }
    }

    private void copyChars(char[] c, int dest, int src, int len) {
        for (int pos = 0; pos < len; pos++) {
            c[pos + dest] = c[pos + src];
        }
    }

    private boolean hasPath(String uri) {
        int pos = uri.indexOf("://");
        if (pos < 0 || uri.indexOf(47, pos + 3) < 0) {
            return false;
        }
        return true;
    }

    protected String toEncoded(String url, String sessionId) {
        if (url == null || sessionId == null) {
            return url;
        }
        String path = url;
        String query = "";
        String anchor = "";
        int question = url.indexOf(63);
        if (question >= 0) {
            path = url.substring(0, question);
            query = url.substring(question);
        }
        int pound = path.indexOf(35);
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        StringBuilder sb = new StringBuilder(path);
        if (sb.length() > 0) {
            sb.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            sb.append(SessionConfig.getSessionUriParamName(this.request.getContext()));
            sb.append(SymbolConstants.EQUAL_SYMBOL);
            sb.append(sessionId);
        }
        sb.append(anchor);
        sb.append(query);
        return sb.toString();
    }
}

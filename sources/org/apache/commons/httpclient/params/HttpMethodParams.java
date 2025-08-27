package org.apache.commons.httpclient.params;

import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/params/HttpMethodParams.class */
public class HttpMethodParams extends DefaultHttpParams {
    private static final Log LOG;
    public static final String USER_AGENT = "http.useragent";
    public static final String PROTOCOL_VERSION = "http.protocol.version";
    public static final String UNAMBIGUOUS_STATUS_LINE = "http.protocol.unambiguous-statusline";
    public static final String SINGLE_COOKIE_HEADER = "http.protocol.single-cookie-header";
    public static final String STRICT_TRANSFER_ENCODING = "http.protocol.strict-transfer-encoding";
    public static final String REJECT_HEAD_BODY = "http.protocol.reject-head-body";
    public static final String HEAD_BODY_CHECK_TIMEOUT = "http.protocol.head-body-timeout";
    public static final String USE_EXPECT_CONTINUE = "http.protocol.expect-continue";
    public static final String CREDENTIAL_CHARSET = "http.protocol.credential-charset";
    public static final String HTTP_ELEMENT_CHARSET = "http.protocol.element-charset";
    public static final String HTTP_URI_CHARSET = "http.protocol.uri-charset";
    public static final String HTTP_CONTENT_CHARSET = "http.protocol.content-charset";
    public static final String COOKIE_POLICY = "http.protocol.cookie-policy";
    public static final String WARN_EXTRA_INPUT = "http.protocol.warn-extra-input";
    public static final String STATUS_LINE_GARBAGE_LIMIT = "http.protocol.status-line-garbage-limit";
    public static final String SO_TIMEOUT = "http.socket.timeout";
    public static final String DATE_PATTERNS = "http.dateparser.patterns";
    public static final String RETRY_HANDLER = "http.method.retry-handler";
    public static final String BUFFER_WARN_TRIGGER_LIMIT = "http.method.response.buffer.warnlimit";
    public static final String VIRTUAL_HOST = "http.virtual-host";
    public static final String MULTIPART_BOUNDARY = "http.method.multipart.boundary";
    private static final String[] PROTOCOL_STRICTNESS_PARAMETERS;
    static Class class$org$apache$commons$httpclient$params$HttpMethodParams;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$params$HttpMethodParams == null) {
            clsClass$ = class$("org.apache.commons.httpclient.params.HttpMethodParams");
            class$org$apache$commons$httpclient$params$HttpMethodParams = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$params$HttpMethodParams;
        }
        LOG = LogFactory.getLog(clsClass$);
        PROTOCOL_STRICTNESS_PARAMETERS = new String[]{UNAMBIGUOUS_STATUS_LINE, SINGLE_COOKIE_HEADER, STRICT_TRANSFER_ENCODING, REJECT_HEAD_BODY, WARN_EXTRA_INPUT};
    }

    public HttpMethodParams() {
        super(getDefaultParams());
    }

    public HttpMethodParams(HttpParams defaults) {
        super(defaults);
    }

    public String getHttpElementCharset() {
        String charset = (String) getParameter(HTTP_ELEMENT_CHARSET);
        if (charset == null) {
            LOG.warn("HTTP element charset not configured, using US-ASCII");
            charset = "US-ASCII";
        }
        return charset;
    }

    public void setHttpElementCharset(String charset) {
        setParameter(HTTP_ELEMENT_CHARSET, charset);
    }

    public String getContentCharset() {
        String charset = (String) getParameter(HTTP_CONTENT_CHARSET);
        if (charset == null) {
            LOG.warn("Default content charset not configured, using ISO-8859-1");
            charset = "ISO-8859-1";
        }
        return charset;
    }

    public void setUriCharset(String charset) {
        setParameter(HTTP_URI_CHARSET, charset);
    }

    public String getUriCharset() {
        String charset = (String) getParameter(HTTP_URI_CHARSET);
        if (charset == null) {
            charset = "UTF-8";
        }
        return charset;
    }

    public void setContentCharset(String charset) {
        setParameter(HTTP_CONTENT_CHARSET, charset);
    }

    public String getCredentialCharset() {
        String charset = (String) getParameter(CREDENTIAL_CHARSET);
        if (charset == null) {
            LOG.debug("Credential charset not configured, using HTTP element charset");
            charset = getHttpElementCharset();
        }
        return charset;
    }

    public void setCredentialCharset(String charset) {
        setParameter(CREDENTIAL_CHARSET, charset);
    }

    public HttpVersion getVersion() {
        Object param = getParameter(PROTOCOL_VERSION);
        if (param == null) {
            return HttpVersion.HTTP_1_1;
        }
        return (HttpVersion) param;
    }

    public void setVersion(HttpVersion version) {
        setParameter(PROTOCOL_VERSION, version);
    }

    public String getCookiePolicy() {
        Object param = getParameter(COOKIE_POLICY);
        if (param == null) {
            return "default";
        }
        return (String) param;
    }

    public void setCookiePolicy(String policy) {
        setParameter(COOKIE_POLICY, policy);
    }

    public int getSoTimeout() {
        return getIntParameter("http.socket.timeout", 0);
    }

    public void setSoTimeout(int timeout) {
        setIntParameter("http.socket.timeout", timeout);
    }

    public void setVirtualHost(String hostname) {
        setParameter(VIRTUAL_HOST, hostname);
    }

    public String getVirtualHost() {
        return (String) getParameter(VIRTUAL_HOST);
    }

    public void makeStrict() {
        setParameters(PROTOCOL_STRICTNESS_PARAMETERS, Boolean.TRUE);
        setIntParameter(STATUS_LINE_GARBAGE_LIMIT, 0);
    }

    public void makeLenient() {
        setParameters(PROTOCOL_STRICTNESS_PARAMETERS, Boolean.FALSE);
        setIntParameter(STATUS_LINE_GARBAGE_LIMIT, Integer.MAX_VALUE);
    }
}

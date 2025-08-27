package org.apache.commons.httpclient.cookie;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.ParameterFormatter;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec.class */
public class RFC2965Spec extends CookieSpecBase implements CookieVersionSupport {
    private static final Comparator PATH_COMPOARATOR = new CookiePathComparator();
    public static final String SET_COOKIE2_KEY = "set-cookie2";
    private final ParameterFormatter formatter = new ParameterFormatter();
    private final List attribHandlerList;
    private final Map attribHandlerMap;
    private final CookieSpec rfc2109;

    /* renamed from: org.apache.commons.httpclient.cookie.RFC2965Spec$1, reason: invalid class name */
    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$1.class */
    static class AnonymousClass1 {
    }

    public RFC2965Spec() {
        this.formatter.setAlwaysUseQuotes(true);
        this.attribHandlerMap = new HashMap(10);
        this.attribHandlerList = new ArrayList(10);
        this.rfc2109 = new RFC2109Spec();
        registerAttribHandler(Cookie2.PATH, new Cookie2PathAttributeHandler(this, null));
        registerAttribHandler(Cookie2.DOMAIN, new Cookie2DomainAttributeHandler(this, null));
        registerAttribHandler("port", new Cookie2PortAttributeHandler(this, null));
        registerAttribHandler("max-age", new Cookie2MaxageAttributeHandler(this, null));
        registerAttribHandler(Cookie2.SECURE, new CookieSecureAttributeHandler(this, null));
        registerAttribHandler("comment", new CookieCommentAttributeHandler(this, null));
        registerAttribHandler(Cookie2.COMMENTURL, new CookieCommentUrlAttributeHandler(this, null));
        registerAttribHandler(Cookie2.DISCARD, new CookieDiscardAttributeHandler(this, null));
        registerAttribHandler("version", new Cookie2VersionAttributeHandler(this, null));
    }

    protected void registerAttribHandler(String name, CookieAttributeHandler handler) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name may not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Attribute handler may not be null");
        }
        if (!this.attribHandlerList.contains(handler)) {
            this.attribHandlerList.add(handler);
        }
        this.attribHandlerMap.put(name, handler);
    }

    protected CookieAttributeHandler findAttribHandler(String name) {
        return (CookieAttributeHandler) this.attribHandlerMap.get(name);
    }

    protected CookieAttributeHandler getAttribHandler(String name) {
        CookieAttributeHandler handler = findAttribHandler(name);
        if (handler == null) {
            throw new IllegalStateException(new StringBuffer().append("Handler not registered for ").append(name).append(" attribute.").toString());
        }
        return handler;
    }

    protected Iterator getAttribHandlerIterator() {
        return this.attribHandlerList.iterator();
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] parse(String host, int port, String path, boolean secure, Header header) throws MalformedCookieException {
        LOG.trace("enter RFC2965.parse(String, int, String, boolean, Header)");
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null.");
        }
        if (header.getName() == null) {
            throw new IllegalArgumentException("Header name may not be null.");
        }
        if (header.getName().equalsIgnoreCase(SET_COOKIE2_KEY)) {
            return parse(host, port, path, secure, header.getValue());
        }
        if (header.getName().equalsIgnoreCase(RFC2109Spec.SET_COOKIE_KEY)) {
            return this.rfc2109.parse(host, port, path, secure, header.getValue());
        }
        throw new MalformedCookieException("Header name is not valid. RFC 2965 supports \"set-cookie\" and \"set-cookie2\" headers.");
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] parse(String host, int port, String path, boolean secure, String header) throws MalformedCookieException {
        LOG.trace("enter RFC2965Spec.parse(String, int, String, boolean, String)");
        if (host == null) {
            throw new IllegalArgumentException("Host of origin may not be null");
        }
        if (host.trim().equals("")) {
            throw new IllegalArgumentException("Host of origin may not be blank");
        }
        if (port < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid port: ").append(port).toString());
        }
        if (path == null) {
            throw new IllegalArgumentException("Path of origin may not be null.");
        }
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null.");
        }
        if (path.trim().equals("")) {
            path = "/";
        }
        String host2 = getEffectiveHost(host);
        HeaderElement[] headerElements = HeaderElement.parseElements(header.toCharArray());
        List cookies = new LinkedList();
        for (HeaderElement headerelement : headerElements) {
            try {
                Cookie2 cookie = new Cookie2(host2, headerelement.getName(), headerelement.getValue(), path, null, false, new int[]{port});
                NameValuePair[] parameters = headerelement.getParameters();
                if (parameters != null) {
                    Map attribmap = new HashMap(parameters.length);
                    for (int j = parameters.length - 1; j >= 0; j--) {
                        NameValuePair param = parameters[j];
                        attribmap.put(param.getName().toLowerCase(), param);
                    }
                    for (Map.Entry entry : attribmap.entrySet()) {
                        parseAttribute((NameValuePair) entry.getValue(), cookie);
                    }
                }
                cookies.add(cookie);
            } catch (IllegalArgumentException ex) {
                throw new MalformedCookieException(ex.getMessage());
            }
        }
        return (Cookie[]) cookies.toArray(new Cookie[cookies.size()]);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute may not be null.");
        }
        if (attribute.getName() == null) {
            throw new IllegalArgumentException("Attribute Name may not be null.");
        }
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null.");
        }
        String paramName = attribute.getName().toLowerCase();
        String paramValue = attribute.getValue();
        CookieAttributeHandler handler = findAttribHandler(paramName);
        if (handler == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(new StringBuffer().append("Unrecognized cookie attribute: ").append(attribute.toString()).toString());
                return;
            }
            return;
        }
        handler.parse(cookie, paramValue);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException, IllegalArgumentException {
        LOG.trace("enter RFC2965Spec.validate(String, int, String, boolean, Cookie)");
        if (cookie instanceof Cookie2) {
            if (cookie.getName().indexOf(32) != -1) {
                throw new MalformedCookieException("Cookie name may not contain blanks");
            }
            if (cookie.getName().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                throw new MalformedCookieException("Cookie name may not start with $");
            }
            CookieOrigin origin = new CookieOrigin(getEffectiveHost(host), port, path, secure);
            Iterator i = getAttribHandlerIterator();
            while (i.hasNext()) {
                CookieAttributeHandler handler = (CookieAttributeHandler) i.next();
                handler.validate(cookie, origin);
            }
            return;
        }
        this.rfc2109.validate(host, port, path, secure, cookie);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public boolean match(String host, int port, String path, boolean secure, Cookie cookie) {
        LOG.trace("enter RFC2965.match(String, int, String, boolean, Cookie");
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (cookie instanceof Cookie2) {
            if (cookie.isPersistent() && cookie.isExpired()) {
                return false;
            }
            CookieOrigin origin = new CookieOrigin(getEffectiveHost(host), port, path, secure);
            Iterator i = getAttribHandlerIterator();
            while (i.hasNext()) {
                CookieAttributeHandler handler = (CookieAttributeHandler) i.next();
                if (!handler.match(cookie, origin)) {
                    return false;
                }
            }
            return true;
        }
        return this.rfc2109.match(host, port, path, secure, cookie);
    }

    private void doFormatCookie2(Cookie2 cookie, StringBuffer buffer) {
        String name = cookie.getName();
        String value = cookie.getValue();
        if (value == null) {
            value = "";
        }
        this.formatter.format(buffer, new NameValuePair(name, value));
        if (cookie.getDomain() != null && cookie.isDomainAttributeSpecified()) {
            buffer.append("; ");
            this.formatter.format(buffer, new NameValuePair("$Domain", cookie.getDomain()));
        }
        if (cookie.getPath() != null && cookie.isPathAttributeSpecified()) {
            buffer.append("; ");
            this.formatter.format(buffer, new NameValuePair("$Path", cookie.getPath()));
        }
        if (cookie.isPortAttributeSpecified()) {
            String portValue = "";
            if (!cookie.isPortAttributeBlank()) {
                portValue = createPortAttribute(cookie.getPorts());
            }
            buffer.append("; ");
            this.formatter.format(buffer, new NameValuePair("$Port", portValue));
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookie(Cookie cookie) {
        LOG.trace("enter RFC2965Spec.formatCookie(Cookie)");
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (cookie instanceof Cookie2) {
            Cookie2 cookie2 = (Cookie2) cookie;
            int version = cookie2.getVersion();
            StringBuffer buffer = new StringBuffer();
            this.formatter.format(buffer, new NameValuePair("$Version", Integer.toString(version)));
            buffer.append("; ");
            doFormatCookie2(cookie2, buffer);
            return buffer.toString();
        }
        return this.rfc2109.formatCookie(cookie);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookies(Cookie[] cookies) {
        LOG.trace("enter RFC2965Spec.formatCookieHeader(Cookie[])");
        if (cookies == null) {
            throw new IllegalArgumentException("Cookies may not be null");
        }
        boolean hasOldStyleCookie = false;
        int version = -1;
        int i = 0;
        while (true) {
            if (i >= cookies.length) {
                break;
            }
            Cookie cookie = cookies[i];
            if (!(cookie instanceof Cookie2)) {
                hasOldStyleCookie = true;
                break;
            }
            if (cookie.getVersion() > version) {
                version = cookie.getVersion();
            }
            i++;
        }
        if (version < 0) {
            version = 0;
        }
        if (hasOldStyleCookie || version < 1) {
            return this.rfc2109.formatCookies(cookies);
        }
        Arrays.sort(cookies, PATH_COMPOARATOR);
        StringBuffer buffer = new StringBuffer();
        this.formatter.format(buffer, new NameValuePair("$Version", Integer.toString(version)));
        for (Cookie cookie2 : cookies) {
            buffer.append("; ");
            doFormatCookie2((Cookie2) cookie2, buffer);
        }
        return buffer.toString();
    }

    private String createPortAttribute(int[] ports) {
        StringBuffer portValue = new StringBuffer();
        int len = ports.length;
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                portValue.append(",");
            }
            portValue.append(ports[i]);
        }
        return portValue.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] parsePortAttribute(String portValue) throws MalformedCookieException {
        StringTokenizer st = new StringTokenizer(portValue, ",");
        int[] ports = new int[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            try {
                ports[i] = Integer.parseInt(st.nextToken().trim());
                if (ports[i] < 0) {
                    throw new MalformedCookieException("Invalid Port attribute.");
                }
                i++;
            } catch (NumberFormatException e) {
                throw new MalformedCookieException(new StringBuffer().append("Invalid Port attribute: ").append(e.getMessage()).toString());
            }
        }
        return ports;
    }

    private static String getEffectiveHost(String host) {
        String effectiveHost = host.toLowerCase();
        if (host.indexOf(46) < 0) {
            effectiveHost = new StringBuffer().append(effectiveHost).append(".local").toString();
        }
        return effectiveHost;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public boolean domainMatch(String host, String domain) {
        boolean match = host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain));
        return match;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean portMatch(int port, int[] ports) {
        boolean portInList = false;
        int i = 0;
        int len = ports.length;
        while (true) {
            if (i >= len) {
                break;
            }
            if (port != ports[i]) {
                i++;
            } else {
                portInList = true;
                break;
            }
        }
        return portInList;
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$Cookie2PathAttributeHandler.class */
    private class Cookie2PathAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private Cookie2PathAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        Cookie2PathAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String path) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (path == null) {
                throw new MalformedCookieException("Missing value for path attribute");
            }
            if (path.trim().equals("")) {
                throw new MalformedCookieException("Blank value for path attribute");
            }
            cookie.setPath(path);
            cookie.setPathAttributeSpecified(true);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            String path = origin.getPath();
            if (path == null) {
                throw new IllegalArgumentException("Path of origin host may not be null.");
            }
            if (cookie.getPath() == null) {
                throw new MalformedCookieException("Invalid cookie state: path attribute is null.");
            }
            if (path.trim().equals("")) {
                path = "/";
            }
            if (!this.this$0.pathMatch(path, cookie.getPath())) {
                throw new MalformedCookieException(new StringBuffer().append("Illegal path attribute \"").append(cookie.getPath()).append("\". Path of origin: \"").append(path).append(SymbolConstants.QUOTES_SYMBOL).toString());
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            String path = origin.getPath();
            if (cookie.getPath() == null) {
                CookieSpecBase.LOG.warn("Invalid cookie state: path attribute is null.");
                return false;
            }
            if (path.trim().equals("")) {
                path = "/";
            }
            if (!this.this$0.pathMatch(path, cookie.getPath())) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$Cookie2DomainAttributeHandler.class */
    private class Cookie2DomainAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private Cookie2DomainAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        Cookie2DomainAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String domain) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (domain == null) {
                throw new MalformedCookieException("Missing value for domain attribute");
            }
            if (domain.trim().equals("")) {
                throw new MalformedCookieException("Blank value for domain attribute");
            }
            String domain2 = domain.toLowerCase();
            if (!domain2.startsWith(".")) {
                domain2 = new StringBuffer().append(".").append(domain2).toString();
            }
            cookie.setDomain(domain2);
            cookie.setDomainAttributeSpecified(true);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            String host = origin.getHost().toLowerCase();
            if (cookie.getDomain() == null) {
                throw new MalformedCookieException("Invalid cookie state: domain not specified");
            }
            String cookieDomain = cookie.getDomain().toLowerCase();
            if (cookie.isDomainAttributeSpecified()) {
                if (!cookieDomain.startsWith(".")) {
                    throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2109: domain must start with a dot").toString());
                }
                int dotIndex = cookieDomain.indexOf(46, 1);
                if ((dotIndex < 0 || dotIndex == cookieDomain.length() - 1) && !cookieDomain.equals(".local")) {
                    throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2965: the value contains no embedded dots ").append("and the value is not .local").toString());
                }
                if (!this.this$0.domainMatch(host, cookieDomain)) {
                    throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2965: effective host name does not ").append("domain-match domain attribute.").toString());
                }
                String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
                if (effectiveHostWithoutDomain.indexOf(46) != -1) {
                    throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2965: ").append("effective host minus domain may not contain any dots").toString());
                }
                return;
            }
            if (!cookie.getDomain().equals(host)) {
                throw new MalformedCookieException(new StringBuffer().append("Illegal domain attribute: \"").append(cookie.getDomain()).append("\".").append("Domain of origin: \"").append(host).append(SymbolConstants.QUOTES_SYMBOL).toString());
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            String host = origin.getHost().toLowerCase();
            String cookieDomain = cookie.getDomain();
            if (!this.this$0.domainMatch(host, cookieDomain)) {
                return false;
            }
            String effectiveHostWithoutDomain = host.substring(0, host.length() - cookieDomain.length());
            if (effectiveHostWithoutDomain.indexOf(46) != -1) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$Cookie2PortAttributeHandler.class */
    private class Cookie2PortAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private Cookie2PortAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        Cookie2PortAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String portValue) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                if (portValue != null && !portValue.trim().equals("")) {
                    int[] ports = this.this$0.parsePortAttribute(portValue);
                    cookie2.setPorts(ports);
                } else {
                    cookie2.setPortAttributeBlank(true);
                }
                cookie2.setPortAttributeSpecified(true);
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                int port = origin.getPort();
                if (cookie2.isPortAttributeSpecified() && !this.this$0.portMatch(port, cookie2.getPorts())) {
                    throw new MalformedCookieException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
                }
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                int port = origin.getPort();
                if (cookie2.isPortAttributeSpecified()) {
                    if (cookie2.getPorts() != null) {
                        if (!this.this$0.portMatch(port, cookie2.getPorts())) {
                            return false;
                        }
                        return true;
                    }
                    CookieSpecBase.LOG.warn("Invalid cookie state: port not specified");
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$Cookie2MaxageAttributeHandler.class */
    private class Cookie2MaxageAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private Cookie2MaxageAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        Cookie2MaxageAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String value) throws MalformedCookieException, NumberFormatException {
            int age;
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (value == null) {
                throw new MalformedCookieException("Missing value for max-age attribute");
            }
            try {
                age = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                age = -1;
            }
            if (age < 0) {
                throw new MalformedCookieException("Invalid max-age attribute.");
            }
            cookie.setExpiryDate(new Date(System.currentTimeMillis() + (age * 1000)));
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) {
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$CookieSecureAttributeHandler.class */
    private class CookieSecureAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private CookieSecureAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        CookieSecureAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String secure) throws MalformedCookieException {
            cookie.setSecure(true);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (origin == null) {
                throw new IllegalArgumentException("Cookie origin may not be null");
            }
            return cookie.getSecure() == origin.isSecure();
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$CookieCommentAttributeHandler.class */
    private class CookieCommentAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private CookieCommentAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        CookieCommentAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String comment) throws MalformedCookieException {
            cookie.setComment(comment);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$CookieCommentUrlAttributeHandler.class */
    private class CookieCommentUrlAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private CookieCommentUrlAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        CookieCommentUrlAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String commenturl) throws MalformedCookieException {
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                cookie2.setCommentURL(commenturl);
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$CookieDiscardAttributeHandler.class */
    private class CookieDiscardAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private CookieDiscardAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        CookieDiscardAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String commenturl) throws MalformedCookieException {
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                cookie2.setDiscard(true);
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            return true;
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2965Spec$Cookie2VersionAttributeHandler.class */
    private class Cookie2VersionAttributeHandler implements CookieAttributeHandler {
        private final RFC2965Spec this$0;

        private Cookie2VersionAttributeHandler(RFC2965Spec rFC2965Spec) {
            this.this$0 = rFC2965Spec;
        }

        Cookie2VersionAttributeHandler(RFC2965Spec x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void parse(Cookie cookie, String value) throws MalformedCookieException, NumberFormatException {
            int version;
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                if (value == null) {
                    throw new MalformedCookieException("Missing value for version attribute");
                }
                try {
                    version = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    version = -1;
                }
                if (version < 0) {
                    throw new MalformedCookieException("Invalid cookie version.");
                }
                cookie2.setVersion(version);
                cookie2.setVersionAttributeSpecified(true);
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
            if (cookie == null) {
                throw new IllegalArgumentException("Cookie may not be null");
            }
            if (cookie instanceof Cookie2) {
                Cookie2 cookie2 = (Cookie2) cookie;
                if (!cookie2.isVersionAttributeSpecified()) {
                    throw new MalformedCookieException("Violates RFC 2965. Version attribute is required.");
                }
            }
        }

        @Override // org.apache.commons.httpclient.cookie.CookieAttributeHandler
        public boolean match(Cookie cookie, CookieOrigin origin) {
            return true;
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieVersionSupport
    public int getVersion() {
        return 1;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieVersionSupport
    public Header getVersionHeader() {
        ParameterFormatter formatter = new ParameterFormatter();
        StringBuffer buffer = new StringBuffer();
        formatter.format(buffer, new NameValuePair("$Version", Integer.toString(getVersion())));
        return new Header("Cookie2", buffer.toString(), true);
    }
}

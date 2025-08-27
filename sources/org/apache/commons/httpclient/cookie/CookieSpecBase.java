package org.apache.commons.httpclient.cookie;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookieSpecBase.class */
public class CookieSpecBase implements CookieSpec {
    protected static final Log LOG;
    private Collection datepatterns = null;
    static Class class$org$apache$commons$httpclient$cookie$CookieSpec;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$cookie$CookieSpec == null) {
            clsClass$ = class$("org.apache.commons.httpclient.cookie.CookieSpec");
            class$org$apache$commons$httpclient$cookie$CookieSpec = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$cookie$CookieSpec;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] parse(String host, int port, String path, boolean secure, String header) throws MalformedCookieException, NumberFormatException {
        HeaderElement[] headerElements;
        LOG.trace("enter CookieSpecBase.parse(String, port, path, boolean, Header)");
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
        String host2 = host.toLowerCase();
        String defaultPath = path;
        int lastSlashIndex = defaultPath.lastIndexOf("/");
        if (lastSlashIndex >= 0) {
            if (lastSlashIndex == 0) {
                lastSlashIndex = 1;
            }
            defaultPath = defaultPath.substring(0, lastSlashIndex);
        }
        boolean isNetscapeCookie = false;
        int i1 = header.toLowerCase().indexOf("expires=");
        if (i1 != -1) {
            int i12 = i1 + "expires=".length();
            int i2 = header.indexOf(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, i12);
            if (i2 == -1) {
                i2 = header.length();
            }
            try {
                DateUtil.parseDate(header.substring(i12, i2), this.datepatterns);
                isNetscapeCookie = true;
            } catch (DateParseException e) {
            }
        }
        if (isNetscapeCookie) {
            headerElements = new HeaderElement[]{new HeaderElement(header.toCharArray())};
        } else {
            headerElements = HeaderElement.parseElements(header.toCharArray());
        }
        Cookie[] cookies = new Cookie[headerElements.length];
        for (int i = 0; i < headerElements.length; i++) {
            HeaderElement headerelement = headerElements[i];
            try {
                Cookie cookie = new Cookie(host2, headerelement.getName(), headerelement.getValue(), defaultPath, (Date) null, false);
                NameValuePair[] parameters = headerelement.getParameters();
                if (parameters != null) {
                    for (NameValuePair nameValuePair : parameters) {
                        parseAttribute(nameValuePair, cookie);
                    }
                }
                cookies[i] = cookie;
            } catch (IllegalArgumentException e2) {
                throw new MalformedCookieException(e2.getMessage());
            }
        }
        return cookies;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] parse(String host, int port, String path, boolean secure, Header header) throws MalformedCookieException {
        LOG.trace("enter CookieSpecBase.parse(String, port, path, boolean, String)");
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null.");
        }
        return parse(host, port, path, secure, header.getValue());
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException, NumberFormatException {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute may not be null.");
        }
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null.");
        }
        String paramName = attribute.getName().toLowerCase();
        String paramValue = attribute.getValue();
        if (paramName.equals(Cookie2.PATH)) {
            if (paramValue == null || paramValue.trim().equals("")) {
                paramValue = "/";
            }
            cookie.setPath(paramValue);
            cookie.setPathAttributeSpecified(true);
            return;
        }
        if (paramName.equals(Cookie2.DOMAIN)) {
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for domain attribute");
            }
            if (paramValue.trim().equals("")) {
                throw new MalformedCookieException("Blank value for domain attribute");
            }
            cookie.setDomain(paramValue);
            cookie.setDomainAttributeSpecified(true);
            return;
        }
        if (paramName.equals("max-age")) {
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for max-age attribute");
            }
            try {
                int age = Integer.parseInt(paramValue);
                cookie.setExpiryDate(new Date(System.currentTimeMillis() + (age * 1000)));
                return;
            } catch (NumberFormatException e) {
                throw new MalformedCookieException(new StringBuffer().append("Invalid max-age attribute: ").append(e.getMessage()).toString());
            }
        }
        if (paramName.equals(Cookie2.SECURE)) {
            cookie.setSecure(true);
            return;
        }
        if (paramName.equals("comment")) {
            cookie.setComment(paramValue);
            return;
        }
        if (paramName.equals("expires")) {
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for expires attribute");
            }
            try {
                cookie.setExpiryDate(DateUtil.parseDate(paramValue, this.datepatterns));
                return;
            } catch (DateParseException dpe) {
                LOG.debug("Error parsing cookie date", dpe);
                throw new MalformedCookieException(new StringBuffer().append("Unable to parse expiration date parameter: ").append(paramValue).toString());
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Unrecognized cookie attribute: ").append(attribute.toString()).toString());
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Collection getValidDateFormats() {
        return this.datepatterns;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public void setValidDateFormats(Collection datepatterns) {
        this.datepatterns = datepatterns;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
        LOG.trace("enter CookieSpecBase.validate(String, port, path, boolean, Cookie)");
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
        if (path.trim().equals("")) {
            path = "/";
        }
        String host2 = host.toLowerCase();
        if (cookie.getVersion() < 0) {
            throw new MalformedCookieException(new StringBuffer().append("Illegal version number ").append(cookie.getValue()).toString());
        }
        if (host2.indexOf(".") >= 0) {
            if (!host2.endsWith(cookie.getDomain())) {
                String s = cookie.getDomain();
                if (s.startsWith(".")) {
                    s = s.substring(1, s.length());
                }
                if (!host2.equals(s)) {
                    throw new MalformedCookieException(new StringBuffer().append("Illegal domain attribute \"").append(cookie.getDomain()).append("\". Domain of origin: \"").append(host2).append(SymbolConstants.QUOTES_SYMBOL).toString());
                }
            }
        } else if (!host2.equals(cookie.getDomain())) {
            throw new MalformedCookieException(new StringBuffer().append("Illegal domain attribute \"").append(cookie.getDomain()).append("\". Domain of origin: \"").append(host2).append(SymbolConstants.QUOTES_SYMBOL).toString());
        }
        if (!path.startsWith(cookie.getPath())) {
            throw new MalformedCookieException(new StringBuffer().append("Illegal path attribute \"").append(cookie.getPath()).append("\". Path of origin: \"").append(path).append(SymbolConstants.QUOTES_SYMBOL).toString());
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public boolean match(String host, int port, String path, boolean secure, Cookie cookie) {
        LOG.trace("enter CookieSpecBase.match(String, int, String, boolean, Cookie");
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
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (path.trim().equals("")) {
            path = "/";
        }
        String host2 = host.toLowerCase();
        if (cookie.getDomain() == null) {
            LOG.warn("Invalid cookie state: domain not specified");
            return false;
        }
        if (cookie.getPath() != null) {
            return (cookie.getExpiryDate() == null || cookie.getExpiryDate().after(new Date())) && domainMatch(host2, cookie.getDomain()) && pathMatch(path, cookie.getPath()) && (!cookie.getSecure() || secure);
        }
        LOG.warn("Invalid cookie state: path not specified");
        return false;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public boolean domainMatch(String host, String domain) {
        if (host.equals(domain)) {
            return true;
        }
        if (!domain.startsWith(".")) {
            domain = new StringBuffer().append(".").append(domain).toString();
        }
        return host.endsWith(domain) || host.equals(domain.substring(1));
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public boolean pathMatch(String path, String topmostPath) {
        boolean match = path.startsWith(topmostPath);
        if (match && path.length() != topmostPath.length() && !topmostPath.endsWith("/")) {
            match = path.charAt(topmostPath.length()) == CookieSpec.PATH_DELIM_CHAR;
        }
        return match;
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] match(String host, int port, String path, boolean secure, Cookie[] cookies) {
        LOG.trace("enter CookieSpecBase.match(String, int, String, boolean, Cookie[])");
        if (cookies == null) {
            return null;
        }
        List matching = new LinkedList();
        for (int i = 0; i < cookies.length; i++) {
            if (match(host, port, path, secure, cookies[i])) {
                addInPathOrder(matching, cookies[i]);
            }
        }
        return (Cookie[]) matching.toArray(new Cookie[matching.size()]);
    }

    private static void addInPathOrder(List list, Cookie addCookie) {
        int i = 0;
        while (i < list.size()) {
            Cookie c = (Cookie) list.get(i);
            if (addCookie.compare(addCookie, c) > 0) {
                break;
            } else {
                i++;
            }
        }
        list.add(i, addCookie);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookie(Cookie cookie) {
        LOG.trace("enter CookieSpecBase.formatCookie(Cookie)");
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        StringBuffer buf = new StringBuffer();
        buf.append(cookie.getName());
        buf.append(SymbolConstants.EQUAL_SYMBOL);
        String s = cookie.getValue();
        if (s != null) {
            buf.append(s);
        }
        return buf.toString();
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookies(Cookie[] cookies) throws IllegalArgumentException {
        LOG.trace("enter CookieSpecBase.formatCookies(Cookie[])");
        if (cookies == null) {
            throw new IllegalArgumentException("Cookie array may not be null");
        }
        if (cookies.length == 0) {
            throw new IllegalArgumentException("Cookie array may not be empty");
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < cookies.length; i++) {
            if (i > 0) {
                buffer.append("; ");
            }
            buffer.append(formatCookie(cookies[i]));
        }
        return buffer.toString();
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Header formatCookieHeader(Cookie[] cookies) {
        LOG.trace("enter CookieSpecBase.formatCookieHeader(Cookie[])");
        return new Header("Cookie", formatCookies(cookies));
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpec
    public Header formatCookieHeader(Cookie cookie) {
        LOG.trace("enter CookieSpecBase.formatCookieHeader(Cookie)");
        return new Header("Cookie", formatCookie(cookie));
    }
}

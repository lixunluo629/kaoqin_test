package org.apache.commons.httpclient.cookie;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.ParameterFormatter;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/RFC2109Spec.class */
public class RFC2109Spec extends CookieSpecBase {
    private final ParameterFormatter formatter = new ParameterFormatter();
    public static final String SET_COOKIE_KEY = "set-cookie";

    public RFC2109Spec() {
        this.formatter.setAlwaysUseQuotes(true);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
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
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for path attribute");
            }
            if (paramValue.trim().equals("")) {
                throw new MalformedCookieException("Blank value for path attribute");
            }
            cookie.setPath(paramValue);
            cookie.setPathAttributeSpecified(true);
            return;
        }
        if (paramName.equals("version")) {
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for version attribute");
            }
            try {
                cookie.setVersion(Integer.parseInt(paramValue));
                return;
            } catch (NumberFormatException e) {
                throw new MalformedCookieException(new StringBuffer().append("Invalid version: ").append(e.getMessage()).toString());
            }
        }
        super.parseAttribute(attribute, cookie);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
        LOG.trace("enter RFC2109Spec.validate(String, int, String, boolean, Cookie)");
        super.validate(host, port, path, secure, cookie);
        if (cookie.getName().indexOf(32) != -1) {
            throw new MalformedCookieException("Cookie name may not contain blanks");
        }
        if (cookie.getName().startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
            throw new MalformedCookieException("Cookie name may not start with $");
        }
        if (cookie.isDomainAttributeSpecified() && !cookie.getDomain().equals(host)) {
            if (!cookie.getDomain().startsWith(".")) {
                throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2109: domain must start with a dot").toString());
            }
            int dotIndex = cookie.getDomain().indexOf(46, 1);
            if (dotIndex < 0 || dotIndex == cookie.getDomain().length() - 1) {
                throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2109: domain must contain an embedded dot").toString());
            }
            String host2 = host.toLowerCase();
            if (!host2.endsWith(cookie.getDomain())) {
                throw new MalformedCookieException(new StringBuffer().append("Illegal domain attribute \"").append(cookie.getDomain()).append("\". Domain of origin: \"").append(host2).append(SymbolConstants.QUOTES_SYMBOL).toString());
            }
            String hostWithoutDomain = host2.substring(0, host2.length() - cookie.getDomain().length());
            if (hostWithoutDomain.indexOf(46) != -1) {
                throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates RFC 2109: host minus domain may not contain any dots").toString());
            }
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public boolean domainMatch(String host, String domain) {
        boolean match = host.equals(domain) || (domain.startsWith(".") && host.endsWith(domain));
        return match;
    }

    private void formatParam(StringBuffer buffer, NameValuePair param, int version) {
        if (version < 1) {
            buffer.append(param.getName());
            buffer.append(SymbolConstants.EQUAL_SYMBOL);
            if (param.getValue() != null) {
                buffer.append(param.getValue());
                return;
            }
            return;
        }
        this.formatter.format(buffer, param);
    }

    private void formatCookieAsVer(StringBuffer buffer, Cookie cookie, int version) {
        String value = cookie.getValue();
        if (value == null) {
            value = "";
        }
        formatParam(buffer, new NameValuePair(cookie.getName(), value), version);
        if (cookie.getPath() != null && cookie.isPathAttributeSpecified()) {
            buffer.append("; ");
            formatParam(buffer, new NameValuePair("$Path", cookie.getPath()), version);
        }
        if (cookie.getDomain() != null && cookie.isDomainAttributeSpecified()) {
            buffer.append("; ");
            formatParam(buffer, new NameValuePair("$Domain", cookie.getDomain()), version);
        }
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookie(Cookie cookie) {
        LOG.trace("enter RFC2109Spec.formatCookie(Cookie)");
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        int version = cookie.getVersion();
        StringBuffer buffer = new StringBuffer();
        formatParam(buffer, new NameValuePair("$Version", Integer.toString(version)), version);
        buffer.append("; ");
        formatCookieAsVer(buffer, cookie, version);
        return buffer.toString();
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public String formatCookies(Cookie[] cookies) {
        LOG.trace("enter RFC2109Spec.formatCookieHeader(Cookie[])");
        int version = Integer.MAX_VALUE;
        for (Cookie cookie : cookies) {
            if (cookie.getVersion() < version) {
                version = cookie.getVersion();
            }
        }
        StringBuffer buffer = new StringBuffer();
        formatParam(buffer, new NameValuePair("$Version", Integer.toString(version)), version);
        for (Cookie cookie2 : cookies) {
            buffer.append("; ");
            formatCookieAsVer(buffer, cookie2, version);
        }
        return buffer.toString();
    }
}

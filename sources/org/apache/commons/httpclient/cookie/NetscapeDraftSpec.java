package org.apache.commons.httpclient.cookie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/NetscapeDraftSpec.class */
public class NetscapeDraftSpec extends CookieSpecBase {
    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public Cookie[] parse(String host, int port, String path, boolean secure, String header) throws MalformedCookieException, NumberFormatException, ParseException {
        LOG.trace("enter NetscapeDraftSpec.parse(String, port, path, boolean, Header)");
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
        HeaderElement headerelement = new HeaderElement(header.toCharArray());
        Cookie cookie = new Cookie(host2, headerelement.getName(), headerelement.getValue(), defaultPath, (Date) null, false);
        NameValuePair[] parameters = headerelement.getParameters();
        if (parameters != null) {
            for (NameValuePair nameValuePair : parameters) {
                parseAttribute(nameValuePair, cookie);
            }
        }
        return new Cookie[]{cookie};
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException, NumberFormatException, ParseException {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute may not be null.");
        }
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null.");
        }
        String paramName = attribute.getName().toLowerCase();
        String paramValue = attribute.getValue();
        if (paramName.equals("expires")) {
            if (paramValue == null) {
                throw new MalformedCookieException("Missing value for expires attribute");
            }
            try {
                DateFormat expiryFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);
                Date date = expiryFormat.parse(paramValue);
                cookie.setExpiryDate(date);
                return;
            } catch (ParseException e) {
                throw new MalformedCookieException(new StringBuffer().append("Invalid expires attribute: ").append(e.getMessage()).toString());
            }
        }
        super.parseAttribute(attribute, cookie);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public boolean domainMatch(String host, String domain) {
        return host.endsWith(domain);
    }

    @Override // org.apache.commons.httpclient.cookie.CookieSpecBase, org.apache.commons.httpclient.cookie.CookieSpec
    public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
        LOG.trace("enterNetscapeDraftCookieProcessor RCF2109CookieProcessor.validate(Cookie)");
        super.validate(host, port, path, secure, cookie);
        if (host.indexOf(".") >= 0) {
            int domainParts = new StringTokenizer(cookie.getDomain(), ".").countTokens();
            if (isSpecialDomain(cookie.getDomain())) {
                if (domainParts < 2) {
                    throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates the Netscape cookie specification for ").append("special domains").toString());
                }
            } else if (domainParts < 3) {
                throw new MalformedCookieException(new StringBuffer().append("Domain attribute \"").append(cookie.getDomain()).append("\" violates the Netscape cookie specification").toString());
            }
        }
    }

    private static boolean isSpecialDomain(String domain) {
        String ucDomain = domain.toUpperCase();
        if (ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT")) {
            return true;
        }
        return false;
    }
}

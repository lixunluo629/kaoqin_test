package org.apache.commons.httpclient.cookie;

import java.util.Collection;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookieSpec.class */
public interface CookieSpec {
    public static final String PATH_DELIM = "/";
    public static final char PATH_DELIM_CHAR = "/".charAt(0);

    Cookie[] parse(String str, int i, String str2, boolean z, String str3) throws MalformedCookieException, IllegalArgumentException;

    Cookie[] parse(String str, int i, String str2, boolean z, Header header) throws MalformedCookieException, IllegalArgumentException;

    void parseAttribute(NameValuePair nameValuePair, Cookie cookie) throws MalformedCookieException, IllegalArgumentException;

    void validate(String str, int i, String str2, boolean z, Cookie cookie) throws MalformedCookieException, IllegalArgumentException;

    void setValidDateFormats(Collection collection);

    Collection getValidDateFormats();

    boolean match(String str, int i, String str2, boolean z, Cookie cookie);

    Cookie[] match(String str, int i, String str2, boolean z, Cookie[] cookieArr);

    boolean domainMatch(String str, String str2);

    boolean pathMatch(String str, String str2);

    String formatCookie(Cookie cookie);

    String formatCookies(Cookie[] cookieArr) throws IllegalArgumentException;

    Header formatCookieHeader(Cookie[] cookieArr) throws IllegalArgumentException;

    Header formatCookieHeader(Cookie cookie) throws IllegalArgumentException;
}

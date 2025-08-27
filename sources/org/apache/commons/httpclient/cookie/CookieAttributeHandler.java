package org.apache.commons.httpclient.cookie;

import org.apache.commons.httpclient.Cookie;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookieAttributeHandler.class */
public interface CookieAttributeHandler {
    void parse(Cookie cookie, String str) throws MalformedCookieException;

    void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException;

    boolean match(Cookie cookie, CookieOrigin cookieOrigin);
}

package org.apache.tomcat.util.http;

import java.nio.charset.Charset;
import javax.servlet.http.Cookie;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/CookieProcessor.class */
public interface CookieProcessor {
    void parseCookieHeader(MimeHeaders mimeHeaders, ServerCookies serverCookies);

    String generateHeader(Cookie cookie);

    Charset getCharset();
}

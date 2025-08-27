package io.netty.handler.codec.http.cookie;

import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/ServerCookieDecoder.class */
public final class ServerCookieDecoder extends CookieDecoder {
    private static final String RFC2965_VERSION = "$Version";
    private static final String RFC2965_PATH = "$Path";
    private static final String RFC2965_DOMAIN = "$Domain";
    private static final String RFC2965_PORT = "$Port";
    public static final ServerCookieDecoder STRICT = new ServerCookieDecoder(true);
    public static final ServerCookieDecoder LAX = new ServerCookieDecoder(false);

    private ServerCookieDecoder(boolean strict) {
        super(strict);
    }

    public List<Cookie> decodeAll(String header) {
        List<Cookie> cookies = new ArrayList<>();
        decode(cookies, header);
        return Collections.unmodifiableList(cookies);
    }

    public Set<Cookie> decode(String header) {
        Set<Cookie> cookies = new TreeSet<>();
        decode(cookies, header);
        return cookies;
    }

    private void decode(Collection<? super Cookie> cookies, String header) {
        int nameEnd;
        int valueEnd;
        int valueBegin;
        int headerLen = ((String) ObjectUtil.checkNotNull(header, "header")).length();
        if (headerLen == 0) {
            return;
        }
        int i = 0;
        boolean rfc2965Style = false;
        if (header.regionMatches(true, 0, RFC2965_VERSION, 0, RFC2965_VERSION.length())) {
            i = header.indexOf(59) + 1;
            rfc2965Style = true;
        }
        while (i != headerLen) {
            char c = header.charAt(i);
            if (c == '\t' || c == '\n' || c == 11 || c == '\f' || c == '\r' || c == ' ' || c == ',' || c == ';') {
                i++;
            } else {
                int nameBegin = i;
                while (true) {
                    char curChar = header.charAt(i);
                    if (curChar == ';') {
                        nameEnd = i;
                        valueEnd = -1;
                        valueBegin = -1;
                        break;
                    }
                    if (curChar == '=') {
                        nameEnd = i;
                        i++;
                        if (i == headerLen) {
                            valueEnd = 0;
                            valueBegin = 0;
                        } else {
                            valueBegin = i;
                            int semiPos = header.indexOf(59, i);
                            int i2 = semiPos > 0 ? semiPos : headerLen;
                            i = i2;
                            valueEnd = i2;
                        }
                    } else {
                        i++;
                        if (i == headerLen) {
                            nameEnd = headerLen;
                            valueEnd = -1;
                            valueBegin = -1;
                            break;
                        }
                    }
                }
                if (!rfc2965Style || (!header.regionMatches(nameBegin, RFC2965_PATH, 0, RFC2965_PATH.length()) && !header.regionMatches(nameBegin, RFC2965_DOMAIN, 0, RFC2965_DOMAIN.length()) && !header.regionMatches(nameBegin, RFC2965_PORT, 0, RFC2965_PORT.length()))) {
                    DefaultCookie cookie = initCookie(header, nameBegin, nameEnd, valueBegin, valueEnd);
                    if (cookie != null) {
                        cookies.add(cookie);
                    }
                }
            }
        }
    }
}

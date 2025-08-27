package io.netty.handler.codec.http;

import java.util.Collection;
import java.util.List;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/ServerCookieEncoder.class */
public final class ServerCookieEncoder {
    @Deprecated
    public static String encode(String name, String value) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(name, value);
    }

    @Deprecated
    public static String encode(Cookie cookie) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie);
    }

    @Deprecated
    public static List<String> encode(Cookie... cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
    }

    @Deprecated
    public static List<String> encode(Collection<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode((Collection<? extends io.netty.handler.codec.http.cookie.Cookie>) cookies);
    }

    @Deprecated
    public static List<String> encode(Iterable<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
    }

    private ServerCookieEncoder() {
    }
}

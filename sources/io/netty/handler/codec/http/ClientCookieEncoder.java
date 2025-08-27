package io.netty.handler.codec.http;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/ClientCookieEncoder.class */
public final class ClientCookieEncoder {
    @Deprecated
    public static String encode(String name, String value) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(name, value);
    }

    @Deprecated
    public static String encode(Cookie cookie) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookie);
    }

    @Deprecated
    public static String encode(Cookie... cookies) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookies);
    }

    @Deprecated
    public static String encode(Iterable<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookies);
    }

    private ClientCookieEncoder() {
    }
}

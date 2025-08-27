package io.netty.handler.codec.http.cookie;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/Cookie.class */
public interface Cookie extends Comparable<Cookie> {
    public static final long UNDEFINED_MAX_AGE = Long.MIN_VALUE;

    String name();

    String value();

    void setValue(String str);

    boolean wrap();

    void setWrap(boolean z);

    String domain();

    void setDomain(String str);

    String path();

    void setPath(String str);

    long maxAge();

    void setMaxAge(long j);

    boolean isSecure();

    void setSecure(boolean z);

    boolean isHttpOnly();

    void setHttpOnly(boolean z);
}

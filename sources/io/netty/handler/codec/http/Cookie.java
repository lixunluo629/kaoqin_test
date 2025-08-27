package io.netty.handler.codec.http;

import java.util.Set;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/Cookie.class */
public interface Cookie extends io.netty.handler.codec.http.cookie.Cookie {
    @Deprecated
    String getName();

    @Deprecated
    String getValue();

    @Deprecated
    String getDomain();

    @Deprecated
    String getPath();

    @Deprecated
    String getComment();

    @Deprecated
    String comment();

    @Deprecated
    void setComment(String str);

    @Deprecated
    long getMaxAge();

    @Override // io.netty.handler.codec.http.cookie.Cookie
    @Deprecated
    long maxAge();

    @Override // io.netty.handler.codec.http.cookie.Cookie
    @Deprecated
    void setMaxAge(long j);

    @Deprecated
    int getVersion();

    @Deprecated
    int version();

    @Deprecated
    void setVersion(int i);

    @Deprecated
    String getCommentUrl();

    @Deprecated
    String commentUrl();

    @Deprecated
    void setCommentUrl(String str);

    @Deprecated
    boolean isDiscard();

    @Deprecated
    void setDiscard(boolean z);

    @Deprecated
    Set<Integer> getPorts();

    @Deprecated
    Set<Integer> ports();

    @Deprecated
    void setPorts(int... iArr);

    @Deprecated
    void setPorts(Iterable<Integer> iterable);
}

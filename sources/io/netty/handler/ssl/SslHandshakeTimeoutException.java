package io.netty.handler.ssl;

import javax.net.ssl.SSLHandshakeException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandshakeTimeoutException.class */
public final class SslHandshakeTimeoutException extends SSLHandshakeException {
    SslHandshakeTimeoutException(String reason) {
        super(reason);
    }
}

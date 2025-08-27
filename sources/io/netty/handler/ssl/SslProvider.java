package io.netty.handler.ssl;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslProvider.class */
public enum SslProvider {
    JDK,
    OPENSSL,
    OPENSSL_REFCNT;

    public static boolean isAlpnSupported(SslProvider provider) {
        switch (provider) {
            case JDK:
                return JdkAlpnApplicationProtocolNegotiator.isAlpnSupported();
            case OPENSSL:
            case OPENSSL_REFCNT:
                return OpenSsl.isAlpnSupported();
            default:
                throw new Error("Unknown SslProvider: " + provider);
        }
    }
}

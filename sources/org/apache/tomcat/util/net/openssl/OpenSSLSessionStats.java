package org.apache.tomcat.util.net.openssl;

import org.apache.tomcat.jni.SSLContext;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/openssl/OpenSSLSessionStats.class */
public final class OpenSSLSessionStats {
    private final long context;

    OpenSSLSessionStats(long context) {
        this.context = context;
    }

    public long number() {
        return SSLContext.sessionNumber(this.context);
    }

    public long connect() {
        return SSLContext.sessionConnect(this.context);
    }

    public long connectGood() {
        return SSLContext.sessionConnectGood(this.context);
    }

    public long connectRenegotiate() {
        return SSLContext.sessionConnectRenegotiate(this.context);
    }

    public long accept() {
        return SSLContext.sessionAccept(this.context);
    }

    public long acceptGood() {
        return SSLContext.sessionAcceptGood(this.context);
    }

    public long acceptRenegotiate() {
        return SSLContext.sessionAcceptRenegotiate(this.context);
    }

    public long hits() {
        return SSLContext.sessionHits(this.context);
    }

    public long cbHits() {
        return SSLContext.sessionCbHits(this.context);
    }

    public long misses() {
        return SSLContext.sessionMisses(this.context);
    }

    public long timeouts() {
        return SSLContext.sessionTimeouts(this.context);
    }

    public long cacheFull() {
        return SSLContext.sessionCacheFull(this.context);
    }
}

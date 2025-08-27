package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyVersion.class */
public enum SpdyVersion {
    SPDY_3_1(3, 1);

    private final int version;
    private final int minorVersion;

    SpdyVersion(int version, int minorVersion) {
        this.version = version;
        this.minorVersion = minorVersion;
    }

    int getVersion() {
        return this.version;
    }

    int getMinorVersion() {
        return this.minorVersion;
    }
}

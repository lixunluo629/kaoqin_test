package io.netty.handler.codec.spdy;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyProtocolException.class */
public class SpdyProtocolException extends Exception {
    private static final long serialVersionUID = 7870000537743847264L;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SpdyProtocolException.class.desiredAssertionStatus();
    }

    public SpdyProtocolException() {
    }

    public SpdyProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpdyProtocolException(String message) {
        super(message);
    }

    public SpdyProtocolException(Throwable cause) {
        super(cause);
    }

    static SpdyProtocolException newStatic(String message) {
        if (PlatformDependent.javaVersion() >= 7) {
            return new SpdyProtocolException(message, true);
        }
        return new SpdyProtocolException(message);
    }

    @SuppressJava6Requirement(reason = "uses Java 7+ Exception.<init>(String, Throwable, boolean, boolean) but is guarded by version checks")
    private SpdyProtocolException(String message, boolean shared) {
        super(message, null, false, true);
        if (!$assertionsDisabled && !shared) {
            throw new AssertionError();
        }
    }
}

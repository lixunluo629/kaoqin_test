package io.netty.channel;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelException.class */
public class ChannelException extends RuntimeException {
    private static final long serialVersionUID = 2908618315971075004L;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChannelException.class.desiredAssertionStatus();
    }

    public ChannelException() {
    }

    public ChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelException(String message) {
        super(message);
    }

    public ChannelException(Throwable cause) {
        super(cause);
    }

    @SuppressJava6Requirement(reason = "uses Java 7+ RuntimeException.<init>(String, Throwable, boolean, boolean) but is guarded by version checks")
    protected ChannelException(String message, Throwable cause, boolean shared) {
        super(message, cause, false, true);
        if (!$assertionsDisabled && !shared) {
            throw new AssertionError();
        }
    }

    static ChannelException newStatic(String message, Throwable cause) {
        if (PlatformDependent.javaVersion() >= 7) {
            return new ChannelException(message, cause, true);
        }
        return new ChannelException(message, cause);
    }
}

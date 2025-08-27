package io.netty.handler.codec;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/CorruptedFrameException.class */
public class CorruptedFrameException extends DecoderException {
    private static final long serialVersionUID = 3918052232492988408L;

    public CorruptedFrameException() {
    }

    public CorruptedFrameException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorruptedFrameException(String message) {
        super(message);
    }

    public CorruptedFrameException(Throwable cause) {
        super(cause);
    }
}

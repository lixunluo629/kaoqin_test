package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.aspectj.weaver.model.AsmRelationshipUtils;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception.class */
public class Http2Exception extends Exception {
    private static final long serialVersionUID = -6941186345430164209L;
    private final Http2Error error;
    private final ShutdownHint shutdownHint;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception$ShutdownHint.class */
    public enum ShutdownHint {
        NO_SHUTDOWN,
        GRACEFUL_SHUTDOWN,
        HARD_SHUTDOWN
    }

    static {
        $assertionsDisabled = !Http2Exception.class.desiredAssertionStatus();
    }

    public Http2Exception(Http2Error error) {
        this(error, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, ShutdownHint shutdownHint) {
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, AsmRelationshipUtils.DECLARE_ERROR);
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Exception(Http2Error error, String message) {
        this(error, message, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, String message, ShutdownHint shutdownHint) {
        super(message);
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, AsmRelationshipUtils.DECLARE_ERROR);
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Exception(Http2Error error, String message, Throwable cause) {
        this(error, message, cause, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, String message, Throwable cause, ShutdownHint shutdownHint) {
        super(message, cause);
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, AsmRelationshipUtils.DECLARE_ERROR);
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    static Http2Exception newStatic(Http2Error error, String message, ShutdownHint shutdownHint) {
        if (PlatformDependent.javaVersion() >= 7) {
            return new Http2Exception(error, message, shutdownHint, true);
        }
        return new Http2Exception(error, message, shutdownHint);
    }

    @SuppressJava6Requirement(reason = "uses Java 7+ Exception.<init>(String, Throwable, boolean, boolean) but is guarded by version checks")
    private Http2Exception(Http2Error error, String message, ShutdownHint shutdownHint, boolean shared) {
        super(message, null, false, true);
        if (!$assertionsDisabled && !shared) {
            throw new AssertionError();
        }
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, AsmRelationshipUtils.DECLARE_ERROR);
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Error error() {
        return this.error;
    }

    public ShutdownHint shutdownHint() {
        return this.shutdownHint;
    }

    public static Http2Exception connectionError(Http2Error error, String fmt, Object... args) {
        return new Http2Exception(error, String.format(fmt, args));
    }

    public static Http2Exception connectionError(Http2Error error, Throwable cause, String fmt, Object... args) {
        return new Http2Exception(error, String.format(fmt, args), cause);
    }

    public static Http2Exception closedStreamError(Http2Error error, String fmt, Object... args) {
        return new ClosedStreamCreationException(error, String.format(fmt, args));
    }

    public static Http2Exception streamError(int id, Http2Error error, String fmt, Object... args) {
        if (0 == id) {
            return connectionError(error, fmt, args);
        }
        return new StreamException(id, error, String.format(fmt, args));
    }

    public static Http2Exception streamError(int id, Http2Error error, Throwable cause, String fmt, Object... args) {
        if (0 == id) {
            return connectionError(error, cause, fmt, args);
        }
        return new StreamException(id, error, String.format(fmt, args), cause);
    }

    public static Http2Exception headerListSizeError(int id, Http2Error error, boolean onDecode, String fmt, Object... args) {
        if (0 == id) {
            return connectionError(error, fmt, args);
        }
        return new HeaderListSizeException(id, error, String.format(fmt, args), onDecode);
    }

    public static boolean isStreamError(Http2Exception e) {
        return e instanceof StreamException;
    }

    public static int streamId(Http2Exception e) {
        if (isStreamError(e)) {
            return ((StreamException) e).streamId();
        }
        return 0;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception$ClosedStreamCreationException.class */
    public static final class ClosedStreamCreationException extends Http2Exception {
        private static final long serialVersionUID = -6746542974372246206L;

        public ClosedStreamCreationException(Http2Error error) {
            super(error);
        }

        public ClosedStreamCreationException(Http2Error error, String message) {
            super(error, message);
        }

        public ClosedStreamCreationException(Http2Error error, String message, Throwable cause) {
            super(error, message, cause);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception$StreamException.class */
    public static class StreamException extends Http2Exception {
        private static final long serialVersionUID = 602472544416984384L;
        private final int streamId;

        StreamException(int streamId, Http2Error error, String message) {
            super(error, message, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }

        StreamException(int streamId, Http2Error error, String message, Throwable cause) {
            super(error, message, cause, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }

        public int streamId() {
            return this.streamId;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception$HeaderListSizeException.class */
    public static final class HeaderListSizeException extends StreamException {
        private static final long serialVersionUID = -8807603212183882637L;
        private final boolean decode;

        HeaderListSizeException(int streamId, Http2Error error, String message, boolean decode) {
            super(streamId, error, message);
            this.decode = decode;
        }

        public boolean duringDecode() {
            return this.decode;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Exception$CompositeStreamException.class */
    public static final class CompositeStreamException extends Http2Exception implements Iterable<StreamException> {
        private static final long serialVersionUID = 7091134858213711015L;
        private final List<StreamException> exceptions;

        public CompositeStreamException(Http2Error error, int initialCapacity) {
            super(error, ShutdownHint.NO_SHUTDOWN);
            this.exceptions = new ArrayList(initialCapacity);
        }

        public void add(StreamException e) {
            this.exceptions.add(e);
        }

        @Override // java.lang.Iterable
        public Iterator<StreamException> iterator() {
            return this.exceptions.iterator();
        }
    }
}

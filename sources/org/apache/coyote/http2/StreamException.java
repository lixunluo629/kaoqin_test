package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/StreamException.class */
public class StreamException extends Http2Exception {
    private static final long serialVersionUID = 1;
    private final int streamId;

    public StreamException(String msg, Http2Error error, int streamId) {
        super(msg, error);
        this.streamId = streamId;
    }

    public int getStreamId() {
        return this.streamId;
    }
}

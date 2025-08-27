package org.apache.coyote.http2;

import org.apache.coyote.http2.HpackDecoder;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/HeaderSink.class */
public class HeaderSink implements HpackDecoder.HeaderEmitter {
    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void emitHeader(String name, String value) {
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void validateHeaders() throws StreamException {
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void setHeaderException(StreamException streamException) {
    }
}

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.Headers;
import io.netty.util.AsciiString;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeaders.class */
public interface SpdyHeaders extends Headers<CharSequence, CharSequence, SpdyHeaders> {
    String getAsString(CharSequence charSequence);

    List<String> getAllAsString(CharSequence charSequence);

    Iterator<Map.Entry<String, String>> iteratorAsString();

    boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean z);

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeaders$HttpNames.class */
    public static final class HttpNames {
        public static final AsciiString HOST = AsciiString.cached(":host");
        public static final AsciiString METHOD = AsciiString.cached(":method");
        public static final AsciiString PATH = AsciiString.cached(":path");
        public static final AsciiString SCHEME = AsciiString.cached(":scheme");
        public static final AsciiString STATUS = AsciiString.cached(":status");
        public static final AsciiString VERSION = AsciiString.cached(":version");

        private HttpNames() {
        }
    }
}

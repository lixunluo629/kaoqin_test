package io.netty.handler.codec.http2;

import io.netty.handler.codec.Headers;
import io.netty.util.AsciiString;
import java.util.Iterator;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Headers.class */
public interface Http2Headers extends Headers<CharSequence, CharSequence, Http2Headers> {
    @Override // io.netty.handler.codec.Headers, java.lang.Iterable
    Iterator<Map.Entry<CharSequence, CharSequence>> iterator();

    Iterator<CharSequence> valueIterator(CharSequence charSequence);

    Http2Headers method(CharSequence charSequence);

    Http2Headers scheme(CharSequence charSequence);

    Http2Headers authority(CharSequence charSequence);

    Http2Headers path(CharSequence charSequence);

    Http2Headers status(CharSequence charSequence);

    CharSequence method();

    CharSequence scheme();

    CharSequence authority();

    CharSequence path();

    CharSequence status();

    boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean z);

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Headers$PseudoHeaderName.class */
    public enum PseudoHeaderName {
        METHOD(":method", true),
        SCHEME(":scheme", true),
        AUTHORITY(":authority", true),
        PATH(":path", true),
        STATUS(":status", false);

        private static final char PSEUDO_HEADER_PREFIX = ':';
        private static final byte PSEUDO_HEADER_PREFIX_BYTE = 58;
        private final AsciiString value;
        private final boolean requestOnly;
        private static final CharSequenceMap<PseudoHeaderName> PSEUDO_HEADERS = new CharSequenceMap<>();

        static {
            for (PseudoHeaderName pseudoHeader : values()) {
                PSEUDO_HEADERS.add((CharSequenceMap<PseudoHeaderName>) pseudoHeader.value(), (AsciiString) pseudoHeader);
            }
        }

        PseudoHeaderName(String value, boolean requestOnly) {
            this.value = AsciiString.cached(value);
            this.requestOnly = requestOnly;
        }

        public AsciiString value() {
            return this.value;
        }

        public static boolean hasPseudoHeaderFormat(CharSequence headerName) {
            if (!(headerName instanceof AsciiString)) {
                return headerName.length() > 0 && headerName.charAt(0) == ':';
            }
            AsciiString asciiHeaderName = (AsciiString) headerName;
            return asciiHeaderName.length() > 0 && asciiHeaderName.byteAt(0) == 58;
        }

        public static boolean isPseudoHeader(CharSequence header) {
            return PSEUDO_HEADERS.contains(header);
        }

        public static PseudoHeaderName getPseudoHeader(CharSequence header) {
            return PSEUDO_HEADERS.get(header);
        }

        public boolean isRequestOnly() {
            return this.requestOnly;
        }
    }
}

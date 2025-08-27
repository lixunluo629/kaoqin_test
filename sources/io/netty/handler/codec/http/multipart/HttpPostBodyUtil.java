package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import org.apache.commons.httpclient.methods.multipart.StringPart;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpPostBodyUtil.class */
final class HttpPostBodyUtil {
    public static final int chunkSize = 8096;
    public static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_TEXT_CONTENT_TYPE = "text/plain";

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpPostBodyUtil$TransferEncodingMechanism.class */
    public enum TransferEncodingMechanism {
        BIT7("7bit"),
        BIT8(StringPart.DEFAULT_TRANSFER_ENCODING),
        BINARY("binary");

        private final String value;

        TransferEncodingMechanism(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.value;
        }
    }

    private HttpPostBodyUtil() {
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadOptimize.class */
    static class SeekAheadOptimize {
        byte[] bytes;
        int readerIndex;
        int pos;
        int origPos;
        int limit;
        ByteBuf buffer;

        SeekAheadOptimize(ByteBuf buffer) {
            if (!buffer.hasArray()) {
                throw new IllegalArgumentException("buffer hasn't backing byte array");
            }
            this.buffer = buffer;
            this.bytes = buffer.array();
            this.readerIndex = buffer.readerIndex();
            int iArrayOffset = buffer.arrayOffset() + this.readerIndex;
            this.pos = iArrayOffset;
            this.origPos = iArrayOffset;
            this.limit = buffer.arrayOffset() + buffer.writerIndex();
        }

        void setReadPosition(int minus) {
            this.pos -= minus;
            this.readerIndex = getReadPosition(this.pos);
            this.buffer.readerIndex(this.readerIndex);
        }

        int getReadPosition(int index) {
            return (index - this.origPos) + this.readerIndex;
        }
    }

    static int findNonWhitespace(String sb, int offset) {
        int result = offset;
        while (result < sb.length() && Character.isWhitespace(sb.charAt(result))) {
            result++;
        }
        return result;
    }

    static int findEndOfString(String sb) {
        int result = sb.length();
        while (result > 0 && Character.isWhitespace(sb.charAt(result - 1))) {
            result--;
        }
        return result;
    }
}

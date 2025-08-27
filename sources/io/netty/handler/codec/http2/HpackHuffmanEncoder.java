package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackHuffmanEncoder.class */
final class HpackHuffmanEncoder {
    private final int[] codes;
    private final byte[] lengths;
    private final EncodedLengthProcessor encodedLengthProcessor;
    private final EncodeProcessor encodeProcessor;

    HpackHuffmanEncoder() {
        this(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
    }

    private HpackHuffmanEncoder(int[] codes, byte[] lengths) {
        this.encodedLengthProcessor = new EncodedLengthProcessor();
        this.encodeProcessor = new EncodeProcessor();
        this.codes = codes;
        this.lengths = lengths;
    }

    public void encode(ByteBuf out, CharSequence data) {
        ObjectUtil.checkNotNull(out, "out");
        if (data instanceof AsciiString) {
            AsciiString string = (AsciiString) data;
            try {
                try {
                    this.encodeProcessor.out = out;
                    string.forEachByte(this.encodeProcessor);
                    this.encodeProcessor.end();
                    return;
                } catch (Exception e) {
                    PlatformDependent.throwException(e);
                    this.encodeProcessor.end();
                    return;
                }
            } catch (Throwable th) {
                this.encodeProcessor.end();
                throw th;
            }
        }
        encodeSlowPath(out, data);
    }

    private void encodeSlowPath(ByteBuf out, CharSequence data) {
        long current = 0;
        int n = 0;
        for (int i = 0; i < data.length(); i++) {
            int b = data.charAt(i) & 255;
            int code = this.codes[b];
            byte b2 = this.lengths[b];
            current = (current << b2) | code;
            n += b2;
            while (n >= 8) {
                n -= 8;
                out.writeByte((int) (current >> n));
            }
        }
        if (n > 0) {
            out.writeByte((int) ((current << (8 - n)) | (255 >>> n)));
        }
    }

    int getEncodedLength(CharSequence data) {
        if (data instanceof AsciiString) {
            AsciiString string = (AsciiString) data;
            try {
                this.encodedLengthProcessor.reset();
                string.forEachByte(this.encodedLengthProcessor);
                return this.encodedLengthProcessor.length();
            } catch (Exception e) {
                PlatformDependent.throwException(e);
                return -1;
            }
        }
        return getEncodedLengthSlowPath(data);
    }

    private int getEncodedLengthSlowPath(CharSequence data) {
        long len = 0;
        for (int i = 0; i < data.length(); i++) {
            len += this.lengths[data.charAt(i) & 255];
        }
        return (int) ((len + 7) >> 3);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor.class */
    private final class EncodeProcessor implements ByteProcessor {
        ByteBuf out;
        private long current;
        private int n;

        private EncodeProcessor() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            int b = value & 255;
            byte b2 = HpackHuffmanEncoder.this.lengths[b];
            this.current <<= b2;
            this.current |= HpackHuffmanEncoder.this.codes[b];
            this.n += b2;
            while (this.n >= 8) {
                this.n -= 8;
                this.out.writeByte((int) (this.current >> this.n));
            }
            return true;
        }

        void end() {
            try {
                if (this.n > 0) {
                    this.current <<= 8 - this.n;
                    this.current |= 255 >>> this.n;
                    this.out.writeByte((int) this.current);
                }
            } finally {
                this.out = null;
                this.current = 0L;
                this.n = 0;
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodedLengthProcessor.class */
    private final class EncodedLengthProcessor implements ByteProcessor {
        private long len;

        private EncodedLengthProcessor() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            this.len += HpackHuffmanEncoder.this.lengths[value & 255];
            return true;
        }

        void reset() {
            this.len = 0L;
        }

        int length() {
            return (int) ((this.len + 7) >> 3);
        }
    }
}

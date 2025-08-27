package io.netty.handler.codec.spdy;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.compression.CompressionException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeaderBlockJZlibEncoder.class */
class SpdyHeaderBlockJZlibEncoder extends SpdyHeaderBlockRawEncoder {
    private final Deflater z;
    private boolean finished;

    SpdyHeaderBlockJZlibEncoder(SpdyVersion version, int compressionLevel, int windowBits, int memLevel) {
        super(version);
        this.z = new Deflater();
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
        }
        int resultCode = this.z.deflateInit(compressionLevel, windowBits, memLevel, JZlib.W_ZLIB);
        if (resultCode != 0) {
            throw new CompressionException("failed to initialize an SPDY header block deflater: " + resultCode);
        }
        int resultCode2 = this.z.deflateSetDictionary(SpdyCodecUtil.SPDY_DICT, SpdyCodecUtil.SPDY_DICT.length);
        if (resultCode2 != 0) {
            throw new CompressionException("failed to set the SPDY dictionary: " + resultCode2);
        }
    }

    private void setInput(ByteBuf decompressed) {
        byte[] in;
        int offset;
        int len = decompressed.readableBytes();
        if (decompressed.hasArray()) {
            in = decompressed.array();
            offset = decompressed.arrayOffset() + decompressed.readerIndex();
        } else {
            in = new byte[len];
            decompressed.getBytes(decompressed.readerIndex(), in);
            offset = 0;
        }
        this.z.next_in = in;
        this.z.next_in_index = offset;
        this.z.avail_in = len;
    }

    private ByteBuf encode(ByteBufAllocator alloc) {
        ByteBuf out = null;
        try {
            int oldNextInIndex = this.z.next_in_index;
            int oldNextOutIndex = this.z.next_out_index;
            int maxOutputLength = ((int) Math.ceil(this.z.next_in.length * 1.001d)) + 12;
            out = alloc.heapBuffer(maxOutputLength);
            this.z.next_out = out.array();
            this.z.next_out_index = out.arrayOffset() + out.writerIndex();
            this.z.avail_out = maxOutputLength;
            try {
                int resultCode = this.z.deflate(2);
                out.skipBytes(this.z.next_in_index - oldNextInIndex);
                if (resultCode != 0) {
                    throw new CompressionException("compression failure: " + resultCode);
                }
                int outputLength = this.z.next_out_index - oldNextOutIndex;
                if (outputLength > 0) {
                    out.writerIndex(out.writerIndex() + outputLength);
                }
                this.z.next_in = null;
                this.z.next_out = null;
                if (0 != 0 && out != null) {
                    out.release();
                }
                return out;
            } catch (Throwable th) {
                out.skipBytes(this.z.next_in_index - oldNextInIndex);
                throw th;
            }
        } catch (Throwable th2) {
            this.z.next_in = null;
            this.z.next_out = null;
            if (1 != 0 && out != null) {
                out.release();
            }
            throw th2;
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder, io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder
    public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
        if (frame == null) {
            throw new IllegalArgumentException("frame");
        }
        if (this.finished) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf decompressed = super.encode(alloc, frame);
        try {
            if (!decompressed.isReadable()) {
                ByteBuf byteBuf = Unpooled.EMPTY_BUFFER;
                decompressed.release();
                return byteBuf;
            }
            setInput(decompressed);
            ByteBuf byteBufEncode = encode(alloc);
            decompressed.release();
            return byteBufEncode;
        } catch (Throwable th) {
            decompressed.release();
            throw th;
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder, io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.z.deflateEnd();
        this.z.next_in = null;
        this.z.next_out = null;
    }
}

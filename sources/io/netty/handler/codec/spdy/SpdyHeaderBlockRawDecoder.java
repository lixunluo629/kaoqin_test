package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeaderBlockRawDecoder.class */
public class SpdyHeaderBlockRawDecoder extends SpdyHeaderBlockDecoder {
    private static final int LENGTH_FIELD_SIZE = 4;
    private final int maxHeaderSize;
    private State state;
    private ByteBuf cumulation;
    private int headerSize;
    private int numHeaders;
    private int length;
    private String name;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeaderBlockRawDecoder$State.class */
    private enum State {
        READ_NUM_HEADERS,
        READ_NAME_LENGTH,
        READ_NAME,
        SKIP_NAME,
        READ_VALUE_LENGTH,
        READ_VALUE,
        SKIP_VALUE,
        END_HEADER_BLOCK,
        ERROR
    }

    public SpdyHeaderBlockRawDecoder(SpdyVersion spdyVersion, int maxHeaderSize) {
        ObjectUtil.checkNotNull(spdyVersion, "spdyVersion");
        this.maxHeaderSize = maxHeaderSize;
        this.state = State.READ_NUM_HEADERS;
    }

    private static int readLengthField(ByteBuf buffer) {
        int length = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
        buffer.skipBytes(4);
        return length;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    void decode(ByteBufAllocator alloc, ByteBuf headerBlock, SpdyHeadersFrame frame) throws Exception {
        ObjectUtil.checkNotNull(headerBlock, "headerBlock");
        ObjectUtil.checkNotNull(frame, "frame");
        if (this.cumulation == null) {
            decodeHeaderBlock(headerBlock, frame);
            if (headerBlock.isReadable()) {
                this.cumulation = alloc.buffer(headerBlock.readableBytes());
                this.cumulation.writeBytes(headerBlock);
                return;
            }
            return;
        }
        this.cumulation.writeBytes(headerBlock);
        decodeHeaderBlock(this.cumulation, frame);
        if (this.cumulation.isReadable()) {
            this.cumulation.discardReadBytes();
        } else {
            releaseBuffer();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x0323 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0000 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decodeHeaderBlock(io.netty.buffer.ByteBuf r8, io.netty.handler.codec.spdy.SpdyHeadersFrame r9) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 944
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.spdy.SpdyHeaderBlockRawDecoder.decodeHeaderBlock(io.netty.buffer.ByteBuf, io.netty.handler.codec.spdy.SpdyHeadersFrame):void");
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    void endHeaderBlock(SpdyHeadersFrame frame) throws Exception {
        if (this.state != State.END_HEADER_BLOCK) {
            frame.setInvalid();
        }
        releaseBuffer();
        this.headerSize = 0;
        this.name = null;
        this.state = State.READ_NUM_HEADERS;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    void end() {
        releaseBuffer();
    }

    private void releaseBuffer() {
        if (this.cumulation != null) {
            this.cumulation.release();
            this.cumulation = null;
        }
    }
}

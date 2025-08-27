package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2Decoder.class */
public class Bzip2Decoder extends ByteToMessageDecoder {
    private State currentState = State.INIT;
    private final Bzip2BitReader reader = new Bzip2BitReader();
    private Bzip2BlockDecompressor blockDecompressor;
    private Bzip2HuffmanStageDecoder huffmanStageDecoder;
    private int blockSize;
    private int blockCRC;
    private int streamCRC;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2Decoder$State.class */
    private enum State {
        INIT,
        INIT_BLOCK,
        INIT_BLOCK_PARAMS,
        RECEIVE_HUFFMAN_USED_MAP,
        RECEIVE_HUFFMAN_USED_BITMAPS,
        RECEIVE_SELECTORS_NUMBER,
        RECEIVE_SELECTORS,
        RECEIVE_HUFFMAN_LENGTH,
        DECODE_HUFFMAN_DATA,
        EOF
    }

    /* JADX WARN: Code restructure failed: missing block: B:130:0x03a4, code lost:
    
        r0[r33][r35] = (byte) r34;
        r35 = r35 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0093, code lost:
    
        throw new io.netty.handler.codec.compression.DecompressionException("block size is invalid");
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x010f, code lost:
    
        throw new io.netty.handler.codec.compression.DecompressionException("bad block header");
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x021c, code lost:
    
        throw new io.netty.handler.codec.compression.DecompressionException("incorrect huffman groups number");
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0278, code lost:
    
        throw new io.netty.handler.codec.compression.DecompressionException("incorrect selectors number");
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0415  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x012a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0165 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x01a5 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0257 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x03cf A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0414 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x03ca A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0258  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02b2  */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r10, io.netty.buffer.ByteBuf r11, java.util.List<java.lang.Object> r12) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 1211
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2Decoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    public boolean isClosed() {
        return this.currentState == State.EOF;
    }
}

package io.netty.handler.codec.compression;

import com.ning.compress.BufferRecycler;
import com.ning.compress.lzf.ChunkDecoder;
import com.ning.compress.lzf.util.ChunkDecoderFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/LzfDecoder.class */
public class LzfDecoder extends ByteToMessageDecoder {
    private State currentState;
    private static final short MAGIC_NUMBER = 23126;
    private ChunkDecoder decoder;
    private BufferRecycler recycler;
    private int chunkLength;
    private int originalLength;
    private boolean isCompressed;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/LzfDecoder$State.class */
    private enum State {
        INIT_BLOCK,
        INIT_ORIGINAL_LENGTH,
        DECOMPRESS_DATA,
        CORRUPTED
    }

    public LzfDecoder() {
        this(false);
    }

    public LzfDecoder(boolean safeInstance) {
        ChunkDecoder chunkDecoderOptimalInstance;
        this.currentState = State.INIT_BLOCK;
        if (safeInstance) {
            chunkDecoderOptimalInstance = ChunkDecoderFactory.safeInstance();
        } else {
            chunkDecoderOptimalInstance = ChunkDecoderFactory.optimalInstance();
        }
        this.decoder = chunkDecoderOptimalInstance;
        this.recycler = BufferRecycler.instance();
    }

    /* JADX WARN: Finally extract failed */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] inputArray;
        int inPos;
        try {
            switch (this.currentState) {
                case INIT_BLOCK:
                    if (in.readableBytes() >= 5) {
                        int magic = in.readUnsignedShort();
                        if (magic != MAGIC_NUMBER) {
                            throw new DecompressionException("unexpected block identifier");
                        }
                        int type = in.readByte();
                        switch (type) {
                            case 0:
                                this.isCompressed = false;
                                this.currentState = State.DECOMPRESS_DATA;
                                break;
                            case 1:
                                this.isCompressed = true;
                                this.currentState = State.INIT_ORIGINAL_LENGTH;
                                break;
                            default:
                                throw new DecompressionException(String.format("unknown type of chunk: %d (expected: %d or %d)", Integer.valueOf(type), 0, 1));
                        }
                        this.chunkLength = in.readUnsignedShort();
                        if (type != 1) {
                        }
                    }
                    return;
                case INIT_ORIGINAL_LENGTH:
                    if (in.readableBytes() >= 2) {
                        this.originalLength = in.readUnsignedShort();
                        this.currentState = State.DECOMPRESS_DATA;
                    } else {
                        return;
                    }
                case DECOMPRESS_DATA:
                    int chunkLength = this.chunkLength;
                    if (in.readableBytes() >= chunkLength) {
                        int originalLength = this.originalLength;
                        if (this.isCompressed) {
                            int idx = in.readerIndex();
                            if (in.hasArray()) {
                                inputArray = in.array();
                                inPos = in.arrayOffset() + idx;
                            } else {
                                inputArray = this.recycler.allocInputBuffer(chunkLength);
                                in.getBytes(idx, inputArray, 0, chunkLength);
                                inPos = 0;
                            }
                            ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
                            byte[] outputArray = uncompressed.array();
                            int outPos = uncompressed.arrayOffset() + uncompressed.writerIndex();
                            boolean success = false;
                            try {
                                this.decoder.decodeChunk(inputArray, inPos, outputArray, outPos, outPos + originalLength);
                                uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
                                out.add(uncompressed);
                                in.skipBytes(chunkLength);
                                success = true;
                                if (1 == 0) {
                                    uncompressed.release();
                                }
                                if (!in.hasArray()) {
                                    this.recycler.releaseInputBuffer(inputArray);
                                }
                            } catch (Throwable th) {
                                if (!success) {
                                    uncompressed.release();
                                }
                                throw th;
                            }
                        } else if (chunkLength > 0) {
                            out.add(in.readRetainedSlice(chunkLength));
                        }
                        this.currentState = State.INIT_BLOCK;
                    }
                    return;
                case CORRUPTED:
                    in.skipBytes(in.readableBytes());
                    return;
                default:
                    throw new IllegalStateException();
            }
        } catch (Exception e) {
            this.currentState = State.CORRUPTED;
            this.decoder = null;
            this.recycler = null;
            throw e;
        }
    }
}

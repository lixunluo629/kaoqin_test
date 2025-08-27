package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.EmptyArrays;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/FastLzFrameDecoder.class */
public class FastLzFrameDecoder extends ByteToMessageDecoder {
    private State currentState;
    private final Checksum checksum;
    private int chunkLength;
    private int originalLength;
    private boolean isCompressed;
    private boolean hasChecksum;
    private int currentChecksum;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/FastLzFrameDecoder$State.class */
    private enum State {
        INIT_BLOCK,
        INIT_BLOCK_PARAMS,
        DECOMPRESS_DATA,
        CORRUPTED
    }

    public FastLzFrameDecoder() {
        this(false);
    }

    public FastLzFrameDecoder(boolean validateChecksums) {
        this(validateChecksums ? new Adler32() : null);
    }

    public FastLzFrameDecoder(Checksum checksum) {
        this.currentState = State.INIT_BLOCK;
        this.checksum = checksum;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf uncompressed;
        byte[] output;
        int outputPtr;
        byte[] input;
        int inputPtr;
        try {
            switch (this.currentState) {
                case INIT_BLOCK:
                    if (in.readableBytes() >= 4) {
                        int magic = in.readUnsignedMedium();
                        if (magic != 4607066) {
                            throw new DecompressionException("unexpected block identifier");
                        }
                        byte options = in.readByte();
                        this.isCompressed = (options & 1) == 1;
                        this.hasChecksum = (options & 16) == 16;
                        this.currentState = State.INIT_BLOCK_PARAMS;
                    } else {
                        return;
                    }
                case INIT_BLOCK_PARAMS:
                    if (in.readableBytes() >= 2 + (this.isCompressed ? 2 : 0) + (this.hasChecksum ? 4 : 0)) {
                        this.currentChecksum = this.hasChecksum ? in.readInt() : 0;
                        this.chunkLength = in.readUnsignedShort();
                        this.originalLength = this.isCompressed ? in.readUnsignedShort() : this.chunkLength;
                        this.currentState = State.DECOMPRESS_DATA;
                    } else {
                        return;
                    }
                case DECOMPRESS_DATA:
                    int chunkLength = this.chunkLength;
                    if (in.readableBytes() >= chunkLength) {
                        int idx = in.readerIndex();
                        int originalLength = this.originalLength;
                        if (originalLength != 0) {
                            uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
                            output = uncompressed.array();
                            outputPtr = uncompressed.arrayOffset() + uncompressed.writerIndex();
                        } else {
                            uncompressed = null;
                            output = EmptyArrays.EMPTY_BYTES;
                            outputPtr = 0;
                        }
                        try {
                            if (this.isCompressed) {
                                if (in.hasArray()) {
                                    input = in.array();
                                    inputPtr = in.arrayOffset() + idx;
                                } else {
                                    input = new byte[chunkLength];
                                    in.getBytes(idx, input);
                                    inputPtr = 0;
                                }
                                int decompressedBytes = FastLz.decompress(input, inputPtr, chunkLength, output, outputPtr, originalLength);
                                if (originalLength != decompressedBytes) {
                                    throw new DecompressionException(String.format("stream corrupted: originalLength(%d) and actual length(%d) mismatch", Integer.valueOf(originalLength), Integer.valueOf(decompressedBytes)));
                                }
                            } else {
                                in.getBytes(idx, output, outputPtr, chunkLength);
                            }
                            Checksum checksum = this.checksum;
                            if (this.hasChecksum && checksum != null) {
                                checksum.reset();
                                checksum.update(output, outputPtr, originalLength);
                                int checksumResult = (int) checksum.getValue();
                                if (checksumResult != this.currentChecksum) {
                                    throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", Integer.valueOf(checksumResult), Integer.valueOf(this.currentChecksum)));
                                }
                            }
                            if (uncompressed != null) {
                                uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
                                out.add(uncompressed);
                            }
                            in.skipBytes(chunkLength);
                            this.currentState = State.INIT_BLOCK;
                            if (1 == 0 && uncompressed != null) {
                                uncompressed.release();
                            }
                        } catch (Throwable th) {
                            if (0 == 0 && uncompressed != null) {
                                uncompressed.release();
                            }
                            throw th;
                        }
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
            throw e;
        }
    }
}

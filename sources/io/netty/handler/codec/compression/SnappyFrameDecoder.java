package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/SnappyFrameDecoder.class */
public class SnappyFrameDecoder extends ByteToMessageDecoder {
    private static final int SNAPPY_IDENTIFIER_LEN = 6;
    private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
    private final Snappy snappy;
    private final boolean validateChecksums;
    private boolean started;
    private boolean corrupted;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/SnappyFrameDecoder$ChunkType.class */
    private enum ChunkType {
        STREAM_IDENTIFIER,
        COMPRESSED_DATA,
        UNCOMPRESSED_DATA,
        RESERVED_UNSKIPPABLE,
        RESERVED_SKIPPABLE
    }

    public SnappyFrameDecoder() {
        this(false);
    }

    public SnappyFrameDecoder(boolean validateChecksums) {
        this.snappy = new Snappy();
        this.validateChecksums = validateChecksums;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.corrupted) {
            in.skipBytes(in.readableBytes());
            return;
        }
        try {
            int idx = in.readerIndex();
            int inSize = in.readableBytes();
            if (inSize < 4) {
                return;
            }
            int chunkTypeVal = in.getUnsignedByte(idx);
            ChunkType chunkType = mapChunkType((byte) chunkTypeVal);
            int chunkLength = in.getUnsignedMediumLE(idx + 1);
            switch (chunkType) {
                case STREAM_IDENTIFIER:
                    if (chunkLength != 6) {
                        throw new DecompressionException("Unexpected length of stream identifier: " + chunkLength);
                    }
                    if (inSize >= 10) {
                        in.skipBytes(4);
                        int offset = in.readerIndex();
                        in.skipBytes(6);
                        int offset2 = offset + 1;
                        checkByte(in.getByte(offset), (byte) 115);
                        int offset3 = offset2 + 1;
                        checkByte(in.getByte(offset2), (byte) 78);
                        int offset4 = offset3 + 1;
                        checkByte(in.getByte(offset3), (byte) 97);
                        int offset5 = offset4 + 1;
                        checkByte(in.getByte(offset4), (byte) 80);
                        checkByte(in.getByte(offset5), (byte) 112);
                        checkByte(in.getByte(offset5 + 1), (byte) 89);
                        this.started = true;
                    }
                    return;
                case RESERVED_SKIPPABLE:
                    if (!this.started) {
                        throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4 + chunkLength);
                    return;
                case RESERVED_UNSKIPPABLE:
                    throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(chunkTypeVal));
                case UNCOMPRESSED_DATA:
                    if (!this.started) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (chunkLength > MAX_UNCOMPRESSED_DATA_SIZE) {
                        throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4);
                    if (this.validateChecksums) {
                        int checksum = in.readIntLE();
                        Snappy.validateChecksum(checksum, in, in.readerIndex(), chunkLength - 4);
                    } else {
                        in.skipBytes(4);
                    }
                    out.add(in.readRetainedSlice(chunkLength - 4));
                    return;
                case COMPRESSED_DATA:
                    if (!this.started) {
                        throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                    }
                    if (inSize < 4 + chunkLength) {
                        return;
                    }
                    in.skipBytes(4);
                    int checksum2 = in.readIntLE();
                    ByteBuf uncompressed = ctx.alloc().buffer();
                    try {
                        if (this.validateChecksums) {
                            int oldWriterIndex = in.writerIndex();
                            try {
                                in.writerIndex((in.readerIndex() + chunkLength) - 4);
                                this.snappy.decode(in, uncompressed);
                                in.writerIndex(oldWriterIndex);
                                Snappy.validateChecksum(checksum2, uncompressed, 0, uncompressed.writerIndex());
                            } catch (Throwable th) {
                                in.writerIndex(oldWriterIndex);
                                throw th;
                            }
                        } else {
                            this.snappy.decode(in.readSlice(chunkLength - 4), uncompressed);
                        }
                        out.add(uncompressed);
                        ByteBuf uncompressed2 = null;
                        if (0 != 0) {
                            uncompressed2.release();
                        }
                        this.snappy.reset();
                        return;
                    } catch (Throwable th2) {
                        if (uncompressed != null) {
                            uncompressed.release();
                        }
                        throw th2;
                    }
                default:
                    return;
            }
        } catch (Exception e) {
            this.corrupted = true;
            throw e;
        }
    }

    private static void checkByte(byte actual, byte expect) {
        if (actual != expect) {
            throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
        }
    }

    private static ChunkType mapChunkType(byte type) {
        if (type == 0) {
            return ChunkType.COMPRESSED_DATA;
        }
        if (type == 1) {
            return ChunkType.UNCOMPRESSED_DATA;
        }
        if (type == -1) {
            return ChunkType.STREAM_IDENTIFIER;
        }
        if ((type & 128) == 128) {
            return ChunkType.RESERVED_SKIPPABLE;
        }
        return ChunkType.RESERVED_UNSKIPPABLE;
    }
}

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import java.util.zip.Checksum;
import net.jpountz.lz4.LZ4Exception;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.apache.naming.factory.Constants;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Lz4FrameDecoder.class */
public class Lz4FrameDecoder extends ByteToMessageDecoder {
    private State currentState;
    private LZ4FastDecompressor decompressor;
    private ByteBufChecksum checksum;
    private int blockType;
    private int compressedLength;
    private int decompressedLength;
    private int currentChecksum;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Lz4FrameDecoder$State.class */
    private enum State {
        INIT_BLOCK,
        DECOMPRESS_DATA,
        FINISHED,
        CORRUPTED
    }

    public Lz4FrameDecoder() {
        this(false);
    }

    public Lz4FrameDecoder(boolean validateChecksums) {
        this(LZ4Factory.fastestInstance(), validateChecksums);
    }

    public Lz4FrameDecoder(LZ4Factory factory, boolean validateChecksums) {
        this(factory, validateChecksums ? new Lz4XXHash32(-1756908916) : null);
    }

    public Lz4FrameDecoder(LZ4Factory factory, Checksum checksum) {
        this.currentState = State.INIT_BLOCK;
        this.decompressor = ((LZ4Factory) ObjectUtil.checkNotNull(factory, Constants.FACTORY)).fastDecompressor();
        this.checksum = checksum == null ? null : ByteBufChecksum.wrapChecksum(checksum);
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r22v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 22, insn: 0x026d: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r22 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('uncompressed' io.netty.buffer.ByteBuf)]) A[TRY_LEAVE], block:B:67:0x026d */
    /* JADX WARN: Type inference failed for: r22v0, names: [uncompressed], types: [io.netty.buffer.ByteBuf] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ?? r22;
        ByteBuf byteBufBuffer;
        try {
            switch (this.currentState) {
                case INIT_BLOCK:
                    if (in.readableBytes() >= 21) {
                        if (in.readLong() != 5501767354678207339L) {
                            throw new DecompressionException("unexpected block identifier");
                        }
                        byte b = in.readByte();
                        int i = (b & 15) + 10;
                        int i2 = b & 240;
                        int iReverseBytes = Integer.reverseBytes(in.readInt());
                        if (iReverseBytes < 0 || iReverseBytes > 33554432) {
                            throw new DecompressionException(String.format("invalid compressedLength: %d (expected: 0-%d)", Integer.valueOf(iReverseBytes), 33554432));
                        }
                        int iReverseBytes2 = Integer.reverseBytes(in.readInt());
                        int i3 = 1 << i;
                        if (iReverseBytes2 < 0 || iReverseBytes2 > i3) {
                            throw new DecompressionException(String.format("invalid decompressedLength: %d (expected: 0-%d)", Integer.valueOf(iReverseBytes2), Integer.valueOf(i3)));
                        }
                        if ((iReverseBytes2 == 0 && iReverseBytes != 0) || ((iReverseBytes2 != 0 && iReverseBytes == 0) || (i2 == 16 && iReverseBytes2 != iReverseBytes))) {
                            throw new DecompressionException(String.format("stream corrupted: compressedLength(%d) and decompressedLength(%d) mismatch", Integer.valueOf(iReverseBytes), Integer.valueOf(iReverseBytes2)));
                        }
                        int iReverseBytes3 = Integer.reverseBytes(in.readInt());
                        if (iReverseBytes2 == 0 && iReverseBytes == 0) {
                            if (iReverseBytes3 != 0) {
                                throw new DecompressionException("stream corrupted: checksum error");
                            }
                            this.currentState = State.FINISHED;
                            this.decompressor = null;
                            this.checksum = null;
                        } else {
                            this.blockType = i2;
                            this.compressedLength = iReverseBytes;
                            this.decompressedLength = iReverseBytes2;
                            this.currentChecksum = iReverseBytes3;
                            this.currentState = State.DECOMPRESS_DATA;
                        }
                    }
                    return;
                case DECOMPRESS_DATA:
                    int blockType = this.blockType;
                    int compressedLength = this.compressedLength;
                    int decompressedLength = this.decompressedLength;
                    int currentChecksum = this.currentChecksum;
                    if (in.readableBytes() >= compressedLength) {
                        try {
                            ByteBufChecksum byteBufChecksum = this.checksum;
                            try {
                                switch (blockType) {
                                    case 16:
                                        byteBufBuffer = in.retainedSlice(in.readerIndex(), decompressedLength);
                                        break;
                                    case 32:
                                        byteBufBuffer = ctx.alloc().buffer(decompressedLength, decompressedLength);
                                        this.decompressor.decompress(CompressionUtil.safeNioBuffer(in), byteBufBuffer.internalNioBuffer(byteBufBuffer.writerIndex(), decompressedLength));
                                        byteBufBuffer.writerIndex(byteBufBuffer.writerIndex() + decompressedLength);
                                        break;
                                    default:
                                        throw new DecompressionException(String.format("unexpected blockType: %d (expected: %d or %d)", Integer.valueOf(blockType), 16, 32));
                                }
                                in.skipBytes(compressedLength);
                                if (byteBufChecksum != null) {
                                    CompressionUtil.checkChecksum(byteBufChecksum, byteBufBuffer, currentChecksum);
                                }
                                out.add(byteBufBuffer);
                                ReferenceCounted referenceCounted = null;
                                this.currentState = State.INIT_BLOCK;
                                if (0 != 0) {
                                    referenceCounted.release();
                                }
                            } catch (LZ4Exception e) {
                                throw new DecompressionException((Throwable) e);
                            }
                        } catch (Throwable th) {
                            if (r22 != 0) {
                                r22.release();
                            }
                            throw th;
                        }
                    }
                    return;
                case FINISHED:
                case CORRUPTED:
                    in.skipBytes(in.readableBytes());
                    return;
                default:
                    throw new IllegalStateException();
            }
        } catch (Exception e2) {
            this.currentState = State.CORRUPTED;
            throw e2;
        }
    }

    public boolean isClosed() {
        return this.currentState == State.FINISHED;
    }
}

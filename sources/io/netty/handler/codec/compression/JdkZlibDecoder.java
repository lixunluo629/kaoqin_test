package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/JdkZlibDecoder.class */
public class JdkZlibDecoder extends ZlibDecoder {
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private static final int FRESERVED = 224;
    private Inflater inflater;
    private final byte[] dictionary;
    private final ByteBufChecksum crc;
    private final boolean decompressConcatenated;
    private GzipState gzipState;
    private int flags;
    private int xlen;
    private volatile boolean finished;
    private boolean decideZlibOrNone;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/JdkZlibDecoder$GzipState.class */
    private enum GzipState {
        HEADER_START,
        HEADER_END,
        FLG_READ,
        XLEN_READ,
        SKIP_FNAME,
        SKIP_COMMENT,
        PROCESS_FHCRC,
        FOOTER_START
    }

    public JdkZlibDecoder() {
        this(ZlibWrapper.ZLIB, null, false, 0);
    }

    public JdkZlibDecoder(int maxAllocation) {
        this(ZlibWrapper.ZLIB, null, false, maxAllocation);
    }

    public JdkZlibDecoder(byte[] dictionary) {
        this(ZlibWrapper.ZLIB, dictionary, false, 0);
    }

    public JdkZlibDecoder(byte[] dictionary, int maxAllocation) {
        this(ZlibWrapper.ZLIB, dictionary, false, maxAllocation);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper) {
        this(wrapper, null, false, 0);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, int maxAllocation) {
        this(wrapper, null, false, maxAllocation);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, boolean decompressConcatenated) {
        this(wrapper, null, decompressConcatenated, 0);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, boolean decompressConcatenated, int maxAllocation) {
        this(wrapper, null, decompressConcatenated, maxAllocation);
    }

    public JdkZlibDecoder(boolean decompressConcatenated) {
        this(ZlibWrapper.GZIP, null, decompressConcatenated, 0);
    }

    public JdkZlibDecoder(boolean decompressConcatenated, int maxAllocation) {
        this(ZlibWrapper.GZIP, null, decompressConcatenated, maxAllocation);
    }

    private JdkZlibDecoder(ZlibWrapper wrapper, byte[] dictionary, boolean decompressConcatenated, int maxAllocation) {
        super(maxAllocation);
        this.gzipState = GzipState.HEADER_START;
        this.flags = -1;
        this.xlen = -1;
        ObjectUtil.checkNotNull(wrapper, "wrapper");
        this.decompressConcatenated = decompressConcatenated;
        switch (wrapper) {
            case GZIP:
                this.inflater = new Inflater(true);
                this.crc = ByteBufChecksum.wrapChecksum(new CRC32());
                break;
            case NONE:
                this.inflater = new Inflater(true);
                this.crc = null;
                break;
            case ZLIB:
                this.inflater = new Inflater();
                this.crc = null;
                break;
            case ZLIB_OR_NONE:
                this.decideZlibOrNone = true;
                this.crc = null;
                break;
            default:
                throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + wrapper);
        }
        this.dictionary = dictionary;
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    public boolean isClosed() {
        return this.finished;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.finished) {
            in.skipBytes(in.readableBytes());
            return;
        }
        int readableBytes = in.readableBytes();
        if (readableBytes == 0) {
            return;
        }
        if (this.decideZlibOrNone) {
            if (readableBytes < 2) {
                return;
            }
            boolean nowrap = !looksLikeZlib(in.getShort(in.readerIndex()));
            this.inflater = new Inflater(nowrap);
            this.decideZlibOrNone = false;
        }
        if (this.crc != null) {
            switch (this.gzipState) {
                case FOOTER_START:
                    if (readGZIPFooter(in)) {
                        this.finished = true;
                        return;
                    }
                    return;
                default:
                    if (this.gzipState != GzipState.HEADER_END && !readGZIPHeader(in)) {
                        return;
                    }
                    readableBytes = in.readableBytes();
                    break;
            }
        }
        if (in.hasArray()) {
            this.inflater.setInput(in.array(), in.arrayOffset() + in.readerIndex(), readableBytes);
        } else {
            byte[] array = new byte[readableBytes];
            in.getBytes(in.readerIndex(), array);
            this.inflater.setInput(array);
        }
        ByteBuf decompressed = prepareDecompressBuffer(ctx, null, this.inflater.getRemaining() << 1);
        boolean readFooter = false;
        while (true) {
            try {
                try {
                    if (!this.inflater.needsInput()) {
                        byte[] outArray = decompressed.array();
                        int writerIndex = decompressed.writerIndex();
                        int outIndex = decompressed.arrayOffset() + writerIndex;
                        int outputLength = this.inflater.inflate(outArray, outIndex, decompressed.writableBytes());
                        if (outputLength > 0) {
                            decompressed.writerIndex(writerIndex + outputLength);
                            if (this.crc != null) {
                                this.crc.update(outArray, outIndex, outputLength);
                            }
                        } else if (this.inflater.needsDictionary()) {
                            if (this.dictionary == null) {
                                throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
                            }
                            this.inflater.setDictionary(this.dictionary);
                        }
                        if (this.inflater.finished()) {
                            if (this.crc == null) {
                                this.finished = true;
                            } else {
                                readFooter = true;
                            }
                        } else {
                            decompressed = prepareDecompressBuffer(ctx, decompressed, this.inflater.getRemaining() << 1);
                        }
                    }
                } catch (DataFormatException e) {
                    throw new DecompressionException("decompression failure", e);
                }
            } finally {
                if (decompressed.isReadable()) {
                    out.add(decompressed);
                } else {
                    decompressed.release();
                }
            }
        }
        in.skipBytes(readableBytes - this.inflater.getRemaining());
        if (readFooter) {
            this.gzipState = GzipState.FOOTER_START;
            if (readGZIPFooter(in)) {
                this.finished = !this.decompressConcatenated;
                if (!this.finished) {
                    this.inflater.reset();
                    this.crc.reset();
                    this.gzipState = GzipState.HEADER_START;
                }
            }
        }
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    protected void decompressionBufferExhausted(ByteBuf buffer) {
        this.finished = true;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved0(ctx);
        if (this.inflater != null) {
            this.inflater.end();
        }
    }

    private boolean readGZIPHeader(ByteBuf in) {
        switch (this.gzipState) {
            case HEADER_START:
                if (in.readableBytes() < 10) {
                    return false;
                }
                int magic0 = in.readByte();
                int magic1 = in.readByte();
                if (magic0 != 31) {
                    throw new DecompressionException("Input is not in the GZIP format");
                }
                this.crc.update(magic0);
                this.crc.update(magic1);
                int method = in.readUnsignedByte();
                if (method != 8) {
                    throw new DecompressionException("Unsupported compression method " + method + " in the GZIP header");
                }
                this.crc.update(method);
                this.flags = in.readUnsignedByte();
                this.crc.update(this.flags);
                if ((this.flags & 224) != 0) {
                    throw new DecompressionException("Reserved flags are set in the GZIP header");
                }
                this.crc.update(in, in.readerIndex(), 4);
                in.skipBytes(4);
                this.crc.update(in.readUnsignedByte());
                this.crc.update(in.readUnsignedByte());
                this.gzipState = GzipState.FLG_READ;
            case FLG_READ:
                if ((this.flags & 4) != 0) {
                    if (in.readableBytes() < 2) {
                        return false;
                    }
                    int xlen1 = in.readUnsignedByte();
                    int xlen2 = in.readUnsignedByte();
                    this.crc.update(xlen1);
                    this.crc.update(xlen2);
                    this.xlen |= (xlen1 << 8) | xlen2;
                }
                this.gzipState = GzipState.XLEN_READ;
            case XLEN_READ:
                if (this.xlen != -1) {
                    if (in.readableBytes() < this.xlen) {
                        return false;
                    }
                    this.crc.update(in, in.readerIndex(), this.xlen);
                    in.skipBytes(this.xlen);
                }
                this.gzipState = GzipState.SKIP_FNAME;
            case SKIP_FNAME:
                if ((this.flags & 8) != 0) {
                    if (!in.isReadable()) {
                        return false;
                    }
                    do {
                        int b = in.readUnsignedByte();
                        this.crc.update(b);
                        if (b == 0) {
                        }
                    } while (in.isReadable());
                }
                this.gzipState = GzipState.SKIP_COMMENT;
            case SKIP_COMMENT:
                if ((this.flags & 16) != 0) {
                    if (!in.isReadable()) {
                        return false;
                    }
                    do {
                        int b2 = in.readUnsignedByte();
                        this.crc.update(b2);
                        if (b2 == 0) {
                        }
                    } while (in.isReadable());
                }
                this.gzipState = GzipState.PROCESS_FHCRC;
            case PROCESS_FHCRC:
                if ((this.flags & 2) != 0) {
                    if (in.readableBytes() < 4) {
                        return false;
                    }
                    verifyCrc(in);
                }
                this.crc.reset();
                this.gzipState = GzipState.HEADER_END;
                return true;
            case HEADER_END:
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    private boolean readGZIPFooter(ByteBuf buf) {
        if (buf.readableBytes() < 8) {
            return false;
        }
        verifyCrc(buf);
        int dataLength = 0;
        for (int i = 0; i < 4; i++) {
            dataLength |= buf.readUnsignedByte() << (i * 8);
        }
        int readLength = this.inflater.getTotalOut();
        if (dataLength != readLength) {
            throw new DecompressionException("Number of bytes mismatch. Expected: " + dataLength + ", Got: " + readLength);
        }
        return true;
    }

    private void verifyCrc(ByteBuf in) {
        long crcValue = 0;
        for (int i = 0; i < 4; i++) {
            crcValue |= in.readUnsignedByte() << (i * 8);
        }
        long readCrc = this.crc.getValue();
        if (crcValue != readCrc) {
            throw new DecompressionException("CRC value mismatch. Expected: " + crcValue + ", Got: " + readCrc);
        }
    }

    private static boolean looksLikeZlib(short cmf_flg) {
        return (cmf_flg & 30720) == 30720 && cmf_flg % 31 == 0;
    }
}

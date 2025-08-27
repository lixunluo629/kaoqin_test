package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.Http2FrameWriter;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.collection.CharObjectMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/DefaultHttp2FrameWriter.class */
public class DefaultHttp2FrameWriter implements Http2FrameWriter, Http2FrameSizePolicy, Http2FrameWriter.Configuration {
    private static final String STREAM_ID = "Stream ID";
    private static final String STREAM_DEPENDENCY = "Stream Dependency";
    private static final ByteBuf ZERO_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(255).writeZero(255)).asReadOnly();
    private final Http2HeadersEncoder headersEncoder;
    private int maxFrameSize;

    public DefaultHttp2FrameWriter() {
        this(new DefaultHttp2HeadersEncoder());
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector));
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector, boolean ignoreMaxHeaderListSize) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector, ignoreMaxHeaderListSize));
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder headersEncoder) {
        this.headersEncoder = headersEncoder;
        this.maxFrameSize = 16384;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public Http2FrameWriter.Configuration configuration() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter.Configuration
    public Http2HeadersEncoder.Configuration headersConfiguration() {
        return this.headersEncoder.configuration();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter.Configuration
    public Http2FrameSizePolicy frameSizePolicy() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public void maxFrameSize(int max) throws Http2Exception {
        if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", Integer.valueOf(max));
        }
        this.maxFrameSize = max;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public int maxFrameSize() {
        return this.maxFrameSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // io.netty.handler.codec.http2.Http2DataWriter
    public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise) {
        ByteBuf lastFrame;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        ByteBuf frameHeader = null;
        try {
            verifyStreamId(streamId, STREAM_ID);
            Http2CodecUtil.verifyPadding(padding);
            int remainingData = data.readableBytes();
            Http2Flags flags = new Http2Flags();
            flags.endOfStream(false);
            flags.paddingPresent(false);
            if (remainingData > this.maxFrameSize) {
                frameHeader = ctx.alloc().buffer(9);
                Http2CodecUtil.writeFrameHeaderInternal(frameHeader, this.maxFrameSize, (byte) 0, flags, streamId);
                do {
                    ctx.write(frameHeader.retainedSlice(), promiseAggregator.newPromise());
                    ctx.write(data.readRetainedSlice(this.maxFrameSize), promiseAggregator.newPromise());
                    remainingData -= this.maxFrameSize;
                } while (remainingData > this.maxFrameSize);
            }
            if (padding == 0) {
                if (frameHeader != null) {
                    frameHeader.release();
                }
                ByteBuf frameHeader2 = ctx.alloc().buffer(9);
                flags.endOfStream(endStream);
                Http2CodecUtil.writeFrameHeaderInternal(frameHeader2, remainingData, (byte) 0, flags, streamId);
                ctx.write(frameHeader2, promiseAggregator.newPromise());
                ByteBuf lastFrame2 = data.readSlice(remainingData);
                ctx.write(lastFrame2, promiseAggregator.newPromise());
            } else {
                if (remainingData != this.maxFrameSize) {
                    if (frameHeader != null) {
                        frameHeader.release();
                    }
                } else {
                    remainingData -= this.maxFrameSize;
                    if (frameHeader == null) {
                        lastFrame = ctx.alloc().buffer(9);
                        Http2CodecUtil.writeFrameHeaderInternal(lastFrame, this.maxFrameSize, (byte) 0, flags, streamId);
                    } else {
                        lastFrame = frameHeader.slice();
                    }
                    ctx.write(lastFrame, promiseAggregator.newPromise());
                    ByteBuf lastFrame3 = data.readableBytes() != this.maxFrameSize ? data.readSlice(this.maxFrameSize) : data;
                    data = null;
                    ctx.write(lastFrame3, promiseAggregator.newPromise());
                }
                while (true) {
                    int frameDataBytes = Math.min(remainingData, this.maxFrameSize);
                    int framePaddingBytes = Math.min(padding, Math.max(0, (this.maxFrameSize - 1) - frameDataBytes));
                    padding -= framePaddingBytes;
                    remainingData -= frameDataBytes;
                    ByteBuf frameHeader22 = ctx.alloc().buffer(10);
                    flags.endOfStream(endStream && remainingData == 0 && padding == 0);
                    flags.paddingPresent(framePaddingBytes > 0);
                    Http2CodecUtil.writeFrameHeaderInternal(frameHeader22, framePaddingBytes + frameDataBytes, (byte) 0, flags, streamId);
                    writePaddingLength(frameHeader22, framePaddingBytes);
                    ctx.write(frameHeader22, promiseAggregator.newPromise());
                    if (frameDataBytes != 0) {
                        if (remainingData == 0) {
                            ByteBuf lastFrame4 = data.readSlice(frameDataBytes);
                            data = null;
                            ctx.write(lastFrame4, promiseAggregator.newPromise());
                        } else {
                            ctx.write(data.readRetainedSlice(frameDataBytes), promiseAggregator.newPromise());
                        }
                    }
                    if (paddingBytes(framePaddingBytes) > 0) {
                        ctx.write(ZERO_BUFFER.slice(0, paddingBytes(framePaddingBytes)), promiseAggregator.newPromise());
                    }
                    if (remainingData == 0 && padding == 0) {
                        break;
                    }
                }
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable cause) {
            if (0 != 0) {
                frameHeader.release();
            }
            if (data != null) {
                try {
                    data.release();
                } finally {
                    promiseAggregator.setFailure(cause);
                    promiseAggregator.doneAllocatingPromises();
                }
            }
            return promiseAggregator;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short) 0, false, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
        try {
            verifyStreamId(streamId, STREAM_ID);
            verifyStreamId(streamDependency, STREAM_DEPENDENCY);
            verifyWeight(weight);
            ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte) 2, new Http2Flags(), streamId);
            buf.writeInt(exclusive ? (int) (2147483648L | streamDependency) : streamDependency);
            buf.writeByte(weight - 1);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        try {
            verifyStreamId(streamId, STREAM_ID);
            verifyErrorCode(errorCode);
            ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte) 3, new Http2Flags(), streamId);
            buf.writeInt((int) errorCode);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
        try {
            ObjectUtil.checkNotNull(settings, "settings");
            int payloadLength = 6 * settings.size();
            ByteBuf buf = ctx.alloc().buffer(9 + (settings.size() * 6));
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 4, new Http2Flags(), 0);
            for (CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                buf.writeChar(entry.key());
                buf.writeInt(entry.value().intValue());
            }
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
        try {
            ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte) 4, new Http2Flags().ack(true), 0);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
        Http2Flags flags = ack ? new Http2Flags().ack(true) : new Http2Flags();
        ByteBuf buf = ctx.alloc().buffer(17);
        Http2CodecUtil.writeFrameHeaderInternal(buf, 8, (byte) 6, flags, 0);
        buf.writeLong(data);
        return ctx.write(buf, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
        ByteBuf headerBlock = null;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            try {
                try {
                    verifyStreamId(streamId, STREAM_ID);
                    verifyStreamId(promisedStreamId, "Promised Stream ID");
                    Http2CodecUtil.verifyPadding(padding);
                    headerBlock = ctx.alloc().buffer();
                    this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
                    Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
                    int nonFragmentLength = 4 + padding;
                    int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
                    ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
                    flags.endOfHeaders(!headerBlock.isReadable());
                    int payloadLength = fragment.readableBytes() + nonFragmentLength;
                    ByteBuf buf = ctx.alloc().buffer(14);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 5, flags, streamId);
                    writePaddingLength(buf, padding);
                    buf.writeInt(promisedStreamId);
                    ctx.write(buf, promiseAggregator.newPromise());
                    ctx.write(fragment, promiseAggregator.newPromise());
                    if (paddingBytes(padding) > 0) {
                        ctx.write(ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
                    }
                    if (!flags.endOfHeaders()) {
                        writeContinuationFrames(ctx, streamId, headerBlock, promiseAggregator);
                    }
                    if (headerBlock != null) {
                        headerBlock.release();
                    }
                } catch (Throwable t) {
                    promiseAggregator.setFailure(t);
                    promiseAggregator.doneAllocatingPromises();
                    PlatformDependent.throwException(t);
                    if (headerBlock != null) {
                        headerBlock.release();
                    }
                }
            } catch (Http2Exception e) {
                promiseAggregator.setFailure((Throwable) e);
                if (headerBlock != null) {
                    headerBlock.release();
                }
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable th) {
            if (headerBlock != null) {
                headerBlock.release();
            }
            throw th;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
            verifyErrorCode(errorCode);
            int payloadLength = 8 + debugData.readableBytes();
            ByteBuf buf = ctx.alloc().buffer(17);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 7, new Http2Flags(), 0);
            buf.writeInt(lastStreamId);
            buf.writeInt((int) errorCode);
            ctx.write(buf, promiseAggregator.newPromise());
            try {
                ctx.write(debugData, promiseAggregator.newPromise());
            } catch (Throwable t) {
                promiseAggregator.setFailure(t);
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable t2) {
            try {
                debugData.release();
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
                return promiseAggregator;
            } catch (Throwable th) {
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
                throw th;
            }
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
        try {
            verifyStreamOrConnectionId(streamId, STREAM_ID);
            verifyWindowSizeIncrement(windowSizeIncrement);
            ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte) 8, new Http2Flags(), streamId);
            buf.writeInt(windowSizeIncrement);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(streamId, STREAM_ID);
            ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
            ctx.write(buf, promiseAggregator.newPromise());
            try {
                ctx.write(payload, promiseAggregator.newPromise());
            } catch (Throwable t) {
                promiseAggregator.setFailure(t);
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable t2) {
            try {
                payload.release();
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
                return promiseAggregator;
            } catch (Throwable th) {
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
                throw th;
            }
        }
    }

    private ChannelFuture writeHeadersInternal(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, boolean hasPriority, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
        ByteBuf headerBlock = null;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            try {
                try {
                    verifyStreamId(streamId, STREAM_ID);
                    if (hasPriority) {
                        verifyStreamOrConnectionId(streamDependency, STREAM_DEPENDENCY);
                        Http2CodecUtil.verifyPadding(padding);
                        verifyWeight(weight);
                    }
                    headerBlock = ctx.alloc().buffer();
                    this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
                    Http2Flags flags = new Http2Flags().endOfStream(endStream).priorityPresent(hasPriority).paddingPresent(padding > 0);
                    int nonFragmentBytes = padding + flags.getNumPriorityBytes();
                    int maxFragmentLength = this.maxFrameSize - nonFragmentBytes;
                    ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
                    flags.endOfHeaders(!headerBlock.isReadable());
                    int payloadLength = fragment.readableBytes() + nonFragmentBytes;
                    ByteBuf buf = ctx.alloc().buffer(15);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 1, flags, streamId);
                    writePaddingLength(buf, padding);
                    if (hasPriority) {
                        buf.writeInt(exclusive ? (int) (2147483648L | streamDependency) : streamDependency);
                        buf.writeByte(weight - 1);
                    }
                    ctx.write(buf, promiseAggregator.newPromise());
                    ctx.write(fragment, promiseAggregator.newPromise());
                    if (paddingBytes(padding) > 0) {
                        ctx.write(ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
                    }
                    if (!flags.endOfHeaders()) {
                        writeContinuationFrames(ctx, streamId, headerBlock, promiseAggregator);
                    }
                    if (headerBlock != null) {
                        headerBlock.release();
                    }
                } catch (Throwable t) {
                    promiseAggregator.setFailure(t);
                    promiseAggregator.doneAllocatingPromises();
                    PlatformDependent.throwException(t);
                    if (headerBlock != null) {
                        headerBlock.release();
                    }
                }
            } catch (Http2Exception e) {
                promiseAggregator.setFailure((Throwable) e);
                if (headerBlock != null) {
                    headerBlock.release();
                }
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable th) {
            if (headerBlock != null) {
                headerBlock.release();
            }
            throw th;
        }
    }

    private ChannelFuture writeContinuationFrames(ChannelHandlerContext ctx, int streamId, ByteBuf headerBlock, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        Http2Flags flags = new Http2Flags();
        if (headerBlock.isReadable()) {
            int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), this.maxFrameSize);
            ByteBuf buf = ctx.alloc().buffer(10);
            Http2CodecUtil.writeFrameHeaderInternal(buf, fragmentReadableBytes, (byte) 9, flags, streamId);
            do {
                int fragmentReadableBytes2 = Math.min(headerBlock.readableBytes(), this.maxFrameSize);
                ByteBuf fragment = headerBlock.readRetainedSlice(fragmentReadableBytes2);
                if (headerBlock.isReadable()) {
                    ctx.write(buf.retain(), promiseAggregator.newPromise());
                } else {
                    flags = flags.endOfHeaders(true);
                    buf.release();
                    buf = ctx.alloc().buffer(10);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, fragmentReadableBytes2, (byte) 9, flags, streamId);
                    ctx.write(buf, promiseAggregator.newPromise());
                }
                ctx.write(fragment, promiseAggregator.newPromise());
            } while (headerBlock.isReadable());
        }
        return promiseAggregator;
    }

    private static int paddingBytes(int padding) {
        return padding - 1;
    }

    private static void writePaddingLength(ByteBuf buf, int padding) {
        if (padding > 0) {
            buf.writeByte(padding - 1);
        }
    }

    private static void verifyStreamId(int streamId, String argumentName) {
        ObjectUtil.checkPositive(streamId, "streamId");
    }

    private static void verifyStreamOrConnectionId(int streamId, String argumentName) {
        ObjectUtil.checkPositiveOrZero(streamId, "streamId");
    }

    private static void verifyWeight(short weight) {
        if (weight < 1 || weight > 256) {
            throw new IllegalArgumentException("Invalid weight: " + ((int) weight));
        }
    }

    private static void verifyErrorCode(long errorCode) {
        if (errorCode < 0 || errorCode > 4294967295L) {
            throw new IllegalArgumentException("Invalid errorCode: " + errorCode);
        }
    }

    private static void verifyWindowSizeIncrement(int windowSizeIncrement) {
        ObjectUtil.checkPositiveOrZero(windowSizeIncrement, "windowSizeIncrement");
    }

    private static void verifyPingPayload(ByteBuf data) {
        if (data == null || data.readableBytes() != 8) {
            throw new IllegalArgumentException("Opaque data must be 8 bytes");
        }
    }
}

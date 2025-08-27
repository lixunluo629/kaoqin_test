package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2RemoteFlowController;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameCodec.class */
public class Http2FrameCodec extends Http2ConnectionHandler {
    private static final InternalLogger LOG;
    protected final Http2Connection.PropertyKey streamKey;
    private final Http2Connection.PropertyKey upgradeKey;
    private final Integer initialFlowControlWindowSize;
    ChannelHandlerContext ctx;
    private int numBufferedStreams;
    private final IntObjectMap<DefaultHttp2FrameStream> frameStreamToInitializeMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Http2FrameCodec.class.desiredAssertionStatus();
        LOG = InternalLoggerFactory.getInstance((Class<?>) Http2FrameCodec.class);
    }

    static /* synthetic */ int access$410(Http2FrameCodec x0) {
        int i = x0.numBufferedStreams;
        x0.numBufferedStreams = i - 1;
        return i;
    }

    Http2FrameCodec(Http2ConnectionEncoder encoder, Http2ConnectionDecoder decoder, Http2Settings initialSettings, boolean decoupleCloseAndGoAway) {
        super(decoder, encoder, initialSettings, decoupleCloseAndGoAway);
        this.frameStreamToInitializeMap = new IntObjectHashMap(8);
        decoder.frameListener(new FrameListener());
        connection().addListener(new ConnectionListener());
        ((Http2RemoteFlowController) connection().remote().flowController()).listener(new Http2RemoteFlowControllerListener());
        this.streamKey = connection().newKey();
        this.upgradeKey = connection().newKey();
        this.initialFlowControlWindowSize = initialSettings.initialWindowSize();
    }

    DefaultHttp2FrameStream newStream() {
        return new DefaultHttp2FrameStream();
    }

    final void forEachActiveStream(final Http2FrameStreamVisitor streamVisitor) throws Http2Exception {
        if (!$assertionsDisabled && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        if (connection().numActiveStreams() > 0) {
            connection().forEachActiveStream(new Http2StreamVisitor() { // from class: io.netty.handler.codec.http2.Http2FrameCodec.1
                @Override // io.netty.handler.codec.http2.Http2StreamVisitor
                public boolean visit(Http2Stream stream) {
                    try {
                        return streamVisitor.visit((Http2FrameStream) stream.getProperty(Http2FrameCodec.this.streamKey));
                    } catch (Throwable cause) {
                        Http2FrameCodec.this.onError(Http2FrameCodec.this.ctx, false, cause);
                        return false;
                    }
                }
            });
        }
    }

    int numInitializingStreams() {
        return this.frameStreamToInitializeMap.size();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public final void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.handlerAdded(ctx);
        handlerAdded0(ctx);
        Http2Connection connection = connection();
        if (connection.isServer()) {
            tryExpandConnectionFlowControlWindow(connection);
        }
    }

    private void tryExpandConnectionFlowControlWindow(Http2Connection connection) throws Http2Exception {
        if (this.initialFlowControlWindowSize != null) {
            Http2Stream connectionStream = connection.connectionStream();
            Http2LocalFlowController localFlowController = (Http2LocalFlowController) connection.local().flowController();
            int delta = this.initialFlowControlWindowSize.intValue() - localFlowController.initialWindowSize(connectionStream);
            if (delta > 0) {
                localFlowController.incrementWindowSize(connectionStream, Math.max(delta << 1, delta));
                flush(this.ctx);
            }
        }
    }

    void handlerAdded0(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt == Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE) {
            tryExpandConnectionFlowControlWindow(connection());
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.codec.http2.Http2FrameCodec.2
                @Override // java.lang.Runnable
                public void run() {
                    ctx.fireUserEventTriggered(evt);
                }
            });
            return;
        }
        if (evt instanceof HttpServerUpgradeHandler.UpgradeEvent) {
            HttpServerUpgradeHandler.UpgradeEvent upgrade = (HttpServerUpgradeHandler.UpgradeEvent) evt;
            try {
                onUpgradeEvent(ctx, upgrade.retain());
                Http2Stream stream = connection().stream(1);
                if (stream.getProperty(this.streamKey) == null) {
                    onStreamActive0(stream);
                }
                upgrade.upgradeRequest().headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), 1);
                stream.setProperty(this.upgradeKey, true);
                InboundHttpToHttp2Adapter.handle(ctx, connection(), decoder().frameListener(), upgrade.upgradeRequest().retain());
                upgrade.release();
                return;
            } catch (Throwable th) {
                upgrade.release();
                throw th;
            }
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof Http2DataFrame) {
            Http2DataFrame dataFrame = (Http2DataFrame) msg;
            encoder().writeData(ctx, dataFrame.stream().id(), dataFrame.content(), dataFrame.padding(), dataFrame.isEndStream(), promise);
            return;
        }
        if (msg instanceof Http2HeadersFrame) {
            writeHeadersFrame(ctx, (Http2HeadersFrame) msg, promise);
            return;
        }
        if (msg instanceof Http2WindowUpdateFrame) {
            Http2WindowUpdateFrame frame = (Http2WindowUpdateFrame) msg;
            Http2FrameStream frameStream = frame.stream();
            try {
                if (frameStream == null) {
                    increaseInitialConnectionWindow(frame.windowSizeIncrement());
                } else {
                    consumeBytes(frameStream.id(), frame.windowSizeIncrement());
                }
                promise.setSuccess();
                return;
            } catch (Throwable t) {
                promise.setFailure(t);
                return;
            }
        }
        if (msg instanceof Http2ResetFrame) {
            Http2ResetFrame rstFrame = (Http2ResetFrame) msg;
            int id = rstFrame.stream().id();
            if (connection().streamMayHaveExisted(id)) {
                encoder().writeRstStream(ctx, rstFrame.stream().id(), rstFrame.errorCode(), promise);
                return;
            } else {
                ReferenceCountUtil.release(rstFrame);
                promise.setFailure((Throwable) Http2Exception.streamError(rstFrame.stream().id(), Http2Error.PROTOCOL_ERROR, "Stream never existed", new Object[0]));
                return;
            }
        }
        if (msg instanceof Http2PingFrame) {
            Http2PingFrame frame2 = (Http2PingFrame) msg;
            encoder().writePing(ctx, frame2.ack(), frame2.content(), promise);
            return;
        }
        if (msg instanceof Http2SettingsFrame) {
            encoder().writeSettings(ctx, ((Http2SettingsFrame) msg).settings(), promise);
            return;
        }
        if (msg instanceof Http2SettingsAckFrame) {
            encoder().writeSettingsAck(ctx, promise);
            return;
        }
        if (msg instanceof Http2GoAwayFrame) {
            writeGoAwayFrame(ctx, (Http2GoAwayFrame) msg, promise);
            return;
        }
        if (msg instanceof Http2UnknownFrame) {
            Http2UnknownFrame unknownFrame = (Http2UnknownFrame) msg;
            encoder().writeFrame(ctx, unknownFrame.frameType(), unknownFrame.stream().id(), unknownFrame.flags(), unknownFrame.content(), promise);
        } else if (!(msg instanceof Http2Frame)) {
            ctx.write(msg, promise);
        } else {
            ReferenceCountUtil.release(msg);
            throw new UnsupportedMessageTypeException(msg, (Class<?>[]) new Class[0]);
        }
    }

    private void increaseInitialConnectionWindow(int deltaBytes) throws Http2Exception {
        ((Http2LocalFlowController) connection().local().flowController()).incrementWindowSize(connection().connectionStream(), deltaBytes);
    }

    final boolean consumeBytes(int streamId, int bytes) throws Http2Exception {
        Http2Stream stream = connection().stream(streamId);
        if (stream != null && streamId == 1) {
            Boolean upgraded = (Boolean) stream.getProperty(this.upgradeKey);
            if (Boolean.TRUE.equals(upgraded)) {
                return false;
            }
        }
        return ((Http2LocalFlowController) connection().local().flowController()).consumeBytes(stream, bytes);
    }

    private void writeGoAwayFrame(ChannelHandlerContext ctx, Http2GoAwayFrame frame, ChannelPromise promise) {
        if (frame.lastStreamId() > -1) {
            frame.release();
            throw new IllegalArgumentException("Last stream id must not be set on GOAWAY frame");
        }
        int lastStreamCreated = connection().remote().lastStreamCreated();
        long lastStreamId = lastStreamCreated + (frame.extraStreamIds() * 2);
        if (lastStreamId > 2147483647L) {
            lastStreamId = 2147483647L;
        }
        goAway(ctx, (int) lastStreamId, frame.errorCode(), frame.content(), promise);
    }

    private void writeHeadersFrame(ChannelHandlerContext ctx, Http2HeadersFrame headersFrame, ChannelPromise promise) {
        if (Http2CodecUtil.isStreamIdValid(headersFrame.stream().id())) {
            encoder().writeHeaders(ctx, headersFrame.stream().id(), headersFrame.headers(), headersFrame.padding(), headersFrame.isEndStream(), promise);
            return;
        }
        DefaultHttp2FrameStream stream = (DefaultHttp2FrameStream) headersFrame.stream();
        Http2Connection connection = connection();
        final int streamId = connection.local().incrementAndGetNextStreamId();
        if (streamId < 0) {
            promise.setFailure((Throwable) new Http2NoMoreStreamIdsException());
            onHttp2Frame(ctx, new DefaultHttp2GoAwayFrame(connection.isServer() ? Integer.MAX_VALUE : 2147483646, Http2Error.NO_ERROR.code(), ByteBufUtil.writeAscii(ctx.alloc(), "Stream IDs exhausted on local stream creation")));
            return;
        }
        stream.id = streamId;
        Object old = this.frameStreamToInitializeMap.put(streamId, (int) stream);
        if (!$assertionsDisabled && old != null) {
            throw new AssertionError();
        }
        encoder().writeHeaders(ctx, streamId, headersFrame.headers(), headersFrame.padding(), headersFrame.isEndStream(), promise);
        if (!promise.isDone()) {
            this.numBufferedStreams++;
            promise.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2FrameCodec.3
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture channelFuture) {
                    Http2FrameCodec.access$410(Http2FrameCodec.this);
                    Http2FrameCodec.this.handleHeaderFuture(channelFuture, streamId);
                }
            });
        } else {
            handleHeaderFuture(promise, streamId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHeaderFuture(ChannelFuture channelFuture, int streamId) {
        if (!channelFuture.isSuccess()) {
            this.frameStreamToInitializeMap.remove(streamId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStreamActive0(Http2Stream stream) {
        if (stream.id() != 1 && connection().local().isValidStreamId(stream.id())) {
            return;
        }
        DefaultHttp2FrameStream stream2 = newStream().setStreamAndProperty(this.streamKey, stream);
        onHttp2StreamStateChanged(this.ctx, stream2);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameCodec$ConnectionListener.class */
    private final class ConnectionListener extends Http2ConnectionAdapter {
        private ConnectionListener() {
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
        public void onStreamAdded(Http2Stream stream) {
            DefaultHttp2FrameStream frameStream = (DefaultHttp2FrameStream) Http2FrameCodec.this.frameStreamToInitializeMap.remove(stream.id());
            if (frameStream != null) {
                frameStream.setStreamAndProperty(Http2FrameCodec.this.streamKey, stream);
            }
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
        public void onStreamActive(Http2Stream stream) {
            Http2FrameCodec.this.onStreamActive0(stream);
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
        public void onStreamClosed(Http2Stream stream) {
            onHttp2StreamStateChanged0(stream);
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
        public void onStreamHalfClosed(Http2Stream stream) {
            onHttp2StreamStateChanged0(stream);
        }

        private void onHttp2StreamStateChanged0(Http2Stream stream) {
            DefaultHttp2FrameStream stream2 = (DefaultHttp2FrameStream) stream.getProperty(Http2FrameCodec.this.streamKey);
            if (stream2 != null) {
                Http2FrameCodec.this.onHttp2StreamStateChanged(Http2FrameCodec.this.ctx, stream2);
            }
        }
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler
    protected void onConnectionError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception http2Ex) {
        if (!outbound) {
            ctx.fireExceptionCaught(cause);
        }
        super.onConnectionError(ctx, outbound, cause, http2Ex);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler
    protected final void onStreamError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception.StreamException streamException) {
        int streamId = streamException.streamId();
        Http2Stream connectionStream = connection().stream(streamId);
        if (connectionStream == null) {
            onHttp2UnknownStreamError(ctx, cause, streamException);
            super.onStreamError(ctx, outbound, cause, streamException);
            return;
        }
        Http2FrameStream stream = (Http2FrameStream) connectionStream.getProperty(this.streamKey);
        if (stream == null) {
            LOG.warn("Stream exception thrown without stream object attached.", cause);
            super.onStreamError(ctx, outbound, cause, streamException);
        } else if (!outbound) {
            onHttp2FrameStreamException(ctx, new Http2FrameStreamException(stream, streamException.error(), cause));
        }
    }

    private void onHttp2UnknownStreamError(ChannelHandlerContext ctx, Throwable cause, Http2Exception.StreamException streamException) {
        InternalLogLevel level = streamException.error() == Http2Error.STREAM_CLOSED ? InternalLogLevel.DEBUG : InternalLogLevel.WARN;
        LOG.log(level, "Stream exception thrown for unknown stream {}.", Integer.valueOf(streamException.streamId()), cause);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler
    protected final boolean isGracefulShutdownComplete() {
        return super.isGracefulShutdownComplete() && this.numBufferedStreams == 0;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameCodec$FrameListener.class */
    private final class FrameListener implements Http2FrameListener {
        private FrameListener() {
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2UnknownFrame(frameType, flags, payload).stream(requireStream(streamId)).retain());
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2SettingsFrame(settings));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingRead(ChannelHandlerContext ctx, long data) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, false));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPingAckRead(ChannelHandlerContext ctx, long data) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, true));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2ResetFrame(errorCode).stream(requireStream(streamId)));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) {
            if (streamId == 0) {
                return;
            }
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2WindowUpdateFrame(windowSizeIncrement).stream(requireStream(streamId)));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) {
            onHeadersRead(ctx, streamId, headers, padding, endStream);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2HeadersFrame(headers, endOfStream, padding).stream(requireStream(streamId)));
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2DataFrame(data, endOfStream, padding).stream(requireStream(streamId)).retain());
            return 0;
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2GoAwayFrame(lastStreamId, errorCode, debugData).retain());
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) {
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onSettingsAckRead(ChannelHandlerContext ctx) {
            Http2FrameCodec.this.onHttp2Frame(ctx, Http2SettingsAckFrame.INSTANCE);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameListener
        public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) {
        }

        private Http2FrameStream requireStream(int streamId) {
            Http2FrameStream stream = (Http2FrameStream) Http2FrameCodec.this.connection().stream(streamId).getProperty(Http2FrameCodec.this.streamKey);
            if (stream == null) {
                throw new IllegalStateException("Stream object required for identifier: " + streamId);
            }
            return stream;
        }
    }

    private void onUpgradeEvent(ChannelHandlerContext ctx, HttpServerUpgradeHandler.UpgradeEvent evt) {
        ctx.fireUserEventTriggered((Object) evt);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHttp2StreamWritabilityChanged(ChannelHandlerContext ctx, DefaultHttp2FrameStream stream, boolean writable) {
        ctx.fireUserEventTriggered((Object) stream.writabilityChanged);
    }

    void onHttp2StreamStateChanged(ChannelHandlerContext ctx, DefaultHttp2FrameStream stream) {
        ctx.fireUserEventTriggered((Object) stream.stateChanged);
    }

    void onHttp2Frame(ChannelHandlerContext ctx, Http2Frame frame) {
        ctx.fireChannelRead((Object) frame);
    }

    void onHttp2FrameStreamException(ChannelHandlerContext ctx, Http2FrameStreamException cause) {
        ctx.fireExceptionCaught((Throwable) cause);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameCodec$Http2RemoteFlowControllerListener.class */
    private final class Http2RemoteFlowControllerListener implements Http2RemoteFlowController.Listener {
        private Http2RemoteFlowControllerListener() {
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.Listener
        public void writabilityChanged(Http2Stream stream) {
            DefaultHttp2FrameStream frameStream = (DefaultHttp2FrameStream) stream.getProperty(Http2FrameCodec.this.streamKey);
            if (frameStream != null) {
                Http2FrameCodec.this.onHttp2StreamWritabilityChanged(Http2FrameCodec.this.ctx, frameStream, ((Http2RemoteFlowController) Http2FrameCodec.this.connection().remote().flowController()).isWritable(stream));
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameCodec$DefaultHttp2FrameStream.class */
    static class DefaultHttp2FrameStream implements Http2FrameStream {
        volatile Http2Stream stream;
        Channel attachment;
        static final /* synthetic */ boolean $assertionsDisabled;
        private volatile int id = -1;
        final Http2FrameStreamEvent stateChanged = Http2FrameStreamEvent.stateChanged(this);
        final Http2FrameStreamEvent writabilityChanged = Http2FrameStreamEvent.writabilityChanged(this);

        DefaultHttp2FrameStream() {
        }

        static {
            $assertionsDisabled = !Http2FrameCodec.class.desiredAssertionStatus();
        }

        DefaultHttp2FrameStream setStreamAndProperty(Http2Connection.PropertyKey streamKey, Http2Stream stream) {
            if (!$assertionsDisabled && this.id != -1 && stream.id() != this.id) {
                throw new AssertionError();
            }
            this.stream = stream;
            stream.setProperty(streamKey, this);
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2FrameStream
        public int id() {
            Http2Stream stream = this.stream;
            return stream == null ? this.id : stream.id();
        }

        @Override // io.netty.handler.codec.http2.Http2FrameStream
        public Http2Stream.State state() {
            Http2Stream stream = this.stream;
            return stream == null ? Http2Stream.State.IDLE : stream.state();
        }

        public String toString() {
            return String.valueOf(id());
        }
    }
}

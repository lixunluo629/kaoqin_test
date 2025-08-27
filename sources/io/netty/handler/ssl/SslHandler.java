package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractCoalescingBufferQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseNotifier;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler.class */
public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
    private static final InternalLogger logger;
    private static final Pattern IGNORABLE_CLASS_IN_STACK;
    private static final Pattern IGNORABLE_ERROR_MESSAGE;
    private static final int MAX_PLAINTEXT_LENGTH = 16384;
    private volatile ChannelHandlerContext ctx;
    private final SSLEngine engine;
    private final SslEngineType engineType;
    private final Executor delegatedTaskExecutor;
    private final boolean jdkCompatibilityMode;
    private final ByteBuffer[] singleBuffer;
    private final boolean startTls;
    private boolean sentFirstMessage;
    private boolean flushedBeforeHandshake;
    private boolean readDuringHandshake;
    private boolean handshakeStarted;
    private SslHandlerCoalescingBufferQueue pendingUnencryptedWrites;
    private Promise<Channel> handshakePromise;
    private final LazyChannelPromise sslClosePromise;
    private boolean needsFlush;
    private boolean outboundClosed;
    private boolean closeNotify;
    private boolean processTask;
    private int packetLength;
    private boolean firedChannelRead;
    private volatile long handshakeTimeoutMillis;
    private volatile long closeNotifyFlushTimeoutMillis;
    private volatile long closeNotifyReadTimeoutMillis;
    volatile int wrapDataSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SslHandler.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) SslHandler.class);
        IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
        IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler$SslEngineType.class */
    private enum SslEngineType {
        TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.1
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException {
                SSLEngineResult result;
                int nioBufferCount = in.nioBufferCount();
                int writerIndex = out.writerIndex();
                if (nioBufferCount > 1) {
                    ReferenceCountedOpenSslEngine opensslEngine = (ReferenceCountedOpenSslEngine) handler.engine;
                    try {
                        handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out.writableBytes());
                        result = opensslEngine.unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
                        handler.singleBuffer[0] = null;
                    } catch (Throwable th) {
                        handler.singleBuffer[0] = null;
                        throw th;
                    }
                } else {
                    result = handler.engine.unwrap(SslHandler.toByteBuffer(in, readerIndex, len), SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.directBuffer(((ReferenceCountedOpenSslEngine) handler.engine).calculateMaxLengthForWrap(pendingBytes, numComponents));
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                int sslPending = ((ReferenceCountedOpenSslEngine) handler.engine).sslPending();
                return sslPending > 0 ? sslPending : guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return ((ReferenceCountedOpenSslEngine) engine).jdkCompatibilityMode;
            }
        },
        CONSCRYPT(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.2
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException {
                SSLEngineResult result;
                int nioBufferCount = in.nioBufferCount();
                int writerIndex = out.writerIndex();
                if (nioBufferCount > 1) {
                    try {
                        handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out.writableBytes());
                        result = ((ConscryptAlpnSslEngine) handler.engine).unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
                        handler.singleBuffer[0] = null;
                    } catch (Throwable th) {
                        handler.singleBuffer[0] = null;
                        throw th;
                    }
                } else {
                    result = handler.engine.unwrap(SslHandler.toByteBuffer(in, readerIndex, len), SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.directBuffer(((ConscryptAlpnSslEngine) handler.engine).calculateOutNetBufSize(pendingBytes, numComponents));
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                return guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return true;
            }
        },
        JDK(false, ByteToMessageDecoder.MERGE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.3
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int readerIndex, int len, ByteBuf out) throws SSLException {
                int consumed;
                int writerIndex = out.writerIndex();
                ByteBuffer inNioBuffer = SslHandler.toByteBuffer(in, readerIndex, len);
                int position = inNioBuffer.position();
                SSLEngineResult result = handler.engine.unwrap(inNioBuffer, SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                out.writerIndex(writerIndex + result.bytesProduced());
                if (result.bytesConsumed() == 0 && (consumed = inNioBuffer.position() - position) != result.bytesConsumed()) {
                    return new SSLEngineResult(result.getStatus(), result.getHandshakeStatus(), consumed, result.bytesProduced());
                }
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.heapBuffer(handler.engine.getSession().getPacketBufferSize());
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                return guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return true;
            }
        };

        final boolean wantsDirectBuffer;
        final ByteToMessageDecoder.Cumulator cumulator;

        abstract SSLEngineResult unwrap(SslHandler sslHandler, ByteBuf byteBuf, int i, int i2, ByteBuf byteBuf2) throws SSLException;

        abstract int calculatePendingData(SslHandler sslHandler, int i);

        abstract boolean jdkCompatibilityMode(SSLEngine sSLEngine);

        abstract ByteBuf allocateWrapBuffer(SslHandler sslHandler, ByteBufAllocator byteBufAllocator, int i, int i2);

        static SslEngineType forEngine(SSLEngine engine) {
            return engine instanceof ReferenceCountedOpenSslEngine ? TCNATIVE : engine instanceof ConscryptAlpnSslEngine ? CONSCRYPT : JDK;
        }

        SslEngineType(boolean wantsDirectBuffer, ByteToMessageDecoder.Cumulator cumulator) {
            this.wantsDirectBuffer = wantsDirectBuffer;
            this.cumulator = cumulator;
        }
    }

    public SslHandler(SSLEngine engine) {
        this(engine, false);
    }

    public SslHandler(SSLEngine engine, boolean startTls) {
        this(engine, startTls, ImmediateExecutor.INSTANCE);
    }

    public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }

    public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
        this.singleBuffer = new ByteBuffer[1];
        this.handshakePromise = new LazyChannelPromise();
        this.sslClosePromise = new LazyChannelPromise();
        this.handshakeTimeoutMillis = 10000L;
        this.closeNotifyFlushTimeoutMillis = 3000L;
        this.wrapDataSize = 16384;
        this.engine = (SSLEngine) ObjectUtil.checkNotNull(engine, "engine");
        this.delegatedTaskExecutor = (Executor) ObjectUtil.checkNotNull(delegatedTaskExecutor, "delegatedTaskExecutor");
        this.engineType = SslEngineType.forEngine(engine);
        this.startTls = startTls;
        this.jdkCompatibilityMode = this.engineType.jdkCompatibilityMode(engine);
        setCumulator(this.engineType.cumulator);
    }

    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }

    public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
        ObjectUtil.checkNotNull(unit, "unit");
        setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }

    public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
        if (handshakeTimeoutMillis < 0) {
            throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
        }
        this.handshakeTimeoutMillis = handshakeTimeoutMillis;
    }

    public final void setWrapDataSize(int wrapDataSize) {
        this.wrapDataSize = wrapDataSize;
    }

    @Deprecated
    public long getCloseNotifyTimeoutMillis() {
        return getCloseNotifyFlushTimeoutMillis();
    }

    @Deprecated
    public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
        setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
    }

    @Deprecated
    public void setCloseNotifyTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
        setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
    }

    public final long getCloseNotifyFlushTimeoutMillis() {
        return this.closeNotifyFlushTimeoutMillis;
    }

    public final void setCloseNotifyFlushTimeout(long closeNotifyFlushTimeout, TimeUnit unit) {
        setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
    }

    public final void setCloseNotifyFlushTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
        if (closeNotifyFlushTimeoutMillis < 0) {
            throw new IllegalArgumentException("closeNotifyFlushTimeoutMillis: " + closeNotifyFlushTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyFlushTimeoutMillis = closeNotifyFlushTimeoutMillis;
    }

    public final long getCloseNotifyReadTimeoutMillis() {
        return this.closeNotifyReadTimeoutMillis;
    }

    public final void setCloseNotifyReadTimeout(long closeNotifyReadTimeout, TimeUnit unit) {
        setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
    }

    public final void setCloseNotifyReadTimeoutMillis(long closeNotifyReadTimeoutMillis) {
        if (closeNotifyReadTimeoutMillis < 0) {
            throw new IllegalArgumentException("closeNotifyReadTimeoutMillis: " + closeNotifyReadTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyReadTimeoutMillis = closeNotifyReadTimeoutMillis;
    }

    public SSLEngine engine() {
        return this.engine;
    }

    public String applicationProtocol() {
        Object objEngine = engine();
        if (!(objEngine instanceof ApplicationProtocolAccessor)) {
            return null;
        }
        return ((ApplicationProtocolAccessor) objEngine).getNegotiatedApplicationProtocol();
    }

    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }

    @Deprecated
    public ChannelFuture close() {
        return closeOutbound();
    }

    @Deprecated
    public ChannelFuture close(ChannelPromise promise) {
        return closeOutbound(promise);
    }

    public ChannelFuture closeOutbound() {
        return closeOutbound(this.ctx.newPromise());
    }

    public ChannelFuture closeOutbound(final ChannelPromise promise) {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx.executor().inEventLoop()) {
            closeOutbound0(promise);
        } else {
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    SslHandler.this.closeOutbound0(promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeOutbound0(ChannelPromise promise) {
        this.outboundClosed = true;
        this.engine.closeOutbound();
        try {
            flush(this.ctx, promise);
        } catch (Exception e) {
            if (!promise.tryFailure(e)) {
                logger.warn("{} flush() raised a masked exception.", this.ctx.channel(), e);
            }
        }
    }

    public Future<Channel> sslCloseFuture() {
        return this.sslClosePromise;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        if (!this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.releaseAndFailAll(ctx, new ChannelException("Pending write on removal of SslHandler"));
        }
        this.pendingUnencryptedWrites = null;
        SSLHandshakeException cause = null;
        if (!this.handshakePromise.isDone()) {
            cause = new SSLHandshakeException("SslHandler removed before handshake completed");
            if (this.handshakePromise.tryFailure(cause)) {
                ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(cause));
            }
        }
        if (!this.sslClosePromise.isDone()) {
            if (cause == null) {
                cause = new SSLHandshakeException("SslHandler removed before handshake completed");
            }
            notifyClosePromise(cause);
        }
        if (this.engine instanceof ReferenceCounted) {
            ((ReferenceCounted) this.engine).release();
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        closeOutboundAndChannel(ctx, promise, true);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        closeOutboundAndChannel(ctx, promise, false);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (!this.handshakePromise.isDone()) {
            this.readDuringHandshake = true;
        }
        ctx.read();
    }

    private static IllegalStateException newPendingWritesNullException() {
        return new IllegalStateException("pendingUnencryptedWrites is null, handlerRemoved0 called?");
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(msg, (Class<?>[]) new Class[]{ByteBuf.class});
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable) exception);
        } else if (this.pendingUnencryptedWrites == null) {
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable) newPendingWritesNullException());
        } else {
            this.pendingUnencryptedWrites.add((ByteBuf) msg, promise);
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            this.sentFirstMessage = true;
            this.pendingUnencryptedWrites.writeAndRemoveAll(ctx);
            forceFlush(ctx);
            startHandshakeProcessing();
            return;
        }
        if (this.processTask) {
            return;
        }
        try {
            wrapAndFlush(ctx);
        } catch (Throwable cause) {
            setHandshakeFailure(ctx, cause);
            PlatformDependent.throwException(cause);
        }
    }

    private void wrapAndFlush(ChannelHandlerContext ctx) throws SSLException {
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
        }
        if (!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshake = true;
        }
        try {
            wrap(ctx, false);
        } finally {
            forceFlush(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0190  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void wrap(io.netty.channel.ChannelHandlerContext r8, boolean r9) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 448
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrap(io.netty.channel.ChannelHandlerContext, boolean):void");
    }

    private void finishWrap(ChannelHandlerContext ctx, ByteBuf out, ChannelPromise promise, boolean inUnwrap, boolean needUnwrap) {
        if (out == null) {
            out = Unpooled.EMPTY_BUFFER;
        } else if (!out.isReadable()) {
            out.release();
            out = Unpooled.EMPTY_BUFFER;
        }
        if (promise != null) {
            ctx.write(out, promise);
        } else {
            ctx.write(out);
        }
        if (inUnwrap) {
            this.needsFlush = true;
        }
        if (needUnwrap) {
            readIfNeeded(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:45:0x010c A[Catch: all -> 0x013c, TryCatch #0 {all -> 0x013c, blocks: (B:3:0x000a, B:7:0x0017, B:8:0x0021, B:10:0x0039, B:12:0x0053, B:14:0x005a, B:15:0x006a, B:16:0x008c, B:21:0x009f, B:43:0x0104, B:45:0x010c, B:48:0x0117, B:50:0x011f, B:31:0x00bd, B:33:0x00c8, B:35:0x00d1, B:41:0x00e5, B:42:0x0103), top: B:65:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0117 A[Catch: all -> 0x013c, TryCatch #0 {all -> 0x013c, blocks: (B:3:0x000a, B:7:0x0017, B:8:0x0021, B:10:0x0039, B:12:0x0053, B:14:0x005a, B:15:0x006a, B:16:0x008c, B:21:0x009f, B:43:0x0104, B:45:0x010c, B:48:0x0117, B:50:0x011f, B:31:0x00bd, B:33:0x00c8, B:35:0x00d1, B:41:0x00e5, B:42:0x0103), top: B:65:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0134 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x014a A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean wrapNonAppData(final io.netty.channel.ChannelHandlerContext r7, boolean r8) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 332
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrapNonAppData(io.netty.channel.ChannelHandlerContext, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0108 A[FINALLY_INSNS] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private javax.net.ssl.SSLEngineResult wrap(io.netty.buffer.ByteBufAllocator r7, javax.net.ssl.SSLEngine r8, io.netty.buffer.ByteBuf r9, io.netty.buffer.ByteBuf r10) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 273
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrap(io.netty.buffer.ByteBufAllocator, javax.net.ssl.SSLEngine, io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf):javax.net.ssl.SSLEngineResult");
    }

    /* renamed from: io.netty.handler.ssl.SslHandler$9, reason: invalid class name */
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler$9.class */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus;
        static final /* synthetic */ int[] $SwitchMap$javax$net$ssl$SSLEngineResult$Status = new int[SSLEngineResult.Status.values().length];

        static {
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$Status[SSLEngineResult.Status.BUFFER_OVERFLOW.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$Status[SSLEngineResult.Status.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus = new int[SSLEngineResult.HandshakeStatus.values().length];
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_TASK.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.FINISHED.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_WRAP.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_UNWRAP.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        boolean handshakeFailed = this.handshakePromise.cause() != null;
        ClosedChannelException exception = new ClosedChannelException();
        setHandshakeFailure(ctx, exception, !this.outboundClosed, this.handshakeStarted, false);
        notifyClosePromise(exception);
        try {
            super.channelInactive(ctx);
        } catch (DecoderException e) {
            if (!handshakeFailed || !(e.getCause() instanceof SSLException)) {
                throw e;
            }
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ignoreException(cause)) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
                return;
            }
            return;
        }
        ctx.fireExceptionCaught(cause);
    }

    private boolean ignoreException(Throwable t) {
        if (!(t instanceof SSLException) && (t instanceof IOException) && this.sslClosePromise.isDone()) {
            String message = t.getMessage();
            if (message != null && IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            StackTraceElement[] elements = t.getStackTrace();
            for (StackTraceElement element : elements) {
                String classname = element.getClassName();
                String methodname = element.getMethodName();
                if (!classname.startsWith("io.netty.") && "read".equals(methodname)) {
                    if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                        return true;
                    }
                    try {
                        Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
                        if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz)) {
                            return true;
                        }
                        if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                            return true;
                        }
                    } catch (Throwable cause) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Unexpected exception while loading class {} classname {}", getClass(), classname, cause);
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isEncrypted(ByteBuf buffer) {
        if (buffer.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2;
    }

    private void decodeJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) throws NotSslRecordException {
        int packetLength = this.packetLength;
        if (packetLength > 0) {
            if (in.readableBytes() < packetLength) {
                return;
            }
        } else {
            int readableBytes = in.readableBytes();
            if (readableBytes < 5) {
                return;
            }
            packetLength = SslUtils.getEncryptedPacketLength(in, in.readerIndex());
            if (packetLength == -2) {
                NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                in.skipBytes(in.readableBytes());
                setHandshakeFailure(ctx, e);
                throw e;
            }
            if (!$assertionsDisabled && packetLength <= 0) {
                throw new AssertionError();
            }
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                return;
            }
        }
        this.packetLength = 0;
        try {
            int bytesConsumed = unwrap(ctx, in, in.readerIndex(), packetLength);
            if (!$assertionsDisabled && bytesConsumed != packetLength && !this.engine.isInboundDone()) {
                throw new AssertionError("we feed the SSLEngine a packets worth of data: " + packetLength + " but it only consumed: " + bytesConsumed);
            }
            in.skipBytes(bytesConsumed);
        } catch (Throwable cause) {
            handleUnwrapThrowable(ctx, cause);
        }
    }

    private void decodeNonJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) {
        try {
            in.skipBytes(unwrap(ctx, in, in.readerIndex(), in.readableBytes()));
        } catch (Throwable cause) {
            handleUnwrapThrowable(ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnwrapThrowable(ChannelHandlerContext ctx, Throwable cause) {
        try {
            try {
                if (this.handshakePromise.tryFailure(cause)) {
                    ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(cause));
                }
                wrapAndFlush(ctx);
                setHandshakeFailure(ctx, cause, true, false, true);
            } catch (SSLException ex) {
                logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", (Throwable) ex);
                setHandshakeFailure(ctx, cause, true, false, true);
            }
            PlatformDependent.throwException(cause);
        } catch (Throwable th) {
            setHandshakeFailure(ctx, cause, true, false, true);
            throw th;
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
        if (this.processTask) {
            return;
        }
        if (this.jdkCompatibilityMode) {
            decodeJdkCompatible(ctx, in);
        } else {
            decodeNonJdkCompatible(ctx, in);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        channelReadComplete0(ctx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void channelReadComplete0(ChannelHandlerContext ctx) {
        discardSomeReadBytes();
        flushIfNeeded(ctx);
        readIfNeeded(ctx);
        this.firedChannelRead = false;
        ctx.fireChannelReadComplete();
    }

    private void readIfNeeded(ChannelHandlerContext ctx) {
        if (ctx.channel().config().isAutoRead()) {
            return;
        }
        if (!this.firedChannelRead || !this.handshakePromise.isDone()) {
            ctx.read();
        }
    }

    private void flushIfNeeded(ChannelHandlerContext ctx) {
        if (this.needsFlush) {
            forceFlush(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unwrapNonAppData(ChannelHandlerContext ctx) throws SSLException {
        unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01c2 A[Catch: all -> 0x0246, PHI: r13
  0x01c2: PHI (r13v8 'wrapLater' boolean) = 
  (r13v6 'wrapLater' boolean)
  (r13v1 'wrapLater' boolean)
  (r13v1 'wrapLater' boolean)
  (r13v1 'wrapLater' boolean)
  (r13v1 'wrapLater' boolean)
  (r13v1 'wrapLater' boolean)
 binds: [B:33:0x0186, B:31:0x017d, B:26:0x016d, B:28:0x0172, B:24:0x0164, B:38:0x019f] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {all -> 0x0246, blocks: (B:3:0x0016, B:5:0x001f, B:6:0x0060, B:7:0x007c, B:9:0x00a1, B:11:0x00b7, B:17:0x00db, B:18:0x0116, B:19:0x0117, B:12:0x00c8, B:22:0x0137, B:23:0x0140, B:42:0x01c2, B:44:0x01ca, B:50:0x01dc, B:52:0x01e4, B:25:0x0167, B:30:0x0178, B:33:0x0186, B:34:0x0190, B:40:0x01a5, B:41:0x01c1, B:54:0x01ef, B:56:0x01f6, B:58:0x0202, B:61:0x020f, B:64:0x021a), top: B:82:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01ca A[Catch: all -> 0x0246, TryCatch #0 {all -> 0x0246, blocks: (B:3:0x0016, B:5:0x001f, B:6:0x0060, B:7:0x007c, B:9:0x00a1, B:11:0x00b7, B:17:0x00db, B:18:0x0116, B:19:0x0117, B:12:0x00c8, B:22:0x0137, B:23:0x0140, B:42:0x01c2, B:44:0x01ca, B:50:0x01dc, B:52:0x01e4, B:25:0x0167, B:30:0x0178, B:33:0x0186, B:34:0x0190, B:40:0x01a5, B:41:0x01c1, B:54:0x01ef, B:56:0x01f6, B:58:0x0202, B:61:0x020f, B:64:0x021a), top: B:82:0x0016 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01dc A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int unwrap(io.netty.channel.ChannelHandlerContext r8, io.netty.buffer.ByteBuf r9, int r10, int r11) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 629
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.unwrap(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ByteBuffer toByteBuffer(ByteBuf out, int index, int len) {
        return out.nioBufferCount() == 1 ? out.internalNioBuffer(index, len) : out.nioBuffer(index, len);
    }

    private static boolean inEventLoop(Executor executor) {
        return (executor instanceof EventExecutor) && ((EventExecutor) executor).inEventLoop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runAllDelegatedTasks(SSLEngine engine) {
        while (true) {
            Runnable task = engine.getDelegatedTask();
            if (task == null) {
                return;
            } else {
                task.run();
            }
        }
    }

    private boolean runDelegatedTasks(boolean inUnwrap) {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE || inEventLoop(this.delegatedTaskExecutor)) {
            runAllDelegatedTasks(this.engine);
            return true;
        }
        executeDelegatedTasks(inUnwrap);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeDelegatedTasks(boolean inUnwrap) {
        this.processTask = true;
        try {
            this.delegatedTaskExecutor.execute(new SslTasksRunner(inUnwrap));
        } catch (RejectedExecutionException e) {
            this.processTask = false;
            throw e;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler$SslTasksRunner.class */
    private final class SslTasksRunner implements Runnable {
        private final boolean inUnwrap;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SslHandler.class.desiredAssertionStatus();
        }

        SslTasksRunner(boolean inUnwrap) {
            this.inUnwrap = inUnwrap;
        }

        private void taskError(Throwable e) {
            if (this.inUnwrap) {
                try {
                    SslHandler.this.handleUnwrapThrowable(SslHandler.this.ctx, e);
                    return;
                } catch (Throwable cause) {
                    safeExceptionCaught(cause);
                    return;
                }
            }
            SslHandler.this.setHandshakeFailure(SslHandler.this.ctx, e);
            SslHandler.this.forceFlush(SslHandler.this.ctx);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void safeExceptionCaught(Throwable cause) {
            try {
                SslHandler.this.exceptionCaught(SslHandler.this.ctx, wrapIfNeeded(cause));
            } catch (Throwable error) {
                SslHandler.this.ctx.fireExceptionCaught(error);
            }
        }

        private Throwable wrapIfNeeded(Throwable cause) {
            if (this.inUnwrap) {
                return cause instanceof DecoderException ? cause : new DecoderException(cause);
            }
            return cause;
        }

        private void tryDecodeAgain() {
            try {
                SslHandler.this.channelRead(SslHandler.this.ctx, Unpooled.EMPTY_BUFFER);
            } catch (Throwable cause) {
                safeExceptionCaught(cause);
            } finally {
                SslHandler.this.channelReadComplete0(SslHandler.this.ctx);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resumeOnEventExecutor() {
            if (!$assertionsDisabled && !SslHandler.this.ctx.executor().inEventLoop()) {
                throw new AssertionError();
            }
            SslHandler.this.processTask = false;
            try {
                SSLEngineResult.HandshakeStatus status = SslHandler.this.engine.getHandshakeStatus();
                switch (AnonymousClass9.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[status.ordinal()]) {
                    case 1:
                        SslHandler.this.executeDelegatedTasks(this.inUnwrap);
                        return;
                    case 2:
                        SslHandler.this.setHandshakeSuccess();
                    case 3:
                        SslHandler.this.setHandshakeSuccessIfStillHandshaking();
                        try {
                            SslHandler.this.wrap(SslHandler.this.ctx, this.inUnwrap);
                            if (this.inUnwrap) {
                                SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                            }
                            SslHandler.this.forceFlush(SslHandler.this.ctx);
                            tryDecodeAgain();
                            return;
                        } catch (Throwable e) {
                            taskError(e);
                            return;
                        }
                    case 4:
                        try {
                            if (!SslHandler.this.wrapNonAppData(SslHandler.this.ctx, false) && this.inUnwrap) {
                                SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                            }
                            SslHandler.this.forceFlush(SslHandler.this.ctx);
                            tryDecodeAgain();
                            return;
                        } catch (Throwable e2) {
                            taskError(e2);
                            return;
                        }
                    case 5:
                        try {
                            SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                            tryDecodeAgain();
                            return;
                        } catch (SSLException e3) {
                            SslHandler.this.handleUnwrapThrowable(SslHandler.this.ctx, e3);
                            return;
                        }
                    default:
                        throw new AssertionError();
                }
            } catch (Throwable cause) {
                safeExceptionCaught(cause);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SslHandler.runAllDelegatedTasks(SslHandler.this.engine);
                if (!$assertionsDisabled && SslHandler.this.engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    throw new AssertionError();
                }
                SslHandler.this.ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.SslTasksRunner.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SslTasksRunner.this.resumeOnEventExecutor();
                    }
                });
            } catch (Throwable cause) {
                handleException(cause);
            }
        }

        private void handleException(final Throwable cause) {
            if (SslHandler.this.ctx.executor().inEventLoop()) {
                SslHandler.this.processTask = false;
                safeExceptionCaught(cause);
            } else {
                try {
                    SslHandler.this.ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.SslTasksRunner.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SslHandler.this.processTask = false;
                            SslTasksRunner.this.safeExceptionCaught(cause);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    SslHandler.this.processTask = false;
                    SslHandler.this.ctx.fireExceptionCaught(cause);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setHandshakeSuccessIfStillHandshaking() {
        if (!this.handshakePromise.isDone()) {
            setHandshakeSuccess();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandshakeSuccess() {
        this.handshakePromise.trySuccess(this.ctx.channel());
        if (logger.isDebugEnabled()) {
            SSLSession session = this.engine.getSession();
            logger.debug("{} HANDSHAKEN: protocol:{} cipher suite:{}", this.ctx.channel(), session.getProtocol(), session.getCipherSuite());
        }
        this.ctx.fireUserEventTriggered((Object) SslHandshakeCompletionEvent.SUCCESS);
        if (this.readDuringHandshake && !this.ctx.channel().config().isAutoRead()) {
            this.readDuringHandshake = false;
            this.ctx.read();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause) {
        setHandshakeFailure(ctx, cause, true, true, false);
    }

    private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean closeInbound, boolean notify, boolean alwaysFlushAndClose) {
        String msg;
        try {
            this.outboundClosed = true;
            this.engine.closeOutbound();
            if (closeInbound) {
                try {
                    this.engine.closeInbound();
                } catch (SSLException e) {
                    if (logger.isDebugEnabled() && ((msg = e.getMessage()) == null || (!msg.contains("possible truncation attack") && !msg.contains("closing inbound before receiving peer's close_notify")))) {
                        logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
                    }
                }
            }
            if (this.handshakePromise.tryFailure(cause) || alwaysFlushAndClose) {
                SslUtils.handleHandshakeFailure(ctx, cause, notify);
            }
        } finally {
            releaseAndFailAll(ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandshakeFailureTransportFailure(ChannelHandlerContext ctx, Throwable cause) {
        try {
            SSLException transportFailure = new SSLException("failure when writing TLS control frames", cause);
            releaseAndFailAll(ctx, transportFailure);
            if (this.handshakePromise.tryFailure(transportFailure)) {
                ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(transportFailure));
            }
        } finally {
            ctx.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseAndFailAll(ChannelHandlerContext ctx, Throwable cause) {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.releaseAndFailAll(ctx, cause);
        }
    }

    private void notifyClosePromise(Throwable cause) {
        if (cause == null) {
            if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
                this.ctx.fireUserEventTriggered((Object) SslCloseCompletionEvent.SUCCESS);
            }
        } else if (this.sslClosePromise.tryFailure(cause)) {
            this.ctx.fireUserEventTriggered((Object) new SslCloseCompletionEvent(cause));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [io.netty.channel.ChannelPromise] */
    /* JADX WARN: Type inference failed for: r3v7, types: [io.netty.channel.ChannelPromise] */
    private void closeOutboundAndChannel(ChannelHandlerContext ctx, final ChannelPromise promise, boolean disconnect) throws Exception {
        this.outboundClosed = true;
        this.engine.closeOutbound();
        if (!ctx.channel().isActive()) {
            if (disconnect) {
                ctx.disconnect(promise);
                return;
            } else {
                ctx.close(promise);
                return;
            }
        }
        ChannelPromise closeNotifyPromise = ctx.newPromise();
        try {
            flush(ctx, closeNotifyPromise);
            if (!this.closeNotify) {
                this.closeNotify = true;
                safeClose(ctx, closeNotifyPromise, ctx.newPromise().addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelPromiseNotifier(false, promise)));
            } else {
                this.sslClosePromise.addListener2((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.3
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) {
                        promise.setSuccess();
                    }
                });
            }
        } catch (Throwable th) {
            if (!this.closeNotify) {
                this.closeNotify = true;
                safeClose(ctx, closeNotifyPromise, ctx.newPromise().addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelPromiseNotifier(false, promise)));
            } else {
                this.sslClosePromise.addListener2((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.3
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) {
                        promise.setSuccess();
                    }
                });
            }
            throw th;
        }
    }

    private void flush(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
        } else {
            promise.setFailure((Throwable) newPendingWritesNullException());
        }
        flush(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.pendingUnencryptedWrites = new SslHandlerCoalescingBufferQueue(ctx.channel(), 16);
        if (ctx.channel().isActive()) {
            startHandshakeProcessing();
        }
    }

    private void startHandshakeProcessing() {
        if (!this.handshakeStarted) {
            this.handshakeStarted = true;
            if (this.engine.getUseClientMode()) {
                handshake();
            }
            applyHandshakeTimeout();
        }
    }

    public Future<Channel> renegotiate() {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        return renegotiate(ctx.executor().newPromise());
    }

    public Future<Channel> renegotiate(final Promise<Channel> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        EventExecutor executor = ctx.executor();
        if (!executor.inEventLoop()) {
            executor.execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.4
                @Override // java.lang.Runnable
                public void run() {
                    SslHandler.this.renegotiateOnEventLoop(promise);
                }
            });
            return promise;
        }
        renegotiateOnEventLoop(promise);
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renegotiateOnEventLoop(Promise<Channel> newHandshakePromise) {
        Promise<Channel> oldHandshakePromise = this.handshakePromise;
        if (!oldHandshakePromise.isDone()) {
            oldHandshakePromise.addListener2((GenericFutureListener<? extends Future<? super Channel>>) new PromiseNotifier(newHandshakePromise));
            return;
        }
        this.handshakePromise = newHandshakePromise;
        handshake();
        applyHandshakeTimeout();
    }

    private void handshake() {
        if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING || this.handshakePromise.isDone()) {
            return;
        }
        ChannelHandlerContext ctx = this.ctx;
        try {
            this.engine.beginHandshake();
            wrapNonAppData(ctx, false);
        } catch (Throwable e) {
            setHandshakeFailure(ctx, e);
        } finally {
            forceFlush(ctx);
        }
    }

    private void applyHandshakeTimeout() {
        final Promise<Channel> localHandshakePromise = this.handshakePromise;
        final long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
        if (handshakeTimeoutMillis <= 0 || localHandshakePromise.isDone()) {
            return;
        }
        final ScheduledFuture<?> timeoutFuture = this.ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.5
            @Override // java.lang.Runnable
            public void run() {
                if (localHandshakePromise.isDone()) {
                    return;
                }
                SSLException exception = new SslHandshakeTimeoutException("handshake timed out after " + handshakeTimeoutMillis + "ms");
                try {
                    if (localHandshakePromise.tryFailure(exception)) {
                        SslUtils.handleHandshakeFailure(SslHandler.this.ctx, exception, true);
                    }
                } finally {
                    SslHandler.this.releaseAndFailAll(SslHandler.this.ctx, exception);
                }
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        localHandshakePromise.addListener2((GenericFutureListener<? extends Future<? super Channel>>) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.6
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Channel> f) throws Exception {
                timeoutFuture.cancel(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceFlush(ChannelHandlerContext ctx) {
        this.needsFlush = false;
        ctx.flush();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!this.startTls) {
            startHandshakeProcessing();
        }
        ctx.fireChannelActive();
    }

    private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
        ScheduledFuture<?> timeoutFuture;
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        if (!flushFuture.isDone()) {
            long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
            if (closeNotifyTimeout > 0) {
                timeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.7
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!flushFuture.isDone()) {
                            SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx.channel());
                            SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    }
                }, closeNotifyTimeout, TimeUnit.MILLISECONDS);
            } else {
                timeoutFuture = null;
            }
        } else {
            timeoutFuture = null;
        }
        final ScheduledFuture<?> scheduledFuture = timeoutFuture;
        flushFuture.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.ssl.SslHandler.8
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture f) throws Exception {
                ScheduledFuture<?> closeNotifyReadTimeoutFuture;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
                final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
                if (closeNotifyReadTimeout <= 0) {
                    SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                    return;
                }
                if (!SslHandler.this.sslClosePromise.isDone()) {
                    closeNotifyReadTimeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!SslHandler.this.sslClosePromise.isDone()) {
                                SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", ctx.channel(), Long.valueOf(closeNotifyReadTimeout));
                                SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                            }
                        }
                    }, closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
                } else {
                    closeNotifyReadTimeoutFuture = null;
                }
                final ScheduledFuture<?> scheduledFuture2 = closeNotifyReadTimeoutFuture;
                SslHandler.this.sslClosePromise.addListener2((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.8.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) throws Exception {
                        if (scheduledFuture2 != null) {
                            scheduledFuture2.cancel(false);
                        }
                        SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addCloseListener(ChannelFuture future, ChannelPromise promise) {
        future.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelPromiseNotifier(false, promise));
    }

    private ByteBuf allocate(ChannelHandlerContext ctx, int capacity) {
        ByteBufAllocator alloc = ctx.alloc();
        if (this.engineType.wantsDirectBuffer) {
            return alloc.directBuffer(capacity);
        }
        return alloc.buffer(capacity);
    }

    private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes, int numComponents) {
        return this.engineType.allocateWrapBuffer(this, ctx.alloc(), pendingBytes, numComponents);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler$SslHandlerCoalescingBufferQueue.class */
    private final class SslHandlerCoalescingBufferQueue extends AbstractCoalescingBufferQueue {
        SslHandlerCoalescingBufferQueue(Channel channel, int initSize) {
            super(channel, initSize);
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf compose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
            int wrapDataSize = SslHandler.this.wrapDataSize;
            if (!(cumulation instanceof CompositeByteBuf)) {
                return SslHandler.attemptCopyToCumulation(cumulation, next, wrapDataSize) ? cumulation : copyAndCompose(alloc, cumulation, next);
            }
            CompositeByteBuf composite = (CompositeByteBuf) cumulation;
            int numComponents = composite.numComponents();
            if (numComponents == 0 || !SslHandler.attemptCopyToCumulation(composite.internalComponent(numComponents - 1), next, wrapDataSize)) {
                composite.addComponent(true, next);
            }
            return composite;
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first) {
            if (first instanceof CompositeByteBuf) {
                CompositeByteBuf composite = (CompositeByteBuf) first;
                if (SslHandler.this.engineType.wantsDirectBuffer) {
                    first = allocator.directBuffer(composite.readableBytes());
                } else {
                    first = allocator.heapBuffer(composite.readableBytes());
                }
                try {
                    first.writeBytes(composite);
                } catch (Throwable cause) {
                    first.release();
                    PlatformDependent.throwException(cause);
                }
                composite.release();
            }
            return first;
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf removeEmptyValue() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean attemptCopyToCumulation(ByteBuf cumulation, ByteBuf next, int wrapDataSize) {
        int inReadableBytes = next.readableBytes();
        int cumulationCapacity = cumulation.capacity();
        if (wrapDataSize - cumulation.readableBytes() >= inReadableBytes) {
            if ((cumulation.isWritable(inReadableBytes) && cumulationCapacity >= wrapDataSize) || (cumulationCapacity < wrapDataSize && ByteBufUtil.ensureWritableSuccess(cumulation.ensureWritable(inReadableBytes, false)))) {
                cumulation.writeBytes(next);
                next.release();
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandler$LazyChannelPromise.class */
    private final class LazyChannelPromise extends DefaultPromise<Channel> {
        private LazyChannelPromise() {
        }

        @Override // io.netty.util.concurrent.DefaultPromise
        protected EventExecutor executor() {
            if (SslHandler.this.ctx != null) {
                return SslHandler.this.ctx.executor();
            }
            throw new IllegalStateException();
        }

        @Override // io.netty.util.concurrent.DefaultPromise
        protected void checkDeadLock() {
            if (SslHandler.this.ctx == null) {
                return;
            }
            super.checkDeadLock();
        }
    }
}

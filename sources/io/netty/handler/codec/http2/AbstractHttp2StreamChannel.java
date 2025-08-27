package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.VoidChannelPromise;
import io.netty.handler.codec.http2.Http2FrameCodec;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2StreamChannel.class */
abstract class AbstractHttp2StreamChannel extends DefaultAttributeMap implements Http2StreamChannel {
    static final Http2FrameStreamVisitor WRITABLE_VISITOR;
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private static final int MIN_HTTP2_FRAME_SIZE = 9;
    private static final AtomicLongFieldUpdater<AbstractHttp2StreamChannel> TOTAL_PENDING_SIZE_UPDATER;
    private static final AtomicIntegerFieldUpdater<AbstractHttp2StreamChannel> UNWRITABLE_UPDATER;
    private final ChannelId channelId;
    private final ChannelPipeline pipeline;
    private final Http2FrameCodec.DefaultHttp2FrameStream stream;
    private final ChannelPromise closePromise;
    private volatile boolean registered;
    private volatile long totalPendingSize;
    private volatile int unwritable;
    private Runnable fireChannelWritabilityChangedTask;
    private boolean outboundClosed;
    private int flowControlledBytes;
    private Queue<Object> inboundBuffer;
    private boolean firstFrameWritten;
    private boolean readCompletePending;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ChannelFutureListener windowUpdateFrameWriteListener = new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.2
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            AbstractHttp2StreamChannel.windowUpdateFrameWriteComplete(future, AbstractHttp2StreamChannel.this);
        }
    };
    private final Http2StreamChannelConfig config = new Http2StreamChannelConfig(this);
    private final Http2ChannelUnsafe unsafe = new Http2ChannelUnsafe();
    private ReadStatus readStatus = ReadStatus.IDLE;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2StreamChannel$ReadStatus.class */
    private enum ReadStatus {
        IDLE,
        IN_PROGRESS,
        REQUESTED
    }

    protected abstract boolean isParentReadInProgress();

    protected abstract void addChannelToReadCompletePendingQueue();

    protected abstract ChannelHandlerContext parentContext();

    static {
        $assertionsDisabled = !AbstractHttp2StreamChannel.class.desiredAssertionStatus();
        WRITABLE_VISITOR = new Http2FrameStreamVisitor() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.1
            @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
            public boolean visit(Http2FrameStream stream) {
                AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
                childChannel.trySetWritable();
                return true;
            }
        };
        logger = InternalLoggerFactory.getInstance((Class<?>) AbstractHttp2StreamChannel.class);
        METADATA = new ChannelMetadata(false, 16);
        TOTAL_PENDING_SIZE_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractHttp2StreamChannel.class, "totalPendingSize");
        UNWRITABLE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractHttp2StreamChannel.class, "unwritable");
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2StreamChannel$FlowControlledFrameSizeEstimator.class */
    private static final class FlowControlledFrameSizeEstimator implements MessageSizeEstimator {
        static final FlowControlledFrameSizeEstimator INSTANCE = new FlowControlledFrameSizeEstimator();
        private static final MessageSizeEstimator.Handle HANDLE_INSTANCE = new MessageSizeEstimator.Handle() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.FlowControlledFrameSizeEstimator.1
            @Override // io.netty.channel.MessageSizeEstimator.Handle
            public int size(Object msg) {
                if (msg instanceof Http2DataFrame) {
                    return (int) Math.min(2147483647L, ((Http2DataFrame) msg).initialFlowControlledBytes() + 9);
                }
                return 9;
            }
        };

        private FlowControlledFrameSizeEstimator() {
        }

        @Override // io.netty.channel.MessageSizeEstimator
        public MessageSizeEstimator.Handle newHandle() {
            return HANDLE_INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void windowUpdateFrameWriteComplete(ChannelFuture future, Channel streamChannel) {
        Throwable unwrappedCause;
        Throwable cause = future.cause();
        if (cause != null) {
            if ((cause instanceof Http2FrameStreamException) && (unwrappedCause = cause.getCause()) != null) {
                cause = unwrappedCause;
            }
            streamChannel.pipeline().fireExceptionCaught(cause);
            streamChannel.unsafe().close(streamChannel.unsafe().voidPromise());
        }
    }

    AbstractHttp2StreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, int id, ChannelHandler inboundHandler) {
        this.stream = stream;
        stream.attachment = this;
        this.pipeline = new DefaultChannelPipeline(this) { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.3
            @Override // io.netty.channel.DefaultChannelPipeline
            protected void incrementPendingOutboundBytes(long size) {
                AbstractHttp2StreamChannel.this.incrementPendingOutboundBytes(size, true);
            }

            @Override // io.netty.channel.DefaultChannelPipeline
            protected void decrementPendingOutboundBytes(long size) {
                AbstractHttp2StreamChannel.this.decrementPendingOutboundBytes(size, true);
            }
        };
        this.closePromise = this.pipeline.newPromise();
        this.channelId = new Http2StreamChannelId(parent().id(), id);
        if (inboundHandler != null) {
            this.pipeline.addLast(inboundHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementPendingOutboundBytes(long size, boolean invokeLater) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
        if (newWriteBufferSize > config().getWriteBufferHighWaterMark()) {
            setUnwritable(invokeLater);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementPendingOutboundBytes(long size, boolean invokeLater) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
        if (newWriteBufferSize < config().getWriteBufferLowWaterMark() && parent().isWritable()) {
            setWritable(invokeLater);
        }
    }

    final void trySetWritable() {
        if (this.totalPendingSize < config().getWriteBufferLowWaterMark()) {
            setWritable(false);
        }
    }

    private void setWritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue & (-2);
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue != 0 && newValue == 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void setUnwritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue | 1;
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue == 0 && newValue != 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void fireChannelWritabilityChanged(boolean invokeLater) {
        final ChannelPipeline pipeline = pipeline();
        if (invokeLater) {
            Runnable task = this.fireChannelWritabilityChangedTask;
            if (task == null) {
                Runnable runnable = new Runnable() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.4
                    @Override // java.lang.Runnable
                    public void run() {
                        pipeline.fireChannelWritabilityChanged();
                    }
                };
                task = runnable;
                this.fireChannelWritabilityChangedTask = runnable;
            }
            eventLoop().execute(task);
            return;
        }
        pipeline.fireChannelWritabilityChanged();
    }

    @Override // io.netty.handler.codec.http2.Http2StreamChannel
    public Http2FrameStream stream() {
        return this.stream;
    }

    void closeOutbound() {
        this.outboundClosed = true;
    }

    void streamClosed() {
        this.unsafe.readEOS();
        this.unsafe.doBeginRead();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return !this.closePromise.isDone();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen();
    }

    @Override // io.netty.channel.Channel
    public boolean isWritable() {
        return this.unwritable == 0;
    }

    @Override // io.netty.channel.Channel
    public ChannelId id() {
        return this.channelId;
    }

    @Override // io.netty.channel.Channel
    public EventLoop eventLoop() {
        return parent().eventLoop();
    }

    @Override // io.netty.channel.Channel
    public Channel parent() {
        return parentContext().channel();
    }

    @Override // io.netty.channel.Channel
    public boolean isRegistered() {
        return this.registered;
    }

    @Override // io.netty.channel.Channel
    public SocketAddress localAddress() {
        return parent().localAddress();
    }

    @Override // io.netty.channel.Channel
    public SocketAddress remoteAddress() {
        return parent().remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public ChannelFuture closeFuture() {
        return this.closePromise;
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeUnwritable() {
        long bytes = config().getWriteBufferHighWaterMark() - this.totalPendingSize;
        if (bytes <= 0 || !isWritable()) {
            return 0L;
        }
        return bytes;
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeWritable() {
        long bytes = this.totalPendingSize - config().getWriteBufferLowWaterMark();
        if (bytes <= 0 || isWritable()) {
            return 0L;
        }
        return bytes;
    }

    @Override // io.netty.channel.Channel
    public Channel.Unsafe unsafe() {
        return this.unsafe;
    }

    @Override // io.netty.channel.Channel
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override // io.netty.channel.Channel
    public ByteBufAllocator alloc() {
        return config().getAllocator();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel read() {
        pipeline().read();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel flush() {
        pipeline().flush();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress) {
        return pipeline().bind(localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return pipeline().connect(remoteAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return pipeline().connect(remoteAddress, localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect() {
        return pipeline().disconnect();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close() {
        return pipeline().close();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister() {
        return pipeline().deregister();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return pipeline().bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return pipeline().connect(remoteAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return pipeline().connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect(ChannelPromise promise) {
        return pipeline().disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close(ChannelPromise promise) {
        return pipeline().close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister(ChannelPromise promise) {
        return pipeline().deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg) {
        return pipeline().write(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return pipeline().write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return pipeline().writeAndFlush(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg) {
        return pipeline().writeAndFlush(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise newPromise() {
        return pipeline().newPromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelProgressivePromise newProgressivePromise() {
        return pipeline().newProgressivePromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newSucceededFuture() {
        return pipeline().newSucceededFuture();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newFailedFuture(Throwable cause) {
        return pipeline().newFailedFuture(cause);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise voidPromise() {
        return pipeline().voidPromise();
    }

    public int hashCode() {
        return id().hashCode();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    @Override // java.lang.Comparable
    public int compareTo(Channel o) {
        if (this == o) {
            return 0;
        }
        return id().compareTo(o.id());
    }

    public String toString() {
        return parent().toString() + "(H2 - " + this.stream + ')';
    }

    void fireChildRead(Http2Frame frame) {
        if (!$assertionsDisabled && !eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        if (!isActive()) {
            ReferenceCountUtil.release(frame);
            return;
        }
        if (this.readStatus != ReadStatus.IDLE) {
            if (!$assertionsDisabled && this.inboundBuffer != null && !this.inboundBuffer.isEmpty()) {
                throw new AssertionError();
            }
            RecvByteBufAllocator.Handle allocHandle = this.unsafe.recvBufAllocHandle();
            this.unsafe.doRead0(frame, allocHandle);
            if (allocHandle.continueReading()) {
                maybeAddChannelToReadCompletePendingQueue();
                return;
            } else {
                this.unsafe.notifyReadComplete(allocHandle, true);
                return;
            }
        }
        if (this.inboundBuffer == null) {
            this.inboundBuffer = new ArrayDeque(4);
        }
        this.inboundBuffer.add(frame);
    }

    void fireChildReadComplete() {
        if (!$assertionsDisabled && !eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.readStatus == ReadStatus.IDLE && this.readCompletePending) {
            throw new AssertionError();
        }
        this.unsafe.notifyReadComplete(this.unsafe.recvBufAllocHandle(), false);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2StreamChannel$Http2ChannelUnsafe.class */
    private final class Http2ChannelUnsafe implements Channel.Unsafe {
        private final VoidChannelPromise unsafeVoidPromise;
        private RecvByteBufAllocator.Handle recvHandle;
        private boolean writeDoneAndNoFlush;
        private boolean closeInitiated;
        private boolean readEOS;

        private Http2ChannelUnsafe() {
            this.unsafeVoidPromise = new VoidChannelPromise(AbstractHttp2StreamChannel.this, false);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            promise.setFailure((Throwable) new UnsupportedOperationException());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public RecvByteBufAllocator.Handle recvBufAllocHandle() {
            if (this.recvHandle == null) {
                this.recvHandle = AbstractHttp2StreamChannel.this.config().getRecvByteBufAllocator().newHandle();
                this.recvHandle.reset(AbstractHttp2StreamChannel.this.config());
            }
            return this.recvHandle;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public SocketAddress localAddress() {
            return AbstractHttp2StreamChannel.this.parent().unsafe().localAddress();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public SocketAddress remoteAddress() {
            return AbstractHttp2StreamChannel.this.parent().unsafe().remoteAddress();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void register(EventLoop eventLoop, ChannelPromise promise) {
            if (promise.setUncancellable()) {
                if (!AbstractHttp2StreamChannel.this.registered) {
                    AbstractHttp2StreamChannel.this.registered = true;
                    promise.setSuccess();
                    AbstractHttp2StreamChannel.this.pipeline().fireChannelRegistered();
                    if (AbstractHttp2StreamChannel.this.isActive()) {
                        AbstractHttp2StreamChannel.this.pipeline().fireChannelActive();
                        return;
                    }
                    return;
                }
                promise.setFailure((Throwable) new UnsupportedOperationException("Re-register is not supported"));
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void bind(SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            promise.setFailure((Throwable) new UnsupportedOperationException());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void disconnect(ChannelPromise promise) {
            close(promise);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void close(final ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            if (this.closeInitiated) {
                if (AbstractHttp2StreamChannel.this.closePromise.isDone()) {
                    promise.setSuccess();
                    return;
                } else {
                    if (!(promise instanceof VoidChannelPromise)) {
                        AbstractHttp2StreamChannel.this.closePromise.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.1
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future) {
                                promise.setSuccess();
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            this.closeInitiated = true;
            AbstractHttp2StreamChannel.this.readCompletePending = false;
            boolean wasActive = AbstractHttp2StreamChannel.this.isActive();
            if (AbstractHttp2StreamChannel.this.parent().isActive() && !this.readEOS && Http2CodecUtil.isStreamIdValid(AbstractHttp2StreamChannel.this.stream.id())) {
                Http2StreamFrame resetFrame = new DefaultHttp2ResetFrame(Http2Error.CANCEL).stream(AbstractHttp2StreamChannel.this.stream());
                write(resetFrame, AbstractHttp2StreamChannel.this.unsafe().voidPromise());
                flush();
            }
            if (AbstractHttp2StreamChannel.this.inboundBuffer != null) {
                while (true) {
                    Object msg = AbstractHttp2StreamChannel.this.inboundBuffer.poll();
                    if (msg == null) {
                        break;
                    } else {
                        ReferenceCountUtil.release(msg);
                    }
                }
                AbstractHttp2StreamChannel.this.inboundBuffer = null;
            }
            AbstractHttp2StreamChannel.this.outboundClosed = true;
            AbstractHttp2StreamChannel.this.closePromise.setSuccess();
            promise.setSuccess();
            fireChannelInactiveAndDeregister(voidPromise(), wasActive);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void closeForcibly() {
            close(AbstractHttp2StreamChannel.this.unsafe().voidPromise());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void deregister(ChannelPromise promise) {
            fireChannelInactiveAndDeregister(promise, false);
        }

        private void fireChannelInactiveAndDeregister(final ChannelPromise promise, final boolean fireChannelInactive) {
            if (promise.setUncancellable()) {
                if (!AbstractHttp2StreamChannel.this.registered) {
                    promise.setSuccess();
                } else {
                    invokeLater(new Runnable() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (fireChannelInactive) {
                                AbstractHttp2StreamChannel.this.pipeline.fireChannelInactive();
                            }
                            if (AbstractHttp2StreamChannel.this.registered) {
                                AbstractHttp2StreamChannel.this.registered = false;
                                AbstractHttp2StreamChannel.this.pipeline.fireChannelUnregistered();
                            }
                            Http2ChannelUnsafe.this.safeSetSuccess(promise);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void safeSetSuccess(ChannelPromise promise) {
            if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
                AbstractHttp2StreamChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }

        private void invokeLater(Runnable task) {
            try {
                AbstractHttp2StreamChannel.this.eventLoop().execute(task);
            } catch (RejectedExecutionException e) {
                AbstractHttp2StreamChannel.logger.warn("Can't invoke task later as EventLoop rejected it", (Throwable) e);
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void beginRead() {
            if (!AbstractHttp2StreamChannel.this.isActive()) {
            }
            updateLocalWindowIfNeeded();
            switch (AbstractHttp2StreamChannel.this.readStatus) {
                case IDLE:
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.IN_PROGRESS;
                    doBeginRead();
                    break;
                case IN_PROGRESS:
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.REQUESTED;
                    break;
            }
        }

        private Object pollQueuedMessage() {
            if (AbstractHttp2StreamChannel.this.inboundBuffer == null) {
                return null;
            }
            return AbstractHttp2StreamChannel.this.inboundBuffer.poll();
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x005d A[PHI: r7
  0x005d: PHI (r7v2 'continueReading' boolean) = (r7v1 'continueReading' boolean), (r7v4 'continueReading' boolean) binds: [B:12:0x004f, B:14:0x005a] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void doBeginRead() {
            /*
                r4 = this;
            L0:
                r0 = r4
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.this
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel$ReadStatus r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.access$1300(r0)
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel$ReadStatus r1 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.ReadStatus.IDLE
                if (r0 == r1) goto L8e
                r0 = r4
                java.lang.Object r0 = r0.pollQueuedMessage()
                r5 = r0
                r0 = r5
                if (r0 != 0) goto L2e
                r0 = r4
                boolean r0 = r0.readEOS
                if (r0 == 0) goto L27
                r0 = r4
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.this
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel$Http2ChannelUnsafe r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.access$1400(r0)
                r0.closeForcibly()
            L27:
                r0 = r4
                r0.flush()
                goto L8e
            L2e:
                r0 = r4
                io.netty.channel.RecvByteBufAllocator$Handle r0 = r0.recvBufAllocHandle()
                r6 = r0
                r0 = r6
                r1 = r4
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel r1 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.this
                io.netty.channel.ChannelConfig r1 = r1.config()
                r0.reset(r1)
                r0 = 0
                r7 = r0
            L42:
                r0 = r4
                r1 = r5
                io.netty.handler.codec.http2.Http2Frame r1 = (io.netty.handler.codec.http2.Http2Frame) r1
                r2 = r6
                r0.doRead0(r1, r2)
                r0 = r4
                boolean r0 = r0.readEOS
                if (r0 != 0) goto L5d
                r0 = r6
                boolean r0 = r0.continueReading()
                r1 = r0
                r7 = r1
                if (r0 == 0) goto L66
            L5d:
                r0 = r4
                java.lang.Object r0 = r0.pollQueuedMessage()
                r1 = r0
                r5 = r1
                if (r0 != 0) goto L42
            L66:
                r0 = r7
                if (r0 == 0) goto L85
                r0 = r4
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.this
                boolean r0 = r0.isParentReadInProgress()
                if (r0 == 0) goto L85
                r0 = r4
                boolean r0 = r0.readEOS
                if (r0 != 0) goto L85
                r0 = r4
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel r0 = io.netty.handler.codec.http2.AbstractHttp2StreamChannel.this
                io.netty.handler.codec.http2.AbstractHttp2StreamChannel.access$1500(r0)
                goto L8b
            L85:
                r0 = r4
                r1 = r6
                r2 = 1
                r0.notifyReadComplete(r1, r2)
            L8b:
                goto L0
            L8e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.doBeginRead():void");
        }

        void readEOS() {
            this.readEOS = true;
        }

        private void updateLocalWindowIfNeeded() {
            if (AbstractHttp2StreamChannel.this.flowControlledBytes != 0) {
                int bytes = AbstractHttp2StreamChannel.this.flowControlledBytes;
                AbstractHttp2StreamChannel.this.flowControlledBytes = 0;
                ChannelFuture future = AbstractHttp2StreamChannel.this.write0(AbstractHttp2StreamChannel.this.parentContext(), new DefaultHttp2WindowUpdateFrame(bytes).stream((Http2FrameStream) AbstractHttp2StreamChannel.this.stream));
                this.writeDoneAndNoFlush = true;
                if (future.isDone()) {
                    AbstractHttp2StreamChannel.windowUpdateFrameWriteComplete(future, AbstractHttp2StreamChannel.this);
                } else {
                    future.addListener2((GenericFutureListener<? extends Future<? super Void>>) AbstractHttp2StreamChannel.this.windowUpdateFrameWriteListener);
                }
            }
        }

        void notifyReadComplete(RecvByteBufAllocator.Handle allocHandle, boolean forceReadComplete) {
            if (AbstractHttp2StreamChannel.this.readCompletePending || forceReadComplete) {
                AbstractHttp2StreamChannel.this.readCompletePending = false;
                if (AbstractHttp2StreamChannel.this.readStatus == ReadStatus.REQUESTED) {
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.IN_PROGRESS;
                } else {
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.IDLE;
                }
                allocHandle.readComplete();
                AbstractHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
                flush();
                if (this.readEOS) {
                    AbstractHttp2StreamChannel.this.unsafe.closeForcibly();
                }
            }
        }

        void doRead0(Http2Frame frame, RecvByteBufAllocator.Handle allocHandle) {
            int bytes;
            if (frame instanceof Http2DataFrame) {
                bytes = ((Http2DataFrame) frame).initialFlowControlledBytes();
                AbstractHttp2StreamChannel.this.flowControlledBytes += bytes;
            } else {
                bytes = 9;
            }
            allocHandle.attemptedBytesRead(bytes);
            allocHandle.lastBytesRead(bytes);
            allocHandle.incMessagesRead(1);
            AbstractHttp2StreamChannel.this.pipeline().fireChannelRead((Object) frame);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void write(Object msg, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                ReferenceCountUtil.release(msg);
                return;
            }
            if (!AbstractHttp2StreamChannel.this.isActive() || (AbstractHttp2StreamChannel.this.outboundClosed && ((msg instanceof Http2HeadersFrame) || (msg instanceof Http2DataFrame)))) {
                ReferenceCountUtil.release(msg);
                promise.setFailure((Throwable) new ClosedChannelException());
                return;
            }
            try {
                if (msg instanceof Http2StreamFrame) {
                    Http2StreamFrame frame = validateStreamFrame((Http2StreamFrame) msg).stream(AbstractHttp2StreamChannel.this.stream());
                    writeHttp2StreamFrame(frame, promise);
                } else {
                    String msgStr = msg.toString();
                    ReferenceCountUtil.release(msg);
                    promise.setFailure((Throwable) new IllegalArgumentException("Message must be an " + StringUtil.simpleClassName((Class<?>) Http2StreamFrame.class) + ": " + msgStr));
                }
            } catch (Throwable t) {
                promise.tryFailure(t);
            }
        }

        private void writeHttp2StreamFrame(Http2StreamFrame frame, final ChannelPromise promise) {
            boolean firstWrite;
            if (AbstractHttp2StreamChannel.this.firstFrameWritten || Http2CodecUtil.isStreamIdValid(AbstractHttp2StreamChannel.this.stream().id()) || (frame instanceof Http2HeadersFrame)) {
                if (!AbstractHttp2StreamChannel.this.firstFrameWritten) {
                    firstWrite = AbstractHttp2StreamChannel.this.firstFrameWritten = true;
                } else {
                    firstWrite = false;
                }
                ChannelFuture f = AbstractHttp2StreamChannel.this.write0(AbstractHttp2StreamChannel.this.parentContext(), frame);
                if (!f.isDone()) {
                    final long bytes = FlowControlledFrameSizeEstimator.HANDLE_INSTANCE.size(frame);
                    AbstractHttp2StreamChannel.this.incrementPendingOutboundBytes(bytes, false);
                    final boolean z = firstWrite;
                    f.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.3
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(ChannelFuture future) {
                            if (z) {
                                Http2ChannelUnsafe.this.firstWriteComplete(future, promise);
                            } else {
                                Http2ChannelUnsafe.this.writeComplete(future, promise);
                            }
                            AbstractHttp2StreamChannel.this.decrementPendingOutboundBytes(bytes, false);
                        }
                    });
                    this.writeDoneAndNoFlush = true;
                    return;
                }
                if (firstWrite) {
                    firstWriteComplete(f, promise);
                    return;
                } else {
                    writeComplete(f, promise);
                    return;
                }
            }
            ReferenceCountUtil.release(frame);
            promise.setFailure((Throwable) new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame.name()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void firstWriteComplete(ChannelFuture future, ChannelPromise promise) {
            Throwable cause = future.cause();
            if (cause == null) {
                promise.setSuccess();
            } else {
                closeForcibly();
                promise.setFailure(wrapStreamClosedError(cause));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeComplete(ChannelFuture future, ChannelPromise promise) {
            Throwable cause = future.cause();
            if (cause == null) {
                promise.setSuccess();
                return;
            }
            Throwable error = wrapStreamClosedError(cause);
            if (error instanceof IOException) {
                if (!AbstractHttp2StreamChannel.this.config.isAutoClose()) {
                    AbstractHttp2StreamChannel.this.outboundClosed = true;
                } else {
                    closeForcibly();
                }
            }
            promise.setFailure(error);
        }

        private Throwable wrapStreamClosedError(Throwable cause) {
            if ((cause instanceof Http2Exception) && ((Http2Exception) cause).error() == Http2Error.STREAM_CLOSED) {
                return new ClosedChannelException().initCause(cause);
            }
            return cause;
        }

        private Http2StreamFrame validateStreamFrame(Http2StreamFrame frame) {
            if (frame.stream() != null && frame.stream() != AbstractHttp2StreamChannel.this.stream) {
                String msgString = frame.toString();
                ReferenceCountUtil.release(frame);
                throw new IllegalArgumentException("Stream " + frame.stream() + " must not be set on the frame: " + msgString);
            }
            return frame;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void flush() {
            if (!this.writeDoneAndNoFlush || AbstractHttp2StreamChannel.this.isParentReadInProgress()) {
                return;
            }
            this.writeDoneAndNoFlush = false;
            AbstractHttp2StreamChannel.this.flush0(AbstractHttp2StreamChannel.this.parentContext());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public ChannelPromise voidPromise() {
            return this.unsafeVoidPromise;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public ChannelOutboundBuffer outboundBuffer() {
            return null;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractHttp2StreamChannel$Http2StreamChannelConfig.class */
    private static final class Http2StreamChannelConfig extends DefaultChannelConfig {
        Http2StreamChannelConfig(Channel channel) {
            super(channel);
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public MessageSizeEstimator getMessageSizeEstimator() {
            return FlowControlledFrameSizeEstimator.INSTANCE;
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
            if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
                throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
            }
            super.setRecvByteBufAllocator(allocator);
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAddChannelToReadCompletePendingQueue() {
        if (!this.readCompletePending) {
            this.readCompletePending = true;
            addChannelToReadCompletePendingQueue();
        }
    }

    protected void flush0(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    protected ChannelFuture write0(ChannelHandlerContext ctx, Object msg) {
        ChannelPromise promise = ctx.newPromise();
        ctx.write(msg, promise);
        return promise;
    }
}

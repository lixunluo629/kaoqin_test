package io.netty.channel.local;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.PreferHeapByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/local/LocalChannel.class */
public class LocalChannel extends AbstractChannel {
    private static final InternalLogger logger;
    private static final AtomicReferenceFieldUpdater<LocalChannel, Future> FINISH_READ_FUTURE_UPDATER;
    private static final ChannelMetadata METADATA;
    private static final int MAX_READER_STACK_DEPTH = 8;
    private final ChannelConfig config;
    final Queue<Object> inboundBuffer;
    private final Runnable readTask;
    private final Runnable shutdownHook;
    private volatile State state;
    private volatile LocalChannel peer;
    private volatile LocalAddress localAddress;
    private volatile LocalAddress remoteAddress;
    private volatile ChannelPromise connectPromise;
    private volatile boolean readInProgress;
    private volatile boolean writeInProgress;
    private volatile Future<?> finishReadFuture;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/local/LocalChannel$State.class */
    private enum State {
        OPEN,
        BOUND,
        CONNECTED,
        CLOSED
    }

    static {
        $assertionsDisabled = !LocalChannel.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) LocalChannel.class);
        FINISH_READ_FUTURE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(LocalChannel.class, Future.class, "finishReadFuture");
        METADATA = new ChannelMetadata(false);
    }

    public LocalChannel() {
        super(null);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = PlatformDependent.newSpscQueue();
        this.readTask = new Runnable() { // from class: io.netty.channel.local.LocalChannel.1
            @Override // java.lang.Runnable
            public void run() {
                if (!LocalChannel.this.inboundBuffer.isEmpty()) {
                    LocalChannel.this.readInbound();
                }
            }
        };
        this.shutdownHook = new Runnable() { // from class: io.netty.channel.local.LocalChannel.2
            @Override // java.lang.Runnable
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
        config().setAllocator(new PreferHeapByteBufAllocator(this.config.getAllocator()));
    }

    protected LocalChannel(LocalServerChannel parent, LocalChannel peer) {
        super(parent);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = PlatformDependent.newSpscQueue();
        this.readTask = new Runnable() { // from class: io.netty.channel.local.LocalChannel.1
            @Override // java.lang.Runnable
            public void run() {
                if (!LocalChannel.this.inboundBuffer.isEmpty()) {
                    LocalChannel.this.readInbound();
                }
            }
        };
        this.shutdownHook = new Runnable() { // from class: io.netty.channel.local.LocalChannel.2
            @Override // java.lang.Runnable
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
        config().setAllocator(new PreferHeapByteBufAllocator(this.config.getAllocator()));
        this.peer = peer;
        this.localAddress = parent.localAddress();
        this.remoteAddress = peer.localAddress();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public LocalServerChannel parent() {
        return (LocalServerChannel) super.parent();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public LocalAddress localAddress() {
        return (LocalAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public LocalAddress remoteAddress() {
        return (LocalAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.state != State.CLOSED;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return this.state == State.CONNECTED;
    }

    @Override // io.netty.channel.AbstractChannel
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new LocalUnsafe();
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof SingleThreadEventLoop;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return this.localAddress;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return this.remoteAddress;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        if (this.peer != null && parent() != null) {
            final LocalChannel peer = this.peer;
            this.state = State.CONNECTED;
            peer.remoteAddress = parent() == null ? null : parent().localAddress();
            peer.state = State.CONNECTED;
            peer.eventLoop().execute(new Runnable() { // from class: io.netty.channel.local.LocalChannel.3
                @Override // java.lang.Runnable
                public void run() {
                    ChannelPromise promise = peer.connectPromise;
                    if (promise != null && promise.trySuccess()) {
                        peer.pipeline().fireChannelActive();
                    }
                }
            });
        }
        ((SingleThreadEventExecutor) eventLoop()).addShutdownHook(this.shutdownHook);
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
        this.state = State.BOUND;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        final LocalChannel peer = this.peer;
        State oldState = this.state;
        try {
            if (oldState != State.CLOSED) {
                if (this.localAddress != null) {
                    if (parent() == null) {
                        LocalChannelRegistry.unregister(this.localAddress);
                    }
                    this.localAddress = null;
                }
                this.state = State.CLOSED;
                if (this.writeInProgress && peer != null) {
                    finishPeerRead(peer);
                }
                ChannelPromise promise = this.connectPromise;
                if (promise != null) {
                    promise.tryFailure(new ClosedChannelException());
                    this.connectPromise = null;
                }
            }
            if (peer != null) {
                this.peer = null;
                EventLoop peerEventLoop = peer.eventLoop();
                final boolean peerIsActive = peer.isActive();
                try {
                    peerEventLoop.execute(new Runnable() { // from class: io.netty.channel.local.LocalChannel.4
                        @Override // java.lang.Runnable
                        public void run() {
                            peer.tryClose(peerIsActive);
                        }
                    });
                } catch (Throwable cause) {
                    logger.warn("Releasing Inbound Queues for channels {}-{} because exception occurred!", this, peer, cause);
                    if (peerEventLoop.inEventLoop()) {
                        peer.releaseInboundBuffers();
                    } else {
                        peer.close();
                    }
                    PlatformDependent.throwException(cause);
                }
            }
        } finally {
            if (oldState != null && oldState != State.CLOSED) {
                releaseInboundBuffers();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryClose(boolean isActive) {
        if (isActive) {
            unsafe().close(unsafe().voidPromise());
        } else {
            releaseInboundBuffers();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDeregister() throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).removeShutdownHook(this.shutdownHook);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readInbound() {
        RecvByteBufAllocator.Handle handle = unsafe().recvBufAllocHandle();
        handle.reset(config());
        ChannelPipeline pipeline = pipeline();
        do {
            Object received = this.inboundBuffer.poll();
            if (received == null) {
                break;
            } else {
                pipeline.fireChannelRead(received);
            }
        } while (handle.continueReading());
        pipeline.fireChannelReadComplete();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
        if (this.readInProgress) {
            return;
        }
        Queue<Object> inboundBuffer = this.inboundBuffer;
        if (inboundBuffer.isEmpty()) {
            this.readInProgress = true;
            return;
        }
        InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
        Integer stackDepth = Integer.valueOf(threadLocals.localChannelReaderStackDepth());
        if (stackDepth.intValue() < 8) {
            threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue() + 1);
            try {
                readInbound();
                threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue());
                return;
            } catch (Throwable th) {
                threadLocals.setLocalChannelReaderStackDepth(stackDepth.intValue());
                throw th;
            }
        }
        try {
            eventLoop().execute(this.readTask);
        } catch (Throwable cause) {
            logger.warn("Closing Local channels {}-{} because exception occurred!", this, this.peer, cause);
            close();
            this.peer.close();
            PlatformDependent.throwException(cause);
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        switch (this.state) {
            case OPEN:
            case BOUND:
                throw new NotYetConnectedException();
            case CLOSED:
                throw new ClosedChannelException();
            case CONNECTED:
            default:
                LocalChannel peer = this.peer;
                this.writeInProgress = true;
                ClosedChannelException exception = null;
                while (true) {
                    try {
                        Object msg = in.current();
                        if (msg != null) {
                            try {
                                if (peer.state == State.CONNECTED) {
                                    peer.inboundBuffer.add(ReferenceCountUtil.retain(msg));
                                    in.remove();
                                } else {
                                    if (exception == null) {
                                        exception = new ClosedChannelException();
                                    }
                                    in.remove(exception);
                                }
                            } catch (Throwable cause) {
                                in.remove(cause);
                            }
                        } else {
                            finishPeerRead(peer);
                            return;
                        }
                    } finally {
                        this.writeInProgress = false;
                    }
                }
        }
    }

    private void finishPeerRead(LocalChannel peer) {
        if (peer.eventLoop() == eventLoop() && !peer.writeInProgress) {
            finishPeerRead0(peer);
        } else {
            runFinishPeerReadTask(peer);
        }
    }

    private void runFinishPeerReadTask(final LocalChannel peer) {
        Runnable finishPeerReadTask = new Runnable() { // from class: io.netty.channel.local.LocalChannel.5
            @Override // java.lang.Runnable
            public void run() {
                LocalChannel.this.finishPeerRead0(peer);
            }
        };
        try {
            if (peer.writeInProgress) {
                peer.finishReadFuture = peer.eventLoop().submit(finishPeerReadTask);
            } else {
                peer.eventLoop().execute(finishPeerReadTask);
            }
        } catch (Throwable cause) {
            logger.warn("Closing Local channels {}-{} because exception occurred!", this, peer, cause);
            close();
            peer.close();
            PlatformDependent.throwException(cause);
        }
    }

    private void releaseInboundBuffers() {
        if (!$assertionsDisabled && eventLoop() != null && !eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        this.readInProgress = false;
        Queue<Object> inboundBuffer = this.inboundBuffer;
        while (true) {
            Object msg = inboundBuffer.poll();
            if (msg != null) {
                ReferenceCountUtil.release(msg);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPeerRead0(LocalChannel peer) {
        Future<?> peerFinishReadFuture = peer.finishReadFuture;
        if (peerFinishReadFuture != null) {
            if (!peerFinishReadFuture.isDone()) {
                runFinishPeerReadTask(peer);
                return;
            }
            FINISH_READ_FUTURE_UPDATER.compareAndSet(peer, peerFinishReadFuture, null);
        }
        if (peer.readInProgress && !peer.inboundBuffer.isEmpty()) {
            peer.readInProgress = false;
            peer.readInbound();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/local/LocalChannel$LocalUnsafe.class */
    private class LocalUnsafe extends AbstractChannel.AbstractUnsafe {
        private LocalUnsafe() {
            super();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (promise.setUncancellable() && ensureOpen(promise)) {
                if (LocalChannel.this.state != State.CONNECTED) {
                    if (LocalChannel.this.connectPromise == null) {
                        LocalChannel.this.connectPromise = promise;
                        if (LocalChannel.this.state != State.BOUND && localAddress == null) {
                            localAddress = new LocalAddress(LocalChannel.this);
                        }
                        if (localAddress != null) {
                            try {
                                LocalChannel.this.doBind(localAddress);
                            } catch (Throwable t) {
                                safeSetFailure(promise, t);
                                close(voidPromise());
                                return;
                            }
                        }
                        Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
                        if (!(boundChannel instanceof LocalServerChannel)) {
                            safeSetFailure(promise, new ConnectException("connection refused: " + remoteAddress));
                            close(voidPromise());
                            return;
                        } else {
                            LocalServerChannel serverChannel = (LocalServerChannel) boundChannel;
                            LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
                            return;
                        }
                    }
                    throw new ConnectionPendingException();
                }
                Exception cause = new AlreadyConnectedException();
                safeSetFailure(promise, cause);
                LocalChannel.this.pipeline().fireExceptionCaught((Throwable) cause);
            }
        }
    }
}

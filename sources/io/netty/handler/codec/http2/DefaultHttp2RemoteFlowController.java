package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2RemoteFlowController;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.codec.http2.StreamByteDistributor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController.class */
public class DefaultHttp2RemoteFlowController implements Http2RemoteFlowController {
    private static final InternalLogger logger;
    private static final int MIN_WRITABLE_CHUNK = 32768;
    private final Http2Connection connection;
    private final Http2Connection.PropertyKey stateKey;
    private final StreamByteDistributor streamByteDistributor;
    private final FlowState connectionState;
    private int initialWindowSize;
    private WritabilityMonitor monitor;
    private ChannelHandlerContext ctx;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DefaultHttp2RemoteFlowController.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) DefaultHttp2RemoteFlowController.class);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection) {
        this(connection, (Http2RemoteFlowController.Listener) null);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor) {
        this(connection, streamByteDistributor, null);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, Http2RemoteFlowController.Listener listener) {
        this(connection, new WeightedFairQueueByteDistributor(connection), listener);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor, Http2RemoteFlowController.Listener listener) {
        this.initialWindowSize = 65535;
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        this.streamByteDistributor = (StreamByteDistributor) ObjectUtil.checkNotNull(streamByteDistributor, "streamWriteDistributor");
        this.stateKey = connection.newKey();
        this.connectionState = new FlowState(connection.connectionStream());
        connection.connectionStream().setProperty(this.stateKey, this.connectionState);
        listener(listener);
        this.monitor.windowSize(this.connectionState, this.initialWindowSize);
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                stream.setProperty(DefaultHttp2RemoteFlowController.this.stateKey, DefaultHttp2RemoteFlowController.this.new FlowState(stream));
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamActive(Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.monitor.windowSize(DefaultHttp2RemoteFlowController.this.state(stream), DefaultHttp2RemoteFlowController.this.initialWindowSize);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamHalfClosed(Http2Stream stream) {
                if (Http2Stream.State.HALF_CLOSED_LOCAL == stream.state()) {
                    DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
                }
            }
        });
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void channelHandlerContext(ChannelHandlerContext ctx) throws Http2Exception {
        this.ctx = (ChannelHandlerContext) ObjectUtil.checkNotNull(ctx, "ctx");
        channelWritabilityChanged();
        if (isChannelWritable()) {
            writePendingBytes();
        }
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public ChannelHandlerContext channelHandlerContext() {
        return this.ctx;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void initialWindowSize(int newWindowSize) throws Http2Exception {
        if (!$assertionsDisabled && this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        this.monitor.initialWindowSize(newWindowSize);
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int initialWindowSize() {
        return this.initialWindowSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int windowSize(Http2Stream stream) {
        return state(stream).windowSize();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public boolean isWritable(Http2Stream stream) {
        return this.monitor.isWritable(state(stream));
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void channelWritabilityChanged() throws Http2Exception {
        this.monitor.channelWritabilityChange();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
        if (!$assertionsDisabled && (weight < 1 || weight > 256)) {
            throw new AssertionError("Invalid weight");
        }
        if (!$assertionsDisabled && childStreamId == parentStreamId) {
            throw new AssertionError("A stream cannot depend on itself");
        }
        if (!$assertionsDisabled && (childStreamId <= 0 || parentStreamId < 0)) {
            throw new AssertionError("childStreamId must be > 0. parentStreamId must be >= 0.");
        }
        this.streamByteDistributor.updateDependencyTree(childStreamId, parentStreamId, weight, exclusive);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelWritable() {
        return this.ctx != null && isChannelWritable0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelWritable0() {
        return this.ctx.channel().isWritable();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void listener(Http2RemoteFlowController.Listener listener) {
        this.monitor = listener == null ? new WritabilityMonitor() : new ListenerWritabilityMonitor(listener);
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
        if (!$assertionsDisabled && this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        this.monitor.incrementWindowSize(state(stream), delta);
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void addFlowControlled(Http2Stream stream, Http2RemoteFlowController.FlowControlled frame) {
        if (!$assertionsDisabled && this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkNotNull(frame, "frame");
        try {
            this.monitor.enqueueFrame(state(stream), frame);
        } catch (Throwable t) {
            frame.error(this.ctx, t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public boolean hasFlowControlled(Http2Stream stream) {
        return state(stream).hasFrame();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FlowState state(Http2Stream stream) {
        return (FlowState) stream.getProperty(this.stateKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int connectionWindowSize() {
        return this.connectionState.windowSize();
    }

    private int minUsableChannelBytes() {
        return Math.max(this.ctx.channel().config().getWriteBufferLowWaterMark(), 32768);
    }

    private int maxUsableChannelBytes() {
        int channelWritableBytes = (int) Math.min(2147483647L, this.ctx.channel().bytesBeforeUnwritable());
        int usableBytes = channelWritableBytes > 0 ? Math.max(channelWritableBytes, minUsableChannelBytes()) : 0;
        return Math.min(this.connectionState.windowSize(), usableBytes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int writableBytes() {
        return Math.min(connectionWindowSize(), maxUsableChannelBytes());
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void writePendingBytes() throws Http2Exception {
        this.monitor.writePendingBytes();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$FlowState.class */
    private final class FlowState implements StreamByteDistributor.StreamState {
        private final Http2Stream stream;
        private final Deque<Http2RemoteFlowController.FlowControlled> pendingWriteQueue = new ArrayDeque(2);
        private int window;
        private long pendingBytes;
        private boolean markedWritable;
        private boolean writing;
        private boolean cancelled;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DefaultHttp2RemoteFlowController.class.desiredAssertionStatus();
        }

        FlowState(Http2Stream stream) {
            this.stream = stream;
        }

        boolean isWritable() {
            return ((long) windowSize()) > pendingBytes() && !this.cancelled;
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public Http2Stream stream() {
            return this.stream;
        }

        boolean markedWritability() {
            return this.markedWritable;
        }

        void markedWritability(boolean isWritable) {
            this.markedWritable = isWritable;
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public int windowSize() {
            return this.window;
        }

        void windowSize(int initialWindowSize) {
            this.window = initialWindowSize;
        }

        /* JADX WARN: Finally extract failed */
        int writeAllocatedBytes(int allocated) {
            int writtenBytes;
            Http2RemoteFlowController.FlowControlled frame;
            int maxBytes;
            try {
                try {
                } catch (Throwable t) {
                    this.cancelled = true;
                    this.writing = false;
                    writtenBytes = allocated - allocated;
                    decrementPendingBytes(writtenBytes, false);
                    decrementFlowControlWindow(writtenBytes);
                    if (this.cancelled) {
                        cancel(Http2Error.INTERNAL_ERROR, t);
                    }
                }
                if (!$assertionsDisabled && this.writing) {
                    throw new AssertionError();
                }
                this.writing = true;
                boolean writeOccurred = false;
                while (!this.cancelled && (frame = peek()) != null && ((maxBytes = Math.min(allocated, writableWindow())) > 0 || frame.size() <= 0)) {
                    writeOccurred = true;
                    int initialFrameSize = frame.size();
                    try {
                        frame.write(DefaultHttp2RemoteFlowController.this.ctx, Math.max(0, maxBytes));
                        if (frame.size() == 0) {
                            this.pendingWriteQueue.remove();
                            frame.writeComplete();
                        }
                        allocated -= initialFrameSize - frame.size();
                    } catch (Throwable th) {
                        int size = allocated - (initialFrameSize - frame.size());
                        throw th;
                    }
                }
                if (writeOccurred) {
                    this.writing = false;
                    writtenBytes = allocated - allocated;
                    decrementPendingBytes(writtenBytes, false);
                    decrementFlowControlWindow(writtenBytes);
                    if (this.cancelled) {
                        cancel(Http2Error.INTERNAL_ERROR, null);
                    }
                    return writtenBytes;
                }
                this.writing = false;
                int writtenBytes2 = allocated - allocated;
                decrementPendingBytes(writtenBytes2, false);
                decrementFlowControlWindow(writtenBytes2);
                if (this.cancelled) {
                    cancel(Http2Error.INTERNAL_ERROR, null);
                }
                return -1;
            } catch (Throwable th2) {
                this.writing = false;
                int writtenBytes3 = allocated - allocated;
                decrementPendingBytes(writtenBytes3, false);
                decrementFlowControlWindow(writtenBytes3);
                if (this.cancelled) {
                    cancel(Http2Error.INTERNAL_ERROR, null);
                }
                throw th2;
            }
        }

        int incrementStreamWindow(int delta) throws Http2Exception {
            if (delta > 0 && Integer.MAX_VALUE - delta < this.window) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Window size overflow for stream: %d", Integer.valueOf(this.stream.id()));
            }
            this.window += delta;
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            return this.window;
        }

        private int writableWindow() {
            return Math.min(this.window, DefaultHttp2RemoteFlowController.this.connectionWindowSize());
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public long pendingBytes() {
            return this.pendingBytes;
        }

        void enqueueFrame(Http2RemoteFlowController.FlowControlled frame) {
            Http2RemoteFlowController.FlowControlled last = this.pendingWriteQueue.peekLast();
            if (last == null) {
                enqueueFrameWithoutMerge(frame);
                return;
            }
            int lastSize = last.size();
            if (last.merge(DefaultHttp2RemoteFlowController.this.ctx, frame)) {
                incrementPendingBytes(last.size() - lastSize, true);
            } else {
                enqueueFrameWithoutMerge(frame);
            }
        }

        private void enqueueFrameWithoutMerge(Http2RemoteFlowController.FlowControlled frame) {
            this.pendingWriteQueue.offer(frame);
            incrementPendingBytes(frame.size(), true);
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public boolean hasFrame() {
            return !this.pendingWriteQueue.isEmpty();
        }

        private Http2RemoteFlowController.FlowControlled peek() {
            return this.pendingWriteQueue.peek();
        }

        void cancel(Http2Error error, Throwable cause) {
            this.cancelled = true;
            if (this.writing) {
                return;
            }
            Http2RemoteFlowController.FlowControlled frame = this.pendingWriteQueue.poll();
            if (frame != null) {
                Http2Exception exception = Http2Exception.streamError(this.stream.id(), error, cause, "Stream closed before write could take place", new Object[0]);
                do {
                    writeError(frame, exception);
                    frame = this.pendingWriteQueue.poll();
                } while (frame != null);
            }
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            DefaultHttp2RemoteFlowController.this.monitor.stateCancelled(this);
        }

        private void incrementPendingBytes(int numBytes, boolean updateStreamableBytes) {
            this.pendingBytes += numBytes;
            DefaultHttp2RemoteFlowController.this.monitor.incrementPendingBytes(numBytes);
            if (updateStreamableBytes) {
                DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            }
        }

        private void decrementPendingBytes(int bytes, boolean updateStreamableBytes) {
            incrementPendingBytes(-bytes, updateStreamableBytes);
        }

        private void decrementFlowControlWindow(int bytes) {
            try {
                int negativeBytes = -bytes;
                DefaultHttp2RemoteFlowController.this.connectionState.incrementStreamWindow(negativeBytes);
                incrementStreamWindow(negativeBytes);
            } catch (Http2Exception e) {
                throw new IllegalStateException("Invalid window state when writing frame: " + e.getMessage(), e);
            }
        }

        private void writeError(Http2RemoteFlowController.FlowControlled frame, Http2Exception cause) {
            if (!$assertionsDisabled && DefaultHttp2RemoteFlowController.this.ctx == null) {
                throw new AssertionError();
            }
            decrementPendingBytes(frame.size(), true);
            frame.error(DefaultHttp2RemoteFlowController.this.ctx, cause);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$WritabilityMonitor.class */
    private class WritabilityMonitor implements StreamByteDistributor.Writer {
        private boolean inWritePendingBytes;
        private long totalPendingBytes;

        private WritabilityMonitor() {
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.Writer
        public final void write(Http2Stream stream, int numBytes) {
            DefaultHttp2RemoteFlowController.this.state(stream).writeAllocatedBytes(numBytes);
        }

        void channelWritabilityChange() throws Http2Exception {
        }

        void stateCancelled(FlowState state) {
        }

        void windowSize(FlowState state, int initialWindowSize) {
            state.windowSize(initialWindowSize);
        }

        void incrementWindowSize(FlowState state, int delta) throws Http2Exception {
            state.incrementStreamWindow(delta);
        }

        void enqueueFrame(FlowState state, Http2RemoteFlowController.FlowControlled frame) throws Http2Exception {
            state.enqueueFrame(frame);
        }

        final void incrementPendingBytes(int delta) {
            this.totalPendingBytes += delta;
        }

        final boolean isWritable(FlowState state) {
            return isWritableConnection() && state.isWritable();
        }

        final void writePendingBytes() throws Http2Exception {
            if (this.inWritePendingBytes) {
                return;
            }
            this.inWritePendingBytes = true;
            try {
                int bytesToWrite = DefaultHttp2RemoteFlowController.this.writableBytes();
                while (DefaultHttp2RemoteFlowController.this.streamByteDistributor.distribute(bytesToWrite, this)) {
                    int iWritableBytes = DefaultHttp2RemoteFlowController.this.writableBytes();
                    bytesToWrite = iWritableBytes;
                    if (iWritableBytes <= 0 || !DefaultHttp2RemoteFlowController.this.isChannelWritable0()) {
                        break;
                    }
                }
            } finally {
                this.inWritePendingBytes = false;
            }
        }

        void initialWindowSize(int newWindowSize) throws Http2Exception {
            ObjectUtil.checkPositiveOrZero(newWindowSize, "newWindowSize");
            final int delta = newWindowSize - DefaultHttp2RemoteFlowController.this.initialWindowSize;
            DefaultHttp2RemoteFlowController.this.initialWindowSize = newWindowSize;
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(new Http2StreamVisitor() { // from class: io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor.1
                @Override // io.netty.handler.codec.http2.Http2StreamVisitor
                public boolean visit(Http2Stream stream) throws Http2Exception {
                    DefaultHttp2RemoteFlowController.this.state(stream).incrementStreamWindow(delta);
                    return true;
                }
            });
            if (delta > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                writePendingBytes();
            }
        }

        final boolean isWritableConnection() {
            return ((long) DefaultHttp2RemoteFlowController.this.connectionState.windowSize()) - this.totalPendingBytes > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/DefaultHttp2RemoteFlowController$ListenerWritabilityMonitor.class */
    private final class ListenerWritabilityMonitor extends WritabilityMonitor implements Http2StreamVisitor {
        private final Http2RemoteFlowController.Listener listener;

        ListenerWritabilityMonitor(Http2RemoteFlowController.Listener listener) {
            super();
            this.listener = listener;
        }

        @Override // io.netty.handler.codec.http2.Http2StreamVisitor
        public boolean visit(Http2Stream stream) throws Http2Exception {
            FlowState state = DefaultHttp2RemoteFlowController.this.state(stream);
            if (isWritable(state) != state.markedWritability()) {
                notifyWritabilityChanged(state);
                return true;
            }
            return true;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void windowSize(FlowState state, int initialWindowSize) {
            super.windowSize(state, initialWindowSize);
            try {
                checkStateWritability(state);
            } catch (Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from window", e);
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void incrementWindowSize(FlowState state, int delta) throws Http2Exception {
            super.incrementWindowSize(state, delta);
            checkStateWritability(state);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void initialWindowSize(int newWindowSize) throws Http2Exception {
            super.initialWindowSize(newWindowSize);
            if (isWritableConnection()) {
                checkAllWritabilityChanged();
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void enqueueFrame(FlowState state, Http2RemoteFlowController.FlowControlled frame) throws Http2Exception {
            super.enqueueFrame(state, frame);
            checkConnectionThenStreamWritabilityChanged(state);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void stateCancelled(FlowState state) {
            try {
                checkConnectionThenStreamWritabilityChanged(state);
            } catch (Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from checkAllWritabilityChanged", e);
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void channelWritabilityChange() throws Http2Exception {
            if (DefaultHttp2RemoteFlowController.this.connectionState.markedWritability() != DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                checkAllWritabilityChanged();
            }
        }

        private void checkStateWritability(FlowState state) throws Http2Exception {
            if (isWritable(state) != state.markedWritability()) {
                if (state == DefaultHttp2RemoteFlowController.this.connectionState) {
                    checkAllWritabilityChanged();
                } else {
                    notifyWritabilityChanged(state);
                }
            }
        }

        private void notifyWritabilityChanged(FlowState state) {
            state.markedWritability(!state.markedWritability());
            try {
                this.listener.writabilityChanged(state.stream);
            } catch (Throwable cause) {
                DefaultHttp2RemoteFlowController.logger.error("Caught Throwable from listener.writabilityChanged", cause);
            }
        }

        private void checkConnectionThenStreamWritabilityChanged(FlowState state) throws Http2Exception {
            if (isWritableConnection() != DefaultHttp2RemoteFlowController.this.connectionState.markedWritability()) {
                checkAllWritabilityChanged();
            } else if (isWritable(state) != state.markedWritability()) {
                notifyWritabilityChanged(state);
            }
        }

        private void checkAllWritabilityChanged() throws Http2Exception {
            DefaultHttp2RemoteFlowController.this.connectionState.markedWritability(isWritableConnection());
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(this);
        }
    }
}

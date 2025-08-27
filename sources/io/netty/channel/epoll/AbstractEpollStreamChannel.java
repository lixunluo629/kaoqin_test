package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.socket.DuplexChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.SocketWritableByteChannel;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Queue;
import java.util.concurrent.Executor;
import org.aspectj.apache.bcel.Constants;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel.class */
public abstract class AbstractEpollStreamChannel extends AbstractEpollChannel implements DuplexChannel {
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private static final InternalLogger logger;
    private final Runnable flushTask;
    private volatile Queue<SpliceInTask> spliceQueue;
    private FileDescriptor pipeIn;
    private FileDescriptor pipeOut;
    private WritableByteChannel byteChannel;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    static {
        $assertionsDisabled = !AbstractEpollStreamChannel.class.desiredAssertionStatus();
        METADATA = new ChannelMetadata(false, 16);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) DefaultFileRegion.class) + ')';
        logger = InternalLoggerFactory.getInstance((Class<?>) AbstractEpollStreamChannel.class);
    }

    protected AbstractEpollStreamChannel(Channel parent, int fd) {
        this(parent, new LinuxSocket(fd));
    }

    protected AbstractEpollStreamChannel(int fd) {
        this(new LinuxSocket(fd));
    }

    AbstractEpollStreamChannel(LinuxSocket fd) {
        this(fd, isSoErrorZero(fd));
    }

    AbstractEpollStreamChannel(Channel parent, LinuxSocket fd) {
        super(parent, fd, true);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    AbstractEpollStreamChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
        super(parent, fd, remote);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    protected AbstractEpollStreamChannel(LinuxSocket fd, boolean active) {
        super((Channel) null, fd, active);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollStreamUnsafe();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch2, int len) {
        return spliceTo(ch2, len, newPromise());
    }

    public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch2, int len, ChannelPromise promise) {
        if (ch2.eventLoop() != eventLoop()) {
            throw new IllegalArgumentException("EventLoops are not the same.");
        }
        ObjectUtil.checkPositiveOrZero(len, "len");
        if (ch2.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED || config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isOpen()) {
            promise.tryFailure(new ClosedChannelException());
        } else {
            addToSpliceQueue(new SpliceInChannelTask(ch2, len, promise));
            failSpliceIfClosed(promise);
        }
        return promise;
    }

    public final ChannelFuture spliceTo(FileDescriptor ch2, int offset, int len) {
        return spliceTo(ch2, offset, len, newPromise());
    }

    public final ChannelFuture spliceTo(FileDescriptor ch2, int offset, int len, ChannelPromise promise) {
        ObjectUtil.checkPositiveOrZero(len, "len");
        ObjectUtil.checkPositiveOrZero(offset, "offset");
        if (config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isOpen()) {
            promise.tryFailure(new ClosedChannelException());
        } else {
            addToSpliceQueue(new SpliceFdTask(ch2, offset, len, promise));
            failSpliceIfClosed(promise);
        }
        return promise;
    }

    private void failSpliceIfClosed(ChannelPromise promise) {
        if (!isOpen() && promise.tryFailure(new ClosedChannelException())) {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    AbstractEpollStreamChannel.this.clearSpliceQueue();
                }
            });
        }
    }

    private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
        int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            in.remove();
            return 0;
        }
        if (buf.hasMemoryAddress() || buf.nioBufferCount() == 1) {
            return doWriteBytes(in, buf);
        }
        ByteBuffer[] nioBuffers = buf.nioBuffers();
        return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, config().getMaxBytesPerGatheringWrite());
    }

    private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite) {
        if (attempted == written) {
            if ((attempted << 1) > oldMaxBytesPerGatheringWrite) {
                config().setMaxBytesPerGatheringWrite(attempted << 1);
            }
        } else if (attempted > Constants.NEGATABLE && written < (attempted >>> 1)) {
            config().setMaxBytesPerGatheringWrite(attempted >>> 1);
        }
    }

    private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array) throws IOException {
        long expectedWrittenBytes = array.size();
        if (!$assertionsDisabled && expectedWrittenBytes == 0) {
            throw new AssertionError();
        }
        int cnt = array.count();
        if (!$assertionsDisabled && cnt == 0) {
            throw new AssertionError();
        }
        long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
        if (localWrittenBytes > 0) {
            adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
            in.removeBytes(localWrittenBytes);
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite) throws IOException {
        if (!$assertionsDisabled && expectedWrittenBytes == 0) {
            throw new AssertionError();
        }
        if (expectedWrittenBytes > maxBytesPerGatheringWrite) {
            expectedWrittenBytes = maxBytesPerGatheringWrite;
        }
        long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes);
        if (localWrittenBytes > 0) {
            adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, maxBytesPerGatheringWrite);
            in.removeBytes(localWrittenBytes);
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region) throws Exception {
        long offset = region.transferred();
        long regionCount = region.count();
        if (offset >= regionCount) {
            in.remove();
            return 0;
        }
        long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
        if (flushedAmount <= 0) {
            if (flushedAmount == 0) {
                validateFileRegion(region, offset);
                return Integer.MAX_VALUE;
            }
            return Integer.MAX_VALUE;
        }
        in.progress(flushedAmount);
        if (region.transferred() >= regionCount) {
            in.remove();
            return 1;
        }
        return 1;
    }

    private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region) throws Exception {
        if (region.transferred() >= region.count()) {
            in.remove();
            return 0;
        }
        if (this.byteChannel == null) {
            this.byteChannel = new EpollSocketWritableByteChannel();
        }
        long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
        if (flushedAmount > 0) {
            in.progress(flushedAmount);
            if (region.transferred() >= region.count()) {
                in.remove();
                return 1;
            }
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int writeSpinCount = config().getWriteSpinCount();
        do {
            int msgCount = in.size();
            if (msgCount > 1 && (in.current() instanceof ByteBuf)) {
                writeSpinCount -= doWriteMultiple(in);
            } else {
                if (msgCount == 0) {
                    clearFlag(Native.EPOLLOUT);
                    return;
                }
                writeSpinCount -= doWriteSingle(in);
            }
        } while (writeSpinCount > 0);
        if (writeSpinCount == 0) {
            clearFlag(Native.EPOLLOUT);
            eventLoop().execute(this.flushTask);
        } else {
            setFlag(Native.EPOLLOUT);
        }
    }

    protected int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
        Object msg = in.current();
        if (msg instanceof ByteBuf) {
            return writeBytes(in, (ByteBuf) msg);
        }
        if (msg instanceof DefaultFileRegion) {
            return writeDefaultFileRegion(in, (DefaultFileRegion) msg);
        }
        if (msg instanceof FileRegion) {
            return writeFileRegion(in, (FileRegion) msg);
        }
        if (msg instanceof SpliceOutTask) {
            if (!((SpliceOutTask) msg).spliceOut()) {
                return Integer.MAX_VALUE;
            }
            in.remove();
            return 1;
        }
        throw new Error();
    }

    private int doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
        long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
        IovArray array = ((EpollEventLoop) eventLoop()).cleanIovArray();
        array.maxBytes(maxBytesPerGatheringWrite);
        in.forEachFlushedMessage(array);
        if (array.count() >= 1) {
            return writeBytesMultiple(in, array);
        }
        in.removeBytes(0L);
        return 0;
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if ((msg instanceof FileRegion) || (msg instanceof SpliceOutTask)) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doShutdownOutput() throws Exception {
        this.socket.shutdown(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownInput0(ChannelPromise promise) {
        try {
            this.socket.shutdown(true, false);
            promise.setSuccess();
        } catch (Throwable cause) {
            promise.setFailure(cause);
        }
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isOutputShutdown() {
        return this.socket.isOutputShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isInputShutdown() {
        return this.socket.isInputShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isShutdown() {
        return this.socket.isShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput() {
        return shutdownOutput(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop.inEventLoop()) {
            ((AbstractChannel.AbstractUnsafe) unsafe()).shutdownOutput(promise);
        } else {
            loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.3
                @Override // java.lang.Runnable
                public void run() {
                    ((AbstractChannel.AbstractUnsafe) AbstractEpollStreamChannel.this.unsafe()).shutdownOutput(promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownInput() {
        return shutdownInput(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        Executor closeExecutor = ((EpollStreamUnsafe) unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.4
                @Override // java.lang.Runnable
                public void run() {
                    AbstractEpollStreamChannel.this.shutdownInput0(promise);
                }
            });
        } else {
            EventLoop loop = eventLoop();
            if (loop.inEventLoop()) {
                shutdownInput0(promise);
            } else {
                loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractEpollStreamChannel.this.shutdownInput0(promise);
                    }
                });
            }
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown() {
        return shutdown(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown(final ChannelPromise promise) {
        ChannelFuture shutdownOutputFuture = shutdownOutput();
        if (shutdownOutputFuture.isDone()) {
            shutdownOutputDone(shutdownOutputFuture, promise);
        } else {
            shutdownOutputFuture.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.6
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownOutputFuture2) throws Exception {
                    AbstractEpollStreamChannel.this.shutdownOutputDone(shutdownOutputFuture2, promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
        ChannelFuture shutdownInputFuture = shutdownInput();
        if (shutdownInputFuture.isDone()) {
            shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
        } else {
            shutdownInputFuture.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.7
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownInputFuture2) throws Exception {
                    AbstractEpollStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture2, promise);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
        Throwable shutdownOutputCause = shutdownOutputFuture.cause();
        Throwable shutdownInputCause = shutdownInputFuture.cause();
        if (shutdownOutputCause != null) {
            if (shutdownInputCause != null) {
                logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
            }
            promise.setFailure(shutdownOutputCause);
        } else if (shutdownInputCause != null) {
            promise.setFailure(shutdownInputCause);
        } else {
            promise.setSuccess();
        }
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        try {
            super.doClose();
        } finally {
            safeClosePipe(this.pipeIn);
            safeClosePipe(this.pipeOut);
            clearSpliceQueue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSpliceQueue() {
        Queue<SpliceInTask> sQueue = this.spliceQueue;
        if (sQueue == null) {
            return;
        }
        ClosedChannelException exception = null;
        while (true) {
            SpliceInTask task = sQueue.poll();
            if (task != null) {
                if (exception == null) {
                    exception = new ClosedChannelException();
                }
                task.promise.tryFailure(exception);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void safeClosePipe(FileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            } catch (IOException e) {
                logger.warn("Error while closing a pipe", (Throwable) e);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$EpollStreamUnsafe.class */
    class EpollStreamUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        EpollStreamUnsafe() {
            super();
        }

        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        protected Executor prepareToClose() {
            return super.prepareToClose();
        }

        private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, EpollRecvByteAllocatorHandle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead((Object) byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || (cause instanceof IOException)) {
                shutdownInput(false);
            }
        }

        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle) {
            return new EpollRecvByteAllocatorStreamingHandle(handle);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x005f A[Catch: Throwable -> 0x0116, all -> 0x012c, PHI: r14
  0x005f: PHI (r14v6 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>) = 
  (r14v1 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>)
  (r14v2 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>)
 binds: [B:9:0x004f, B:11:0x005c] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {Throwable -> 0x0116, blocks: (B:10:0x0052, B:19:0x008e, B:21:0x00aa, B:27:0x00c6, B:33:0x00f9, B:35:0x0109, B:28:0x00ce, B:31:0x00f2, B:12:0x005f, B:14:0x0070, B:16:0x0079, B:18:0x0083), top: B:47:0x0052, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:19:0x008e A[Catch: Throwable -> 0x0116, all -> 0x012c, PHI: r14
  0x008e: PHI (r14v3 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>) = 
  (r14v2 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>)
  (r14v6 'sQueue' java.util.Queue<io.netty.channel.epoll.AbstractEpollStreamChannel$SpliceInTask>)
 binds: [B:11:0x005c, B:13:0x006d] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {Throwable -> 0x0116, blocks: (B:10:0x0052, B:19:0x008e, B:21:0x00aa, B:27:0x00c6, B:33:0x00f9, B:35:0x0109, B:28:0x00ce, B:31:0x00f2, B:12:0x005f, B:14:0x0070, B:16:0x0079, B:18:0x0083), top: B:47:0x0052, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:49:0x00f9 A[EDGE_INSN: B:49:0x00f9->B:33:0x00f9 BREAK  A[LOOP:0: B:8:0x004d->B:53:?], SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:53:? A[LOOP:0: B:8:0x004d->B:53:?, LOOP_END, SYNTHETIC] */
        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void epollInReady() {
            /*
                Method dump skipped, instructions count: 311
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.AbstractEpollStreamChannel.EpollStreamUnsafe.epollInReady():void");
        }
    }

    private void addToSpliceQueue(SpliceInTask task) {
        Queue<SpliceInTask> sQueue = this.spliceQueue;
        if (sQueue == null) {
            synchronized (this) {
                sQueue = this.spliceQueue;
                if (sQueue == null) {
                    Queue<SpliceInTask> queueNewMpscQueue = PlatformDependent.newMpscQueue();
                    sQueue = queueNewMpscQueue;
                    this.spliceQueue = queueNewMpscQueue;
                }
            }
        }
        sQueue.add(task);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$SpliceInTask.class */
    protected abstract class SpliceInTask {
        final ChannelPromise promise;
        int len;

        abstract boolean spliceIn(RecvByteBufAllocator.Handle handle);

        protected SpliceInTask(int len, ChannelPromise promise) {
            this.promise = promise;
            this.len = len;
        }

        protected final int spliceIn(FileDescriptor pipeOut, RecvByteBufAllocator.Handle handle) throws IOException {
            int length = Math.min(handle.guess(), this.len);
            int splicedIn = 0;
            while (true) {
                int localSplicedIn = Native.splice(AbstractEpollStreamChannel.this.socket.intValue(), -1L, pipeOut.intValue(), -1L, length);
                if (localSplicedIn != 0) {
                    splicedIn += localSplicedIn;
                    length -= localSplicedIn;
                } else {
                    return splicedIn;
                }
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$SpliceInChannelTask.class */
    private final class SpliceInChannelTask extends SpliceInTask implements ChannelFutureListener {

        /* renamed from: ch, reason: collision with root package name */
        private final AbstractEpollStreamChannel f3ch;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AbstractEpollStreamChannel.class.desiredAssertionStatus();
        }

        SpliceInChannelTask(AbstractEpollStreamChannel ch2, int len, ChannelPromise promise) {
            super(len, promise);
            this.f3ch = ch2;
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                this.promise.setFailure(future.cause());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v32, types: [io.netty.channel.Channel$Unsafe] */
        /* JADX WARN: Type inference failed for: r0v37, types: [io.netty.channel.ChannelPromise] */
        /* JADX WARN: Type inference failed for: r2v2, types: [io.netty.channel.ChannelPromise] */
        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.SpliceInTask
        public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
            Future<Void> futureAddListener2;
            if (!$assertionsDisabled && !this.f3ch.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            if (this.len != 0) {
                try {
                    FileDescriptor pipeOut = this.f3ch.pipeOut;
                    if (pipeOut == null) {
                        FileDescriptor[] pipe = FileDescriptor.pipe();
                        this.f3ch.pipeIn = pipe[0];
                        pipeOut = this.f3ch.pipeOut = pipe[1];
                    }
                    int splicedIn = spliceIn(pipeOut, handle);
                    if (splicedIn > 0) {
                        if (this.len != Integer.MAX_VALUE) {
                            this.len -= splicedIn;
                        }
                        if (this.len == 0) {
                            futureAddListener2 = this.promise;
                        } else {
                            futureAddListener2 = this.f3ch.newPromise().addListener2((GenericFutureListener<? extends Future<? super Void>>) this);
                        }
                        boolean autoRead = AbstractEpollStreamChannel.this.config().isAutoRead();
                        this.f3ch.unsafe().write(AbstractEpollStreamChannel.this.new SpliceOutTask(this.f3ch, splicedIn, autoRead), futureAddListener2);
                        this.f3ch.unsafe().flush();
                        if (autoRead && !futureAddListener2.isDone()) {
                            AbstractEpollStreamChannel.this.config().setAutoRead(false);
                        }
                    }
                    return this.len == 0;
                } catch (Throwable cause) {
                    this.promise.setFailure(cause);
                    return true;
                }
            }
            this.promise.setSuccess();
            return true;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$SpliceOutTask.class */
    private final class SpliceOutTask {

        /* renamed from: ch, reason: collision with root package name */
        private final AbstractEpollStreamChannel f4ch;
        private final boolean autoRead;
        private int len;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AbstractEpollStreamChannel.class.desiredAssertionStatus();
        }

        SpliceOutTask(AbstractEpollStreamChannel ch2, int len, boolean autoRead) {
            this.f4ch = ch2;
            this.len = len;
            this.autoRead = autoRead;
        }

        public boolean spliceOut() throws Exception {
            if (!$assertionsDisabled && !this.f4ch.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            try {
                int splicedOut = Native.splice(this.f4ch.pipeIn.intValue(), -1L, this.f4ch.socket.intValue(), -1L, this.len);
                this.len -= splicedOut;
                if (this.len == 0) {
                    if (this.autoRead) {
                        AbstractEpollStreamChannel.this.config().setAutoRead(true);
                        return true;
                    }
                    return true;
                }
                return false;
            } catch (IOException e) {
                if (this.autoRead) {
                    AbstractEpollStreamChannel.this.config().setAutoRead(true);
                }
                throw e;
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$SpliceFdTask.class */
    private final class SpliceFdTask extends SpliceInTask {
        private final FileDescriptor fd;
        private final ChannelPromise promise;
        private int offset;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !AbstractEpollStreamChannel.class.desiredAssertionStatus();
        }

        SpliceFdTask(FileDescriptor fd, int offset, int len, ChannelPromise promise) {
            super(len, promise);
            this.fd = fd;
            this.promise = promise;
            this.offset = offset;
        }

        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.SpliceInTask
        public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
            if (!$assertionsDisabled && !AbstractEpollStreamChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            if (this.len == 0) {
                this.promise.setSuccess();
                return true;
            }
            try {
                FileDescriptor[] pipe = FileDescriptor.pipe();
                FileDescriptor pipeIn = pipe[0];
                FileDescriptor pipeOut = pipe[1];
                try {
                    int splicedIn = spliceIn(pipeOut, handle);
                    if (splicedIn > 0) {
                        if (this.len != Integer.MAX_VALUE) {
                            this.len -= splicedIn;
                        }
                        do {
                            int splicedOut = Native.splice(pipeIn.intValue(), -1L, this.fd.intValue(), this.offset, splicedIn);
                            this.offset += splicedOut;
                            splicedIn -= splicedOut;
                        } while (splicedIn > 0);
                        if (this.len == 0) {
                            this.promise.setSuccess();
                            AbstractEpollStreamChannel.safeClosePipe(pipeIn);
                            AbstractEpollStreamChannel.safeClosePipe(pipeOut);
                            return true;
                        }
                    }
                    return false;
                } finally {
                    AbstractEpollStreamChannel.safeClosePipe(pipeIn);
                    AbstractEpollStreamChannel.safeClosePipe(pipeOut);
                }
            } catch (Throwable cause) {
                this.promise.setFailure(cause);
                return true;
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/AbstractEpollStreamChannel$EpollSocketWritableByteChannel.class */
    private final class EpollSocketWritableByteChannel extends SocketWritableByteChannel {
        EpollSocketWritableByteChannel() {
            super(AbstractEpollStreamChannel.this.socket);
        }

        @Override // io.netty.channel.unix.SocketWritableByteChannel
        protected ByteBufAllocator alloc() {
            return AbstractEpollStreamChannel.this.alloc();
        }
    }
}

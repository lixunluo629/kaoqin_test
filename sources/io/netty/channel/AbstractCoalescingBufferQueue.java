package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/AbstractCoalescingBufferQueue.class */
public abstract class AbstractCoalescingBufferQueue {
    private static final InternalLogger logger;
    private final ArrayDeque<Object> bufAndListenerPairs;
    private final PendingBytesTracker tracker;
    private int readableBytes;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract ByteBuf compose(ByteBufAllocator byteBufAllocator, ByteBuf byteBuf, ByteBuf byteBuf2);

    protected abstract ByteBuf removeEmptyValue();

    static {
        $assertionsDisabled = !AbstractCoalescingBufferQueue.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) AbstractCoalescingBufferQueue.class);
    }

    protected AbstractCoalescingBufferQueue(Channel channel, int initSize) {
        this.bufAndListenerPairs = new ArrayDeque<>(initSize);
        this.tracker = channel == null ? null : PendingBytesTracker.newTracker(channel);
    }

    public final void addFirst(ByteBuf buf, ChannelPromise promise) {
        addFirst(buf, toChannelFutureListener(promise));
    }

    private void addFirst(ByteBuf buf, ChannelFutureListener listener) {
        if (listener != null) {
            this.bufAndListenerPairs.addFirst(listener);
        }
        this.bufAndListenerPairs.addFirst(buf);
        incrementReadableBytes(buf.readableBytes());
    }

    public final void add(ByteBuf buf) {
        add(buf, (ChannelFutureListener) null);
    }

    public final void add(ByteBuf buf, ChannelPromise promise) {
        add(buf, toChannelFutureListener(promise));
    }

    public final void add(ByteBuf buf, ChannelFutureListener listener) {
        this.bufAndListenerPairs.add(buf);
        if (listener != null) {
            this.bufAndListenerPairs.add(listener);
        }
        incrementReadableBytes(buf.readableBytes());
    }

    public final ByteBuf removeFirst(ChannelPromise aggregatePromise) {
        Object entry = this.bufAndListenerPairs.poll();
        if (entry == null) {
            return null;
        }
        if (!$assertionsDisabled && !(entry instanceof ByteBuf)) {
            throw new AssertionError();
        }
        ByteBuf result = (ByteBuf) entry;
        decrementReadableBytes(result.readableBytes());
        Object entry2 = this.bufAndListenerPairs.peek();
        if (entry2 instanceof ChannelFutureListener) {
            aggregatePromise.addListener2((GenericFutureListener<? extends Future<? super Void>>) entry2);
            this.bufAndListenerPairs.poll();
        }
        return result;
    }

    public final ByteBuf remove(ByteBufAllocator alloc, int bytes, ChannelPromise aggregatePromise) {
        ObjectUtil.checkPositiveOrZero(bytes, "bytes");
        ObjectUtil.checkNotNull(aggregatePromise, "aggregatePromise");
        if (this.bufAndListenerPairs.isEmpty()) {
            return removeEmptyValue();
        }
        int bytes2 = Math.min(bytes, this.readableBytes);
        ByteBuf toReturn = null;
        ByteBuf entryBuffer = null;
        while (true) {
            try {
                Object entry = this.bufAndListenerPairs.poll();
                if (entry == null) {
                    break;
                }
                if (entry instanceof ChannelFutureListener) {
                    aggregatePromise.addListener2((GenericFutureListener<? extends Future<? super Void>>) entry);
                } else {
                    ByteBuf entryBuffer2 = (ByteBuf) entry;
                    if (entryBuffer2.readableBytes() > bytes2) {
                        this.bufAndListenerPairs.addFirst(entryBuffer2);
                        if (bytes2 > 0) {
                            ByteBuf entryBuffer3 = entryBuffer2.readRetainedSlice(bytes2);
                            toReturn = toReturn == null ? composeFirst(alloc, entryBuffer3) : compose(alloc, toReturn, entryBuffer3);
                            bytes2 = 0;
                        }
                    } else {
                        bytes2 -= entryBuffer2.readableBytes();
                        toReturn = toReturn == null ? composeFirst(alloc, entryBuffer2) : compose(alloc, toReturn, entryBuffer2);
                        entryBuffer = null;
                    }
                }
            } catch (Throwable cause) {
                ReferenceCountUtil.safeRelease(entryBuffer);
                ReferenceCountUtil.safeRelease(toReturn);
                aggregatePromise.setFailure(cause);
                PlatformDependent.throwException(cause);
            }
        }
        decrementReadableBytes(bytes2 - bytes2);
        return toReturn;
    }

    public final int readableBytes() {
        return this.readableBytes;
    }

    public final boolean isEmpty() {
        return this.bufAndListenerPairs.isEmpty();
    }

    public final void releaseAndFailAll(ChannelOutboundInvoker invoker, Throwable cause) {
        releaseAndCompleteAll(invoker.newFailedFuture(cause));
    }

    public final void copyTo(AbstractCoalescingBufferQueue dest) {
        dest.bufAndListenerPairs.addAll(this.bufAndListenerPairs);
        dest.incrementReadableBytes(this.readableBytes);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x008f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0089 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void writeAndRemoveAll(io.netty.channel.ChannelHandlerContext r6) {
        /*
            r5 = this;
            r0 = r5
            r1 = r5
            int r1 = r1.readableBytes
            r0.decrementReadableBytes(r1)
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
        Lc:
            r0 = r5
            java.util.ArrayDeque<java.lang.Object> r0 = r0.bufAndListenerPairs
            java.lang.Object r0 = r0.poll()
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L2f
            r0 = r8
            if (r0 == 0) goto L2c
            r0 = r6
            r1 = r8
            r2 = r6
            io.netty.channel.ChannelPromise r2 = r2.voidPromise()     // Catch: java.lang.Throwable -> L83
            io.netty.channel.ChannelFuture r0 = r0.write(r1, r2)     // Catch: java.lang.Throwable -> L83
        L2c:
            goto L9f
        L2f:
            r0 = r9
            boolean r0 = r0 instanceof io.netty.buffer.ByteBuf     // Catch: java.lang.Throwable -> L83
            if (r0 == 0) goto L52
            r0 = r8
            if (r0 == 0) goto L49
            r0 = r6
            r1 = r8
            r2 = r6
            io.netty.channel.ChannelPromise r2 = r2.voidPromise()     // Catch: java.lang.Throwable -> L83
            io.netty.channel.ChannelFuture r0 = r0.write(r1, r2)     // Catch: java.lang.Throwable -> L83
        L49:
            r0 = r9
            io.netty.buffer.ByteBuf r0 = (io.netty.buffer.ByteBuf) r0     // Catch: java.lang.Throwable -> L83
            r8 = r0
            goto L80
        L52:
            r0 = r9
            boolean r0 = r0 instanceof io.netty.channel.ChannelPromise     // Catch: java.lang.Throwable -> L83
            if (r0 == 0) goto L6c
            r0 = r6
            r1 = r8
            r2 = r9
            io.netty.channel.ChannelPromise r2 = (io.netty.channel.ChannelPromise) r2     // Catch: java.lang.Throwable -> L83
            io.netty.channel.ChannelFuture r0 = r0.write(r1, r2)     // Catch: java.lang.Throwable -> L83
            r0 = 0
            r8 = r0
            goto L80
        L6c:
            r0 = r6
            r1 = r8
            io.netty.channel.ChannelFuture r0 = r0.write(r1)     // Catch: java.lang.Throwable -> L83
            r1 = r9
            io.netty.channel.ChannelFutureListener r1 = (io.netty.channel.ChannelFutureListener) r1     // Catch: java.lang.Throwable -> L83
            io.netty.channel.ChannelFuture r0 = r0.addListener2(r1)     // Catch: java.lang.Throwable -> L83
            r0 = 0
            r8 = r0
        L80:
            goto L9c
        L83:
            r10 = move-exception
            r0 = r7
            if (r0 != 0) goto L8f
            r0 = r10
            r7 = r0
            goto L9c
        L8f:
            io.netty.util.internal.logging.InternalLogger r0 = io.netty.channel.AbstractCoalescingBufferQueue.logger
            java.lang.String r1 = "Throwable being suppressed because Throwable {} is already pending"
            r2 = r7
            r3 = r10
            r0.info(r1, r2, r3)
        L9c:
            goto Lc
        L9f:
            r0 = r7
            if (r0 == 0) goto Lac
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            throw r0
        Lac:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.AbstractCoalescingBufferQueue.writeAndRemoveAll(io.netty.channel.ChannelHandlerContext):void");
    }

    protected final ByteBuf composeIntoComposite(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
        CompositeByteBuf composite = alloc.compositeBuffer(size() + 2);
        try {
            composite.addComponent(true, cumulation);
            composite.addComponent(true, next);
        } catch (Throwable cause) {
            composite.release();
            ReferenceCountUtil.safeRelease(next);
            PlatformDependent.throwException(cause);
        }
        return composite;
    }

    protected final ByteBuf copyAndCompose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
        ByteBuf newCumulation = alloc.ioBuffer(cumulation.readableBytes() + next.readableBytes());
        try {
            newCumulation.writeBytes(cumulation).writeBytes(next);
        } catch (Throwable cause) {
            newCumulation.release();
            ReferenceCountUtil.safeRelease(next);
            PlatformDependent.throwException(cause);
        }
        cumulation.release();
        next.release();
        return newCumulation;
    }

    protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first) {
        return first;
    }

    protected final int size() {
        return this.bufAndListenerPairs.size();
    }

    private void releaseAndCompleteAll(ChannelFuture future) {
        decrementReadableBytes(this.readableBytes);
        Throwable pending = null;
        while (true) {
            Object entry = this.bufAndListenerPairs.poll();
            if (entry == null) {
                break;
            }
            try {
                if (entry instanceof ByteBuf) {
                    ReferenceCountUtil.safeRelease(entry);
                } else {
                    ((ChannelFutureListener) entry).operationComplete(future);
                }
            } catch (Throwable t) {
                if (pending == null) {
                    pending = t;
                } else {
                    logger.info("Throwable being suppressed because Throwable {} is already pending", pending, t);
                }
            }
        }
        if (pending != null) {
            throw new IllegalStateException(pending);
        }
    }

    private void incrementReadableBytes(int increment) {
        int nextReadableBytes = this.readableBytes + increment;
        if (nextReadableBytes < this.readableBytes) {
            throw new IllegalStateException("buffer queue length overflow: " + this.readableBytes + " + " + increment);
        }
        this.readableBytes = nextReadableBytes;
        if (this.tracker != null) {
            this.tracker.incrementPendingOutboundBytes(increment);
        }
    }

    private void decrementReadableBytes(int decrement) {
        this.readableBytes -= decrement;
        if (!$assertionsDisabled && this.readableBytes < 0) {
            throw new AssertionError();
        }
        if (this.tracker != null) {
            this.tracker.decrementPendingOutboundBytes(decrement);
        }
    }

    private static ChannelFutureListener toChannelFutureListener(ChannelPromise promise) {
        if (promise.isVoid()) {
            return null;
        }
        return new DelegatingChannelPromiseNotifier(promise);
    }
}

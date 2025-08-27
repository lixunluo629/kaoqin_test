package io.netty.channel.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.AbstractNioChannel;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/nio/AbstractNioMessageChannel.class */
public abstract class AbstractNioMessageChannel extends AbstractNioChannel {
    boolean inputShutdown;

    protected abstract int doReadMessages(List<Object> list) throws Exception;

    protected abstract boolean doWriteMessage(Object obj, ChannelOutboundBuffer channelOutboundBuffer) throws Exception;

    protected AbstractNioMessageChannel(Channel parent, SelectableChannel ch2, int readInterestOp) {
        super(parent, ch2, readInterestOp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
        return new NioMessageUnsafe();
    }

    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
        if (this.inputShutdown) {
            return;
        }
        super.doBeginRead();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/nio/AbstractNioMessageChannel$NioMessageUnsafe.class */
    private final class NioMessageUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
        private final List<Object> readBuf;
        static final /* synthetic */ boolean $assertionsDisabled;

        private NioMessageUnsafe() {
            super();
            this.readBuf = new ArrayList();
        }

        static {
            $assertionsDisabled = !AbstractNioMessageChannel.class.desiredAssertionStatus();
        }

        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        public void read() {
            boolean z;
            if (!$assertionsDisabled && !AbstractNioMessageChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            ChannelConfig config = AbstractNioMessageChannel.this.config();
            ChannelPipeline pipeline = AbstractNioMessageChannel.this.pipeline();
            RecvByteBufAllocator.Handle allocHandle = AbstractNioMessageChannel.this.unsafe().recvBufAllocHandle();
            allocHandle.reset(config);
            boolean closed = false;
            Throwable exception = null;
            while (true) {
                try {
                    try {
                        int localRead = AbstractNioMessageChannel.this.doReadMessages(this.readBuf);
                        if (localRead == 0) {
                            break;
                        }
                        if (localRead < 0) {
                            closed = true;
                            break;
                        } else {
                            allocHandle.incMessagesRead(localRead);
                            if (!allocHandle.continueReading()) {
                                break;
                            }
                        }
                    } catch (Throwable t) {
                        exception = t;
                    }
                } finally {
                    if (!AbstractNioMessageChannel.this.readPending && !config.isAutoRead()) {
                        removeReadOp();
                    }
                }
            }
            int size = this.readBuf.size();
            for (int i = 0; i < size; i++) {
                AbstractNioMessageChannel.this.readPending = false;
                pipeline.fireChannelRead(this.readBuf.get(i));
            }
            this.readBuf.clear();
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            if (exception != null) {
                closed = AbstractNioMessageChannel.this.closeOnReadError(exception);
                pipeline.fireExceptionCaught(exception);
            }
            if (closed) {
                AbstractNioMessageChannel.this.inputShutdown = true;
                if (AbstractNioMessageChannel.this.isOpen()) {
                    close(voidPromise());
                }
            }
            if (z) {
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:?, code lost:
    
        return;
     */
    @Override // io.netty.channel.AbstractChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void doWrite(io.netty.channel.ChannelOutboundBuffer r5) throws java.lang.Exception {
        /*
            r4 = this;
            r0 = r4
            java.nio.channels.SelectionKey r0 = r0.selectionKey()
            r6 = r0
            r0 = r6
            int r0 = r0.interestOps()
            r7 = r0
        La:
            r0 = r5
            java.lang.Object r0 = r0.current()
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L27
            r0 = r7
            r1 = 4
            r0 = r0 & r1
            if (r0 == 0) goto L8c
            r0 = r6
            r1 = r7
            r2 = -5
            r1 = r1 & r2
            java.nio.channels.SelectionKey r0 = r0.interestOps(r1)
            goto L8c
        L27:
            r0 = 0
            r9 = r0
            r0 = r4
            io.netty.channel.ChannelConfig r0 = r0.config()     // Catch: java.lang.Exception -> L73
            int r0 = r0.getWriteSpinCount()     // Catch: java.lang.Exception -> L73
            r1 = 1
            int r0 = r0 - r1
            r10 = r0
        L37:
            r0 = r10
            if (r0 < 0) goto L52
            r0 = r4
            r1 = r8
            r2 = r5
            boolean r0 = r0.doWriteMessage(r1, r2)     // Catch: java.lang.Exception -> L73
            if (r0 == 0) goto L4c
            r0 = 1
            r9 = r0
            goto L52
        L4c:
            int r10 = r10 + (-1)
            goto L37
        L52:
            r0 = r9
            if (r0 == 0) goto L5f
            r0 = r5
            boolean r0 = r0.remove()     // Catch: java.lang.Exception -> L73
            goto L70
        L5f:
            r0 = r7
            r1 = 4
            r0 = r0 & r1
            if (r0 != 0) goto L6d
            r0 = r6
            r1 = r7
            r2 = 4
            r1 = r1 | r2
            java.nio.channels.SelectionKey r0 = r0.interestOps(r1)     // Catch: java.lang.Exception -> L73
        L6d:
            goto L8c
        L70:
            goto L89
        L73:
            r9 = move-exception
            r0 = r4
            boolean r0 = r0.continueOnWriteError()
            if (r0 == 0) goto L86
            r0 = r5
            r1 = r9
            boolean r0 = r0.remove(r1)
            goto L89
        L86:
            r0 = r9
            throw r0
        L89:
            goto La
        L8c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.nio.AbstractNioMessageChannel.doWrite(io.netty.channel.ChannelOutboundBuffer):void");
    }

    protected boolean continueOnWriteError() {
        return false;
    }

    protected boolean closeOnReadError(Throwable cause) {
        if (!isActive()) {
            return true;
        }
        if (cause instanceof PortUnreachableException) {
            return false;
        }
        return ((cause instanceof IOException) && (this instanceof ServerChannel)) ? false : true;
    }
}

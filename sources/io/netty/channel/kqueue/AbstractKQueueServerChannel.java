package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoop;
import io.netty.channel.ServerChannel;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/kqueue/AbstractKQueueServerChannel.class */
public abstract class AbstractKQueueServerChannel extends AbstractKQueueChannel implements ServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);

    abstract Channel newChildChannel(int i, byte[] bArr, int i2, int i3) throws Exception;

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    AbstractKQueueServerChannel(BsdSocket fd) {
        this(fd, isSoErrorZero(fd));
    }

    AbstractKQueueServerChannel(BsdSocket fd, boolean active) {
        super((Channel) null, fd, active);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof KQueueEventLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public InetSocketAddress remoteAddress0() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueServerSocketUnsafe();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/kqueue/AbstractKQueueServerChannel$KQueueServerSocketUnsafe.class */
    final class KQueueServerSocketUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
        private final byte[] acceptedAddress;
        static final /* synthetic */ boolean $assertionsDisabled;

        KQueueServerSocketUnsafe() {
            super();
            this.acceptedAddress = new byte[26];
        }

        static {
            $assertionsDisabled = !AbstractKQueueServerChannel.class.desiredAssertionStatus();
        }

        /* JADX WARN: Code restructure failed: missing block: B:15:0x0064, code lost:
        
            r9.lastBytesRead(-1);
         */
        @Override // io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void readReady(io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle r9) {
            /*
                r8 = this;
                boolean r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.KQueueServerSocketUnsafe.$assertionsDisabled
                if (r0 != 0) goto L1d
                r0 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this
                io.netty.channel.EventLoop r0 = r0.eventLoop()
                boolean r0 = r0.inEventLoop()
                if (r0 != 0) goto L1d
                java.lang.AssertionError r0 = new java.lang.AssertionError
                r1 = r0
                r1.<init>()
                throw r0
            L1d:
                r0 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this
                io.netty.channel.kqueue.KQueueChannelConfig r0 = r0.config()
                r10 = r0
                r0 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this
                r1 = r10
                boolean r0 = r0.shouldBreakReadReady(r1)
                if (r0 == 0) goto L35
                r0 = r8
                r0.clearReadFilter0()
                return
            L35:
                r0 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this
                io.netty.channel.ChannelPipeline r0 = r0.pipeline()
                r11 = r0
                r0 = r9
                r1 = r10
                r0.reset(r1)
                r0 = r9
                r1 = 1
                r0.attemptedBytesRead(r1)
                r0 = r8
                r0.readReadyBefore()
                r0 = 0
                r12 = r0
            L4e:
                r0 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r0 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                io.netty.channel.kqueue.BsdSocket r0 = r0.socket     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r1 = r8
                byte[] r1 = r1.acceptedAddress     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                int r0 = r0.accept(r1)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r13 = r0
                r0 = r13
                r1 = -1
                if (r0 != r1) goto L6c
                r0 = r9
                r1 = -1
                r0.lastBytesRead(r1)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                goto L9d
            L6c:
                r0 = r9
                r1 = 1
                r0.lastBytesRead(r1)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r0 = r9
                r1 = 1
                r0.incMessagesRead(r1)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r0 = r8
                r1 = 0
                r0.readPending = r1     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r0 = r11
                r1 = r8
                io.netty.channel.kqueue.AbstractKQueueServerChannel r1 = io.netty.channel.kqueue.AbstractKQueueServerChannel.this     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r2 = r13
                r3 = r8
                byte[] r3 = r3.acceptedAddress     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r4 = 1
                r5 = r8
                byte[] r5 = r5.acceptedAddress     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r6 = 0
                r5 = r5[r6]     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                io.netty.channel.Channel r1 = r1.newChildChannel(r2, r3, r4, r5)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                io.netty.channel.ChannelPipeline r0 = r0.fireChannelRead(r1)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                r0 = r9
                boolean r0 = r0.continueReading()     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Lc7
                if (r0 != 0) goto L4e
            L9d:
                goto La6
            La0:
                r13 = move-exception
                r0 = r13
                r12 = r0
            La6:
                r0 = r9
                r0.readComplete()     // Catch: java.lang.Throwable -> Lc7
                r0 = r11
                io.netty.channel.ChannelPipeline r0 = r0.fireChannelReadComplete()     // Catch: java.lang.Throwable -> Lc7
                r0 = r12
                if (r0 == 0) goto Lbf
                r0 = r11
                r1 = r12
                io.netty.channel.ChannelPipeline r0 = r0.fireExceptionCaught(r1)     // Catch: java.lang.Throwable -> Lc7
            Lbf:
                r0 = r8
                r1 = r10
                r0.readReadyFinally(r1)
                goto Ld1
            Lc7:
                r14 = move-exception
                r0 = r8
                r1 = r10
                r0.readReadyFinally(r1)
                r0 = r14
                throw r0
            Ld1:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.AbstractKQueueServerChannel.KQueueServerSocketUnsafe.readReady(io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle):void");
        }
    }
}

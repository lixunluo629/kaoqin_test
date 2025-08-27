package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslClientHelloHandler.class */
public abstract class SslClientHelloHandler<T> extends ByteToMessageDecoder implements ChannelOutboundHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SslClientHelloHandler.class);
    private boolean handshakeFailed;
    private boolean suppressRead;
    private boolean readPending;
    private ByteBuf handshakeBuffer;

    protected abstract Future<T> lookup(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception;

    protected abstract void onLookupComplete(ChannelHandlerContext channelHandlerContext, Future<T> future) throws Exception;

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!this.suppressRead && !this.handshakeFailed) {
            try {
                int readerIndex = in.readerIndex();
                int readableBytes = in.readableBytes();
                int handshakeLength = -1;
                while (readableBytes >= 5) {
                    int contentType = in.getUnsignedByte(readerIndex);
                    switch (contentType) {
                        case 20:
                        case 21:
                            int len = SslUtils.getEncryptedPacketLength(in, readerIndex);
                            if (len == -2) {
                                this.handshakeFailed = true;
                                NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                                in.skipBytes(in.readableBytes());
                                ctx.fireUserEventTriggered((Object) new SniCompletionEvent(e));
                                SslUtils.handleHandshakeFailure(ctx, e, true);
                                throw e;
                            }
                            if (len == -1) {
                                return;
                            }
                            select(ctx, null);
                            return;
                        case 22:
                            int majorVersion = in.getUnsignedByte(readerIndex + 1);
                            if (majorVersion != 3) {
                                break;
                            } else {
                                int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
                                if (readableBytes < packetLength) {
                                    return;
                                }
                                if (packetLength == 5) {
                                    select(ctx, null);
                                    return;
                                }
                                int endOffset = readerIndex + packetLength;
                                if (handshakeLength == -1) {
                                    if (readerIndex + 4 > endOffset) {
                                        return;
                                    }
                                    int handshakeType = in.getUnsignedByte(readerIndex + 5);
                                    if (handshakeType != 1) {
                                        select(ctx, null);
                                        return;
                                    }
                                    handshakeLength = in.getUnsignedMedium(readerIndex + 5 + 1);
                                    readerIndex += 4;
                                    packetLength -= 4;
                                    if (handshakeLength + 4 + 5 <= packetLength) {
                                        select(ctx, in.retainedSlice(readerIndex + 5, handshakeLength));
                                        return;
                                    } else if (this.handshakeBuffer == null) {
                                        this.handshakeBuffer = ctx.alloc().buffer(handshakeLength);
                                    } else {
                                        this.handshakeBuffer.clear();
                                    }
                                }
                                this.handshakeBuffer.writeBytes(in, readerIndex + 5, packetLength - 5);
                                readerIndex += packetLength;
                                readableBytes -= packetLength;
                                if (handshakeLength <= this.handshakeBuffer.readableBytes()) {
                                    ByteBuf clientHello = this.handshakeBuffer.setIndex(0, handshakeLength);
                                    this.handshakeBuffer = null;
                                    select(ctx, clientHello);
                                    return;
                                }
                            }
                    }
                    select(ctx, null);
                    return;
                }
            } catch (NotSslRecordException e2) {
                throw e2;
            } catch (Exception e3) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), (Throwable) e3);
                }
                select(ctx, null);
            }
        }
    }

    private void releaseHandshakeBuffer() {
        releaseIfNotNull(this.handshakeBuffer);
        this.handshakeBuffer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void releaseIfNotNull(ByteBuf buffer) {
        if (buffer != null) {
            buffer.release();
        }
    }

    private void select(final ChannelHandlerContext ctx, final ByteBuf clientHello) throws Exception {
        try {
            try {
                Future<T> future = lookup(ctx, clientHello);
                if (future.isDone()) {
                    onLookupComplete(ctx, future);
                } else {
                    this.suppressRead = true;
                    future.addListener2(new FutureListener<T>() { // from class: io.netty.handler.ssl.SslClientHelloHandler.1
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<T> future2) {
                            SslClientHelloHandler.releaseIfNotNull(clientHello);
                            try {
                                SslClientHelloHandler.this.suppressRead = false;
                                try {
                                    try {
                                        try {
                                            SslClientHelloHandler.this.onLookupComplete(ctx, future2);
                                        } catch (Throwable cause) {
                                            ctx.fireExceptionCaught(cause);
                                        }
                                    } catch (DecoderException err) {
                                        ctx.fireExceptionCaught((Throwable) err);
                                    }
                                } catch (Exception cause2) {
                                    ctx.fireExceptionCaught((Throwable) new DecoderException(cause2));
                                }
                            } finally {
                                if (SslClientHelloHandler.this.readPending) {
                                    SslClientHelloHandler.this.readPending = false;
                                    ctx.read();
                                }
                            }
                        }
                    });
                    clientHello = null;
                }
                releaseIfNotNull(clientHello);
            } catch (Throwable cause) {
                PlatformDependent.throwException(cause);
                releaseIfNotNull(clientHello);
            }
        } catch (Throwable th) {
            releaseIfNotNull(clientHello);
            throw th;
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        releaseHandshakeBuffer();
        super.handlerRemoved0(ctx);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (this.suppressRead) {
            this.readPending = true;
        } else {
            ctx.read();
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
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}

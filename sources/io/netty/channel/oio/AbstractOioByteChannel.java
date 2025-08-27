package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.util.internal.StringUtil;
import java.io.IOException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/oio/AbstractOioByteChannel.class */
public abstract class AbstractOioByteChannel extends AbstractOioChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) FileRegion.class) + ')';

    protected abstract boolean isInputShutdown();

    protected abstract ChannelFuture shutdownInput();

    protected abstract int available();

    protected abstract int doReadBytes(ByteBuf byteBuf) throws Exception;

    protected abstract void doWriteBytes(ByteBuf byteBuf) throws Exception;

    protected abstract void doWriteFileRegion(FileRegion fileRegion) throws Exception;

    protected AbstractOioByteChannel(Channel parent) {
        super(parent);
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    private void closeOnRead(ChannelPipeline pipeline) {
        if (isOpen()) {
            if (Boolean.TRUE.equals(config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                shutdownInput();
                pipeline.fireUserEventTriggered((Object) ChannelInputShutdownEvent.INSTANCE);
            } else {
                unsafe().close(unsafe().voidPromise());
            }
            pipeline.fireUserEventTriggered((Object) ChannelInputShutdownReadComplete.INSTANCE);
        }
    }

    private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, RecvByteBufAllocator.Handle allocHandle) {
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
            closeOnRead(pipeline);
        }
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doRead() {
        ChannelConfig config = config();
        if (isInputShutdown() || !this.readPending) {
            return;
        }
        this.readPending = false;
        ChannelPipeline pipeline = pipeline();
        ByteBufAllocator allocator = config.getAllocator();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        allocHandle.reset(config);
        ByteBuf byteBuf = null;
        boolean close = false;
        boolean readData = false;
        try {
            try {
                byteBuf = allocHandle.allocate(allocator);
                while (true) {
                    allocHandle.lastBytesRead(doReadBytes(byteBuf));
                    if (allocHandle.lastBytesRead() <= 0) {
                        if (!byteBuf.isReadable()) {
                            byteBuf.release();
                            byteBuf = null;
                            close = allocHandle.lastBytesRead() < 0;
                            if (close) {
                                this.readPending = false;
                            }
                        }
                    } else {
                        readData = true;
                        int available = available();
                        if (available <= 0) {
                            break;
                        }
                        if (!byteBuf.isWritable()) {
                            int capacity = byteBuf.capacity();
                            int maxCapacity = byteBuf.maxCapacity();
                            if (capacity == maxCapacity) {
                                allocHandle.incMessagesRead(1);
                                this.readPending = false;
                                pipeline.fireChannelRead((Object) byteBuf);
                                byteBuf = allocHandle.allocate(allocator);
                            } else {
                                int writerIndex = byteBuf.writerIndex();
                                if (writerIndex + available > maxCapacity) {
                                    byteBuf.capacity(maxCapacity);
                                } else {
                                    byteBuf.ensureWritable(available);
                                }
                            }
                        }
                        if (!allocHandle.continueReading()) {
                            break;
                        }
                    }
                }
                if (byteBuf != null) {
                    if (byteBuf.isReadable()) {
                        this.readPending = false;
                        pipeline.fireChannelRead((Object) byteBuf);
                    } else {
                        byteBuf.release();
                    }
                    byteBuf = null;
                }
                if (readData) {
                    allocHandle.readComplete();
                    pipeline.fireChannelReadComplete();
                }
                if (close) {
                    closeOnRead(pipeline);
                }
                if (this.readPending || config.isAutoRead() || (!readData && isActive())) {
                    read();
                }
            } catch (Throwable t) {
                handleReadException(pipeline, byteBuf, t, close, allocHandle);
                if (this.readPending || config.isAutoRead() || (!readData && isActive())) {
                    read();
                }
            }
        } catch (Throwable th) {
            if (this.readPending || config.isAutoRead() || (!readData && isActive())) {
                read();
            }
            throw th;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        while (true) {
            Object msg = in.current();
            if (msg != null) {
                if (msg instanceof ByteBuf) {
                    ByteBuf buf = (ByteBuf) msg;
                    int i = buf.readableBytes();
                    while (true) {
                        int readableBytes = i;
                        if (readableBytes <= 0) {
                            break;
                        }
                        doWriteBytes(buf);
                        int newReadableBytes = buf.readableBytes();
                        in.progress(readableBytes - newReadableBytes);
                        i = newReadableBytes;
                    }
                    in.remove();
                } else if (msg instanceof FileRegion) {
                    FileRegion region = (FileRegion) msg;
                    long transferred = region.transferred();
                    doWriteFileRegion(region);
                    in.progress(region.transferred() - transferred);
                    in.remove();
                } else {
                    in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
                }
            } else {
                return;
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected final Object filterOutboundMessage(Object msg) throws Exception {
        if ((msg instanceof ByteBuf) || (msg instanceof FileRegion)) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }
}

package io.netty.channel.unix;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/SocketWritableByteChannel.class */
public abstract class SocketWritableByteChannel implements WritableByteChannel {
    private final FileDescriptor fd;

    protected abstract ByteBufAllocator alloc();

    protected SocketWritableByteChannel(FileDescriptor fd) {
        this.fd = (FileDescriptor) ObjectUtil.checkNotNull(fd, "fd");
    }

    @Override // java.nio.channels.WritableByteChannel
    public final int write(ByteBuffer src) throws IOException {
        int written;
        int position = src.position();
        int limit = src.limit();
        if (src.isDirect()) {
            written = this.fd.write(src, position, src.limit());
        } else {
            int readableBytes = limit - position;
            ByteBuf buffer = null;
            try {
                if (readableBytes == 0) {
                    buffer = Unpooled.EMPTY_BUFFER;
                } else {
                    ByteBufAllocator alloc = alloc();
                    if (alloc.isDirectBufferPooled()) {
                        buffer = alloc.directBuffer(readableBytes);
                    } else {
                        buffer = ByteBufUtil.threadLocalDirectBuffer();
                        if (buffer == null) {
                            buffer = Unpooled.directBuffer(readableBytes);
                        }
                    }
                }
                buffer.writeBytes(src.duplicate());
                ByteBuffer nioBuffer = buffer.internalNioBuffer(buffer.readerIndex(), readableBytes);
                written = this.fd.write(nioBuffer, nioBuffer.position(), nioBuffer.limit());
                if (buffer != null) {
                    buffer.release();
                }
            } catch (Throwable th) {
                if (buffer != null) {
                    buffer.release();
                }
                throw th;
            }
        }
        if (written > 0) {
            src.position(position + written);
        }
        return written;
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return this.fd.isOpen();
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.fd.close();
    }
}

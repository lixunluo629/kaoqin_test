package io.netty.channel.unix;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/IovArray.class */
public final class IovArray implements ChannelOutboundBuffer.MessageProcessor {
    private static final int ADDRESS_SIZE;
    private static final int IOV_SIZE;
    private static final int CAPACITY;
    private int count;
    private long size;
    static final /* synthetic */ boolean $assertionsDisabled;
    private long maxBytes = Limits.SSIZE_MAX;
    private final ByteBuffer memory = Buffer.allocateDirectWithNativeOrder(CAPACITY);
    private final long memoryAddress = Buffer.memoryAddress(this.memory);

    static {
        $assertionsDisabled = !IovArray.class.desiredAssertionStatus();
        ADDRESS_SIZE = Buffer.addressSize();
        IOV_SIZE = 2 * ADDRESS_SIZE;
        CAPACITY = Limits.IOV_MAX * IOV_SIZE;
    }

    public void clear() {
        this.count = 0;
        this.size = 0L;
    }

    @Deprecated
    public boolean add(ByteBuf buf) {
        return add(buf, buf.readerIndex(), buf.readableBytes());
    }

    public boolean add(ByteBuf buf, int offset, int len) {
        if (this.count == Limits.IOV_MAX) {
            return false;
        }
        if (buf.nioBufferCount() == 1) {
            if (len == 0) {
                return true;
            }
            if (buf.hasMemoryAddress()) {
                return add(buf.memoryAddress() + offset, len);
            }
            return add(Buffer.memoryAddress(buf.internalNioBuffer(offset, len)) + r0.position(), len);
        }
        ByteBuffer[] buffers = buf.nioBuffers(offset, len);
        for (ByteBuffer nioBuffer : buffers) {
            int remaining = nioBuffer.remaining();
            if (remaining != 0 && (!add(Buffer.memoryAddress(nioBuffer) + nioBuffer.position(), remaining) || this.count == Limits.IOV_MAX)) {
                return false;
            }
        }
        return true;
    }

    private boolean add(long addr, int len) {
        if (!$assertionsDisabled && addr == 0) {
            throw new AssertionError();
        }
        if (this.maxBytes - len < this.size && this.count > 0) {
            return false;
        }
        int baseOffset = idx(this.count);
        int lengthOffset = baseOffset + ADDRESS_SIZE;
        this.size += len;
        this.count++;
        if (ADDRESS_SIZE == 8) {
            if (PlatformDependent.hasUnsafe()) {
                PlatformDependent.putLong(baseOffset + this.memoryAddress, addr);
                PlatformDependent.putLong(lengthOffset + this.memoryAddress, len);
                return true;
            }
            this.memory.putLong(baseOffset, addr);
            this.memory.putLong(lengthOffset, len);
            return true;
        }
        if (!$assertionsDisabled && ADDRESS_SIZE != 4) {
            throw new AssertionError();
        }
        if (PlatformDependent.hasUnsafe()) {
            PlatformDependent.putInt(baseOffset + this.memoryAddress, (int) addr);
            PlatformDependent.putInt(lengthOffset + this.memoryAddress, len);
            return true;
        }
        this.memory.putInt(baseOffset, (int) addr);
        this.memory.putInt(lengthOffset, len);
        return true;
    }

    public int count() {
        return this.count;
    }

    public long size() {
        return this.size;
    }

    public void maxBytes(long maxBytes) {
        this.maxBytes = Math.min(Limits.SSIZE_MAX, ObjectUtil.checkPositive(maxBytes, "maxBytes"));
    }

    public long maxBytes() {
        return this.maxBytes;
    }

    public long memoryAddress(int offset) {
        return this.memoryAddress + idx(offset);
    }

    public void release() {
        Buffer.free(this.memory);
    }

    @Override // io.netty.channel.ChannelOutboundBuffer.MessageProcessor
    public boolean processMessage(Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buffer = (ByteBuf) msg;
            return add(buffer, buffer.readerIndex(), buffer.readableBytes());
        }
        return false;
    }

    private static int idx(int index) {
        return IOV_SIZE * index;
    }
}

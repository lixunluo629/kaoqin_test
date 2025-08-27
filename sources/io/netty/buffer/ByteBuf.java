package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBuf.class */
public abstract class ByteBuf implements ReferenceCounted, Comparable<ByteBuf> {
    public abstract int capacity();

    public abstract ByteBuf capacity(int i);

    public abstract int maxCapacity();

    public abstract ByteBufAllocator alloc();

    @Deprecated
    public abstract ByteOrder order();

    @Deprecated
    public abstract ByteBuf order(ByteOrder byteOrder);

    public abstract ByteBuf unwrap();

    public abstract boolean isDirect();

    public abstract boolean isReadOnly();

    public abstract ByteBuf asReadOnly();

    public abstract int readerIndex();

    public abstract ByteBuf readerIndex(int i);

    public abstract int writerIndex();

    public abstract ByteBuf writerIndex(int i);

    public abstract ByteBuf setIndex(int i, int i2);

    public abstract int readableBytes();

    public abstract int writableBytes();

    public abstract int maxWritableBytes();

    public abstract boolean isReadable();

    public abstract boolean isReadable(int i);

    public abstract boolean isWritable();

    public abstract boolean isWritable(int i);

    public abstract ByteBuf clear();

    public abstract ByteBuf markReaderIndex();

    public abstract ByteBuf resetReaderIndex();

    public abstract ByteBuf markWriterIndex();

    public abstract ByteBuf resetWriterIndex();

    public abstract ByteBuf discardReadBytes();

    public abstract ByteBuf discardSomeReadBytes();

    public abstract ByteBuf ensureWritable(int i);

    public abstract int ensureWritable(int i, boolean z);

    public abstract boolean getBoolean(int i);

    public abstract byte getByte(int i);

    public abstract short getUnsignedByte(int i);

    public abstract short getShort(int i);

    public abstract short getShortLE(int i);

    public abstract int getUnsignedShort(int i);

    public abstract int getUnsignedShortLE(int i);

    public abstract int getMedium(int i);

    public abstract int getMediumLE(int i);

    public abstract int getUnsignedMedium(int i);

    public abstract int getUnsignedMediumLE(int i);

    public abstract int getInt(int i);

    public abstract int getIntLE(int i);

    public abstract long getUnsignedInt(int i);

    public abstract long getUnsignedIntLE(int i);

    public abstract long getLong(int i);

    public abstract long getLongLE(int i);

    public abstract char getChar(int i);

    public abstract float getFloat(int i);

    public abstract double getDouble(int i);

    public abstract ByteBuf getBytes(int i, ByteBuf byteBuf);

    public abstract ByteBuf getBytes(int i, ByteBuf byteBuf, int i2);

    public abstract ByteBuf getBytes(int i, ByteBuf byteBuf, int i2, int i3);

    public abstract ByteBuf getBytes(int i, byte[] bArr);

    public abstract ByteBuf getBytes(int i, byte[] bArr, int i2, int i3);

    public abstract ByteBuf getBytes(int i, ByteBuffer byteBuffer);

    public abstract ByteBuf getBytes(int i, OutputStream outputStream, int i2) throws IOException;

    public abstract int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i2) throws IOException;

    public abstract int getBytes(int i, FileChannel fileChannel, long j, int i2) throws IOException;

    public abstract CharSequence getCharSequence(int i, int i2, Charset charset);

    public abstract ByteBuf setBoolean(int i, boolean z);

    public abstract ByteBuf setByte(int i, int i2);

    public abstract ByteBuf setShort(int i, int i2);

    public abstract ByteBuf setShortLE(int i, int i2);

    public abstract ByteBuf setMedium(int i, int i2);

    public abstract ByteBuf setMediumLE(int i, int i2);

    public abstract ByteBuf setInt(int i, int i2);

    public abstract ByteBuf setIntLE(int i, int i2);

    public abstract ByteBuf setLong(int i, long j);

    public abstract ByteBuf setLongLE(int i, long j);

    public abstract ByteBuf setChar(int i, int i2);

    public abstract ByteBuf setFloat(int i, float f);

    public abstract ByteBuf setDouble(int i, double d);

    public abstract ByteBuf setBytes(int i, ByteBuf byteBuf);

    public abstract ByteBuf setBytes(int i, ByteBuf byteBuf, int i2);

    public abstract ByteBuf setBytes(int i, ByteBuf byteBuf, int i2, int i3);

    public abstract ByteBuf setBytes(int i, byte[] bArr);

    public abstract ByteBuf setBytes(int i, byte[] bArr, int i2, int i3);

    public abstract ByteBuf setBytes(int i, ByteBuffer byteBuffer);

    public abstract int setBytes(int i, InputStream inputStream, int i2) throws IOException;

    public abstract int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i2) throws IOException;

    public abstract int setBytes(int i, FileChannel fileChannel, long j, int i2) throws IOException;

    public abstract ByteBuf setZero(int i, int i2);

    public abstract int setCharSequence(int i, CharSequence charSequence, Charset charset);

    public abstract boolean readBoolean();

    public abstract byte readByte();

    public abstract short readUnsignedByte();

    public abstract short readShort();

    public abstract short readShortLE();

    public abstract int readUnsignedShort();

    public abstract int readUnsignedShortLE();

    public abstract int readMedium();

    public abstract int readMediumLE();

    public abstract int readUnsignedMedium();

    public abstract int readUnsignedMediumLE();

    public abstract int readInt();

    public abstract int readIntLE();

    public abstract long readUnsignedInt();

    public abstract long readUnsignedIntLE();

    public abstract long readLong();

    public abstract long readLongLE();

    public abstract char readChar();

    public abstract float readFloat();

    public abstract double readDouble();

    public abstract ByteBuf readBytes(int i);

    public abstract ByteBuf readSlice(int i);

    public abstract ByteBuf readRetainedSlice(int i);

    public abstract ByteBuf readBytes(ByteBuf byteBuf);

    public abstract ByteBuf readBytes(ByteBuf byteBuf, int i);

    public abstract ByteBuf readBytes(ByteBuf byteBuf, int i, int i2);

    public abstract ByteBuf readBytes(byte[] bArr);

    public abstract ByteBuf readBytes(byte[] bArr, int i, int i2);

    public abstract ByteBuf readBytes(ByteBuffer byteBuffer);

    public abstract ByteBuf readBytes(OutputStream outputStream, int i) throws IOException;

    public abstract int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException;

    public abstract CharSequence readCharSequence(int i, Charset charset);

    public abstract int readBytes(FileChannel fileChannel, long j, int i) throws IOException;

    public abstract ByteBuf skipBytes(int i);

    public abstract ByteBuf writeBoolean(boolean z);

    public abstract ByteBuf writeByte(int i);

    public abstract ByteBuf writeShort(int i);

    public abstract ByteBuf writeShortLE(int i);

    public abstract ByteBuf writeMedium(int i);

    public abstract ByteBuf writeMediumLE(int i);

    public abstract ByteBuf writeInt(int i);

    public abstract ByteBuf writeIntLE(int i);

    public abstract ByteBuf writeLong(long j);

    public abstract ByteBuf writeLongLE(long j);

    public abstract ByteBuf writeChar(int i);

    public abstract ByteBuf writeFloat(float f);

    public abstract ByteBuf writeDouble(double d);

    public abstract ByteBuf writeBytes(ByteBuf byteBuf);

    public abstract ByteBuf writeBytes(ByteBuf byteBuf, int i);

    public abstract ByteBuf writeBytes(ByteBuf byteBuf, int i, int i2);

    public abstract ByteBuf writeBytes(byte[] bArr);

    public abstract ByteBuf writeBytes(byte[] bArr, int i, int i2);

    public abstract ByteBuf writeBytes(ByteBuffer byteBuffer);

    public abstract int writeBytes(InputStream inputStream, int i) throws IOException;

    public abstract int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException;

    public abstract int writeBytes(FileChannel fileChannel, long j, int i) throws IOException;

    public abstract ByteBuf writeZero(int i);

    public abstract int writeCharSequence(CharSequence charSequence, Charset charset);

    public abstract int indexOf(int i, int i2, byte b);

    public abstract int bytesBefore(byte b);

    public abstract int bytesBefore(int i, byte b);

    public abstract int bytesBefore(int i, int i2, byte b);

    public abstract int forEachByte(ByteProcessor byteProcessor);

    public abstract int forEachByte(int i, int i2, ByteProcessor byteProcessor);

    public abstract int forEachByteDesc(ByteProcessor byteProcessor);

    public abstract int forEachByteDesc(int i, int i2, ByteProcessor byteProcessor);

    public abstract ByteBuf copy();

    public abstract ByteBuf copy(int i, int i2);

    public abstract ByteBuf slice();

    public abstract ByteBuf retainedSlice();

    public abstract ByteBuf slice(int i, int i2);

    public abstract ByteBuf retainedSlice(int i, int i2);

    public abstract ByteBuf duplicate();

    public abstract ByteBuf retainedDuplicate();

    public abstract int nioBufferCount();

    public abstract ByteBuffer nioBuffer();

    public abstract ByteBuffer nioBuffer(int i, int i2);

    public abstract ByteBuffer internalNioBuffer(int i, int i2);

    public abstract ByteBuffer[] nioBuffers();

    public abstract ByteBuffer[] nioBuffers(int i, int i2);

    public abstract boolean hasArray();

    public abstract byte[] array();

    public abstract int arrayOffset();

    public abstract boolean hasMemoryAddress();

    public abstract long memoryAddress();

    public abstract String toString(Charset charset);

    public abstract String toString(int i, int i2, Charset charset);

    public abstract int hashCode();

    public abstract boolean equals(Object obj);

    @Override // java.lang.Comparable
    public abstract int compareTo(ByteBuf byteBuf);

    public abstract String toString();

    @Override // io.netty.util.ReferenceCounted
    public abstract ByteBuf retain(int i);

    @Override // io.netty.util.ReferenceCounted
    public abstract ByteBuf retain();

    @Override // io.netty.util.ReferenceCounted
    public abstract ByteBuf touch();

    @Override // io.netty.util.ReferenceCounted
    public abstract ByteBuf touch(Object obj);

    public int maxFastWritableBytes() {
        return writableBytes();
    }

    public float getFloatLE(int index) {
        return Float.intBitsToFloat(getIntLE(index));
    }

    public double getDoubleLE(int index) {
        return Double.longBitsToDouble(getLongLE(index));
    }

    public ByteBuf setFloatLE(int index, float value) {
        return setIntLE(index, Float.floatToRawIntBits(value));
    }

    public ByteBuf setDoubleLE(int index, double value) {
        return setLongLE(index, Double.doubleToRawLongBits(value));
    }

    public float readFloatLE() {
        return Float.intBitsToFloat(readIntLE());
    }

    public double readDoubleLE() {
        return Double.longBitsToDouble(readLongLE());
    }

    public ByteBuf writeFloatLE(float value) {
        return writeIntLE(Float.floatToRawIntBits(value));
    }

    public ByteBuf writeDoubleLE(double value) {
        return writeLongLE(Double.doubleToRawLongBits(value));
    }

    public boolean isContiguous() {
        return false;
    }

    boolean isAccessible() {
        return refCnt() != 0;
    }
}

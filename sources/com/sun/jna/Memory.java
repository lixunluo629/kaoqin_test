package com.sun.jna;

import java.nio.ByteBuffer;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Memory.class */
public class Memory extends Pointer {
    protected long size;

    static native long malloc(long j);

    static native void free(long j);

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Memory$SharedMemory.class */
    private class SharedMemory extends Memory {
        private final Memory this$0;

        public SharedMemory(Memory memory, long offset) {
            this.this$0 = memory;
            this.size = memory.size - offset;
            this.peer = memory.peer + offset;
        }

        @Override // com.sun.jna.Memory
        protected void finalize() {
        }

        @Override // com.sun.jna.Memory
        protected void boundsCheck(long off, long sz) {
            this.this$0.boundsCheck((this.peer - this.this$0.peer) + off, sz);
        }

        @Override // com.sun.jna.Memory, com.sun.jna.Pointer
        public String toString() {
            return new StringBuffer().append(super.toString()).append(" (shared from ").append(this.this$0.toString()).append(")").toString();
        }
    }

    public Memory(long size) {
        this.size = size;
        if (size <= 0) {
            throw new IllegalArgumentException("Allocation size must be >= 0");
        }
        this.peer = malloc(size);
        if (this.peer == 0) {
            throw new OutOfMemoryError(new StringBuffer().append("Cannot allocate ").append(size).append(" bytes").toString());
        }
    }

    protected Memory() {
    }

    @Override // com.sun.jna.Pointer
    public Pointer share(long offset) {
        return share(offset, getSize() - offset);
    }

    @Override // com.sun.jna.Pointer
    public Pointer share(long offset, long sz) {
        boundsCheck(offset, sz);
        return new SharedMemory(this, offset);
    }

    public Pointer align(int byteBoundary) {
        if (byteBoundary <= 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Byte boundary must be positive: ").append(byteBoundary).toString());
        }
        long mask = (byteBoundary - 1) ^ (-1);
        if ((this.peer & (mask ^ (-1))) != this.peer) {
            long newPeer = (this.peer + (mask ^ (-1))) & mask;
            return share(newPeer - this.peer, (this.peer + this.size) - newPeer);
        }
        return this;
    }

    protected void finalize() {
        if (this.peer != 0) {
            free(this.peer);
            this.peer = 0L;
        }
    }

    public void clear() {
        clear(this.size);
    }

    public boolean isValid() {
        return this.peer != 0;
    }

    public long getSize() {
        return this.size;
    }

    protected void boundsCheck(long off, long sz) {
        if (off < 0) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("Invalid offset: ").append(off).toString());
        }
        if (off + sz > this.size) {
            String msg = new StringBuffer().append("Bounds exceeds available space : size=").append(this.size).append(", offset=").append(off + sz).toString();
            throw new IndexOutOfBoundsException(msg);
        }
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, byte[] buf, int index, int length) {
        boundsCheck(bOff, length * 1);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, short[] buf, int index, int length) {
        boundsCheck(bOff, length * 2);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, char[] buf, int index, int length) {
        boundsCheck(bOff, length * 2);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, int[] buf, int index, int length) {
        boundsCheck(bOff, length * 4);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, long[] buf, int index, int length) {
        boundsCheck(bOff, length * 8);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, float[] buf, int index, int length) {
        boundsCheck(bOff, length * 4);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void read(long bOff, double[] buf, int index, int length) {
        boundsCheck(bOff, length * 8);
        super.read(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, byte[] buf, int index, int length) {
        boundsCheck(bOff, length * 1);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, short[] buf, int index, int length) {
        boundsCheck(bOff, length * 2);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, char[] buf, int index, int length) {
        boundsCheck(bOff, length * 2);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, int[] buf, int index, int length) {
        boundsCheck(bOff, length * 4);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, long[] buf, int index, int length) {
        boundsCheck(bOff, length * 8);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, float[] buf, int index, int length) {
        boundsCheck(bOff, length * 4);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public void write(long bOff, double[] buf, int index, int length) {
        boundsCheck(bOff, length * 8);
        super.write(bOff, buf, index, length);
    }

    @Override // com.sun.jna.Pointer
    public byte getByte(long offset) {
        boundsCheck(offset, 1L);
        return super.getByte(offset);
    }

    @Override // com.sun.jna.Pointer
    public char getChar(long offset) {
        boundsCheck(offset, 1L);
        return super.getChar(offset);
    }

    @Override // com.sun.jna.Pointer
    public short getShort(long offset) {
        boundsCheck(offset, 2L);
        return super.getShort(offset);
    }

    @Override // com.sun.jna.Pointer
    public int getInt(long offset) {
        boundsCheck(offset, 4L);
        return super.getInt(offset);
    }

    @Override // com.sun.jna.Pointer
    public long getLong(long offset) {
        boundsCheck(offset, 8L);
        return super.getLong(offset);
    }

    @Override // com.sun.jna.Pointer
    public float getFloat(long offset) {
        boundsCheck(offset, 4L);
        return super.getFloat(offset);
    }

    @Override // com.sun.jna.Pointer
    public double getDouble(long offset) {
        boundsCheck(offset, 8L);
        return super.getDouble(offset);
    }

    @Override // com.sun.jna.Pointer
    public Pointer getPointer(long offset) {
        boundsCheck(offset, Pointer.SIZE);
        return super.getPointer(offset);
    }

    @Override // com.sun.jna.Pointer
    public ByteBuffer getByteBuffer(long offset, long length) {
        boundsCheck(offset, length);
        return super.getByteBuffer(offset, length);
    }

    @Override // com.sun.jna.Pointer
    public String getString(long offset, boolean wide) {
        boundsCheck(offset, 0L);
        return super.getString(offset, wide);
    }

    @Override // com.sun.jna.Pointer
    public void setByte(long offset, byte value) {
        boundsCheck(offset, 1L);
        super.setByte(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setChar(long offset, char value) {
        boundsCheck(offset, Native.WCHAR_SIZE);
        super.setChar(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setShort(long offset, short value) {
        boundsCheck(offset, 2L);
        super.setShort(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setInt(long offset, int value) {
        boundsCheck(offset, 4L);
        super.setInt(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setLong(long offset, long value) {
        boundsCheck(offset, 8L);
        super.setLong(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setFloat(long offset, float value) {
        boundsCheck(offset, 4L);
        super.setFloat(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setDouble(long offset, double value) {
        boundsCheck(offset, 8L);
        super.setDouble(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setPointer(long offset, Pointer value) {
        boundsCheck(offset, Pointer.SIZE);
        super.setPointer(offset, value);
    }

    @Override // com.sun.jna.Pointer
    public void setString(long offset, String value, boolean wide) {
        if (wide) {
            boundsCheck(offset, (value.length() + 1) * Native.WCHAR_SIZE);
        } else {
            boundsCheck(offset, value.getBytes().length + 1);
        }
        super.setString(offset, value, wide);
    }

    @Override // com.sun.jna.Pointer
    public String toString() {
        return new StringBuffer().append("allocated@0x").append(Long.toHexString(this.peer)).append(" (").append(this.size).append(" bytes)").toString();
    }
}

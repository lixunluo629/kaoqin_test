package com.sun.jna;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Pointer.class */
public class Pointer {
    public static final int SIZE;
    public static final Pointer NULL;
    long peer;

    /* renamed from: com.sun.jna.Pointer$1, reason: invalid class name */
    /* loaded from: jna-3.0.9.jar:com/sun/jna/Pointer$1.class */
    static class AnonymousClass1 {
    }

    private static native long _indexOf(long j, byte b);

    private static native void _read(long j, byte[] bArr, int i, int i2);

    private static native void _read(long j, short[] sArr, int i, int i2);

    private static native void _read(long j, char[] cArr, int i, int i2);

    private static native void _read(long j, int[] iArr, int i, int i2);

    private static native void _read(long j, long[] jArr, int i, int i2);

    private static native void _read(long j, float[] fArr, int i, int i2);

    private static native void _read(long j, double[] dArr, int i, int i2);

    private static native void _write(long j, byte[] bArr, int i, int i2);

    private static native void _write(long j, short[] sArr, int i, int i2);

    private static native void _write(long j, char[] cArr, int i, int i2);

    private static native void _write(long j, int[] iArr, int i, int i2);

    private static native void _write(long j, long[] jArr, int i, int i2);

    private static native void _write(long j, float[] fArr, int i, int i2);

    private static native void _write(long j, double[] dArr, int i, int i2);

    private static native byte _getByte(long j);

    private static native char _getChar(long j);

    private static native short _getShort(long j);

    private static native int _getInt(long j);

    private static native long _getLong(long j);

    private native float _getFloat(long j);

    private static native double _getDouble(long j);

    private static native Pointer _getPointer(long j);

    private native ByteBuffer _getDirectByteBuffer(long j, long j2);

    private static native String _getString(long j, boolean z);

    private static native void _setMemory(long j, long j2, byte b);

    private static native void _setByte(long j, byte b);

    private static native void _setShort(long j, short s);

    private static native void _setChar(long j, char c);

    private static native void _setInt(long j, int i);

    private static native void _setLong(long j, long j2);

    private static native void _setFloat(long j, float f);

    private static native void _setDouble(long j, double d);

    private static native void _setPointer(long j, long j2);

    private static native void _setString(long j, String str, boolean z);

    static {
        int i = Native.POINTER_SIZE;
        SIZE = i;
        if (i == 0) {
            throw new Error("Native library not initialized");
        }
        NULL = null;
    }

    public static final Pointer createConstant(long peer) {
        return new Opaque(peer, null);
    }

    Pointer() {
    }

    Pointer(long peer) {
        this.peer = peer;
    }

    public Pointer share(long offset) {
        return share(offset, 0L);
    }

    public Pointer share(long offset, long sz) {
        return new Pointer(this.peer + offset);
    }

    void clear(long size) {
        setMemory(0L, size, (byte) 0);
    }

    public boolean equals(Object o) {
        return o == null ? this.peer == 0 : (o instanceof Pointer) && ((Pointer) o).peer == this.peer;
    }

    public int hashCode() {
        return (int) ((this.peer >>> 32) + (this.peer & (-1)));
    }

    public long indexOf(long offset, byte value) {
        return _indexOf(this.peer + offset, value);
    }

    public void read(long offset, byte[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, short[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, char[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, int[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, long[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, float[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, double[] buf, int index, int length) {
        _read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, Pointer[] buf, int index, int length) {
        for (int i = 0; i < length; i++) {
            buf[i + index] = getPointer(offset + (i * SIZE));
        }
    }

    public void write(long offset, byte[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, short[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, char[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, int[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, long[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, float[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, double[] buf, int index, int length) {
        _write(this.peer + offset, buf, index, length);
    }

    public void write(long bOff, Pointer[] buf, int index, int length) {
        for (int i = 0; i < length; i++) {
            setPointer(bOff + (i * SIZE), buf[index + i]);
        }
    }

    public byte getByte(long offset) {
        return _getByte(this.peer + offset);
    }

    public char getChar(long offset) {
        return _getChar(this.peer + offset);
    }

    public short getShort(long offset) {
        return _getShort(this.peer + offset);
    }

    public int getInt(long offset) {
        return _getInt(this.peer + offset);
    }

    public long getLong(long offset) {
        return _getLong(this.peer + offset);
    }

    public NativeLong getNativeLong(long offset) {
        return new NativeLong(NativeLong.SIZE == 8 ? getLong(offset) : getInt(offset));
    }

    public float getFloat(long offset) {
        return _getFloat(this.peer + offset);
    }

    public double getDouble(long offset) {
        return _getDouble(this.peer + offset);
    }

    public Pointer getPointer(long offset) {
        return _getPointer(this.peer + offset);
    }

    public ByteBuffer getByteBuffer(long offset, long length) {
        return _getDirectByteBuffer(this.peer + offset, length).order(ByteOrder.nativeOrder());
    }

    public String getString(long offset, boolean wide) {
        return _getString(this.peer + offset, wide);
    }

    public String getString(long offset) {
        String encoding = System.getProperty("jna.encoding");
        if (encoding != null) {
            long len = indexOf(offset, (byte) 0);
            if (len != -1) {
                if (len > 2147483647L) {
                    throw new OutOfMemoryError(new StringBuffer().append("String exceeds maximum length: ").append(len).toString());
                }
                byte[] data = getByteArray(offset, (int) len);
                try {
                    return new String(data, encoding);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return getString(offset, false);
    }

    public byte[] getByteArray(long offset, int arraySize) {
        byte[] buf = new byte[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public char[] getCharArray(long offset, int arraySize) {
        char[] buf = new char[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public short[] getShortArray(long offset, int arraySize) {
        short[] buf = new short[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public int[] getIntArray(long offset, int arraySize) {
        int[] buf = new int[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public long[] getLongArray(long offset, int arraySize) {
        long[] buf = new long[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public float[] getFloatArray(long offset, int arraySize) {
        float[] buf = new float[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public double[] getDoubleArray(long offset, int arraySize) {
        double[] buf = new double[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public Pointer[] getPointerArray(long base) {
        List array = new ArrayList();
        int offset = 0;
        Pointer pointer = getPointer(base);
        while (true) {
            Pointer p = pointer;
            if (p != null) {
                array.add(p);
                offset += SIZE;
                pointer = getPointer(base + offset);
            } else {
                return (Pointer[]) array.toArray(new Pointer[array.size()]);
            }
        }
    }

    public Pointer[] getPointerArray(long offset, int arraySize) {
        Pointer[] buf = new Pointer[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public String[] getStringArray(long base) {
        return getStringArray(base, false);
    }

    public String[] getStringArray(long base, boolean wide) {
        List strings = new ArrayList();
        int offset = 0;
        Pointer pointer = getPointer(base);
        while (true) {
            Pointer p = pointer;
            if (p != null) {
                strings.add(p.getString(0L, wide));
                offset += SIZE;
                pointer = getPointer(base + offset);
            } else {
                return (String[]) strings.toArray(new String[strings.size()]);
            }
        }
    }

    public void setMemory(long offset, long length, byte value) {
        _setMemory(this.peer + offset, length, value);
    }

    public void setByte(long offset, byte value) {
        _setByte(this.peer + offset, value);
    }

    public void setShort(long offset, short value) {
        _setShort(this.peer + offset, value);
    }

    public void setChar(long offset, char value) {
        _setChar(this.peer + offset, value);
    }

    public void setInt(long offset, int value) {
        _setInt(this.peer + offset, value);
    }

    public void setLong(long offset, long value) {
        _setLong(this.peer + offset, value);
    }

    public void setNativeLong(long offset, NativeLong value) {
        if (NativeLong.SIZE == 8) {
            setLong(offset, value.longValue());
        } else {
            setInt(offset, value.intValue());
        }
    }

    public void setFloat(long offset, float value) {
        _setFloat(this.peer + offset, value);
    }

    public void setDouble(long offset, double value) {
        _setDouble(this.peer + offset, value);
    }

    public void setPointer(long offset, Pointer value) {
        _setPointer(this.peer + offset, value != null ? value.peer : 0L);
    }

    public void setString(long offset, String value, boolean wide) {
        _setString(this.peer + offset, value, wide);
    }

    public void setString(long offset, String value) {
        byte[] data = Native.getBytes(value);
        write(offset, data, 0, data.length);
        setByte(offset + data.length, (byte) 0);
    }

    public String toString() {
        return new StringBuffer().append("native@0x").append(Long.toHexString(this.peer)).toString();
    }

    /* loaded from: jna-3.0.9.jar:com/sun/jna/Pointer$Opaque.class */
    private static class Opaque extends Pointer {
        private String MSG;

        Opaque(long x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Opaque(long peer) {
            super(peer);
            this.MSG = new StringBuffer().append("This pointer is opaque: ").append(this).toString();
        }

        @Override // com.sun.jna.Pointer
        public long indexOf(long offset, byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, byte[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, char[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, short[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, int[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, long[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, float[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, double[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, byte[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, char[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, short[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, int[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, long[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, float[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, double[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public byte getByte(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public char getChar(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public short getShort(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public int getInt(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public long getLong(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public float getFloat(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public double getDouble(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public Pointer getPointer(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public String getString(long bOff, boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setByte(long bOff, byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setChar(long bOff, char value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setShort(long bOff, short value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setInt(long bOff, int value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setLong(long bOff, long value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setFloat(long bOff, float value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setDouble(long bOff, double value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setPointer(long offset, Pointer value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setString(long offset, String value, boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public String toString() {
            return new StringBuffer().append("opaque@0x").append(Long.toHexString(this.peer)).toString();
        }
    }
}

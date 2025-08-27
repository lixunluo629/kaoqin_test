package com.itextpdf.io.source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/OutputStream.class */
public class OutputStream<T extends java.io.OutputStream> extends java.io.OutputStream implements Serializable {
    private static final long serialVersionUID = -5337390096148526418L;
    private final ByteBuffer numBuffer;
    protected java.io.OutputStream outputStream;
    protected long currentPos;
    protected boolean closeStream;

    public static boolean getHighPrecision() {
        return ByteUtils.HighPrecision;
    }

    public static void setHighPrecision(boolean value) {
        ByteUtils.HighPrecision = value;
    }

    public OutputStream(java.io.OutputStream outputStream) {
        this.numBuffer = new ByteBuffer(32);
        this.outputStream = null;
        this.currentPos = 0L;
        this.closeStream = true;
        this.outputStream = outputStream;
    }

    protected OutputStream() {
        this.numBuffer = new ByteBuffer(32);
        this.outputStream = null;
        this.currentPos = 0L;
        this.closeStream = true;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.outputStream.write(b);
        this.currentPos++;
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        this.outputStream.write(b);
        this.currentPos += b.length;
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        this.currentPos += len;
    }

    public void writeByte(byte value) {
        try {
            write(value);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteByte, (Throwable) e);
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.outputStream.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeStream) {
            this.outputStream.close();
        }
    }

    public T writeLong(long value) {
        try {
            ByteUtils.getIsoBytes(value, this.numBuffer.reset());
            write(this.numBuffer.getInternalBuffer(), this.numBuffer.capacity() - this.numBuffer.size(), this.numBuffer.size());
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteIntNumber, (Throwable) e);
        }
    }

    public T writeInteger(int value) {
        try {
            ByteUtils.getIsoBytes(value, this.numBuffer.reset());
            write(this.numBuffer.getInternalBuffer(), this.numBuffer.capacity() - this.numBuffer.size(), this.numBuffer.size());
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteIntNumber, (Throwable) e);
        }
    }

    public T writeFloat(float f) {
        return (T) writeFloat(f, ByteUtils.HighPrecision);
    }

    public T writeFloat(float f, boolean z) {
        return (T) writeDouble(f, z);
    }

    public T writeFloats(float[] value) {
        for (int i = 0; i < value.length; i++) {
            writeFloat(value[i]);
            if (i < value.length - 1) {
                writeSpace();
            }
        }
        return this;
    }

    public T writeDouble(double d) {
        return (T) writeDouble(d, ByteUtils.HighPrecision);
    }

    public T writeDouble(double value, boolean highPrecision) {
        try {
            ByteUtils.getIsoBytes(value, this.numBuffer.reset(), highPrecision);
            write(this.numBuffer.getInternalBuffer(), this.numBuffer.capacity() - this.numBuffer.size(), this.numBuffer.size());
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteFloatNumber, (Throwable) e);
        }
    }

    public T writeByte(int value) {
        try {
            write(value);
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteByte, (Throwable) e);
        }
    }

    public T writeSpace() {
        return (T) writeByte(32);
    }

    public T writeNewLine() {
        return (T) writeByte(10);
    }

    public T writeString(String str) {
        return (T) writeBytes(ByteUtils.getIsoBytes(str));
    }

    public T writeBytes(byte[] b) {
        try {
            write(b);
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteBytes, (Throwable) e);
        }
    }

    public T writeBytes(byte[] b, int off, int len) {
        try {
            write(b, off, len);
            return this;
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotWriteBytes, (Throwable) e);
        }
    }

    public long getCurrentPos() {
        return this.currentPos;
    }

    public java.io.OutputStream getOutputStream() {
        return this.outputStream;
    }

    public boolean isCloseStream() {
        return this.closeStream;
    }

    public void setCloseStream(boolean closeStream) {
        this.closeStream = closeStream;
    }

    public void assignBytes(byte[] bytes, int count) {
        if (this.outputStream instanceof ByteArrayOutputStream) {
            ((ByteArrayOutputStream) this.outputStream).assignBytes(bytes, count);
            this.currentPos = count;
            return;
        }
        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.BytesCanBeAssignedToByteArrayOutputStreamOnly);
    }

    public void reset() {
        if (this.outputStream instanceof ByteArrayOutputStream) {
            ((ByteArrayOutputStream) this.outputStream).reset();
            this.currentPos = 0L;
            return;
        }
        throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.BytesCanBeResetInByteArrayOutputStreamOnly);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        java.io.OutputStream tempOutputStream = this.outputStream;
        this.outputStream = null;
        out.defaultWriteObject();
        this.outputStream = tempOutputStream;
    }
}

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/AbstractByteArrayOutputStream.class */
public abstract class AbstractByteArrayOutputStream extends OutputStream {
    static final int DEFAULT_SIZE = 1024;
    private int currentBufferIndex;
    private int filledBufferSum;
    private byte[] currentBuffer;
    protected int count;
    private final List<byte[]> buffers = new ArrayList();
    private boolean reuseBuffers = true;

    @FunctionalInterface
    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/AbstractByteArrayOutputStream$InputStreamConstructor.class */
    protected interface InputStreamConstructor<T extends InputStream> {
        T construct(byte[] bArr, int i, int i2);
    }

    public abstract void reset();

    public abstract int size();

    public abstract byte[] toByteArray();

    public abstract InputStream toInputStream();

    @Override // java.io.OutputStream
    public abstract void write(byte[] bArr, int i, int i2);

    public abstract int write(InputStream inputStream) throws IOException;

    @Override // java.io.OutputStream
    public abstract void write(int i);

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    protected void needNewBuffer(int newCount) {
        int newBufferSize;
        if (this.currentBufferIndex < this.buffers.size() - 1) {
            this.filledBufferSum += this.currentBuffer.length;
            this.currentBufferIndex++;
            this.currentBuffer = this.buffers.get(this.currentBufferIndex);
            return;
        }
        if (this.currentBuffer == null) {
            newBufferSize = newCount;
            this.filledBufferSum = 0;
        } else {
            newBufferSize = Math.max(this.currentBuffer.length << 1, newCount - this.filledBufferSum);
            this.filledBufferSum += this.currentBuffer.length;
        }
        this.currentBufferIndex++;
        this.currentBuffer = IOUtils.byteArray(newBufferSize);
        this.buffers.add(this.currentBuffer);
    }

    protected void resetImpl() {
        this.count = 0;
        this.filledBufferSum = 0;
        this.currentBufferIndex = 0;
        if (this.reuseBuffers) {
            this.currentBuffer = this.buffers.get(this.currentBufferIndex);
            return;
        }
        this.currentBuffer = null;
        int size = this.buffers.get(0).length;
        this.buffers.clear();
        needNewBuffer(size);
        this.reuseBuffers = true;
    }

    protected byte[] toByteArrayImpl() {
        int remaining = this.count;
        if (remaining == 0) {
            return IOUtils.EMPTY_BYTE_ARRAY;
        }
        byte[] newBuf = IOUtils.byteArray(remaining);
        int pos = 0;
        for (byte[] buf : this.buffers) {
            int c = Math.min(buf.length, remaining);
            System.arraycopy(buf, 0, newBuf, pos, c);
            pos += c;
            remaining -= c;
            if (remaining == 0) {
                break;
            }
        }
        return newBuf;
    }

    protected <T extends InputStream> InputStream toInputStream(InputStreamConstructor<T> isConstructor) {
        int remaining = this.count;
        if (remaining == 0) {
            return ClosedInputStream.INSTANCE;
        }
        ArrayList arrayList = new ArrayList(this.buffers.size());
        for (byte[] buf : this.buffers) {
            int c = Math.min(buf.length, remaining);
            arrayList.add(isConstructor.construct(buf, 0, c));
            remaining -= c;
            if (remaining == 0) {
                break;
            }
        }
        this.reuseBuffers = false;
        return new SequenceInputStream(Collections.enumeration(arrayList));
    }

    @Deprecated
    public String toString() {
        return new String(toByteArray(), Charset.defaultCharset());
    }

    public String toString(Charset charset) {
        return new String(toByteArray(), charset);
    }

    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(toByteArray(), enc);
    }

    protected void writeImpl(byte[] b, int off, int len) {
        int newCount = this.count + len;
        int remaining = len;
        int inBufferPos = this.count - this.filledBufferSum;
        while (remaining > 0) {
            int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
            System.arraycopy(b, (off + len) - remaining, this.currentBuffer, inBufferPos, part);
            remaining -= part;
            if (remaining > 0) {
                needNewBuffer(newCount);
                inBufferPos = 0;
            }
        }
        this.count = newCount;
    }

    protected int writeImpl(InputStream in) throws IOException {
        int readCount = 0;
        int inBufferPos = this.count - this.filledBufferSum;
        int i = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
        while (true) {
            int n = i;
            if (n != -1) {
                readCount += n;
                inBufferPos += n;
                this.count += n;
                if (inBufferPos == this.currentBuffer.length) {
                    needNewBuffer(this.currentBuffer.length);
                    inBufferPos = 0;
                }
                i = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
            } else {
                return readCount;
            }
        }
    }

    protected void writeImpl(int b) {
        int inBufferPos = this.count - this.filledBufferSum;
        if (inBufferPos == this.currentBuffer.length) {
            needNewBuffer(this.count + 1);
            inBufferPos = 0;
        }
        this.currentBuffer[inBufferPos] = (byte) b;
        this.count++;
    }

    protected void writeToImpl(OutputStream out) throws IOException {
        int remaining = this.count;
        for (byte[] buf : this.buffers) {
            int c = Math.min(buf.length, remaining);
            out.write(buf, 0, c);
            remaining -= c;
            if (remaining == 0) {
                return;
            }
        }
    }
}

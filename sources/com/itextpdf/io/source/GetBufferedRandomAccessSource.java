package com.itextpdf.io.source;

import java.io.IOException;
import java.io.Serializable;
import org.aspectj.apache.bcel.Constants;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/GetBufferedRandomAccessSource.class */
public class GetBufferedRandomAccessSource implements IRandomAccessSource, Serializable {
    private static final long serialVersionUID = -8922625738755763494L;
    private final IRandomAccessSource source;
    private final byte[] getBuffer;
    private long getBufferStart;
    private long getBufferEnd;

    public GetBufferedRandomAccessSource(IRandomAccessSource source) {
        this.getBufferStart = -1L;
        this.getBufferEnd = -1L;
        this.source = source;
        this.getBuffer = new byte[(int) Math.min(Math.max(source.length() / 4, 1L), Constants.NEGATABLE)];
        this.getBufferStart = -1L;
        this.getBufferEnd = -1L;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        if (position < this.getBufferStart || position > this.getBufferEnd) {
            int count = this.source.get(position, this.getBuffer, 0, this.getBuffer.length);
            if (count == -1) {
                return -1;
            }
            this.getBufferStart = position;
            this.getBufferEnd = (position + count) - 1;
        }
        int bufPos = (int) (position - this.getBufferStart);
        return 255 & this.getBuffer[bufPos];
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        return this.source.get(position, bytes, off, len);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.source.length();
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        this.source.close();
        this.getBufferStart = -1L;
        this.getBufferEnd = -1L;
    }
}

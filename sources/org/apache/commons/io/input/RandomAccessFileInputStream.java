package org.apache.commons.io.input;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/RandomAccessFileInputStream.class */
public class RandomAccessFileInputStream extends AbstractInputStream {
    private final boolean propagateClose;
    private final RandomAccessFile randomAccessFile;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/RandomAccessFileInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<RandomAccessFileInputStream, Builder> {
        private boolean propagateClose;

        @Override // org.apache.commons.io.function.IOSupplier
        public RandomAccessFileInputStream get() throws IOException {
            return new RandomAccessFileInputStream(getRandomAccessFile(), this.propagateClose);
        }

        public Builder setCloseOnClose(boolean propagateClose) {
            this.propagateClose = propagateClose;
            return this;
        }

        @Override // org.apache.commons.io.build.AbstractOriginSupplier
        public Builder setRandomAccessFile(RandomAccessFile randomAccessFile) {
            return (Builder) super.setRandomAccessFile(randomAccessFile);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public RandomAccessFileInputStream(RandomAccessFile file) {
        this(file, false);
    }

    @Deprecated
    public RandomAccessFileInputStream(RandomAccessFile file, boolean propagateClose) {
        this.randomAccessFile = (RandomAccessFile) Objects.requireNonNull(file, "file");
        this.propagateClose = propagateClose;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        long avail = availableLong();
        if (avail > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) avail;
    }

    public long availableLong() throws IOException {
        if (isClosed()) {
            return 0L;
        }
        return this.randomAccessFile.length() - this.randomAccessFile.getFilePointer();
    }

    @Override // org.apache.commons.io.input.AbstractInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (this.propagateClose) {
            this.randomAccessFile.close();
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return this.randomAccessFile;
    }

    public boolean isCloseOnClose() {
        return this.propagateClose;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.randomAccessFile.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes) throws IOException {
        return this.randomAccessFile.read(bytes);
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return this.randomAccessFile.read(bytes, offset, length);
    }

    @Override // java.io.InputStream
    public long skip(long skipCount) throws IOException {
        if (skipCount <= 0) {
            return 0L;
        }
        long filePointer = this.randomAccessFile.getFilePointer();
        long fileLength = this.randomAccessFile.length();
        if (filePointer >= fileLength) {
            return 0L;
        }
        long targetPos = filePointer + skipCount;
        long newPos = targetPos > fileLength ? fileLength - 1 : targetPos;
        if (newPos > 0) {
            this.randomAccessFile.seek(newPos);
        }
        return this.randomAccessFile.getFilePointer() - filePointer;
    }
}

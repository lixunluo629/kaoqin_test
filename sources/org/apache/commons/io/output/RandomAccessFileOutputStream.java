package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/RandomAccessFileOutputStream.class */
public final class RandomAccessFileOutputStream extends OutputStream {
    private final RandomAccessFile randomAccessFile;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/RandomAccessFileOutputStream$Builder.class */
    public static final class Builder extends AbstractStreamBuilder<RandomAccessFileOutputStream, Builder> {
        private Builder() {
            setOpenOptions(StandardOpenOption.WRITE);
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public RandomAccessFileOutputStream get() throws IOException {
            return new RandomAccessFileOutputStream(getRandomAccessFile());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private RandomAccessFileOutputStream(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = (RandomAccessFile) Objects.requireNonNull(randomAccessFile);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.randomAccessFile.close();
        super.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.randomAccessFile.getChannel().force(true);
        super.flush();
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.randomAccessFile.write(b);
    }
}

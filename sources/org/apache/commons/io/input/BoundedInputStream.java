package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOBiConsumer;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.input.ProxyInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/BoundedInputStream.class */
public class BoundedInputStream extends ProxyInputStream {
    private long count;
    private long mark;
    private final long maxCount;
    private final IOBiConsumer<Long, Long> onMaxCount;
    private boolean propagateClose;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/BoundedInputStream$AbstractBuilder.class */
    static abstract class AbstractBuilder<T extends AbstractBuilder<T>> extends ProxyInputStream.AbstractBuilder<BoundedInputStream, T> {
        private long count;
        private long maxCount = -1;
        private IOBiConsumer<Long, Long> onMaxCount = IOBiConsumer.noop();
        private boolean propagateClose = true;

        AbstractBuilder() {
        }

        long getCount() {
            return this.count;
        }

        long getMaxCount() {
            return this.maxCount;
        }

        IOBiConsumer<Long, Long> getOnMaxCount() {
            return this.onMaxCount;
        }

        boolean isPropagateClose() {
            return this.propagateClose;
        }

        public T setCount(long count) {
            this.count = Math.max(0L, count);
            return (T) asThis();
        }

        public T setMaxCount(long maxCount) {
            this.maxCount = Math.max(-1L, maxCount);
            return (T) asThis();
        }

        public T setOnMaxCount(IOBiConsumer<Long, Long> onMaxCount) {
            this.onMaxCount = onMaxCount != null ? onMaxCount : IOBiConsumer.noop();
            return (T) asThis();
        }

        public T setPropagateClose(boolean propagateClose) {
            this.propagateClose = propagateClose;
            return (T) asThis();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/BoundedInputStream$Builder.class */
    public static class Builder extends AbstractBuilder<Builder> {
        @Override // org.apache.commons.io.input.BoundedInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractBuilder setPropagateClose(boolean z) {
            return super.setPropagateClose(z);
        }

        @Override // org.apache.commons.io.input.BoundedInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractBuilder setOnMaxCount(IOBiConsumer iOBiConsumer) {
            return super.setOnMaxCount(iOBiConsumer);
        }

        @Override // org.apache.commons.io.input.BoundedInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractBuilder setMaxCount(long j) {
            return super.setMaxCount(j);
        }

        @Override // org.apache.commons.io.input.BoundedInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractBuilder setCount(long j) {
            return super.setCount(j);
        }

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractStreamBuilder setAfterRead(IOIntConsumer iOIntConsumer) {
            return super.setAfterRead(iOIntConsumer);
        }

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ IOIntConsumer getAfterRead() {
            return super.getAfterRead();
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public BoundedInputStream get() throws IOException {
            return new BoundedInputStream(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    BoundedInputStream(Builder builder) throws IOException {
        super(builder);
        this.propagateClose = true;
        this.count = builder.getCount();
        this.maxCount = builder.getMaxCount();
        this.propagateClose = builder.isPropagateClose();
        this.onMaxCount = builder.getOnMaxCount();
    }

    @Deprecated
    public BoundedInputStream(InputStream in) {
        this(in, -1L);
    }

    BoundedInputStream(InputStream inputStream, Builder builder) {
        super(inputStream, builder);
        this.propagateClose = true;
        this.count = builder.getCount();
        this.maxCount = builder.getMaxCount();
        this.propagateClose = builder.isPropagateClose();
        this.onMaxCount = builder.getOnMaxCount();
    }

    @Deprecated
    public BoundedInputStream(InputStream inputStream, long maxCount) {
        this(inputStream, (Builder) builder().setMaxCount(maxCount));
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected synchronized void afterRead(int n) throws IOException {
        if (n != -1) {
            this.count += n;
        }
        super.afterRead(n);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (isMaxCount()) {
            onMaxLength(this.maxCount, getCount());
            return 0;
        }
        return this.in.available();
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.propagateClose) {
            super.close();
        }
    }

    public synchronized long getCount() {
        return this.count;
    }

    public long getMaxCount() {
        return this.maxCount;
    }

    @Deprecated
    public long getMaxLength() {
        return this.maxCount;
    }

    public long getRemaining() {
        return Math.max(0L, getMaxCount() - getCount());
    }

    private boolean isMaxCount() {
        return this.maxCount >= 0 && getCount() >= this.maxCount;
    }

    public boolean isPropagateClose() {
        return this.propagateClose;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int readLimit) {
        this.in.mark(readLimit);
        this.mark = this.count;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void onMaxLength(long max, long count) throws IOException {
        this.onMaxCount.accept(Long.valueOf(max), Long.valueOf(count));
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (isMaxCount()) {
            onMaxLength(this.maxCount, getCount());
            return -1;
        }
        return super.read();
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (isMaxCount()) {
            onMaxLength(this.maxCount, getCount());
            return -1;
        }
        return super.read(b, off, (int) toReadLen(len));
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.count = this.mark;
    }

    @Deprecated
    public void setPropagateClose(boolean propagateClose) {
        this.propagateClose = propagateClose;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized long skip(long n) throws IOException {
        long skip = super.skip(toReadLen(n));
        this.count += skip;
        return skip;
    }

    private long toReadLen(long len) {
        return this.maxCount >= 0 ? Math.min(len, this.maxCount - getCount()) : len;
    }

    public String toString() {
        return this.in.toString();
    }
}

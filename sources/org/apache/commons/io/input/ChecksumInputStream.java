package org.apache.commons.io.input;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.input.ProxyInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ChecksumInputStream.class */
public final class ChecksumInputStream extends CountingInputStream {
    private final long expectedChecksumValue;
    private final long countThreshold;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ChecksumInputStream$Builder.class */
    public static class Builder extends ProxyInputStream.AbstractBuilder<ChecksumInputStream, Builder> {
        private Checksum checksum;
        private long countThreshold = -1;
        private long expectedChecksumValue;

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractStreamBuilder setAfterRead(IOIntConsumer iOIntConsumer) {
            return super.setAfterRead(iOIntConsumer);
        }

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ IOIntConsumer getAfterRead() {
            return super.getAfterRead();
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public ChecksumInputStream get() throws IOException {
            return new ChecksumInputStream(this);
        }

        public Builder setChecksum(Checksum checksum) {
            this.checksum = checksum;
            return this;
        }

        public Builder setCountThreshold(long countThreshold) {
            this.countThreshold = countThreshold;
            return this;
        }

        public Builder setExpectedChecksumValue(long expectedChecksumValue) {
            this.expectedChecksumValue = expectedChecksumValue;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private ChecksumInputStream(Builder builder) throws IOException {
        super(new CheckedInputStream(builder.getInputStream(), (Checksum) Objects.requireNonNull(builder.checksum, "builder.checksum")), builder);
        this.countThreshold = builder.countThreshold;
        this.expectedChecksumValue = builder.expectedChecksumValue;
    }

    @Override // org.apache.commons.io.input.CountingInputStream, org.apache.commons.io.input.ProxyInputStream
    protected synchronized void afterRead(int n) throws IOException {
        super.afterRead(n);
        if (((this.countThreshold > 0 && getByteCount() >= this.countThreshold) || n == -1) && this.expectedChecksumValue != getChecksum().getValue()) {
            throw new IOException("Checksum verification failed.");
        }
    }

    private Checksum getChecksum() {
        return ((CheckedInputStream) this.in).getChecksum();
    }

    public long getRemaining() {
        return this.countThreshold - getByteCount();
    }
}

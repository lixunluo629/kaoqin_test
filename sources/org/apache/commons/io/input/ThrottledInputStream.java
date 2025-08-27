package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.input.ProxyInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ThrottledInputStream.class */
public final class ThrottledInputStream extends CountingInputStream {
    private final long maxBytesPerSecond;
    private final long startTime;
    private Duration totalSleepDuration;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ThrottledInputStream$Builder.class */
    public static class Builder extends ProxyInputStream.AbstractBuilder<ThrottledInputStream, Builder> {
        private long maxBytesPerSecond = Long.MAX_VALUE;

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractStreamBuilder setAfterRead(IOIntConsumer iOIntConsumer) {
            return super.setAfterRead(iOIntConsumer);
        }

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ IOIntConsumer getAfterRead() {
            return super.getAfterRead();
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public ThrottledInputStream get() throws IOException {
            return new ThrottledInputStream(this);
        }

        public void setMaxBytesPerSecond(long maxBytesPerSecond) {
            this.maxBytesPerSecond = maxBytesPerSecond;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static long toSleepMillis(long bytesRead, long maxBytesPerSec, long elapsedMillis) {
        if (elapsedMillis < 0) {
            throw new IllegalArgumentException("The elapsed time should be greater or equal to zero");
        }
        if (bytesRead <= 0 || maxBytesPerSec <= 0 || elapsedMillis == 0) {
            return 0L;
        }
        long millis = (long) (((bytesRead / maxBytesPerSec) * 1000.0d) - elapsedMillis);
        if (millis <= 0) {
            return 0L;
        }
        return millis;
    }

    private ThrottledInputStream(Builder builder) throws IOException {
        super(builder);
        this.startTime = System.currentTimeMillis();
        this.totalSleepDuration = Duration.ZERO;
        if (builder.maxBytesPerSecond <= 0) {
            throw new IllegalArgumentException("Bandwidth " + builder.maxBytesPerSecond + " is invalid.");
        }
        this.maxBytesPerSecond = builder.maxBytesPerSecond;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected void beforeRead(int n) throws InterruptedException, IOException {
        throttle();
    }

    private long getBytesPerSecond() {
        long elapsedSeconds = (System.currentTimeMillis() - this.startTime) / 1000;
        if (elapsedSeconds == 0) {
            return getByteCount();
        }
        return getByteCount() / elapsedSeconds;
    }

    private long getSleepMillis() {
        return toSleepMillis(getByteCount(), this.maxBytesPerSecond, System.currentTimeMillis() - this.startTime);
    }

    Duration getTotalSleepDuration() {
        return this.totalSleepDuration;
    }

    private void throttle() throws InterruptedException, InterruptedIOException {
        long sleepMillis = getSleepMillis();
        if (sleepMillis > 0) {
            this.totalSleepDuration = this.totalSleepDuration.plus(sleepMillis, ChronoUnit.MILLIS);
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (InterruptedException e) {
                throw new InterruptedIOException("Thread aborted");
            }
        }
    }

    public String toString() {
        return "ThrottledInputStream[bytesRead=" + getByteCount() + ", maxBytesPerSec=" + this.maxBytesPerSecond + ", bytesPerSec=" + getBytesPerSecond() + ", totalSleepDuration=" + this.totalSleepDuration + ']';
    }
}

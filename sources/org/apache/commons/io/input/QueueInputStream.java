package org.apache.commons.io.input;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.output.QueueOutputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/QueueInputStream.class */
public class QueueInputStream extends InputStream {
    private final BlockingQueue<Integer> blockingQueue;
    private final long timeoutNanos;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/QueueInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<QueueInputStream, Builder> {
        private BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue();
        private Duration timeout = Duration.ZERO;

        @Override // org.apache.commons.io.function.IOSupplier
        public QueueInputStream get() {
            return new QueueInputStream(this.blockingQueue, this.timeout);
        }

        public Builder setBlockingQueue(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue != null ? blockingQueue : new LinkedBlockingQueue<>();
            return this;
        }

        public Builder setTimeout(Duration timeout) {
            if (timeout != null && timeout.toNanos() < 0) {
                throw new IllegalArgumentException("timeout must not be negative");
            }
            this.timeout = timeout != null ? timeout : Duration.ZERO;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public QueueInputStream() {
        this(new LinkedBlockingQueue());
    }

    @Deprecated
    public QueueInputStream(BlockingQueue<Integer> blockingQueue) {
        this(blockingQueue, Duration.ZERO);
    }

    private QueueInputStream(BlockingQueue<Integer> blockingQueue, Duration timeout) {
        this.blockingQueue = (BlockingQueue) Objects.requireNonNull(blockingQueue, "blockingQueue");
        this.timeoutNanos = ((Duration) Objects.requireNonNull(timeout, RtspHeaders.Values.TIMEOUT)).toNanos();
    }

    BlockingQueue<Integer> getBlockingQueue() {
        return this.blockingQueue;
    }

    Duration getTimeout() {
        return Duration.ofNanos(this.timeoutNanos);
    }

    public QueueOutputStream newQueueOutputStream() {
        return new QueueOutputStream(this.blockingQueue);
    }

    @Override // java.io.InputStream
    public int read() throws InterruptedException {
        try {
            Integer value = this.blockingQueue.poll(this.timeoutNanos, TimeUnit.NANOSECONDS);
            if (value == null) {
                return -1;
            }
            return 255 & value.intValue();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}

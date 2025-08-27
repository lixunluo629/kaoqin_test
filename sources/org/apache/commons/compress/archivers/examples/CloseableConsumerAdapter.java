package org.apache.commons.compress.archivers.examples;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/CloseableConsumerAdapter.class */
final class CloseableConsumerAdapter implements Closeable {
    private final CloseableConsumer consumer;
    private Closeable closeable;

    CloseableConsumerAdapter(CloseableConsumer consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer must not be null");
        }
        this.consumer = consumer;
    }

    <C extends Closeable> C track(C closeable) {
        this.closeable = closeable;
        return closeable;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeable != null) {
            this.consumer.accept(this.closeable);
        }
    }
}

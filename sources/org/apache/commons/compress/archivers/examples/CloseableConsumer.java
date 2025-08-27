package org.apache.commons.compress.archivers.examples;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/examples/CloseableConsumer.class */
public interface CloseableConsumer {
    public static final CloseableConsumer CLOSING_CONSUMER = new CloseableConsumer() { // from class: org.apache.commons.compress.archivers.examples.CloseableConsumer.1
        @Override // org.apache.commons.compress.archivers.examples.CloseableConsumer
        public void accept(Closeable c) throws IOException {
            c.close();
        }
    };
    public static final CloseableConsumer NULL_CONSUMER = new CloseableConsumer() { // from class: org.apache.commons.compress.archivers.examples.CloseableConsumer.2
        @Override // org.apache.commons.compress.archivers.examples.CloseableConsumer
        public void accept(Closeable c) {
        }
    };

    void accept(Closeable closeable) throws IOException;
}

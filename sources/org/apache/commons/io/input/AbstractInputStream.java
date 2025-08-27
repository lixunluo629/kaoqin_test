package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/AbstractInputStream.class */
public abstract class AbstractInputStream extends InputStream {
    private boolean closed;

    void checkOpen() throws IOException {
        Input.checkOpen(!isClosed());
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}

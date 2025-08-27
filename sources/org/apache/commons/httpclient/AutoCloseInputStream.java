package org.apache.commons.httpclient;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/AutoCloseInputStream.class */
class AutoCloseInputStream extends FilterInputStream {
    private boolean streamOpen;
    private boolean selfClosed;
    private ResponseConsumedWatcher watcher;

    public AutoCloseInputStream(InputStream in, ResponseConsumedWatcher watcher) {
        super(in);
        this.streamOpen = true;
        this.selfClosed = false;
        this.watcher = null;
        this.watcher = watcher;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int l = -1;
        if (isReadAllowed()) {
            l = super.read();
            checkClose(l);
        }
        return l;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int l = -1;
        if (isReadAllowed()) {
            l = super.read(b, off, len);
            checkClose(l);
        }
        return l;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws IOException {
        int l = -1;
        if (isReadAllowed()) {
            l = super.read(b);
            checkClose(l);
        }
        return l;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        int a = 0;
        if (isReadAllowed()) {
            a = super.available();
        }
        return a;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.selfClosed) {
            this.selfClosed = true;
            notifyWatcher();
        }
    }

    private void checkClose(int readResult) throws IOException {
        if (readResult == -1) {
            notifyWatcher();
        }
    }

    private boolean isReadAllowed() throws IOException {
        if (!this.streamOpen && this.selfClosed) {
            throw new IOException("Attempted read on closed stream.");
        }
        return this.streamOpen;
    }

    private void notifyWatcher() throws IOException {
        if (this.streamOpen) {
            super.close();
            this.streamOpen = false;
            if (this.watcher != null) {
                this.watcher.responseConsumed();
            }
        }
    }
}

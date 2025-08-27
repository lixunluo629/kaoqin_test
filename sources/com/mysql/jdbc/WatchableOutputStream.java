package com.mysql.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/WatchableOutputStream.class */
class WatchableOutputStream extends ByteArrayOutputStream {
    private OutputStreamWatcher watcher;

    WatchableOutputStream() {
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (this.watcher != null) {
            this.watcher.streamClosed(this);
        }
    }

    public void setWatcher(OutputStreamWatcher watcher) {
        this.watcher = watcher;
    }
}

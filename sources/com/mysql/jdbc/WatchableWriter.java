package com.mysql.jdbc;

import java.io.CharArrayWriter;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/WatchableWriter.class */
class WatchableWriter extends CharArrayWriter {
    private WriterWatcher watcher;

    WatchableWriter() {
    }

    @Override // java.io.CharArrayWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        super.close();
        if (this.watcher != null) {
            this.watcher.writerClosed(this);
        }
    }

    public void setWatcher(WriterWatcher watcher) {
        this.watcher = watcher;
    }
}

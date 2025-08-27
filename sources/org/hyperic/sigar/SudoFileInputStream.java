package org.hyperic.sigar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SudoFileInputStream.class */
public class SudoFileInputStream extends InputStream {
    private Process process;
    private InputStream is;

    public SudoFileInputStream(String file) throws IOException {
        this(new File(file));
    }

    public SudoFileInputStream(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        String[] args = {"sudo", "cat", file.toString()};
        this.process = Runtime.getRuntime().exec(args);
        this.is = this.process.getInputStream();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.process.destroy();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.is.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return this.is.read(b);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        return this.is.read(b, off, len);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        return this.is.skip(n);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.is.available();
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.is.mark(readlimit);
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.is.reset();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.is.markSupported();
    }
}

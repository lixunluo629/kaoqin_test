package org.apache.commons.compress.parallel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/parallel/FileBasedScatterGatherBackingStore.class */
public class FileBasedScatterGatherBackingStore implements ScatterGatherBackingStore {
    private final File target;
    private final OutputStream os;
    private boolean closed;

    public FileBasedScatterGatherBackingStore(File target) throws FileNotFoundException {
        this.target = target;
        try {
            this.os = Files.newOutputStream(target.toPath(), new OpenOption[0]);
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    @Override // org.apache.commons.compress.parallel.ScatterGatherBackingStore
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.target.toPath(), new OpenOption[0]);
    }

    @Override // org.apache.commons.compress.parallel.ScatterGatherBackingStore
    public void closeForWriting() throws IOException {
        if (!this.closed) {
            this.os.close();
            this.closed = true;
        }
    }

    @Override // org.apache.commons.compress.parallel.ScatterGatherBackingStore
    public void writeOut(byte[] data, int offset, int length) throws IOException {
        this.os.write(data, offset, length);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            closeForWriting();
        } finally {
            if (this.target.exists() && !this.target.delete()) {
                this.target.deleteOnExit();
            }
        }
    }
}

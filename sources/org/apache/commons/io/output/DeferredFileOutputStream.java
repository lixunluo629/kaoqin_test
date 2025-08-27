package org.apache.commons.io.output;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.file.PathUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/DeferredFileOutputStream.class */
public class DeferredFileOutputStream extends ThresholdingOutputStream {
    private ByteArrayOutputStream memoryOutputStream;
    private OutputStream currentOutputStream;
    private Path outputPath;
    private final String prefix;
    private final String suffix;
    private final Path directory;
    private boolean closed;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/DeferredFileOutputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<DeferredFileOutputStream, Builder> {
        private int threshold;
        private Path outputFile;
        private String prefix;
        private String suffix;
        private Path directory;

        public Builder() {
            setBufferSizeDefault(1024);
            setBufferSize(1024);
        }

        @Override // org.apache.commons.io.function.IOSupplier
        public DeferredFileOutputStream get() {
            return new DeferredFileOutputStream(this.threshold, this.outputFile, this.prefix, this.suffix, this.directory, getBufferSize());
        }

        public Builder setDirectory(File directory) {
            this.directory = DeferredFileOutputStream.toPath(directory, (Supplier<Path>) null);
            return this;
        }

        public Builder setDirectory(Path directory) {
            this.directory = DeferredFileOutputStream.toPath(directory, (Supplier<Path>) null);
            return this;
        }

        public Builder setOutputFile(File outputFile) {
            this.outputFile = DeferredFileOutputStream.toPath(outputFile, (Supplier<Path>) null);
            return this;
        }

        public Builder setOutputFile(Path outputFile) {
            this.outputFile = DeferredFileOutputStream.toPath(outputFile, (Supplier<Path>) null);
            return this;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder setThreshold(int threshold) {
            this.threshold = threshold;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static int checkBufferSize(int initialBufferSize) {
        if (initialBufferSize < 0) {
            throw new IllegalArgumentException("Initial buffer size must be at least 0.");
        }
        return initialBufferSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Path toPath(File file, Supplier<Path> defaultPathSupplier) {
        if (file != null) {
            return file.toPath();
        }
        if (defaultPathSupplier == null) {
            return null;
        }
        return defaultPathSupplier.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Path toPath(Path file, Supplier<Path> defaultPathSupplier) {
        if (file != null) {
            return file;
        }
        if (defaultPathSupplier == null) {
            return null;
        }
        return defaultPathSupplier.get();
    }

    @Deprecated
    public DeferredFileOutputStream(int threshold, File outputFile) {
        this(threshold, outputFile, (String) null, (String) null, (File) null, 1024);
    }

    private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory, int initialBufferSize) {
        super(threshold);
        this.outputPath = toPath(outputFile, (Supplier<Path>) null);
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = toPath(directory, (Supplier<Path>) PathUtils::getTempDirectory);
        this.memoryOutputStream = new ByteArrayOutputStream(checkBufferSize(initialBufferSize));
        this.currentOutputStream = this.memoryOutputStream;
    }

    @Deprecated
    public DeferredFileOutputStream(int threshold, int initialBufferSize, File outputFile) {
        this(threshold, outputFile, (String) null, (String) null, (File) null, initialBufferSize);
    }

    @Deprecated
    public DeferredFileOutputStream(int threshold, int initialBufferSize, String prefix, String suffix, File directory) {
        this(threshold, (File) null, (String) Objects.requireNonNull(prefix, "prefix"), suffix, directory, initialBufferSize);
    }

    private DeferredFileOutputStream(int threshold, Path outputFile, String prefix, String suffix, Path directory, int initialBufferSize) {
        super(threshold);
        this.outputPath = toPath(outputFile, (Supplier<Path>) null);
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = toPath(directory, (Supplier<Path>) PathUtils::getTempDirectory);
        this.memoryOutputStream = new ByteArrayOutputStream(checkBufferSize(initialBufferSize));
        this.currentOutputStream = this.memoryOutputStream;
    }

    @Deprecated
    public DeferredFileOutputStream(int threshold, String prefix, String suffix, File directory) {
        this(threshold, (File) null, (String) Objects.requireNonNull(prefix, "prefix"), suffix, directory, 1024);
    }

    @Override // org.apache.commons.io.output.ThresholdingOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.closed = true;
    }

    public byte[] getData() {
        if (this.memoryOutputStream != null) {
            return this.memoryOutputStream.toByteArray();
        }
        return null;
    }

    public File getFile() {
        if (this.outputPath != null) {
            return this.outputPath.toFile();
        }
        return null;
    }

    public Path getPath() {
        return this.outputPath;
    }

    @Override // org.apache.commons.io.output.ThresholdingOutputStream
    protected OutputStream getStream() throws IOException {
        return this.currentOutputStream;
    }

    public boolean isInMemory() {
        return !isThresholdExceeded();
    }

    @Override // org.apache.commons.io.output.ThresholdingOutputStream
    protected void thresholdReached() throws IOException {
        if (this.prefix != null) {
            this.outputPath = Files.createTempFile(this.directory, this.prefix, this.suffix, new FileAttribute[0]);
        }
        PathUtils.createParentDirectories(this.outputPath, null, PathUtils.EMPTY_FILE_ATTRIBUTE_ARRAY);
        OutputStream fos = Files.newOutputStream(this.outputPath, new OpenOption[0]);
        try {
            this.memoryOutputStream.writeTo(fos);
            this.currentOutputStream = fos;
            this.memoryOutputStream = null;
        } catch (IOException e) {
            fos.close();
            throw e;
        }
    }

    public InputStream toInputStream() throws IOException {
        if (!this.closed) {
            throw new IOException("Stream not closed");
        }
        if (isInMemory()) {
            return this.memoryOutputStream.toInputStream();
        }
        return Files.newInputStream(this.outputPath, new OpenOption[0]);
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (!this.closed) {
            throw new IOException("Stream not closed");
        }
        if (isInMemory()) {
            this.memoryOutputStream.writeTo(outputStream);
        } else {
            Files.copy(this.outputPath, outputStream);
        }
    }
}

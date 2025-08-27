package org.apache.commons.io.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.file.spi.FileSystemProviders;
import org.apache.commons.io.input.BufferedFileChannelInputStream;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.RandomAccessFileOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.springframework.beans.PropertyAccessor;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin.class */
public abstract class AbstractOrigin<T, B extends AbstractOrigin<T, B>> extends AbstractSupplier<T, B> {
    final T origin;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$AbstractRandomAccessFileOrigin.class */
    public static abstract class AbstractRandomAccessFileOrigin<T extends RandomAccessFile, B extends AbstractRandomAccessFileOrigin<T, B>> extends AbstractOrigin<T, B> {
        public AbstractRandomAccessFileOrigin(T origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray() throws IOException {
            long longLen = this.origin.length();
            if (longLen > 2147483647L) {
                throw new IllegalStateException("Origin too large.");
            }
            return RandomAccessFiles.read(this.origin, 0L, (int) longLen);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray(long position, int length) throws IOException {
            return RandomAccessFiles.read(this.origin, position, length);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public CharSequence getCharSequence(Charset charset) throws IOException {
            return new String(getByteArray(), charset);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... options) throws IOException {
            return BufferedFileChannelInputStream.builder().setFileChannel(this.origin.getChannel()).get();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.io.build.AbstractOrigin
        public OutputStream getOutputStream(OpenOption... options) throws IOException {
            return ((RandomAccessFileOutputStream.Builder) RandomAccessFileOutputStream.builder().setRandomAccessFile(this.origin)).get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public T getRandomAccessFile(OpenOption... openOption) {
            return get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(new OpenOption[0]), charset);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Writer getWriter(Charset charset, OpenOption... options) throws IOException {
            return new OutputStreamWriter(getOutputStream(options), charset);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public long size() throws IOException {
            return this.origin.length();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$ByteArrayOrigin.class */
    public static class ByteArrayOrigin extends AbstractOrigin<byte[], ByteArrayOrigin> {
        public ByteArrayOrigin(byte[] origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray() {
            return get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... openOptionArr) throws IOException {
            return new ByteArrayInputStream((byte[]) this.origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(new OpenOption[0]), charset);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public long size() throws IOException {
            return ((byte[]) this.origin).length;
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$CharSequenceOrigin.class */
    public static class CharSequenceOrigin extends AbstractOrigin<CharSequence, CharSequenceOrigin> {
        public CharSequenceOrigin(CharSequence origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray() {
            return ((CharSequence) this.origin).toString().getBytes(Charset.defaultCharset());
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public CharSequence getCharSequence(Charset charset) {
            return get();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... options) throws IOException {
            return ((CharSequenceInputStream.Builder) CharSequenceInputStream.builder().setCharSequence(getCharSequence(Charset.defaultCharset()))).get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Reader getReader(Charset charset) throws IOException {
            return new CharSequenceReader(get());
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public long size() throws IOException {
            return ((CharSequence) this.origin).length();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$FileOrigin.class */
    public static class FileOrigin extends AbstractOrigin<File, FileOrigin> {
        public FileOrigin(File origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray(long j, int i) throws IOException {
            RandomAccessFile randomAccessFileCreate = RandomAccessFileMode.READ_ONLY.create((File) this.origin);
            try {
                byte[] bArr = RandomAccessFiles.read(randomAccessFileCreate, j, i);
                if (randomAccessFileCreate != null) {
                    randomAccessFileCreate.close();
                }
                return bArr;
            } catch (Throwable th) {
                if (randomAccessFileCreate != null) {
                    try {
                        randomAccessFileCreate.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public File getFile() {
            return get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Path getPath() {
            return get().toPath();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$InputStreamOrigin.class */
    public static class InputStreamOrigin extends AbstractOrigin<InputStream, InputStreamOrigin> {
        public InputStreamOrigin(InputStream origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray((InputStream) this.origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... options) {
            return get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Reader getReader(Charset charset) throws IOException {
            return new InputStreamReader(getInputStream(new OpenOption[0]), charset);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$IORandomAccessFileOrigin.class */
    public static class IORandomAccessFileOrigin extends AbstractRandomAccessFileOrigin<IORandomAccessFile, IORandomAccessFileOrigin> {
        public IORandomAccessFileOrigin(IORandomAccessFile origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public File getFile() {
            return ((IORandomAccessFile) get()).getFile();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Path getPath() {
            return getFile().toPath();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$OutputStreamOrigin.class */
    public static class OutputStreamOrigin extends AbstractOrigin<OutputStream, OutputStreamOrigin> {
        public OutputStreamOrigin(OutputStream origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public OutputStream getOutputStream(OpenOption... options) {
            return get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Writer getWriter(Charset charset, OpenOption... openOptionArr) throws IOException {
            return new OutputStreamWriter((OutputStream) this.origin, charset);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$PathOrigin.class */
    public static class PathOrigin extends AbstractOrigin<Path, PathOrigin> {
        public PathOrigin(Path origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray(long position, int length) throws IOException {
            return (byte[]) RandomAccessFileMode.READ_ONLY.apply((Path) this.origin, raf -> {
                return RandomAccessFiles.read(raf, position, length);
            });
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public File getFile() {
            return get().toFile();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Path getPath() {
            return get();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$RandomAccessFileOrigin.class */
    public static class RandomAccessFileOrigin extends AbstractRandomAccessFileOrigin<RandomAccessFile, RandomAccessFileOrigin> {
        public RandomAccessFileOrigin(RandomAccessFile origin) {
            super(origin);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$ReaderOrigin.class */
    public static class ReaderOrigin extends AbstractOrigin<Reader, ReaderOrigin> {
        public ReaderOrigin(Reader origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public byte[] getByteArray() throws IOException {
            return IOUtils.toByteArray((Reader) this.origin, Charset.defaultCharset());
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public CharSequence getCharSequence(Charset charset) throws IOException {
            return IOUtils.toString((Reader) this.origin);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... openOptionArr) throws IOException {
            return ((ReaderInputStream.Builder) ReaderInputStream.builder().setReader((Reader) this.origin)).setCharset(Charset.defaultCharset()).get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Reader getReader(Charset charset) throws IOException {
            return get();
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$URIOrigin.class */
    public static class URIOrigin extends AbstractOrigin<URI, URIOrigin> {
        private static final String SCHEME_HTTPS = "https";
        private static final String SCHEME_HTTP = "http";

        public URIOrigin(URI origin) {
            super(origin);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public File getFile() {
            return getPath().toFile();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public InputStream getInputStream(OpenOption... options) throws IOException {
            URI uri = get();
            String scheme = uri.getScheme();
            FileSystemProvider fileSystemProvider = FileSystemProviders.installed().getFileSystemProvider(scheme);
            if (fileSystemProvider != null) {
                return Files.newInputStream(fileSystemProvider.getPath(uri), options);
            }
            if (SCHEME_HTTP.equalsIgnoreCase(scheme) || SCHEME_HTTPS.equalsIgnoreCase(scheme)) {
                return uri.toURL().openStream();
            }
            return Files.newInputStream(getPath(), options);
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Path getPath() {
            return Paths.get(get());
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOrigin$WriterOrigin.class */
    public static class WriterOrigin extends AbstractOrigin<Writer, WriterOrigin> {
        public WriterOrigin(Writer origin) {
            super(origin);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.io.build.AbstractOrigin
        public OutputStream getOutputStream(OpenOption... openOptionArr) throws IOException {
            return ((WriterOutputStream.Builder) WriterOutputStream.builder().setWriter((Writer) this.origin)).setCharset(Charset.defaultCharset()).get();
        }

        @Override // org.apache.commons.io.build.AbstractOrigin
        public Writer getWriter(Charset charset, OpenOption... options) throws IOException {
            return get();
        }
    }

    protected AbstractOrigin(T t) {
        this.origin = (T) Objects.requireNonNull(t, "origin");
    }

    @Override // org.apache.commons.io.function.IOSupplier
    public T get() {
        return this.origin;
    }

    public byte[] getByteArray() throws IOException {
        return Files.readAllBytes(getPath());
    }

    public byte[] getByteArray(long position, int length) throws IOException {
        byte[] bytes = getByteArray();
        int start = Math.toIntExact(position);
        if (start < 0 || length < 0 || start + length < 0 || start + length > bytes.length) {
            throw new IllegalArgumentException("Couldn't read array (start: " + start + ", length: " + length + ", data length: " + bytes.length + ").");
        }
        return Arrays.copyOfRange(bytes, start, start + length);
    }

    public CharSequence getCharSequence(Charset charset) throws IOException {
        return new String(getByteArray(), charset);
    }

    public File getFile() {
        throw new UnsupportedOperationException(String.format("%s#getFile() for %s origin %s", getSimpleClassName(), this.origin.getClass().getSimpleName(), this.origin));
    }

    public InputStream getInputStream(OpenOption... options) throws IOException {
        return Files.newInputStream(getPath(), options);
    }

    public OutputStream getOutputStream(OpenOption... options) throws IOException {
        return Files.newOutputStream(getPath(), options);
    }

    public Path getPath() {
        throw new UnsupportedOperationException(String.format("%s#getPath() for %s origin %s", getSimpleClassName(), this.origin.getClass().getSimpleName(), this.origin));
    }

    public RandomAccessFile getRandomAccessFile(OpenOption... openOption) throws FileNotFoundException {
        return RandomAccessFileMode.valueOf(openOption).create(getFile());
    }

    public Reader getReader(Charset charset) throws IOException {
        return Files.newBufferedReader(getPath(), charset);
    }

    private String getSimpleClassName() {
        return getClass().getSimpleName();
    }

    public Writer getWriter(Charset charset, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(getPath(), charset, options);
    }

    public long size() throws IOException {
        return Files.size(getPath());
    }

    public String toString() {
        return getSimpleClassName() + PropertyAccessor.PROPERTY_KEY_PREFIX + this.origin.toString() + "]";
    }
}

package org.apache.commons.io.build;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IORandomAccessFile;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractOriginSupplier;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractOriginSupplier.class */
public abstract class AbstractOriginSupplier<T, B extends AbstractOriginSupplier<T, B>> extends AbstractSupplier<T, B> {
    private AbstractOrigin<?, ?> origin;

    protected static AbstractOrigin.ByteArrayOrigin newByteArrayOrigin(byte[] origin) {
        return new AbstractOrigin.ByteArrayOrigin(origin);
    }

    protected static AbstractOrigin.CharSequenceOrigin newCharSequenceOrigin(CharSequence origin) {
        return new AbstractOrigin.CharSequenceOrigin(origin);
    }

    protected static AbstractOrigin.FileOrigin newFileOrigin(File origin) {
        return new AbstractOrigin.FileOrigin(origin);
    }

    protected static AbstractOrigin.FileOrigin newFileOrigin(String origin) {
        return new AbstractOrigin.FileOrigin(new File(origin));
    }

    protected static AbstractOrigin.InputStreamOrigin newInputStreamOrigin(InputStream origin) {
        return new AbstractOrigin.InputStreamOrigin(origin);
    }

    protected static AbstractOrigin.OutputStreamOrigin newOutputStreamOrigin(OutputStream origin) {
        return new AbstractOrigin.OutputStreamOrigin(origin);
    }

    protected static AbstractOrigin.PathOrigin newPathOrigin(Path origin) {
        return new AbstractOrigin.PathOrigin(origin);
    }

    protected static AbstractOrigin.PathOrigin newPathOrigin(String origin) {
        return new AbstractOrigin.PathOrigin(Paths.get(origin, new String[0]));
    }

    protected static AbstractOrigin.IORandomAccessFileOrigin newRandomAccessFileOrigin(IORandomAccessFile origin) {
        return new AbstractOrigin.IORandomAccessFileOrigin(origin);
    }

    protected static AbstractOrigin.RandomAccessFileOrigin newRandomAccessFileOrigin(RandomAccessFile origin) {
        return new AbstractOrigin.RandomAccessFileOrigin(origin);
    }

    protected static AbstractOrigin.ReaderOrigin newReaderOrigin(Reader origin) {
        return new AbstractOrigin.ReaderOrigin(origin);
    }

    protected static AbstractOrigin.URIOrigin newURIOrigin(URI origin) {
        return new AbstractOrigin.URIOrigin(origin);
    }

    protected static AbstractOrigin.WriterOrigin newWriterOrigin(Writer origin) {
        return new AbstractOrigin.WriterOrigin(origin);
    }

    protected AbstractOrigin<?, ?> checkOrigin() {
        if (this.origin == null) {
            throw new IllegalStateException("origin == null");
        }
        return this.origin;
    }

    protected AbstractOrigin<?, ?> getOrigin() {
        return this.origin;
    }

    protected boolean hasOrigin() {
        return this.origin != null;
    }

    public B setByteArray(byte[] bArr) {
        return (B) setOrigin(newByteArrayOrigin(bArr));
    }

    public B setCharSequence(CharSequence charSequence) {
        return (B) setOrigin(newCharSequenceOrigin(charSequence));
    }

    public B setFile(File file) {
        return (B) setOrigin(newFileOrigin(file));
    }

    public B setFile(String str) {
        return (B) setOrigin(newFileOrigin(str));
    }

    public B setInputStream(InputStream inputStream) {
        return (B) setOrigin(newInputStreamOrigin(inputStream));
    }

    protected B setOrigin(AbstractOrigin<?, ?> origin) {
        this.origin = origin;
        return asThis();
    }

    public B setOutputStream(OutputStream outputStream) {
        return (B) setOrigin(newOutputStreamOrigin(outputStream));
    }

    public B setPath(Path path) {
        return (B) setOrigin(newPathOrigin(path));
    }

    public B setPath(String str) {
        return (B) setOrigin(newPathOrigin(str));
    }

    public B setRandomAccessFile(IORandomAccessFile iORandomAccessFile) {
        return (B) setOrigin(newRandomAccessFileOrigin(iORandomAccessFile));
    }

    public B setRandomAccessFile(RandomAccessFile randomAccessFile) {
        return (B) setOrigin(newRandomAccessFileOrigin(randomAccessFile));
    }

    public B setReader(Reader reader) {
        return (B) setOrigin(newReaderOrigin(reader));
    }

    public B setURI(URI uri) {
        return (B) setOrigin(newURIOrigin(uri));
    }

    public B setWriter(Writer writer) {
        return (B) setOrigin(newWriterOrigin(writer));
    }
}

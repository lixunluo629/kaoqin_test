package org.apache.commons.io.build;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.function.IntUnaryOperator;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.file.PathUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractStreamBuilder.class */
public abstract class AbstractStreamBuilder<T, B extends AbstractStreamBuilder<T, B>> extends AbstractOriginSupplier<T, B> {
    private static final int DEFAULT_MAX_VALUE = Integer.MAX_VALUE;
    private static final OpenOption[] DEFAULT_OPEN_OPTIONS = PathUtils.EMPTY_OPEN_OPTION_ARRAY;
    private int bufferSize = 8192;
    private int bufferSizeDefault = 8192;
    private int bufferSizeMax = Integer.MAX_VALUE;
    private Charset charset = Charset.defaultCharset();
    private Charset charsetDefault = Charset.defaultCharset();
    private OpenOption[] openOptions = DEFAULT_OPEN_OPTIONS;
    private final IntUnaryOperator defaultSizeChecker = size -> {
        return size > this.bufferSizeMax ? throwIae(size, this.bufferSizeMax) : size;
    };
    private IntUnaryOperator bufferSizeChecker = this.defaultSizeChecker;

    private int checkBufferSize(int size) {
        return this.bufferSizeChecker.applyAsInt(size);
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public int getBufferSizeDefault() {
        return this.bufferSizeDefault;
    }

    public CharSequence getCharSequence() throws IOException {
        return checkOrigin().getCharSequence(getCharset());
    }

    public Charset getCharset() {
        return this.charset;
    }

    public Charset getCharsetDefault() {
        return this.charsetDefault;
    }

    public File getFile() {
        return checkOrigin().getFile();
    }

    public InputStream getInputStream() throws IOException {
        return checkOrigin().getInputStream(getOpenOptions());
    }

    public OpenOption[] getOpenOptions() {
        return this.openOptions;
    }

    public OutputStream getOutputStream() throws IOException {
        return checkOrigin().getOutputStream(getOpenOptions());
    }

    public Path getPath() {
        return checkOrigin().getPath();
    }

    public RandomAccessFile getRandomAccessFile() throws IOException {
        return checkOrigin().getRandomAccessFile(getOpenOptions());
    }

    public Reader getReader() throws IOException {
        return checkOrigin().getReader(getCharset());
    }

    public Writer getWriter() throws IOException {
        return checkOrigin().getWriter(getCharset(), getOpenOptions());
    }

    public B setBufferSize(int bufferSize) {
        this.bufferSize = checkBufferSize(bufferSize > 0 ? bufferSize : this.bufferSizeDefault);
        return asThis();
    }

    public B setBufferSize(Integer bufferSize) {
        setBufferSize(bufferSize != null ? bufferSize.intValue() : this.bufferSizeDefault);
        return asThis();
    }

    public B setBufferSizeChecker(IntUnaryOperator bufferSizeChecker) {
        this.bufferSizeChecker = bufferSizeChecker != null ? bufferSizeChecker : this.defaultSizeChecker;
        return asThis();
    }

    protected B setBufferSizeDefault(int bufferSizeDefault) {
        this.bufferSizeDefault = bufferSizeDefault;
        return asThis();
    }

    public B setBufferSizeMax(int bufferSizeMax) {
        this.bufferSizeMax = bufferSizeMax > 0 ? bufferSizeMax : Integer.MAX_VALUE;
        return asThis();
    }

    public B setCharset(Charset charset) {
        this.charset = Charsets.toCharset(charset, this.charsetDefault);
        return asThis();
    }

    public B setCharset(String str) {
        return (B) setCharset(Charsets.toCharset(str, this.charsetDefault));
    }

    protected B setCharsetDefault(Charset defaultCharset) {
        this.charsetDefault = defaultCharset;
        return asThis();
    }

    public B setOpenOptions(OpenOption... openOptions) {
        this.openOptions = openOptions != null ? openOptions : DEFAULT_OPEN_OPTIONS;
        return asThis();
    }

    private int throwIae(int size, int max) {
        throw new IllegalArgumentException(String.format("Request %,d exceeds maximum %,d", Integer.valueOf(size), Integer.valueOf(max)));
    }
}

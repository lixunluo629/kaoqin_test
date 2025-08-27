package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.charset.CharsetEncoders;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ReaderInputStream.class */
public class ReaderInputStream extends AbstractInputStream {
    private final Reader reader;
    private final CharsetEncoder charsetEncoder;
    private final CharBuffer encoderIn;
    private final ByteBuffer encoderOut;
    private CoderResult lastCoderResult;
    private boolean endOfInput;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ReaderInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<ReaderInputStream, Builder> {
        private CharsetEncoder charsetEncoder = ReaderInputStream.newEncoder(getCharset());

        @Override // org.apache.commons.io.function.IOSupplier
        public ReaderInputStream get() throws IOException {
            return new ReaderInputStream(getReader(), this.charsetEncoder, getBufferSize());
        }

        CharsetEncoder getCharsetEncoder() {
            return this.charsetEncoder;
        }

        @Override // org.apache.commons.io.build.AbstractStreamBuilder
        public Builder setCharset(Charset charset) {
            super.setCharset(charset);
            this.charsetEncoder = ReaderInputStream.newEncoder(getCharset());
            return this;
        }

        public Builder setCharsetEncoder(CharsetEncoder newEncoder) {
            this.charsetEncoder = CharsetEncoders.toCharsetEncoder(newEncoder, () -> {
                return ReaderInputStream.newEncoder(getCharsetDefault());
            });
            super.setCharset(this.charsetEncoder.charset());
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    static int checkMinBufferSize(CharsetEncoder charsetEncoder, int bufferSize) {
        float minRequired = minBufferSize(charsetEncoder);
        if (bufferSize < minRequired) {
            throw new IllegalArgumentException(String.format("Buffer size %,d must be at least %s for a CharsetEncoder %s.", Integer.valueOf(bufferSize), Float.valueOf(minRequired), charsetEncoder.charset().displayName()));
        }
        return bufferSize;
    }

    static float minBufferSize(CharsetEncoder charsetEncoder) {
        return charsetEncoder.maxBytesPerChar() * 2.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CharsetEncoder newEncoder(Charset charset) {
        return Charsets.toCharset(charset).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    @Deprecated
    public ReaderInputStream(Reader reader) {
        this(reader, Charset.defaultCharset());
    }

    @Deprecated
    public ReaderInputStream(Reader reader, Charset charset) {
        this(reader, charset, 8192);
    }

    @Deprecated
    public ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
        this(reader, Charsets.toCharset(charset).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }

    @Deprecated
    public ReaderInputStream(Reader reader, CharsetEncoder charsetEncoder) {
        this(reader, charsetEncoder, 8192);
    }

    @Deprecated
    public ReaderInputStream(Reader reader, CharsetEncoder charsetEncoder, int bufferSize) {
        this.reader = reader;
        this.charsetEncoder = CharsetEncoders.toCharsetEncoder(charsetEncoder);
        this.encoderIn = CharBuffer.allocate(checkMinBufferSize(this.charsetEncoder, bufferSize));
        this.encoderIn.flip();
        this.encoderOut = ByteBuffer.allocate(128);
        this.encoderOut.flip();
    }

    @Deprecated
    public ReaderInputStream(Reader reader, String charsetName) {
        this(reader, charsetName, 8192);
    }

    @Deprecated
    public ReaderInputStream(Reader reader, String charsetName, int bufferSize) {
        this(reader, Charsets.toCharset(charsetName), bufferSize);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.encoderOut.hasRemaining()) {
            return this.encoderOut.remaining();
        }
        return 0;
    }

    @Override // org.apache.commons.io.input.AbstractInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
        super.close();
    }

    private void fillBuffer() throws IOException {
        if (this.endOfInput) {
            return;
        }
        if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
            this.encoderIn.compact();
            int position = this.encoderIn.position();
            int c = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
            if (c == -1) {
                this.endOfInput = true;
            } else {
                this.encoderIn.position(position + c);
            }
            this.encoderIn.flip();
        }
        this.encoderOut.compact();
        this.lastCoderResult = this.charsetEncoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
        if (this.endOfInput) {
            this.lastCoderResult = this.charsetEncoder.flush(this.encoderOut);
        }
        if (this.lastCoderResult.isError()) {
            this.lastCoderResult.throwException();
        }
        this.encoderOut.flip();
    }

    CharsetEncoder getCharsetEncoder() {
        return this.charsetEncoder;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        checkOpen();
        while (!this.encoderOut.hasRemaining()) {
            fillBuffer();
            if (this.endOfInput && !this.encoderOut.hasRemaining()) {
                return -1;
            }
        }
        return this.encoderOut.get() & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] array, int off, int len) throws IOException {
        Objects.requireNonNull(array, "array");
        if (len < 0 || off < 0 || off + len > array.length) {
            throw new IndexOutOfBoundsException("Array size=" + array.length + ", offset=" + off + ", length=" + len);
        }
        int read = 0;
        if (len == 0) {
            return 0;
        }
        while (len > 0) {
            if (this.encoderOut.hasRemaining()) {
                int c = Math.min(this.encoderOut.remaining(), len);
                this.encoderOut.get(array, off, c);
                off += c;
                len -= c;
                read += c;
            } else {
                if (this.endOfInput) {
                    break;
                }
                fillBuffer();
            }
        }
        if (read == 0 && this.endOfInput) {
            return -1;
        }
        return read;
    }
}

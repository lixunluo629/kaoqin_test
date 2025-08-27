package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.charset.CharsetDecoders;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/WriterOutputStream.class */
public class WriterOutputStream extends OutputStream {
    private static final int BUFFER_SIZE = 8192;
    private final Writer writer;
    private final CharsetDecoder decoder;
    private final boolean writeImmediately;
    private final ByteBuffer decoderIn;
    private final CharBuffer decoderOut;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/WriterOutputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<WriterOutputStream, Builder> {
        private CharsetDecoder charsetDecoder = getCharset().newDecoder();
        private boolean writeImmediately;

        @Override // org.apache.commons.io.function.IOSupplier
        public WriterOutputStream get() throws IOException {
            return new WriterOutputStream(getWriter(), this.charsetDecoder, getBufferSize(), this.writeImmediately);
        }

        @Override // org.apache.commons.io.build.AbstractStreamBuilder
        public Builder setCharset(Charset charset) {
            super.setCharset(charset);
            this.charsetDecoder = getCharset().newDecoder();
            return this;
        }

        @Override // org.apache.commons.io.build.AbstractStreamBuilder
        public Builder setCharset(String charset) {
            super.setCharset(charset);
            this.charsetDecoder = getCharset().newDecoder();
            return this;
        }

        public Builder setCharsetDecoder(CharsetDecoder charsetDecoder) {
            this.charsetDecoder = charsetDecoder != null ? charsetDecoder : getCharsetDefault().newDecoder();
            super.setCharset(this.charsetDecoder.charset());
            return this;
        }

        public Builder setWriteImmediately(boolean writeImmediately) {
            this.writeImmediately = writeImmediately;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static void checkIbmJdkWithBrokenUTF16(Charset charset) {
        if (!StandardCharsets.UTF_16.name().equals(charset.name())) {
            return;
        }
        byte[] bytes = "vés".getBytes(charset);
        CharsetDecoder charsetDecoder2 = charset.newDecoder();
        ByteBuffer bb2 = ByteBuffer.allocate(16);
        CharBuffer cb2 = CharBuffer.allocate("vés".length());
        int len = bytes.length;
        int i = 0;
        while (i < len) {
            bb2.put(bytes[i]);
            bb2.flip();
            try {
                charsetDecoder2.decode(bb2, cb2, i == len - 1);
                bb2.compact();
                i++;
            } catch (IllegalArgumentException e) {
                throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream");
            }
        }
        cb2.rewind();
        if (!"vés".equals(cb2.toString())) {
            throw new UnsupportedOperationException("UTF-16 requested when running on an IBM JDK with broken UTF-16 support. Please find a JDK that supports UTF-16 if you intend to use UF-16 with WriterOutputStream");
        }
    }

    @Deprecated
    public WriterOutputStream(Writer writer) {
        this(writer, Charset.defaultCharset(), 8192, false);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, Charset charset) {
        this(writer, charset, 8192, false);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, Charset charset, int bufferSize, boolean writeImmediately) {
        this(writer, Charsets.toCharset(charset).newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?"), bufferSize, writeImmediately);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, CharsetDecoder decoder) {
        this(writer, decoder, 8192, false);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, CharsetDecoder decoder, int bufferSize, boolean writeImmediately) {
        this.decoderIn = ByteBuffer.allocate(128);
        checkIbmJdkWithBrokenUTF16(CharsetDecoders.toCharsetDecoder(decoder).charset());
        this.writer = writer;
        this.decoder = CharsetDecoders.toCharsetDecoder(decoder);
        this.writeImmediately = writeImmediately;
        this.decoderOut = CharBuffer.allocate(bufferSize);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, String charsetName) {
        this(writer, charsetName, 8192, false);
    }

    @Deprecated
    public WriterOutputStream(Writer writer, String charsetName, int bufferSize, boolean writeImmediately) {
        this(writer, Charsets.toCharset(charsetName), bufferSize, writeImmediately);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        processInput(true);
        flushOutput();
        this.writer.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        flushOutput();
        this.writer.flush();
    }

    private void flushOutput() throws IOException {
        if (this.decoderOut.position() > 0) {
            this.writer.write(this.decoderOut.array(), 0, this.decoderOut.position());
            this.decoderOut.rewind();
        }
    }

    private void processInput(boolean endOfInput) throws IOException {
        CoderResult coderResult;
        this.decoderIn.flip();
        while (true) {
            coderResult = this.decoder.decode(this.decoderIn, this.decoderOut, endOfInput);
            if (!coderResult.isOverflow()) {
                break;
            } else {
                flushOutput();
            }
        }
        if (!coderResult.isUnderflow()) {
            throw new IOException("Unexpected coder result");
        }
        this.decoderIn.compact();
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int c = Math.min(len, this.decoderIn.remaining());
            this.decoderIn.put(b, off, c);
            processInput(false);
            len -= c;
            off += c;
        }
        if (this.writeImmediately) {
            flushOutput();
        }
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }
}

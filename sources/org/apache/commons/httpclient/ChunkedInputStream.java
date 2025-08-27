package org.apache.commons.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.util.ExceptionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ChunkedInputStream.class */
public class ChunkedInputStream extends InputStream {
    private InputStream in;
    private int chunkSize;
    private int pos;
    private boolean bof;
    private boolean eof;
    private boolean closed;
    private HttpMethod method;
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$ChunkedInputStream;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$ChunkedInputStream == null) {
            clsClass$ = class$("org.apache.commons.httpclient.ChunkedInputStream");
            class$org$apache$commons$httpclient$ChunkedInputStream = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$ChunkedInputStream;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public ChunkedInputStream(InputStream in, HttpMethod method) throws IOException {
        this.bof = true;
        this.eof = false;
        this.closed = false;
        this.method = null;
        if (in == null) {
            throw new IllegalArgumentException("InputStream parameter may not be null");
        }
        this.in = in;
        this.method = method;
        this.pos = 0;
    }

    public ChunkedInputStream(InputStream in) throws IOException {
        this(in, null);
    }

    @Override // java.io.InputStream
    public int read() throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.pos >= this.chunkSize) {
            nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        this.pos++;
        return this.in.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (this.pos >= this.chunkSize) {
            nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        int count = this.in.read(b, off, Math.min(len, this.chunkSize - this.pos));
        this.pos += count;
        return count;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    private void readCRLF() throws IOException {
        int cr = this.in.read();
        int lf = this.in.read();
        if (cr != 13 || lf != 10) {
            throw new IOException(new StringBuffer().append("CRLF expected at end of chunk: ").append(cr).append("/").append(lf).toString());
        }
    }

    private void nextChunk() throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        if (!this.bof) {
            readCRLF();
        }
        this.chunkSize = getChunkSizeFromInputStream(this.in);
        this.bof = false;
        this.pos = 0;
        if (this.chunkSize == 0) {
            this.eof = true;
            parseTrailerHeaders();
        }
    }

    private static int getChunkSizeFromInputStream(InputStream in) throws IOException, NumberFormatException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int state = 0;
        while (state != -1) {
            int b = in.read();
            if (b == -1) {
                throw new IOException("chunked stream ended unexpectedly");
            }
            switch (state) {
                case 0:
                    switch (b) {
                        case 13:
                            state = 1;
                            continue;
                        case 34:
                            state = 2;
                            break;
                    }
                    baos.write(b);
                    break;
                case 1:
                    if (b == 10) {
                        state = -1;
                        break;
                    } else {
                        throw new IOException("Protocol violation: Unexpected single newline character in chunk size");
                    }
                case 2:
                    switch (b) {
                        case 34:
                            state = 0;
                            break;
                        case 92:
                            baos.write(in.read());
                            continue;
                    }
                    baos.write(b);
                    break;
                default:
                    throw new RuntimeException("assertion failed");
            }
        }
        String dataString = EncodingUtil.getAsciiString(baos.toByteArray());
        int separator = dataString.indexOf(59);
        String dataString2 = separator > 0 ? dataString.substring(0, separator).trim() : dataString.trim();
        try {
            int result = Integer.parseInt(dataString2.trim(), 16);
            return result;
        } catch (NumberFormatException e) {
            throw new IOException(new StringBuffer().append("Bad chunk size: ").append(dataString2).toString());
        }
    }

    private void parseTrailerHeaders() throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            String charset = "US-ASCII";
            if (this.method != null) {
                charset = this.method.getParams().getHttpElementCharset();
            }
            Header[] footers = HttpParser.parseHeaders(this.in, charset);
            if (this.method != null) {
                for (Header header : footers) {
                    this.method.addResponseFooter(header);
                }
            }
        } catch (HttpException e) {
            LOG.error("Error parsing trailer headers", e);
            IOException ioe = new IOException(e.getMessage());
            ExceptionUtil.initCause(ioe, e);
            throw ioe;
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (!this.eof) {
                    exhaustInputStream(this);
                }
            } finally {
                this.eof = true;
                this.closed = true;
            }
        }
    }

    static void exhaustInputStream(InputStream inStream) throws IOException {
        byte[] buffer = new byte[1024];
        while (inStream.read(buffer) >= 0) {
        }
    }
}

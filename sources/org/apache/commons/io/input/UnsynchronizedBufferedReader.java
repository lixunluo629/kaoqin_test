package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedBufferedReader.class */
public class UnsynchronizedBufferedReader extends UnsynchronizedReader {
    private static final char NUL = 0;
    private final Reader in;
    private char[] buf;
    private int pos;
    private int end;
    private int mark;
    private int markLimit;

    public UnsynchronizedBufferedReader(Reader in) {
        this(in, 8192);
    }

    public UnsynchronizedBufferedReader(Reader in, int size) {
        this.mark = -1;
        this.markLimit = -1;
        if (size <= 0) {
            throw new IllegalArgumentException("size <= 0");
        }
        this.in = in;
        this.buf = new char[size];
    }

    final void chompNewline() throws IOException {
        if ((this.pos != this.end || fillBuf() != -1) && this.buf[this.pos] == '\n') {
            this.pos++;
        }
    }

    @Override // org.apache.commons.io.input.UnsynchronizedReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!isClosed()) {
            this.in.close();
            this.buf = null;
            super.close();
        }
    }

    private int fillBuf() throws IOException {
        if (this.mark == -1 || this.pos - this.mark >= this.markLimit) {
            int result = this.in.read(this.buf, 0, this.buf.length);
            if (result > 0) {
                this.mark = -1;
                this.pos = 0;
                this.end = result;
            }
            return result;
        }
        if (this.mark == 0 && this.markLimit > this.buf.length) {
            int newLength = this.buf.length * 2;
            if (newLength > this.markLimit) {
                newLength = this.markLimit;
            }
            char[] newbuf = new char[newLength];
            System.arraycopy(this.buf, 0, newbuf, 0, this.buf.length);
            this.buf = newbuf;
        } else if (this.mark > 0) {
            System.arraycopy(this.buf, this.mark, this.buf, 0, this.buf.length - this.mark);
            this.pos -= this.mark;
            this.end -= this.mark;
            this.mark = 0;
        }
        int count = this.in.read(this.buf, this.pos, this.buf.length - this.pos);
        if (count != -1) {
            this.end += count;
        }
        return count;
    }

    @Override // java.io.Reader
    public void mark(int markLimit) throws IOException {
        if (markLimit < 0) {
            throw new IllegalArgumentException();
        }
        checkOpen();
        this.markLimit = markLimit;
        this.mark = this.pos;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    public int peek() throws IOException {
        mark(1);
        int c = read();
        reset();
        return c;
    }

    public int peek(char[] buf) throws IOException {
        int n = buf.length;
        mark(n);
        int c = read(buf, 0, n);
        reset();
        return c;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        checkOpen();
        if (this.pos < this.end || fillBuf() != -1) {
            char[] cArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            return cArr[i];
        }
        return -1;
    }

    @Override // java.io.Reader
    public int read(char[] buffer, int offset, int length) throws IOException {
        checkOpen();
        if (offset < 0 || offset > buffer.length - length || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        int outstanding = length;
        while (true) {
            if (outstanding <= 0) {
                break;
            }
            int available = this.end - this.pos;
            if (available > 0) {
                int count = available >= outstanding ? outstanding : available;
                System.arraycopy(this.buf, this.pos, buffer, offset, count);
                this.pos += count;
                offset += count;
                outstanding -= count;
            }
            if (outstanding == 0 || (outstanding < length && !this.in.ready())) {
                break;
            }
            if ((this.mark == -1 || this.pos - this.mark >= this.markLimit) && outstanding >= this.buf.length) {
                int count2 = this.in.read(buffer, offset, outstanding);
                if (count2 > 0) {
                    outstanding -= count2;
                    this.mark = -1;
                }
            } else if (fillBuf() == -1) {
                break;
            }
        }
        int count3 = length - outstanding;
        if (count3 > 0 || count3 == length) {
            return count3;
        }
        return -1;
    }

    public String readLine() throws IOException {
        checkOpen();
        if (this.pos == this.end && fillBuf() == -1) {
            return null;
        }
        for (int charPos = this.pos; charPos < this.end; charPos++) {
            char ch2 = this.buf[charPos];
            if (ch2 <= '\r') {
                if (ch2 == '\n') {
                    String res = new String(this.buf, this.pos, charPos - this.pos);
                    this.pos = charPos + 1;
                    return res;
                }
                if (ch2 == '\r') {
                    String res2 = new String(this.buf, this.pos, charPos - this.pos);
                    this.pos = charPos + 1;
                    if ((this.pos < this.end || fillBuf() != -1) && this.buf[this.pos] == '\n') {
                        this.pos++;
                    }
                    return res2;
                }
            }
        }
        char eol = 0;
        StringBuilder result = new StringBuilder(80);
        result.append(this.buf, this.pos, this.end - this.pos);
        while (true) {
            this.pos = this.end;
            if (eol == '\n') {
                return result.toString();
            }
            if (fillBuf() == -1) {
                if (result.length() > 0 || eol != 0) {
                    return result.toString();
                }
                return null;
            }
            for (int charPos2 = this.pos; charPos2 < this.end; charPos2++) {
                char c = this.buf[charPos2];
                if (eol != 0) {
                    if (eol == '\r' && c == '\n') {
                        if (charPos2 > this.pos) {
                            result.append(this.buf, this.pos, (charPos2 - this.pos) - 1);
                        }
                        this.pos = charPos2 + 1;
                    } else {
                        if (charPos2 > this.pos) {
                            result.append(this.buf, this.pos, (charPos2 - this.pos) - 1);
                        }
                        this.pos = charPos2;
                    }
                    return result.toString();
                }
                if (c == '\n' || c == '\r') {
                    eol = c;
                }
            }
            if (eol == 0) {
                result.append(this.buf, this.pos, this.end - this.pos);
            } else {
                result.append(this.buf, this.pos, (this.end - this.pos) - 1);
            }
        }
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        checkOpen();
        return this.end - this.pos > 0 || this.in.ready();
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        checkOpen();
        if (this.mark == -1) {
            throw new IOException("mark == -1");
        }
        this.pos = this.mark;
    }

    @Override // org.apache.commons.io.input.UnsynchronizedReader, java.io.Reader
    public long skip(long amount) throws IOException {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        checkOpen();
        if (amount < 1) {
            return 0L;
        }
        if (this.end - this.pos >= amount) {
            this.pos += Math.toIntExact(amount);
            return amount;
        }
        long read = this.end - this.pos;
        this.pos = this.end;
        while (read < amount) {
            if (fillBuf() == -1) {
                return read;
            }
            if (this.end - this.pos >= amount - read) {
                this.pos += Math.toIntExact(amount - read);
                return amount;
            }
            read += this.end - this.pos;
            this.pos = this.end;
        }
        return amount;
    }
}

package redis.clients.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import redis.clients.jedis.exceptions.JedisConnectionException;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/RedisInputStream.class */
public class RedisInputStream extends FilterInputStream {
    protected final byte[] buf;
    protected int count;
    protected int limit;

    public RedisInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }

    public RedisInputStream(InputStream in) {
        this(in, 8192);
    }

    public byte readByte() throws JedisConnectionException {
        ensureFill();
        byte[] bArr = this.buf;
        int i = this.count;
        this.count = i + 1;
        return bArr[i];
    }

    public String readLine() throws JedisConnectionException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            ensureFill();
            byte[] bArr = this.buf;
            int i = this.count;
            this.count = i + 1;
            byte b = bArr[i];
            if (b == 13) {
                ensureFill();
                byte[] bArr2 = this.buf;
                int i2 = this.count;
                this.count = i2 + 1;
                byte c = bArr2[i2];
                if (c == 10) {
                    break;
                }
                sb.append((char) b);
                sb.append((char) c);
            } else {
                sb.append((char) b);
            }
        }
        String reply = sb.toString();
        if (reply.length() == 0) {
            throw new JedisConnectionException("It seems like server has closed the connection.");
        }
        return reply;
    }

    public byte[] readLineBytes() throws JedisConnectionException {
        ensureFill();
        int pos = this.count;
        byte[] buf = this.buf;
        while (pos != this.limit) {
            int i = pos;
            pos++;
            if (buf[i] == 13) {
                if (pos == this.limit) {
                    return readLineBytesSlowly();
                }
                pos++;
                if (buf[pos] == 10) {
                    int N = (pos - this.count) - 2;
                    byte[] line = new byte[N];
                    System.arraycopy(buf, this.count, line, 0, N);
                    this.count = pos;
                    return line;
                }
            }
        }
        return readLineBytesSlowly();
    }

    private byte[] readLineBytesSlowly() throws JedisConnectionException {
        ByteArrayOutputStream bout = null;
        while (true) {
            ensureFill();
            byte[] bArr = this.buf;
            int i = this.count;
            this.count = i + 1;
            byte b = bArr[i];
            if (b == 13) {
                ensureFill();
                byte[] bArr2 = this.buf;
                int i2 = this.count;
                this.count = i2 + 1;
                byte c = bArr2[i2];
                if (c == 10) {
                    break;
                }
                if (bout == null) {
                    bout = new ByteArrayOutputStream(16);
                }
                bout.write(b);
                bout.write(c);
            } else {
                if (bout == null) {
                    bout = new ByteArrayOutputStream(16);
                }
                bout.write(b);
            }
        }
        return bout == null ? new byte[0] : bout.toByteArray();
    }

    public int readIntCrLf() {
        return (int) readLongCrLf();
    }

    public long readLongCrLf() throws JedisConnectionException {
        long value;
        byte[] buf = this.buf;
        ensureFill();
        boolean isNeg = buf[this.count] == 45;
        if (isNeg) {
            this.count++;
        }
        long j = 0;
        while (true) {
            value = j;
            ensureFill();
            int i = this.count;
            this.count = i + 1;
            byte b = buf[i];
            if (b == 13) {
                break;
            }
            j = ((value * 10) + b) - 48;
        }
        ensureFill();
        int i2 = this.count;
        this.count = i2 + 1;
        if (buf[i2] != 10) {
            throw new JedisConnectionException("Unexpected character!");
        }
        return isNeg ? -value : value;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws JedisConnectionException {
        ensureFill();
        int length = Math.min(this.limit - this.count, len);
        System.arraycopy(this.buf, this.count, b, off, length);
        this.count += length;
        return length;
    }

    private void ensureFill() throws JedisConnectionException {
        if (this.count >= this.limit) {
            try {
                this.limit = this.in.read(this.buf);
                this.count = 0;
                if (this.limit == -1) {
                    throw new JedisConnectionException("Unexpected end of stream.");
                }
            } catch (IOException e) {
                throw new JedisConnectionException(e);
            }
        }
    }
}

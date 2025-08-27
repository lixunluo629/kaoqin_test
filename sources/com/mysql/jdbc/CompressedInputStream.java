package com.mysql.jdbc;

import com.mysql.jdbc.ConnectionPropertiesImpl;
import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.NullLogger;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CompressedInputStream.class */
class CompressedInputStream extends InputStream {
    private byte[] buffer;
    private InputStream in;
    private Inflater inflater;
    private ConnectionPropertiesImpl.BooleanConnectionProperty traceProtocol;
    private Log log;
    private byte[] packetHeaderBuffer = new byte[7];
    private int pos = 0;

    /* JADX WARN: Multi-variable type inference failed */
    public CompressedInputStream(Connection connection, InputStream streamFromServer) {
        this.traceProtocol = ((ConnectionPropertiesImpl) connection).traceProtocol;
        try {
            this.log = connection.getLog();
        } catch (SQLException e) {
            this.log = new NullLogger(null);
        }
        this.in = streamFromServer;
        this.inflater = new Inflater();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.buffer == null) {
            return this.in.available();
        }
        return (this.buffer.length - this.pos) + this.in.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
        this.buffer = null;
        this.inflater.end();
        this.inflater = null;
        this.traceProtocol = null;
        this.log = null;
    }

    private void getNextPacketFromServer() throws DataFormatException, IOException {
        byte[] uncompressedData;
        int lengthRead = readFully(this.packetHeaderBuffer, 0, 7);
        if (lengthRead < 7) {
            throw new IOException("Unexpected end of input stream");
        }
        int compressedPacketLength = (this.packetHeaderBuffer[0] & 255) + ((this.packetHeaderBuffer[1] & 255) << 8) + ((this.packetHeaderBuffer[2] & 255) << 16);
        int uncompressedLength = (this.packetHeaderBuffer[4] & 255) + ((this.packetHeaderBuffer[5] & 255) << 8) + ((this.packetHeaderBuffer[6] & 255) << 16);
        boolean doTrace = this.traceProtocol.getValueAsBoolean();
        if (doTrace) {
            this.log.logTrace("Reading compressed packet of length " + compressedPacketLength + " uncompressed to " + uncompressedLength);
        }
        if (uncompressedLength > 0) {
            uncompressedData = new byte[uncompressedLength];
            byte[] compressedBuffer = new byte[compressedPacketLength];
            readFully(compressedBuffer, 0, compressedPacketLength);
            this.inflater.reset();
            this.inflater.setInput(compressedBuffer);
            try {
                this.inflater.inflate(uncompressedData);
            } catch (DataFormatException e) {
                throw new IOException("Error while uncompressing packet from server.");
            }
        } else {
            if (doTrace) {
                this.log.logTrace("Packet didn't meet compression threshold, not uncompressing...");
            }
            uncompressedLength = compressedPacketLength;
            uncompressedData = new byte[uncompressedLength];
            readFully(uncompressedData, 0, uncompressedLength);
        }
        if (doTrace) {
            if (uncompressedLength > 1024) {
                this.log.logTrace("Uncompressed packet: \n" + StringUtils.dumpAsHex(uncompressedData, 256));
                byte[] tempData = new byte[256];
                System.arraycopy(uncompressedData, uncompressedLength - 256, tempData, 0, 256);
                this.log.logTrace("Uncompressed packet: \n" + StringUtils.dumpAsHex(tempData, 256));
                this.log.logTrace("Large packet dump truncated. Showing first and last 256 bytes.");
            } else {
                this.log.logTrace("Uncompressed packet: \n" + StringUtils.dumpAsHex(uncompressedData, uncompressedLength));
            }
        }
        if (this.buffer != null && this.pos < this.buffer.length) {
            if (doTrace) {
                this.log.logTrace("Combining remaining packet with new: ");
            }
            int remaining = this.buffer.length - this.pos;
            byte[] newBuffer = new byte[remaining + uncompressedData.length];
            System.arraycopy(this.buffer, this.pos, newBuffer, 0, remaining);
            System.arraycopy(uncompressedData, 0, newBuffer, remaining, uncompressedData.length);
            uncompressedData = newBuffer;
        }
        this.pos = 0;
        this.buffer = uncompressedData;
    }

    private void getNextPacketIfRequired(int numBytes) throws DataFormatException, IOException {
        if (this.buffer == null || this.pos + numBytes > this.buffer.length) {
            getNextPacketFromServer();
        }
    }

    @Override // java.io.InputStream
    public int read() throws DataFormatException, IOException {
        try {
            getNextPacketIfRequired(1);
            byte[] bArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i] & 255;
        } catch (IOException e) {
            return -1;
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws DataFormatException, IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (len <= 0) {
            return 0;
        }
        try {
            getNextPacketIfRequired(len);
            int remainingBufferLength = this.buffer.length - this.pos;
            int consummedBytesLength = Math.min(remainingBufferLength, len);
            System.arraycopy(this.buffer, this.pos, b, off, consummedBytesLength);
            this.pos += consummedBytesLength;
            return consummedBytesLength;
        } catch (IOException e) {
            return -1;
        }
    }

    private final int readFully(byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i = 0;
        while (true) {
            int n = i;
            if (n < len) {
                int count = this.in.read(b, off + n, len - n);
                if (count < 0) {
                    throw new EOFException();
                }
                i = n + count;
            } else {
                return n;
            }
        }
    }

    @Override // java.io.InputStream
    public long skip(long n) throws DataFormatException, IOException {
        long count = 0;
        long j = 0;
        while (true) {
            long i = j;
            if (i >= n) {
                break;
            }
            int bytesRead = read();
            if (bytesRead == -1) {
                break;
            }
            count++;
            j = i + 1;
        }
        return count;
    }
}

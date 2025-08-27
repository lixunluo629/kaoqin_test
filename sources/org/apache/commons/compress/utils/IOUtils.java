package org.apache.commons.compress.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import org.aspectj.apache.bcel.Constants;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/IOUtils.class */
public final class IOUtils {
    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;
    private static final byte[] SKIP_BUF = new byte[4096];

    private IOUtils() {
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, COPY_BUF_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int buffersize) throws IOException {
        if (buffersize < 1) {
            throw new IllegalArgumentException("buffersize must be bigger than 0");
        }
        byte[] buffer = new byte[buffersize];
        long j = 0;
        while (true) {
            long count = j;
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
                j = count + n;
            } else {
                return count;
            }
        }
    }

    public static long skip(InputStream input, long numToSkip) throws IOException {
        int read;
        while (numToSkip > 0) {
            long skipped = input.skip(numToSkip);
            if (skipped == 0) {
                break;
            }
            numToSkip -= skipped;
        }
        while (numToSkip > 0 && (read = readFully(input, SKIP_BUF, 0, (int) Math.min(numToSkip, Constants.NEGATABLE))) >= 1) {
            numToSkip -= read;
        }
        return numToSkip - numToSkip;
    }

    public static int readFully(InputStream input, byte[] b) throws IOException {
        return readFully(input, b, 0, b.length);
    }

    public static int readFully(InputStream input, byte[] b, int offset, int len) throws IOException {
        int x;
        if (len < 0 || offset < 0 || len + offset > b.length) {
            throw new IndexOutOfBoundsException();
        }
        int count = 0;
        while (count != len && (x = input.read(b, offset + count, len - count)) != -1) {
            count += x;
        }
        return count;
    }

    public static void readFully(ReadableByteChannel channel, ByteBuffer b) throws IOException {
        int read;
        int readNow;
        int expectedLength = b.remaining();
        int i = 0;
        while (true) {
            read = i;
            if (read >= expectedLength || (readNow = channel.read(b)) <= 0) {
                break;
            } else {
                i = read + readNow;
            }
        }
        if (read < expectedLength) {
            throw new EOFException();
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static void closeQuietly(Closeable c) throws IOException {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}

package org.bouncycastle.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/io/Streams.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/io/Streams.class */
public final class Streams {
    private static int BUFFER_SIZE = 512;

    public static void drain(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[BUFFER_SIZE];
        while (inputStream.read(bArr, 0, bArr.length) >= 0) {
        }
    }

    public static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAll(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inputStream, int i) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAllLimited(inputStream, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int readFully(InputStream inputStream, byte[] bArr) throws IOException {
        return readFully(inputStream, bArr, 0, bArr.length);
    }

    public static int readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        int i3;
        int i4;
        int i5 = 0;
        while (true) {
            i3 = i5;
            if (i3 >= i2 || (i4 = inputStream.read(bArr, i + i3, i2 - i3)) < 0) {
                break;
            }
            i5 = i3 + i4;
        }
        return i3;
    }

    public static void pipeAll(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[BUFFER_SIZE];
        while (true) {
            int i = inputStream.read(bArr, 0, bArr.length);
            if (i < 0) {
                return;
            } else {
                outputStream.write(bArr, 0, i);
            }
        }
    }

    public static long pipeAllLimited(InputStream inputStream, long j, OutputStream outputStream) throws IOException {
        long j2 = 0;
        byte[] bArr = new byte[BUFFER_SIZE];
        while (true) {
            int i = inputStream.read(bArr, 0, bArr.length);
            if (i < 0) {
                return j2;
            }
            j2 += i;
            if (j2 > j) {
                throw new StreamOverflowException("Data Overflow");
            }
            outputStream.write(bArr, 0, i);
        }
    }
}

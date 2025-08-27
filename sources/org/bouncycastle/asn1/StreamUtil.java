package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/StreamUtil.class */
class StreamUtil {
    private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();

    StreamUtil() {
    }

    static int findLimit(InputStream inputStream) {
        if (inputStream instanceof LimitedInputStream) {
            return ((LimitedInputStream) inputStream).getLimit();
        }
        if (inputStream instanceof ASN1InputStream) {
            return ((ASN1InputStream) inputStream).getLimit();
        }
        if (inputStream instanceof ByteArrayInputStream) {
            return ((ByteArrayInputStream) inputStream).available();
        }
        if (inputStream instanceof FileInputStream) {
            try {
                FileChannel channel = ((FileInputStream) inputStream).getChannel();
                long size = channel != null ? channel.size() : 2147483647L;
                if (size < 2147483647L) {
                    return (int) size;
                }
            } catch (IOException e) {
            }
        }
        if (MAX_MEMORY > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) MAX_MEMORY;
    }

    static int calculateBodyLength(int i) {
        int i2 = 1;
        if (i > 127) {
            int i3 = 1;
            int i4 = i;
            while (true) {
                int i5 = i4 >>> 8;
                i4 = i5;
                if (i5 == 0) {
                    break;
                }
                i3++;
            }
            for (int i6 = (i3 - 1) * 8; i6 >= 0; i6 -= 8) {
                i2++;
            }
        }
        return i2;
    }

    static int calculateTagLength(int i) throws IOException {
        int length = 1;
        if (i >= 31) {
            if (i < 128) {
                length = 1 + 1;
            } else {
                byte[] bArr = new byte[5];
                int length2 = bArr.length - 1;
                bArr[length2] = (byte) (i & 127);
                do {
                    i >>= 7;
                    length2--;
                    bArr[length2] = (byte) ((i & 127) | 128);
                } while (i > 127);
                length = 1 + (bArr.length - length2);
            }
        }
        return length;
    }
}

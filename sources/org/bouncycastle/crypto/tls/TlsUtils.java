package org.bouncycastle.crypto.tls;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.io.Streams;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsUtils.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsUtils.class */
public class TlsUtils {
    protected static void writeUint8(short s, OutputStream outputStream) throws IOException {
        outputStream.write(s);
    }

    protected static void writeUint8(short s, byte[] bArr, int i) {
        bArr[i] = (byte) s;
    }

    protected static void writeUint16(int i, OutputStream outputStream) throws IOException {
        outputStream.write(i >> 8);
        outputStream.write(i);
    }

    protected static void writeUint16(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >> 8);
        bArr[i2 + 1] = (byte) i;
    }

    protected static void writeUint24(int i, OutputStream outputStream) throws IOException {
        outputStream.write(i >> 16);
        outputStream.write(i >> 8);
        outputStream.write(i);
    }

    protected static void writeUint24(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >> 16);
        bArr[i2 + 1] = (byte) (i >> 8);
        bArr[i2 + 2] = (byte) i;
    }

    protected static void writeUint32(long j, OutputStream outputStream) throws IOException {
        outputStream.write((int) (j >> 24));
        outputStream.write((int) (j >> 16));
        outputStream.write((int) (j >> 8));
        outputStream.write((int) j);
    }

    protected static void writeUint32(long j, byte[] bArr, int i) {
        bArr[i] = (byte) (j >> 24);
        bArr[i + 1] = (byte) (j >> 16);
        bArr[i + 2] = (byte) (j >> 8);
        bArr[i + 3] = (byte) j;
    }

    protected static void writeUint64(long j, OutputStream outputStream) throws IOException {
        outputStream.write((int) (j >> 56));
        outputStream.write((int) (j >> 48));
        outputStream.write((int) (j >> 40));
        outputStream.write((int) (j >> 32));
        outputStream.write((int) (j >> 24));
        outputStream.write((int) (j >> 16));
        outputStream.write((int) (j >> 8));
        outputStream.write((int) j);
    }

    protected static void writeUint64(long j, byte[] bArr, int i) {
        bArr[i] = (byte) (j >> 56);
        bArr[i + 1] = (byte) (j >> 48);
        bArr[i + 2] = (byte) (j >> 40);
        bArr[i + 3] = (byte) (j >> 32);
        bArr[i + 4] = (byte) (j >> 24);
        bArr[i + 5] = (byte) (j >> 16);
        bArr[i + 6] = (byte) (j >> 8);
        bArr[i + 7] = (byte) j;
    }

    protected static void writeOpaque8(byte[] bArr, OutputStream outputStream) throws IOException {
        writeUint8((short) bArr.length, outputStream);
        outputStream.write(bArr);
    }

    protected static void writeOpaque16(byte[] bArr, OutputStream outputStream) throws IOException {
        writeUint16(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    protected static void writeOpaque24(byte[] bArr, OutputStream outputStream) throws IOException {
        writeUint24(bArr.length, outputStream);
        outputStream.write(bArr);
    }

    protected static void writeUint8Array(short[] sArr, OutputStream outputStream) throws IOException {
        for (short s : sArr) {
            writeUint8(s, outputStream);
        }
    }

    protected static void writeUint16Array(int[] iArr, OutputStream outputStream) throws IOException {
        for (int i : iArr) {
            writeUint16(i, outputStream);
        }
    }

    protected static short readUint8(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        if (i == -1) {
            throw new EOFException();
        }
        return (short) i;
    }

    protected static int readUint16(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        if ((i | i2) < 0) {
            throw new EOFException();
        }
        return (i << 8) | i2;
    }

    protected static int readUint24(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        if ((i | i2 | i3) < 0) {
            throw new EOFException();
        }
        return (i << 16) | (i2 << 8) | i3;
    }

    protected static long readUint32(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        int i4 = inputStream.read();
        if ((i | i2 | i3 | i4) < 0) {
            throw new EOFException();
        }
        return (i << 24) | (i2 << 16) | (i3 << 8) | i4;
    }

    protected static void readFully(byte[] bArr, InputStream inputStream) throws IOException {
        if (Streams.readFully(inputStream, bArr) != bArr.length) {
            throw new EOFException();
        }
    }

    protected static byte[] readOpaque8(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[readUint8(inputStream)];
        readFully(bArr, inputStream);
        return bArr;
    }

    protected static byte[] readOpaque16(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[readUint16(inputStream)];
        readFully(bArr, inputStream);
        return bArr;
    }

    protected static void checkVersion(byte[] bArr, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        if (bArr[0] != 3 || bArr[1] != 1) {
            throw new TlsFatalAlert((short) 70);
        }
    }

    protected static void checkVersion(InputStream inputStream, TlsProtocolHandler tlsProtocolHandler) throws IOException {
        int i = inputStream.read();
        int i2 = inputStream.read();
        if (i != 3 || i2 != 1) {
            throw new TlsFatalAlert((short) 70);
        }
    }

    protected static void writeGMTUnixTime(byte[] bArr, int i) {
        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        bArr[i] = (byte) (iCurrentTimeMillis >> 24);
        bArr[i + 1] = (byte) (iCurrentTimeMillis >> 16);
        bArr[i + 2] = (byte) (iCurrentTimeMillis >> 8);
        bArr[i + 3] = (byte) iCurrentTimeMillis;
    }

    protected static void writeVersion(OutputStream outputStream) throws IOException {
        outputStream.write(3);
        outputStream.write(1);
    }

    protected static void writeVersion(byte[] bArr, int i) throws IOException {
        bArr[i] = 3;
        bArr[i + 1] = 1;
    }

    private static void hmac_hash(Digest digest, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        HMac hMac = new HMac(digest);
        KeyParameter keyParameter = new KeyParameter(bArr);
        byte[] bArr4 = bArr2;
        int digestSize = digest.getDigestSize();
        int length = ((bArr3.length + digestSize) - 1) / digestSize;
        byte[] bArr5 = new byte[hMac.getMacSize()];
        byte[] bArr6 = new byte[hMac.getMacSize()];
        for (int i = 0; i < length; i++) {
            hMac.init(keyParameter);
            hMac.update(bArr4, 0, bArr4.length);
            hMac.doFinal(bArr5, 0);
            bArr4 = bArr5;
            hMac.init(keyParameter);
            hMac.update(bArr4, 0, bArr4.length);
            hMac.update(bArr2, 0, bArr2.length);
            hMac.doFinal(bArr6, 0);
            System.arraycopy(bArr6, 0, bArr3, digestSize * i, Math.min(digestSize, bArr3.length - (digestSize * i)));
        }
    }

    protected static byte[] PRF(byte[] bArr, String str, byte[] bArr2, int i) {
        byte[] byteArray = Strings.toByteArray(str);
        int length = (bArr.length + 1) / 2;
        byte[] bArr3 = new byte[length];
        byte[] bArr4 = new byte[length];
        System.arraycopy(bArr, 0, bArr3, 0, length);
        System.arraycopy(bArr, bArr.length - length, bArr4, 0, length);
        byte[] bArrConcat = concat(byteArray, bArr2);
        byte[] bArr5 = new byte[i];
        byte[] bArr6 = new byte[i];
        hmac_hash(new MD5Digest(), bArr3, bArrConcat, bArr6);
        hmac_hash(new SHA1Digest(), bArr4, bArrConcat, bArr5);
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = i2;
            bArr5[i3] = (byte) (bArr5[i3] ^ bArr6[i2]);
        }
        return bArr5;
    }

    static byte[] concat(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    static void validateKeyUsage(X509CertificateStructure x509CertificateStructure, int i) throws IOException {
        X509Extension extension;
        X509Extensions extensions = x509CertificateStructure.getTBSCertificate().getExtensions();
        if (extensions != null && (extension = extensions.getExtension(X509Extension.keyUsage)) != null && (KeyUsage.getInstance(extension).getBytes()[0] & 255 & i) != i) {
            throw new TlsFatalAlert((short) 46);
        }
    }
}

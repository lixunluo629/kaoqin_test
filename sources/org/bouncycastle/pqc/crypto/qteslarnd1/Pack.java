package org.bouncycastle.pqc.crypto.qteslarnd1;

import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.IEEEDouble;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/Pack.class */
class Pack {
    Pack() {
    }

    public static void encodePrivateKeyI(byte[] bArr, int[] iArr, int[] iArr2, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 512; i3 += 4) {
            bArr[i2 + 0] = (byte) iArr[i3 + 0];
            bArr[i2 + 1] = (byte) (((iArr[i3 + 0] >> 8) & 3) | (iArr[i3 + 1] << 2));
            bArr[i2 + 2] = (byte) (((iArr[i3 + 1] >> 6) & 15) | (iArr[i3 + 2] << 4));
            bArr[i2 + 3] = (byte) (((iArr[i3 + 2] >> 4) & 63) | (iArr[i3 + 3] << 6));
            bArr[i2 + 4] = (byte) (iArr[i3 + 3] >> 2);
            i2 += 5;
        }
        for (int i4 = 0; i4 < 512; i4 += 4) {
            bArr[i2 + 0] = (byte) iArr2[i4 + 0];
            bArr[i2 + 1] = (byte) (((iArr2[i4 + 0] >> 8) & 3) | (iArr2[i4 + 1] << 2));
            bArr[i2 + 2] = (byte) (((iArr2[i4 + 1] >> 6) & 15) | (iArr2[i4 + 2] << 4));
            bArr[i2 + 3] = (byte) (((iArr2[i4 + 2] >> 4) & 63) | (iArr2[i4 + 3] << 6));
            bArr[i2 + 4] = (byte) (iArr2[i4 + 3] >> 2);
            i2 += 5;
        }
        System.arraycopy(bArr2, i, bArr, 1280, 64);
    }

    public static void encodePrivateKeyIIISize(byte[] bArr, int[] iArr, int[] iArr2, byte[] bArr2, int i) {
        for (int i2 = 0; i2 < 1024; i2++) {
            bArr[i2] = (byte) iArr[i2];
        }
        for (int i3 = 0; i3 < 1024; i3++) {
            bArr[1024 + i3] = (byte) iArr2[i3];
        }
        System.arraycopy(bArr2, i, bArr, 2048, 64);
    }

    public static void encodePrivateKeyIIISpeed(byte[] bArr, int[] iArr, int[] iArr2, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 1024; i3 += 8) {
            bArr[i2 + 0] = (byte) iArr[i3 + 0];
            bArr[i2 + 1] = (byte) (((iArr[i3 + 0] >> 8) & 1) | (iArr[i3 + 1] << 1));
            bArr[i2 + 2] = (byte) (((iArr[i3 + 1] >> 7) & 3) | (iArr[i3 + 2] << 2));
            bArr[i2 + 3] = (byte) (((iArr[i3 + 2] >> 6) & 7) | (iArr[i3 + 3] << 3));
            bArr[i2 + 4] = (byte) (((iArr[i3 + 3] >> 5) & 15) | (iArr[i3 + 4] << 4));
            bArr[i2 + 5] = (byte) (((iArr[i3 + 4] >> 4) & 31) | (iArr[i3 + 5] << 5));
            bArr[i2 + 6] = (byte) (((iArr[i3 + 5] >> 3) & 63) | (iArr[i3 + 6] << 6));
            bArr[i2 + 7] = (byte) (((iArr[i3 + 6] >> 2) & 127) | (iArr[i3 + 7] << 7));
            bArr[i2 + 8] = (byte) (iArr[i3 + 7] >> 1);
            i2 += 9;
        }
        for (int i4 = 0; i4 < 1024; i4 += 8) {
            bArr[i2 + 0] = (byte) iArr2[i4 + 0];
            bArr[i2 + 1] = (byte) (((iArr2[i4 + 0] >> 8) & 1) | (iArr2[i4 + 1] << 1));
            bArr[i2 + 2] = (byte) (((iArr2[i4 + 1] >> 7) & 3) | (iArr2[i4 + 2] << 2));
            bArr[i2 + 3] = (byte) (((iArr2[i4 + 2] >> 6) & 7) | (iArr2[i4 + 3] << 3));
            bArr[i2 + 4] = (byte) (((iArr2[i4 + 3] >> 5) & 15) | (iArr2[i4 + 4] << 4));
            bArr[i2 + 5] = (byte) (((iArr2[i4 + 4] >> 4) & 31) | (iArr2[i4 + 5] << 5));
            bArr[i2 + 6] = (byte) (((iArr2[i4 + 5] >> 3) & 63) | (iArr2[i4 + 6] << 6));
            bArr[i2 + 7] = (byte) (((iArr2[i4 + 6] >> 2) & 127) | (iArr2[i4 + 7] << 7));
            bArr[i2 + 8] = (byte) (iArr2[i4 + 7] >> 1);
            i2 += 9;
        }
        System.arraycopy(bArr2, i, bArr, OlympusCameraSettingsMakernoteDirectory.TagManometerPressure, 64);
    }

    public static void decodePrivateKeyI(byte[] bArr, short[] sArr, short[] sArr2, byte[] bArr2) {
        int i = 0;
        for (int i2 = 0; i2 < 512; i2 += 4) {
            sArr[i2 + 0] = (short) (bArr2[i + 0] & 255);
            int i3 = i2 + 0;
            sArr[i3] = (short) (sArr[i3] | ((short) (((bArr2[i + 1] & 255) << 30) >> 22)));
            sArr[i2 + 1] = (short) ((bArr2[i + 1] & 255) >> 2);
            int i4 = i2 + 1;
            sArr[i4] = (short) (sArr[i4] | ((short) (((bArr2[i + 2] & 255) << 28) >> 22)));
            sArr[i2 + 2] = (short) ((bArr2[i + 2] & 255) >> 4);
            int i5 = i2 + 2;
            sArr[i5] = (short) (sArr[i5] | ((short) (((bArr2[i + 3] & 255) << 26) >> 22)));
            sArr[i2 + 3] = (short) ((bArr2[i + 3] & 255) >> 6);
            int i6 = i2 + 3;
            sArr[i6] = (short) (sArr[i6] | ((short) (bArr2[i + 4] << 2)));
            i += 5;
        }
        for (int i7 = 0; i7 < 512; i7 += 4) {
            sArr2[i7 + 0] = (short) (bArr2[i + 0] & 255);
            int i8 = i7 + 0;
            sArr2[i8] = (short) (sArr2[i8] | ((short) (((bArr2[i + 1] & 255) << 30) >> 22)));
            sArr2[i7 + 1] = (short) ((bArr2[i + 1] & 255) >> 2);
            int i9 = i7 + 1;
            sArr2[i9] = (short) (sArr2[i9] | ((short) (((bArr2[i + 2] & 255) << 28) >> 22)));
            sArr2[i7 + 2] = (short) ((bArr2[i + 2] & 255) >> 4);
            int i10 = i7 + 2;
            sArr2[i10] = (short) (sArr2[i10] | ((short) (((bArr2[i + 3] & 255) << 26) >> 22)));
            sArr2[i7 + 3] = (short) ((bArr2[i + 3] & 255) >> 6);
            int i11 = i7 + 3;
            sArr2[i11] = (short) (sArr2[i11] | ((short) (bArr2[i + 4] << 2)));
            i += 5;
        }
        System.arraycopy(bArr2, 1280, bArr, 0, 64);
    }

    public static void decodePrivateKeyIIISize(byte[] bArr, short[] sArr, short[] sArr2, byte[] bArr2) {
        for (int i = 0; i < 1024; i++) {
            sArr[i] = bArr2[i];
        }
        for (int i2 = 0; i2 < 1024; i2++) {
            sArr2[i2] = bArr2[1024 + i2];
        }
        System.arraycopy(bArr2, 2048, bArr, 0, 64);
    }

    public static void decodePrivateKeyIIISpeed(byte[] bArr, short[] sArr, short[] sArr2, byte[] bArr2) {
        int i = 0;
        for (int i2 = 0; i2 < 1024; i2 += 8) {
            sArr[i2 + 0] = (short) (bArr2[i + 0] & 255);
            int i3 = i2 + 0;
            sArr[i3] = (short) (sArr[i3] | ((short) (((bArr2[i + 1] & 255) << 31) >> 23)));
            sArr[i2 + 1] = (short) ((bArr2[i + 1] & 255) >> 1);
            int i4 = i2 + 1;
            sArr[i4] = (short) (sArr[i4] | ((short) (((bArr2[i + 2] & 255) << 30) >> 23)));
            sArr[i2 + 2] = (short) ((bArr2[i + 2] & 255) >> 2);
            int i5 = i2 + 2;
            sArr[i5] = (short) (sArr[i5] | ((short) (((bArr2[i + 3] & 255) << 29) >> 23)));
            sArr[i2 + 3] = (short) ((bArr2[i + 3] & 255) >> 3);
            int i6 = i2 + 3;
            sArr[i6] = (short) (sArr[i6] | ((short) (((bArr2[i + 4] & 255) << 28) >> 23)));
            sArr[i2 + 4] = (short) ((bArr2[i + 4] & 255) >> 4);
            int i7 = i2 + 4;
            sArr[i7] = (short) (sArr[i7] | ((short) (((bArr2[i + 5] & 255) << 27) >> 23)));
            sArr[i2 + 5] = (short) ((bArr2[i + 5] & 255) >> 5);
            int i8 = i2 + 5;
            sArr[i8] = (short) (sArr[i8] | ((short) (((bArr2[i + 6] & 255) << 26) >> 23)));
            sArr[i2 + 6] = (short) ((bArr2[i + 6] & 255) >> 6);
            int i9 = i2 + 6;
            sArr[i9] = (short) (sArr[i9] | ((short) (((bArr2[i + 7] & 255) << 25) >> 23)));
            sArr[i2 + 7] = (short) ((bArr2[i + 7] & 255) >> 7);
            int i10 = i2 + 7;
            sArr[i10] = (short) (sArr[i10] | ((short) (bArr2[i + 8] << 1)));
            i += 9;
        }
        for (int i11 = 0; i11 < 1024; i11 += 8) {
            sArr2[i11 + 0] = (short) (bArr2[i + 0] & 255);
            int i12 = i11 + 0;
            sArr2[i12] = (short) (sArr2[i12] | ((short) (((bArr2[i + 1] & 255) << 31) >> 23)));
            sArr2[i11 + 1] = (short) ((bArr2[i + 1] & 255) >> 1);
            int i13 = i11 + 1;
            sArr2[i13] = (short) (sArr2[i13] | ((short) (((bArr2[i + 2] & 255) << 30) >> 23)));
            sArr2[i11 + 2] = (short) ((bArr2[i + 2] & 255) >> 2);
            int i14 = i11 + 2;
            sArr2[i14] = (short) (sArr2[i14] | ((short) (((bArr2[i + 3] & 255) << 29) >> 23)));
            sArr2[i11 + 3] = (short) ((bArr2[i + 3] & 255) >> 3);
            int i15 = i11 + 3;
            sArr2[i15] = (short) (sArr2[i15] | ((short) (((bArr2[i + 4] & 255) << 28) >> 23)));
            sArr2[i11 + 4] = (short) ((bArr2[i + 4] & 255) >> 4);
            int i16 = i11 + 4;
            sArr2[i16] = (short) (sArr2[i16] | ((short) (((bArr2[i + 5] & 255) << 27) >> 23)));
            sArr2[i11 + 5] = (short) ((bArr2[i + 5] & 255) >> 5);
            int i17 = i11 + 5;
            sArr2[i17] = (short) (sArr2[i17] | ((short) (((bArr2[i + 6] & 255) << 26) >> 23)));
            sArr2[i11 + 6] = (short) ((bArr2[i + 6] & 255) >> 6);
            int i18 = i11 + 6;
            sArr2[i18] = (short) (sArr2[i18] | ((short) (((bArr2[i + 7] & 255) << 25) >> 23)));
            sArr2[i11 + 7] = (short) ((bArr2[i + 7] & 255) >> 7);
            int i19 = i11 + 7;
            sArr2[i19] = (short) (sArr2[i19] | ((short) (bArr2[i + 8] << 1)));
            i += 9;
        }
        System.arraycopy(bArr2, OlympusCameraSettingsMakernoteDirectory.TagManometerPressure, bArr, 0, 64);
    }

    public static void packPrivateKey(byte[] bArr, long[] jArr, long[] jArr2, byte[] bArr2, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i2; i4++) {
            bArr[i4] = (byte) jArr[i4];
        }
        for (int i5 = 0; i5 < i3; i5++) {
            for (int i6 = 0; i6 < i2; i6++) {
                bArr[i2 + (i5 * i2) + i6] = (byte) jArr2[(i5 * i2) + i6];
            }
        }
        System.arraycopy(bArr2, i, bArr, i2 + (i3 * i2), 64);
    }

    public static void encodePublicKey(byte[] bArr, int[] iArr, byte[] bArr2, int i, int i2, int i3) {
        int i4 = 0;
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= (i2 * i3) / 32) {
                System.arraycopy(bArr2, i, bArr, (i2 * i3) / 8, 32);
                return;
            }
            CommonFunction.store32(bArr, 4 * (i6 + 0), iArr[i4 + 0] | (iArr[i4 + 1] << 23));
            CommonFunction.store32(bArr, 4 * (i6 + 1), (iArr[i4 + 1] >> 9) | (iArr[i4 + 2] << 14));
            CommonFunction.store32(bArr, 4 * (i6 + 2), (iArr[i4 + 2] >> 18) | (iArr[i4 + 3] << 5) | (iArr[i4 + 4] << 28));
            CommonFunction.store32(bArr, 4 * (i6 + 3), (iArr[i4 + 4] >> 4) | (iArr[i4 + 5] << 19));
            CommonFunction.store32(bArr, 4 * (i6 + 4), (iArr[i4 + 5] >> 13) | (iArr[i4 + 6] << 10));
            CommonFunction.store32(bArr, 4 * (i6 + 5), (iArr[i4 + 6] >> 22) | (iArr[i4 + 7] << 1) | (iArr[i4 + 8] << 24));
            CommonFunction.store32(bArr, 4 * (i6 + 6), (iArr[i4 + 8] >> 8) | (iArr[i4 + 9] << 15));
            CommonFunction.store32(bArr, 4 * (i6 + 7), (iArr[i4 + 9] >> 17) | (iArr[i4 + 10] << 6) | (iArr[i4 + 11] << 29));
            CommonFunction.store32(bArr, 4 * (i6 + 8), (iArr[i4 + 11] >> 3) | (iArr[i4 + 12] << 20));
            CommonFunction.store32(bArr, 4 * (i6 + 9), (iArr[i4 + 12] >> 12) | (iArr[i4 + 13] << 11));
            CommonFunction.store32(bArr, 4 * (i6 + 10), (iArr[i4 + 13] >> 21) | (iArr[i4 + 14] << 2) | (iArr[i4 + 15] << 25));
            CommonFunction.store32(bArr, 4 * (i6 + 11), (iArr[i4 + 15] >> 7) | (iArr[i4 + 16] << 16));
            CommonFunction.store32(bArr, 4 * (i6 + 12), (iArr[i4 + 16] >> 16) | (iArr[i4 + 17] << 7) | (iArr[i4 + 18] << 30));
            CommonFunction.store32(bArr, 4 * (i6 + 13), (iArr[i4 + 18] >> 2) | (iArr[i4 + 19] << 21));
            CommonFunction.store32(bArr, 4 * (i6 + 14), (iArr[i4 + 19] >> 11) | (iArr[i4 + 20] << 12));
            CommonFunction.store32(bArr, 4 * (i6 + 15), (iArr[i4 + 20] >> 20) | (iArr[i4 + 21] << 3) | (iArr[i4 + 22] << 26));
            CommonFunction.store32(bArr, 4 * (i6 + 16), (iArr[i4 + 22] >> 6) | (iArr[i4 + 23] << 17));
            CommonFunction.store32(bArr, 4 * (i6 + 17), (iArr[i4 + 23] >> 15) | (iArr[i4 + 24] << 8) | (iArr[i4 + 25] << 31));
            CommonFunction.store32(bArr, 4 * (i6 + 18), (iArr[i4 + 25] >> 1) | (iArr[i4 + 26] << 22));
            CommonFunction.store32(bArr, 4 * (i6 + 19), (iArr[i4 + 26] >> 10) | (iArr[i4 + 27] << 13));
            CommonFunction.store32(bArr, 4 * (i6 + 20), (iArr[i4 + 27] >> 19) | (iArr[i4 + 28] << 4) | (iArr[i4 + 29] << 27));
            CommonFunction.store32(bArr, 4 * (i6 + 21), (iArr[i4 + 29] >> 5) | (iArr[i4 + 30] << 18));
            CommonFunction.store32(bArr, 4 * (i6 + 22), (iArr[i4 + 30] >> 14) | (iArr[i4 + 31] << 9));
            i4 += 32;
            i5 = i6 + i3;
        }
    }

    public static void encodePublicKeyIIISpeed(byte[] bArr, int[] iArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 768; i3 += 3) {
            CommonFunction.store32(bArr, 4 * (i3 + 0), iArr[i2 + 0] | (iArr[i2 + 1] << 24));
            CommonFunction.store32(bArr, 4 * (i3 + 1), (iArr[i2 + 1] >> 8) | (iArr[i2 + 2] << 16));
            CommonFunction.store32(bArr, 4 * (i3 + 2), (iArr[i2 + 2] >> 16) | (iArr[i2 + 3] << 8));
            i2 += 4;
        }
        System.arraycopy(bArr2, i, bArr, 3072, 32);
    }

    public static void encodePublicKeyIP(byte[] bArr, long[] jArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 3712; i3 += 29) {
            CommonFunction.store32(bArr, 4 * (i3 + 0), (int) (jArr[i2 + 0] | (jArr[i2 + 1] << 29)));
            CommonFunction.store32(bArr, 4 * (i3 + 1), (int) ((jArr[i2 + 1] >> 3) | (jArr[i2 + 2] << 26)));
            CommonFunction.store32(bArr, 4 * (i3 + 2), (int) ((jArr[i2 + 2] >> 6) | (jArr[i2 + 3] << 23)));
            CommonFunction.store32(bArr, 4 * (i3 + 3), (int) ((jArr[i2 + 3] >> 9) | (jArr[i2 + 4] << 20)));
            CommonFunction.store32(bArr, 4 * (i3 + 4), (int) ((jArr[i2 + 4] >> 12) | (jArr[i2 + 5] << 17)));
            CommonFunction.store32(bArr, 4 * (i3 + 5), (int) ((jArr[i2 + 5] >> 15) | (jArr[i2 + 6] << 14)));
            CommonFunction.store32(bArr, 4 * (i3 + 6), (int) ((jArr[i2 + 6] >> 18) | (jArr[i2 + 7] << 11)));
            CommonFunction.store32(bArr, 4 * (i3 + 7), (int) ((jArr[i2 + 7] >> 21) | (jArr[i2 + 8] << 8)));
            CommonFunction.store32(bArr, 4 * (i3 + 8), (int) ((jArr[i2 + 8] >> 24) | (jArr[i2 + 9] << 5)));
            CommonFunction.store32(bArr, 4 * (i3 + 9), (int) ((jArr[i2 + 9] >> 27) | (jArr[i2 + 10] << 2) | (jArr[i2 + 11] << 31)));
            CommonFunction.store32(bArr, 4 * (i3 + 10), (int) ((jArr[i2 + 11] >> 1) | (jArr[i2 + 12] << 28)));
            CommonFunction.store32(bArr, 4 * (i3 + 11), (int) ((jArr[i2 + 12] >> 4) | (jArr[i2 + 13] << 25)));
            CommonFunction.store32(bArr, 4 * (i3 + 12), (int) ((jArr[i2 + 13] >> 7) | (jArr[i2 + 14] << 22)));
            CommonFunction.store32(bArr, 4 * (i3 + 13), (int) ((jArr[i2 + 14] >> 10) | (jArr[i2 + 15] << 19)));
            CommonFunction.store32(bArr, 4 * (i3 + 14), (int) ((jArr[i2 + 15] >> 13) | (jArr[i2 + 16] << 16)));
            CommonFunction.store32(bArr, 4 * (i3 + 15), (int) ((jArr[i2 + 16] >> 16) | (jArr[i2 + 17] << 13)));
            CommonFunction.store32(bArr, 4 * (i3 + 16), (int) ((jArr[i2 + 17] >> 19) | (jArr[i2 + 18] << 10)));
            CommonFunction.store32(bArr, 4 * (i3 + 17), (int) ((jArr[i2 + 18] >> 22) | (jArr[i2 + 19] << 7)));
            CommonFunction.store32(bArr, 4 * (i3 + 18), (int) ((jArr[i2 + 19] >> 25) | (jArr[i2 + 20] << 4)));
            CommonFunction.store32(bArr, 4 * (i3 + 19), (int) ((jArr[i2 + 20] >> 28) | (jArr[i2 + 21] << 1) | (jArr[i2 + 22] << 30)));
            CommonFunction.store32(bArr, 4 * (i3 + 20), (int) ((jArr[i2 + 22] >> 2) | (jArr[i2 + 23] << 27)));
            CommonFunction.store32(bArr, 4 * (i3 + 21), (int) ((jArr[i2 + 23] >> 5) | (jArr[i2 + 24] << 24)));
            CommonFunction.store32(bArr, 4 * (i3 + 22), (int) ((jArr[i2 + 24] >> 8) | (jArr[i2 + 25] << 21)));
            CommonFunction.store32(bArr, 4 * (i3 + 23), (int) ((jArr[i2 + 25] >> 11) | (jArr[i2 + 26] << 18)));
            CommonFunction.store32(bArr, 4 * (i3 + 24), (int) ((jArr[i2 + 26] >> 14) | (jArr[i2 + 27] << 15)));
            CommonFunction.store32(bArr, 4 * (i3 + 25), (int) ((jArr[i2 + 27] >> 17) | (jArr[i2 + 28] << 12)));
            CommonFunction.store32(bArr, 4 * (i3 + 26), (int) ((jArr[i2 + 28] >> 20) | (jArr[i2 + 29] << 9)));
            CommonFunction.store32(bArr, 4 * (i3 + 27), (int) ((jArr[i2 + 29] >> 23) | (jArr[i2 + 30] << 6)));
            CommonFunction.store32(bArr, 4 * (i3 + 28), (int) ((jArr[i2 + 30] >> 26) | (jArr[i2 + 31] << 3)));
            i2 += 32;
        }
        System.arraycopy(bArr2, i, bArr, 14848, 32);
    }

    public static void encodePublicKeyIIIP(byte[] bArr, long[] jArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 9920; i3 += 31) {
            for (int i4 = 0; i4 < 31; i4++) {
                CommonFunction.store32(bArr, 4 * (i3 + i4), (int) ((jArr[i2 + i4] >> i4) | (jArr[(i2 + i4) + 1] << (31 - i4))));
            }
            i2 += 32;
        }
        System.arraycopy(bArr2, i, bArr, 39680, 32);
    }

    public static void decodePublicKey(int[] iArr, byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        int i4 = 0;
        int i5 = (1 << i3) - 1;
        for (int i6 = 0; i6 < i2; i6 += 32) {
            iArr[i6 + 0] = CommonFunction.load32(bArr2, 4 * (i4 + 0)) & i5;
            iArr[i6 + 1] = ((CommonFunction.load32(bArr2, 4 * (i4 + 0)) >>> 23) | (CommonFunction.load32(bArr2, 4 * (i4 + 1)) << 9)) & i5;
            iArr[i6 + 2] = ((CommonFunction.load32(bArr2, 4 * (i4 + 1)) >>> 14) | (CommonFunction.load32(bArr2, 4 * (i4 + 2)) << 18)) & i5;
            iArr[i6 + 3] = (CommonFunction.load32(bArr2, 4 * (i4 + 2)) >>> 5) & i5;
            iArr[i6 + 4] = ((CommonFunction.load32(bArr2, 4 * (i4 + 2)) >>> 28) | (CommonFunction.load32(bArr2, 4 * (i4 + 3)) << 4)) & i5;
            iArr[i6 + 5] = ((CommonFunction.load32(bArr2, 4 * (i4 + 3)) >>> 19) | (CommonFunction.load32(bArr2, 4 * (i4 + 4)) << 13)) & i5;
            iArr[i6 + 6] = ((CommonFunction.load32(bArr2, 4 * (i4 + 4)) >>> 10) | (CommonFunction.load32(bArr2, 4 * (i4 + 5)) << 22)) & i5;
            iArr[i6 + 7] = (CommonFunction.load32(bArr2, 4 * (i4 + 5)) >>> 1) & i5;
            iArr[i6 + 8] = ((CommonFunction.load32(bArr2, 4 * (i4 + 5)) >>> 24) | (CommonFunction.load32(bArr2, 4 * (i4 + 6)) << 8)) & i5;
            iArr[i6 + 9] = ((CommonFunction.load32(bArr2, 4 * (i4 + 6)) >>> 15) | (CommonFunction.load32(bArr2, 4 * (i4 + 7)) << 17)) & i5;
            iArr[i6 + 10] = (CommonFunction.load32(bArr2, 4 * (i4 + 7)) >>> 6) & i5;
            iArr[i6 + 11] = ((CommonFunction.load32(bArr2, 4 * (i4 + 7)) >>> 29) | (CommonFunction.load32(bArr2, 4 * (i4 + 8)) << 3)) & i5;
            iArr[i6 + 12] = ((CommonFunction.load32(bArr2, 4 * (i4 + 8)) >>> 20) | (CommonFunction.load32(bArr2, 4 * (i4 + 9)) << 12)) & i5;
            iArr[i6 + 13] = ((CommonFunction.load32(bArr2, 4 * (i4 + 9)) >>> 11) | (CommonFunction.load32(bArr2, 4 * (i4 + 10)) << 21)) & i5;
            iArr[i6 + 14] = (CommonFunction.load32(bArr2, 4 * (i4 + 10)) >>> 2) & i5;
            iArr[i6 + 15] = ((CommonFunction.load32(bArr2, 4 * (i4 + 10)) >>> 25) | (CommonFunction.load32(bArr2, 4 * (i4 + 11)) << 7)) & i5;
            iArr[i6 + 16] = ((CommonFunction.load32(bArr2, 4 * (i4 + 11)) >>> 16) | (CommonFunction.load32(bArr2, 4 * (i4 + 12)) << 16)) & i5;
            iArr[i6 + 17] = (CommonFunction.load32(bArr2, 4 * (i4 + 12)) >>> 7) & i5;
            iArr[i6 + 18] = ((CommonFunction.load32(bArr2, 4 * (i4 + 12)) >>> 30) | (CommonFunction.load32(bArr2, 4 * (i4 + 13)) << 2)) & i5;
            iArr[i6 + 19] = ((CommonFunction.load32(bArr2, 4 * (i4 + 13)) >>> 21) | (CommonFunction.load32(bArr2, 4 * (i4 + 14)) << 11)) & i5;
            iArr[i6 + 20] = ((CommonFunction.load32(bArr2, 4 * (i4 + 14)) >>> 12) | (CommonFunction.load32(bArr2, 4 * (i4 + 15)) << 20)) & i5;
            iArr[i6 + 21] = (CommonFunction.load32(bArr2, 4 * (i4 + 15)) >>> 3) & i5;
            iArr[i6 + 22] = ((CommonFunction.load32(bArr2, 4 * (i4 + 15)) >>> 26) | (CommonFunction.load32(bArr2, 4 * (i4 + 16)) << 6)) & i5;
            iArr[i6 + 23] = ((CommonFunction.load32(bArr2, 4 * (i4 + 16)) >>> 17) | (CommonFunction.load32(bArr2, 4 * (i4 + 17)) << 15)) & i5;
            iArr[i6 + 24] = (CommonFunction.load32(bArr2, 4 * (i4 + 17)) >>> 8) & i5;
            iArr[i6 + 25] = ((CommonFunction.load32(bArr2, 4 * (i4 + 17)) >>> 31) | (CommonFunction.load32(bArr2, 4 * (i4 + 18)) << 1)) & i5;
            iArr[i6 + 26] = ((CommonFunction.load32(bArr2, 4 * (i4 + 18)) >>> 22) | (CommonFunction.load32(bArr2, 4 * (i4 + 19)) << 10)) & i5;
            iArr[i6 + 27] = ((CommonFunction.load32(bArr2, 4 * (i4 + 19)) >>> 13) | (CommonFunction.load32(bArr2, 4 * (i4 + 20)) << 19)) & i5;
            iArr[i6 + 28] = (CommonFunction.load32(bArr2, 4 * (i4 + 20)) >>> 4) & i5;
            iArr[i6 + 29] = ((CommonFunction.load32(bArr2, 4 * (i4 + 20)) >>> 27) | (CommonFunction.load32(bArr2, 4 * (i4 + 21)) << 5)) & i5;
            iArr[i6 + 30] = ((CommonFunction.load32(bArr2, 4 * (i4 + 21)) >>> 18) | (CommonFunction.load32(bArr2, 4 * (i4 + 22)) << 14)) & i5;
            iArr[i6 + 31] = CommonFunction.load32(bArr2, 4 * (i4 + 22)) >>> 9;
            i4 += i3;
        }
        System.arraycopy(bArr2, (i2 * i3) / 8, bArr, i, 32);
    }

    public static void decodePublicKeyIIISpeed(int[] iArr, byte[] bArr, int i, byte[] bArr2) {
        int i2 = 0;
        for (int i3 = 0; i3 < 1024; i3 += 4) {
            iArr[i3 + 0] = CommonFunction.load32(bArr2, 4 * (i2 + 0)) & 16777215;
            iArr[i3 + 1] = ((CommonFunction.load32(bArr2, 4 * (i2 + 0)) >>> 24) | (CommonFunction.load32(bArr2, 4 * (i2 + 1)) << 8)) & 16777215;
            iArr[i3 + 2] = ((CommonFunction.load32(bArr2, 4 * (i2 + 1)) >>> 16) | (CommonFunction.load32(bArr2, 4 * (i2 + 2)) << 16)) & 16777215;
            iArr[i3 + 3] = CommonFunction.load32(bArr2, 4 * (i2 + 2)) >>> 8;
            i2 += 3;
        }
        System.arraycopy(bArr2, 3072, bArr, i, 32);
    }

    public static void decodePublicKeyIP(int[] iArr, byte[] bArr, int i, byte[] bArr2) {
        int i2 = 0;
        for (int i3 = 0; i3 < 4096; i3 += 32) {
            iArr[i3 + 0] = CommonFunction.load32(bArr2, 4 * (i2 + 0)) & 536870911;
            iArr[i3 + 1] = ((CommonFunction.load32(bArr2, 4 * (i2 + 0)) >>> 29) | (CommonFunction.load32(bArr2, 4 * (i2 + 1)) << 3)) & 536870911;
            iArr[i3 + 2] = ((CommonFunction.load32(bArr2, 4 * (i2 + 1)) >>> 26) | (CommonFunction.load32(bArr2, 4 * (i2 + 2)) << 6)) & 536870911;
            iArr[i3 + 3] = ((CommonFunction.load32(bArr2, 4 * (i2 + 2)) >>> 23) | (CommonFunction.load32(bArr2, 4 * (i2 + 3)) << 9)) & 536870911;
            iArr[i3 + 4] = ((CommonFunction.load32(bArr2, 4 * (i2 + 3)) >>> 20) | (CommonFunction.load32(bArr2, 4 * (i2 + 4)) << 12)) & 536870911;
            iArr[i3 + 5] = ((CommonFunction.load32(bArr2, 4 * (i2 + 4)) >>> 17) | (CommonFunction.load32(bArr2, 4 * (i2 + 5)) << 15)) & 536870911;
            iArr[i3 + 6] = ((CommonFunction.load32(bArr2, 4 * (i2 + 5)) >>> 14) | (CommonFunction.load32(bArr2, 4 * (i2 + 6)) << 18)) & 536870911;
            iArr[i3 + 7] = ((CommonFunction.load32(bArr2, 4 * (i2 + 6)) >>> 11) | (CommonFunction.load32(bArr2, 4 * (i2 + 7)) << 21)) & 536870911;
            iArr[i3 + 8] = ((CommonFunction.load32(bArr2, 4 * (i2 + 7)) >>> 8) | (CommonFunction.load32(bArr2, 4 * (i2 + 8)) << 24)) & 536870911;
            iArr[i3 + 9] = ((CommonFunction.load32(bArr2, 4 * (i2 + 8)) >>> 5) | (CommonFunction.load32(bArr2, 4 * (i2 + 9)) << 27)) & 536870911;
            iArr[i3 + 10] = (CommonFunction.load32(bArr2, 4 * (i2 + 9)) >>> 2) & 536870911;
            iArr[i3 + 11] = ((CommonFunction.load32(bArr2, 4 * (i2 + 9)) >>> 31) | (CommonFunction.load32(bArr2, 4 * (i2 + 10)) << 1)) & 536870911;
            iArr[i3 + 12] = ((CommonFunction.load32(bArr2, 4 * (i2 + 10)) >>> 28) | (CommonFunction.load32(bArr2, 4 * (i2 + 11)) << 4)) & 536870911;
            iArr[i3 + 13] = ((CommonFunction.load32(bArr2, 4 * (i2 + 11)) >>> 25) | (CommonFunction.load32(bArr2, 4 * (i2 + 12)) << 7)) & 536870911;
            iArr[i3 + 14] = ((CommonFunction.load32(bArr2, 4 * (i2 + 12)) >>> 22) | (CommonFunction.load32(bArr2, 4 * (i2 + 13)) << 10)) & 536870911;
            iArr[i3 + 15] = ((CommonFunction.load32(bArr2, 4 * (i2 + 13)) >>> 19) | (CommonFunction.load32(bArr2, 4 * (i2 + 14)) << 13)) & 536870911;
            iArr[i3 + 16] = ((CommonFunction.load32(bArr2, 4 * (i2 + 14)) >>> 16) | (CommonFunction.load32(bArr2, 4 * (i2 + 15)) << 16)) & 536870911;
            iArr[i3 + 17] = ((CommonFunction.load32(bArr2, 4 * (i2 + 15)) >>> 13) | (CommonFunction.load32(bArr2, 4 * (i2 + 16)) << 19)) & 536870911;
            iArr[i3 + 18] = ((CommonFunction.load32(bArr2, 4 * (i2 + 16)) >>> 10) | (CommonFunction.load32(bArr2, 4 * (i2 + 17)) << 22)) & 536870911;
            iArr[i3 + 19] = ((CommonFunction.load32(bArr2, 4 * (i2 + 17)) >>> 7) | (CommonFunction.load32(bArr2, 4 * (i2 + 18)) << 25)) & 536870911;
            iArr[i3 + 20] = ((CommonFunction.load32(bArr2, 4 * (i2 + 18)) >>> 4) | (CommonFunction.load32(bArr2, 4 * (i2 + 19)) << 28)) & 536870911;
            iArr[i3 + 21] = (CommonFunction.load32(bArr2, 4 * (i2 + 19)) >>> 1) & 536870911;
            iArr[i3 + 22] = ((CommonFunction.load32(bArr2, 4 * (i2 + 19)) >>> 30) | (CommonFunction.load32(bArr2, 4 * (i2 + 20)) << 2)) & 536870911;
            iArr[i3 + 23] = ((CommonFunction.load32(bArr2, 4 * (i2 + 20)) >>> 27) | (CommonFunction.load32(bArr2, 4 * (i2 + 21)) << 5)) & 536870911;
            iArr[i3 + 24] = ((CommonFunction.load32(bArr2, 4 * (i2 + 21)) >>> 24) | (CommonFunction.load32(bArr2, 4 * (i2 + 22)) << 8)) & 536870911;
            iArr[i3 + 25] = ((CommonFunction.load32(bArr2, 4 * (i2 + 22)) >>> 21) | (CommonFunction.load32(bArr2, 4 * (i2 + 23)) << 11)) & 536870911;
            iArr[i3 + 26] = ((CommonFunction.load32(bArr2, 4 * (i2 + 23)) >>> 18) | (CommonFunction.load32(bArr2, 4 * (i2 + 24)) << 14)) & 536870911;
            iArr[i3 + 27] = ((CommonFunction.load32(bArr2, 4 * (i2 + 24)) >>> 15) | (CommonFunction.load32(bArr2, 4 * (i2 + 25)) << 17)) & 536870911;
            iArr[i3 + 28] = ((CommonFunction.load32(bArr2, 4 * (i2 + 25)) >>> 12) | (CommonFunction.load32(bArr2, 4 * (i2 + 26)) << 20)) & 536870911;
            iArr[i3 + 29] = ((CommonFunction.load32(bArr2, 4 * (i2 + 26)) >>> 9) | (CommonFunction.load32(bArr2, 4 * (i2 + 27)) << 23)) & 536870911;
            iArr[i3 + 30] = ((CommonFunction.load32(bArr2, 4 * (i2 + 27)) >>> 6) | (CommonFunction.load32(bArr2, 4 * (i2 + 28)) << 26)) & 536870911;
            iArr[i3 + 31] = CommonFunction.load32(bArr2, 4 * (i2 + 28)) >>> 3;
            i2 += 29;
        }
        System.arraycopy(bArr2, 14848, bArr, i, 32);
    }

    public static void decodePublicKeyIIIP(int[] iArr, byte[] bArr, int i, byte[] bArr2) {
        int i2 = 0;
        for (int i3 = 0; i3 < 10240; i3 += 32) {
            iArr[i3] = CommonFunction.load32(bArr2, 4 * i2) & Integer.MAX_VALUE;
            for (int i4 = 1; i4 < 31; i4++) {
                iArr[i3 + i4] = ((CommonFunction.load32(bArr2, 4 * ((i2 + i4) - 1)) >>> (32 - i4)) | (CommonFunction.load32(bArr2, 4 * (i2 + i4)) << i4)) & Integer.MAX_VALUE;
            }
            iArr[i3 + 31] = CommonFunction.load32(bArr2, 4 * ((i2 + 31) - 1)) >>> 1;
            i2 += 31;
        }
        System.arraycopy(bArr2, 39680, bArr, i, 32);
    }

    public static void encodeSignature(byte[] bArr, int i, byte[] bArr2, int i2, int[] iArr, int i3, int i4) {
        int i5 = 0;
        int i6 = 0;
        while (true) {
            int i7 = i6;
            if (i7 >= (i3 * i4) / 32) {
                System.arraycopy(bArr2, i2, bArr, i + ((i3 * i4) / 8), 32);
                return;
            }
            CommonFunction.store32(bArr, i + (4 * (i7 + 0)), (iArr[i5 + 0] & 2097151) | (iArr[i5 + 1] << 21));
            CommonFunction.store32(bArr, i + (4 * (i7 + 1)), ((iArr[i5 + 1] >>> 11) & 1023) | ((iArr[i5 + 2] & 2097151) << 10) | (iArr[i5 + 3] << 31));
            CommonFunction.store32(bArr, i + (4 * (i7 + 2)), ((iArr[i5 + 3] >>> 1) & 1048575) | (iArr[i5 + 4] << 20));
            CommonFunction.store32(bArr, i + (4 * (i7 + 3)), ((iArr[i5 + 4] >>> 12) & 511) | ((iArr[i5 + 5] & 2097151) << 9) | (iArr[i5 + 6] << 30));
            CommonFunction.store32(bArr, i + (4 * (i7 + 4)), ((iArr[i5 + 6] >>> 2) & 524287) | (iArr[i5 + 7] << 19));
            CommonFunction.store32(bArr, i + (4 * (i7 + 5)), ((iArr[i5 + 7] >>> 13) & 255) | ((iArr[i5 + 8] & 2097151) << 8) | (iArr[i5 + 9] << 29));
            CommonFunction.store32(bArr, i + (4 * (i7 + 6)), ((iArr[i5 + 9] >>> 3) & 262143) | (iArr[i5 + 10] << 18));
            CommonFunction.store32(bArr, i + (4 * (i7 + 7)), ((iArr[i5 + 10] >>> 14) & 127) | ((iArr[i5 + 11] & 2097151) << 7) | (iArr[i5 + 12] << 28));
            CommonFunction.store32(bArr, i + (4 * (i7 + 8)), ((iArr[i5 + 12] >>> 4) & 131071) | (iArr[i5 + 13] << 17));
            CommonFunction.store32(bArr, i + (4 * (i7 + 9)), ((iArr[i5 + 13] >>> 15) & 63) | ((iArr[i5 + 14] & 2097151) << 6) | (iArr[i5 + 15] << 27));
            CommonFunction.store32(bArr, i + (4 * (i7 + 10)), ((iArr[i5 + 15] >>> 5) & 65535) | (iArr[i5 + 16] << 16));
            CommonFunction.store32(bArr, i + (4 * (i7 + 11)), ((iArr[i5 + 16] >>> 16) & 31) | ((iArr[i5 + 17] & 2097151) << 5) | (iArr[i5 + 18] << 26));
            CommonFunction.store32(bArr, i + (4 * (i7 + 12)), ((iArr[i5 + 18] >>> 6) & Font.COLOR_NORMAL) | (iArr[i5 + 19] << 15));
            CommonFunction.store32(bArr, i + (4 * (i7 + 13)), ((iArr[i5 + 19] >>> 17) & 15) | ((iArr[i5 + 20] & 2097151) << 4) | (iArr[i5 + 21] << 25));
            CommonFunction.store32(bArr, i + (4 * (i7 + 14)), ((iArr[i5 + 21] >>> 7) & 16383) | (iArr[i5 + 22] << 14));
            CommonFunction.store32(bArr, i + (4 * (i7 + 15)), ((iArr[i5 + 22] >>> 18) & 7) | ((iArr[i5 + 23] & 2097151) << 3) | (iArr[i5 + 24] << 24));
            CommonFunction.store32(bArr, i + (4 * (i7 + 16)), ((iArr[i5 + 24] >>> 8) & 8191) | (iArr[i5 + 25] << 13));
            CommonFunction.store32(bArr, i + (4 * (i7 + 17)), ((iArr[i5 + 25] >>> 19) & 3) | ((iArr[i5 + 26] & 2097151) << 2) | (iArr[i5 + 27] << 23));
            CommonFunction.store32(bArr, i + (4 * (i7 + 18)), ((iArr[i5 + 27] >>> 9) & 4095) | (iArr[i5 + 28] << 12));
            CommonFunction.store32(bArr, i + (4 * (i7 + 19)), ((iArr[i5 + 28] >>> 20) & 1) | ((iArr[i5 + 29] & 2097151) << 1) | (iArr[i5 + 30] << 22));
            CommonFunction.store32(bArr, i + (4 * (i7 + 20)), ((iArr[i5 + 30] >>> 10) & IEEEDouble.BIASED_EXPONENT_SPECIAL_VALUE) | (iArr[i5 + 31] << 11));
            i5 += 32;
            i6 = i7 + i4;
        }
    }

    public static void encodeSignatureIIISpeed(byte[] bArr, int i, byte[] bArr2, int i2, int[] iArr) {
        int i3 = 0;
        for (int i4 = 0; i4 < 704; i4 += 11) {
            CommonFunction.store32(bArr, i + (4 * (i4 + 0)), (iArr[i3 + 0] & 4194303) | (iArr[i3 + 1] << 22));
            CommonFunction.store32(bArr, i + (4 * (i4 + 1)), ((iArr[i3 + 1] >>> 10) & 4095) | (iArr[i3 + 2] << 12));
            CommonFunction.store32(bArr, i + (4 * (i4 + 2)), ((iArr[i3 + 2] >>> 20) & 3) | ((iArr[i3 + 3] & 4194303) << 2) | (iArr[i3 + 4] << 24));
            CommonFunction.store32(bArr, i + (4 * (i4 + 3)), ((iArr[i3 + 4] >>> 8) & 16383) | (iArr[i3 + 5] << 14));
            CommonFunction.store32(bArr, i + (4 * (i4 + 4)), ((iArr[i3 + 5] >>> 18) & 15) | ((iArr[i3 + 6] & 4194303) << 4) | (iArr[i3 + 7] << 26));
            CommonFunction.store32(bArr, i + (4 * (i4 + 5)), ((iArr[i3 + 7] >>> 6) & 65535) | (iArr[i3 + 8] << 16));
            CommonFunction.store32(bArr, i + (4 * (i4 + 6)), ((iArr[i3 + 8] >>> 16) & 63) | ((iArr[i3 + 9] & 4194303) << 6) | (iArr[i3 + 10] << 28));
            CommonFunction.store32(bArr, i + (4 * (i4 + 7)), ((iArr[i3 + 10] >>> 4) & 262143) | (iArr[i3 + 11] << 18));
            CommonFunction.store32(bArr, i + (4 * (i4 + 8)), ((iArr[i3 + 11] >>> 14) & 255) | ((iArr[i3 + 12] & 4194303) << 8) | (iArr[i3 + 13] << 30));
            CommonFunction.store32(bArr, i + (4 * (i4 + 9)), ((iArr[i3 + 13] >>> 2) & 1048575) | (iArr[i3 + 14] << 20));
            CommonFunction.store32(bArr, i + (4 * (i4 + 10)), ((iArr[i3 + 14] >>> 12) & 1023) | (iArr[i3 + 15] << 10));
            i3 += 16;
        }
        System.arraycopy(bArr2, i2, bArr, i + 2816, 32);
    }

    public static void encodeSignatureIP(byte[] bArr, int i, byte[] bArr2, int i2, long[] jArr) {
        int i3 = 0;
        for (int i4 = 0; i4 < 704; i4 += 11) {
            CommonFunction.store32(bArr, i + (4 * (i4 + 0)), (int) ((jArr[i3 + 0] & 4194303) | (jArr[i3 + 1] << 22)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 1)), (int) (((jArr[i3 + 1] >>> 10) & 4095) | (jArr[i3 + 2] << 12)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 2)), (int) (((jArr[i3 + 2] >>> 20) & 3) | ((jArr[i3 + 3] & 4194303) << 2) | (jArr[i3 + 4] << 24)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 3)), (int) (((jArr[i3 + 4] >>> 8) & 16383) | (jArr[i3 + 5] << 14)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 4)), (int) (((jArr[i3 + 5] >>> 18) & 15) | ((jArr[i3 + 6] & 4194303) << 4) | (jArr[i3 + 7] << 26)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 5)), (int) (((jArr[i3 + 7] >>> 6) & 65535) | (jArr[i3 + 8] << 16)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 6)), (int) (((jArr[i3 + 8] >>> 16) & 63) | ((jArr[i3 + 9] & 4194303) << 6) | (jArr[i3 + 10] << 28)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 7)), (int) (((jArr[i3 + 10] >>> 4) & 262143) | (jArr[i3 + 11] << 18)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 8)), (int) (((jArr[i3 + 11] >>> 14) & 255) | ((jArr[i3 + 12] & 4194303) << 8) | (jArr[i3 + 13] << 30)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 9)), (int) (((jArr[i3 + 13] >>> 2) & 1048575) | (jArr[i3 + 14] << 20)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 10)), (int) (((jArr[i3 + 14] >>> 12) & 1023) | (jArr[i3 + 15] << 10)));
            i3 += 16;
        }
        System.arraycopy(bArr2, i2, bArr, i + 2816, 32);
    }

    public static void encodeSignatureIIIP(byte[] bArr, int i, byte[] bArr2, int i2, long[] jArr) {
        int i3 = 0;
        for (int i4 = 0; i4 < 1536; i4 += 3) {
            CommonFunction.store32(bArr, i + (4 * (i4 + 0)), (int) ((jArr[i3 + 0] & 16777215) | (jArr[i3 + 1] << 24)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 1)), (int) (((jArr[i3 + 1] >>> 8) & 65535) | (jArr[i3 + 2] << 16)));
            CommonFunction.store32(bArr, i + (4 * (i4 + 2)), (int) (((jArr[i3 + 2] >>> 16) & 255) | (jArr[i3 + 3] << 8)));
            i3 += 4;
        }
        System.arraycopy(bArr2, i2, bArr, i + 6144, 32);
    }

    public static void decodeSignature(byte[] bArr, int[] iArr, byte[] bArr2, int i, int i2, int i3) {
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5 += 32) {
            iArr[i5 + 0] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 0))) << 11) >> 11;
            iArr[i5 + 1] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 0))) >>> 21) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 1))) << 22) >> 11);
            iArr[i5 + 2] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 1))) << 1) >> 11;
            iArr[i5 + 3] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 1))) >>> 31) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 2))) << 12) >> 11);
            iArr[i5 + 4] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 2))) >>> 20) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 3))) << 23) >> 11);
            iArr[i5 + 5] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 3))) << 2) >> 11;
            iArr[i5 + 6] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 3))) >>> 30) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 4))) << 13) >> 11);
            iArr[i5 + 7] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 4))) >>> 19) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 5))) << 24) >> 11);
            iArr[i5 + 8] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 5))) << 3) >> 11;
            iArr[i5 + 9] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 5))) >>> 29) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 6))) << 14) >> 11);
            iArr[i5 + 10] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 6))) >>> 18) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 7))) << 25) >> 11);
            iArr[i5 + 11] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 7))) << 4) >> 11;
            iArr[i5 + 12] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 7))) >>> 28) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 8))) << 15) >> 11);
            iArr[i5 + 13] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 8))) >>> 17) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 9))) << 26) >> 11);
            iArr[i5 + 14] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 9))) << 5) >> 11;
            iArr[i5 + 15] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 9))) >>> 27) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 10))) << 16) >> 11);
            iArr[i5 + 16] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 10))) >>> 16) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 11))) << 27) >> 11);
            iArr[i5 + 17] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 11))) << 6) >> 11;
            iArr[i5 + 18] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 11))) >>> 26) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 12))) << 17) >> 11);
            iArr[i5 + 19] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 12))) >>> 15) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 13))) << 28) >> 11);
            iArr[i5 + 20] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 13))) << 7) >> 11;
            iArr[i5 + 21] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 13))) >>> 25) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 14))) << 18) >> 11);
            iArr[i5 + 22] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 14))) >>> 14) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 15))) << 29) >> 11);
            iArr[i5 + 23] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 15))) << 8) >> 11;
            iArr[i5 + 24] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 15))) >>> 24) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 16))) << 19) >> 11);
            iArr[i5 + 25] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 16))) >>> 13) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 17))) << 30) >> 11);
            iArr[i5 + 26] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 17))) << 9) >> 11;
            iArr[i5 + 27] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 17))) >>> 23) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 18))) << 20) >> 11);
            iArr[i5 + 28] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 18))) >>> 12) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 19))) << 31) >> 11);
            iArr[i5 + 29] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 19))) << 10) >> 11;
            iArr[i5 + 30] = (CommonFunction.load32(bArr2, i + (4 * (i4 + 19))) >>> 22) | ((CommonFunction.load32(bArr2, i + (4 * (i4 + 20))) << 21) >> 11);
            iArr[i5 + 31] = CommonFunction.load32(bArr2, i + (4 * (i4 + 20))) >> 11;
            i4 += i3;
        }
        System.arraycopy(bArr2, i + ((i2 * i3) / 8), bArr, 0, 32);
    }

    public static void decodeSignatureIIISpeed(byte[] bArr, int[] iArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 1024; i3 += 16) {
            iArr[i3 + 0] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) << 10) >> 10;
            iArr[i3 + 1] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) >>> 22) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) << 20) >> 10);
            iArr[i3 + 2] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) >>> 12) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) << 30) >> 10);
            iArr[i3 + 3] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) << 8) >> 10;
            iArr[i3 + 4] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) >>> 24) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 3))) << 18) >> 10);
            iArr[i3 + 5] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 3))) >>> 14) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) << 28) >> 10);
            iArr[i3 + 6] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) << 6) >> 10;
            iArr[i3 + 7] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) >>> 26) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 5))) << 16) >> 10);
            iArr[i3 + 8] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 5))) >>> 16) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) << 26) >> 10);
            iArr[i3 + 9] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) << 4) >> 10;
            iArr[i3 + 10] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) >>> 28) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 7))) << 14) >> 10);
            iArr[i3 + 11] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 7))) >>> 18) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) << 24) >> 10);
            iArr[i3 + 12] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) << 2) >> 10;
            iArr[i3 + 13] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) >>> 30) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 9))) << 12) >> 10);
            iArr[i3 + 14] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 9))) >>> 20) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 10))) << 22) >> 10);
            iArr[i3 + 15] = CommonFunction.load32(bArr2, i + (4 * (i2 + 10))) >> 10;
            i2 += 11;
        }
        System.arraycopy(bArr2, i + 2816, bArr, 0, 32);
    }

    public static void decodeSignatureIP(byte[] bArr, long[] jArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 1024; i3 += 16) {
            jArr[i3 + 0] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) << 10) >> 10;
            jArr[i3 + 1] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) >>> 22) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) << 20) >> 10);
            jArr[i3 + 2] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) >>> 12) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) << 30) >> 10);
            jArr[i3 + 3] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) << 8) >> 10;
            jArr[i3 + 4] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) >>> 24) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 3))) << 18) >> 10);
            jArr[i3 + 5] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 3))) >>> 14) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) << 28) >> 10);
            jArr[i3 + 6] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) << 6) >> 10;
            jArr[i3 + 7] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 4))) >>> 26) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 5))) << 16) >> 10);
            jArr[i3 + 8] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 5))) >>> 16) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) << 26) >> 10);
            jArr[i3 + 9] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) << 4) >> 10;
            jArr[i3 + 10] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 6))) >>> 28) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 7))) << 14) >> 10);
            jArr[i3 + 11] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 7))) >>> 18) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) << 24) >> 10);
            jArr[i3 + 12] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) << 2) >> 10;
            jArr[i3 + 13] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 8))) >>> 30) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 9))) << 12) >> 10);
            jArr[i3 + 14] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 9))) >>> 20) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 10))) << 22) >> 10);
            jArr[i3 + 15] = CommonFunction.load32(bArr2, i + (4 * (i2 + 10))) >> 10;
            i2 += 11;
        }
        System.arraycopy(bArr2, i + 2816, bArr, 0, 32);
    }

    public static void decodeSignatureIIIP(byte[] bArr, long[] jArr, byte[] bArr2, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < 2048; i3 += 4) {
            jArr[i3 + 0] = (CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) << 8) >> 8;
            jArr[i3 + 1] = ((CommonFunction.load32(bArr2, i + (4 * (i2 + 0))) >>> 24) & 255) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) << 16) >> 8);
            jArr[i3 + 2] = ((CommonFunction.load32(bArr2, i + (4 * (i2 + 1))) >>> 16) & 65535) | ((CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) << 24) >> 8);
            jArr[i3 + 3] = CommonFunction.load32(bArr2, i + (4 * (i2 + 2))) >> 8;
            i2 += 3;
        }
        System.arraycopy(bArr2, i + 6144, bArr, 0, 32);
    }
}

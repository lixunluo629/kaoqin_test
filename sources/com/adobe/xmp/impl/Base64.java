package com.adobe.xmp.impl;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/Base64.class */
public class Base64 {
    private static final byte INVALID = -1;
    private static final byte WHITESPACE = -2;
    private static final byte EQUAL = -3;
    private static byte[] base64 = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static byte[] ascii = new byte[255];

    public static final byte[] encode(byte[] bArr) {
        return encode(bArr, 0);
    }

    public static final byte[] encode(byte[] bArr, int i) {
        int i2 = (i / 4) * 4;
        if (i2 < 0) {
            i2 = 0;
        }
        int length = ((bArr.length + 2) / 3) * 4;
        if (i2 > 0) {
            length += (length - 1) / i2;
        }
        byte[] bArr2 = new byte[length];
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i4 + 3 <= bArr.length) {
            int i6 = i4;
            int i7 = i4 + 1;
            int i8 = i7 + 1;
            int i9 = ((bArr[i6] & 255) << 16) | ((bArr[i7] & 255) << 8);
            i4 = i8 + 1;
            int i10 = i9 | ((bArr[i8] & 255) << 0);
            int i11 = i3;
            int i12 = i3 + 1;
            bArr2[i11] = base64[(i10 & 16515072) >> 18];
            int i13 = i12 + 1;
            bArr2[i12] = base64[(i10 & 258048) >> 12];
            int i14 = i13 + 1;
            bArr2[i13] = base64[(i10 & 4032) >> 6];
            i3 = i14 + 1;
            bArr2[i14] = base64[i10 & 63];
            i5 += 4;
            if (i3 < length && i2 > 0 && i5 % i2 == 0) {
                i3++;
                bArr2[i3] = 10;
            }
        }
        if (bArr.length - i4 == 2) {
            int i15 = ((bArr[i4] & 255) << 16) | ((bArr[i4 + 1] & 255) << 8);
            int i16 = i3;
            int i17 = i3 + 1;
            bArr2[i16] = base64[(i15 & 16515072) >> 18];
            int i18 = i17 + 1;
            bArr2[i17] = base64[(i15 & 258048) >> 12];
            int i19 = i18 + 1;
            bArr2[i18] = base64[(i15 & 4032) >> 6];
            int i20 = i19 + 1;
            bArr2[i19] = 61;
        } else if (bArr.length - i4 == 1) {
            int i21 = (bArr[i4] & 255) << 16;
            int i22 = i3;
            int i23 = i3 + 1;
            bArr2[i22] = base64[(i21 & 16515072) >> 18];
            int i24 = i23 + 1;
            bArr2[i23] = base64[(i21 & 258048) >> 12];
            int i25 = i24 + 1;
            bArr2[i24] = 61;
            int i26 = i25 + 1;
            bArr2[i25] = 61;
        }
        return bArr2;
    }

    public static final String encode(String str) {
        return new String(encode(str.getBytes()));
    }

    public static final byte[] decode(byte[] bArr) throws IllegalArgumentException {
        int i = 0;
        for (byte b : bArr) {
            byte b2 = ascii[b];
            if (b2 >= 0) {
                int i2 = i;
                i++;
                bArr[i2] = b2;
            } else if (b2 == -1) {
                throw new IllegalArgumentException("Invalid base 64 string");
            }
        }
        while (i > 0 && bArr[i - 1] == -3) {
            i--;
        }
        byte[] bArr2 = new byte[(i * 3) / 4];
        int i3 = 0;
        int i4 = 0;
        while (i4 < bArr2.length - 2) {
            bArr2[i4] = (byte) (((bArr[i3] << 2) & 255) | ((bArr[i3 + 1] >>> 4) & 3));
            bArr2[i4 + 1] = (byte) (((bArr[i3 + 1] << 4) & 255) | ((bArr[i3 + 2] >>> 2) & 15));
            bArr2[i4 + 2] = (byte) (((bArr[i3 + 2] << 6) & 255) | (bArr[i3 + 3] & 63));
            i3 += 4;
            i4 += 3;
        }
        if (i4 < bArr2.length) {
            bArr2[i4] = (byte) (((bArr[i3] << 2) & 255) | ((bArr[i3 + 1] >>> 4) & 3));
        }
        int i5 = i4 + 1;
        if (i5 < bArr2.length) {
            bArr2[i5] = (byte) (((bArr[i3 + 1] << 4) & 255) | ((bArr[i3 + 2] >>> 2) & 15));
        }
        return bArr2;
    }

    public static final String decode(String str) {
        return new String(decode(str.getBytes()));
    }

    static {
        for (int i = 0; i < 255; i++) {
            ascii[i] = -1;
        }
        for (int i2 = 0; i2 < base64.length; i2++) {
            ascii[base64[i2]] = (byte) i2;
        }
        ascii[9] = -2;
        ascii[10] = -2;
        ascii[13] = -2;
        ascii[32] = -2;
        ascii[61] = -3;
    }
}

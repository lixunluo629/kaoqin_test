package org.apache.xmlbeans.impl.util;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/util/Base64.class */
public final class Base64 {
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final byte PAD = 61;
    private static final boolean fDebug = false;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    static {
        for (int i = 0; i < 255; i++) {
            base64Alphabet[i] = -1;
        }
        for (int i2 = 90; i2 >= 65; i2--) {
            base64Alphabet[i2] = (byte) (i2 - 65);
        }
        for (int i3 = 122; i3 >= 97; i3--) {
            base64Alphabet[i3] = (byte) ((i3 - 97) + 26);
        }
        for (int i4 = 57; i4 >= 48; i4--) {
            base64Alphabet[i4] = (byte) ((i4 - 48) + 52);
        }
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (int i5 = 0; i5 <= 25; i5++) {
            lookUpBase64Alphabet[i5] = (byte) (65 + i5);
        }
        int i6 = 26;
        int j = 0;
        while (i6 <= 51) {
            lookUpBase64Alphabet[i6] = (byte) (97 + j);
            i6++;
            j++;
        }
        int i7 = 52;
        int j2 = 0;
        while (i7 <= 61) {
            lookUpBase64Alphabet[i7] = (byte) (48 + j2);
            i7++;
            j2++;
        }
        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }

    protected static boolean isWhiteSpace(byte octect) {
        return octect == 32 || octect == 13 || octect == 10 || octect == 9;
    }

    protected static boolean isPad(byte octect) {
        return octect == 61;
    }

    protected static boolean isData(byte octect) {
        return base64Alphabet[octect] != -1;
    }

    protected static boolean isBase64(byte octect) {
        return isWhiteSpace(octect) || isPad(octect) || isData(octect);
    }

    public static byte[] encode(byte[] binaryData) {
        byte[] encodedData;
        if (binaryData == null) {
            return null;
        }
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        if (fewerThan24bits != 0) {
            encodedData = new byte[(numberTriplets + 1) * 4];
        } else {
            encodedData = new byte[numberTriplets * 4];
        }
        int i = 0;
        while (i < numberTriplets) {
            int dataIndex = i * 3;
            byte b1 = binaryData[dataIndex];
            byte b2 = binaryData[dataIndex + 1];
            byte b3 = binaryData[dataIndex + 2];
            byte l = (byte) (b2 & 15);
            byte k = (byte) (b1 & 3);
            int encodedIndex = i * 4;
            byte val1 = (b1 & Byte.MIN_VALUE) == 0 ? (byte) (b1 >> 2) : (byte) ((b1 >> 2) ^ 192);
            byte val2 = (b2 & Byte.MIN_VALUE) == 0 ? (byte) (b2 >> 4) : (byte) ((b2 >> 4) ^ 240);
            byte val3 = (byte) ((b3 & Byte.MIN_VALUE) == 0 ? b3 >> 6 : (b3 >> 6) ^ 252);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            i++;
        }
        int dataIndex2 = i * 3;
        int encodedIndex2 = i * 4;
        if (fewerThan24bits == 8) {
            byte b12 = binaryData[dataIndex2];
            byte k2 = (byte) (b12 & 3);
            byte val12 = (b12 & Byte.MIN_VALUE) == 0 ? (byte) (b12 >> 2) : (byte) ((b12 >> 2) ^ 192);
            encodedData[encodedIndex2] = lookUpBase64Alphabet[val12];
            encodedData[encodedIndex2 + 1] = lookUpBase64Alphabet[k2 << 4];
            encodedData[encodedIndex2 + 2] = 61;
            encodedData[encodedIndex2 + 3] = 61;
        } else if (fewerThan24bits == 16) {
            byte b13 = binaryData[dataIndex2];
            byte b22 = binaryData[dataIndex2 + 1];
            byte l2 = (byte) (b22 & 15);
            byte k3 = (byte) (b13 & 3);
            byte val13 = (b13 & Byte.MIN_VALUE) == 0 ? (byte) (b13 >> 2) : (byte) ((b13 >> 2) ^ 192);
            byte val22 = (b22 & Byte.MIN_VALUE) == 0 ? (byte) (b22 >> 4) : (byte) ((b22 >> 4) ^ 240);
            encodedData[encodedIndex2] = lookUpBase64Alphabet[val13];
            encodedData[encodedIndex2 + 1] = lookUpBase64Alphabet[val22 | (k3 << 4)];
            encodedData[encodedIndex2 + 2] = lookUpBase64Alphabet[l2 << 2];
            encodedData[encodedIndex2 + 3] = 61;
        }
        return encodedData;
    }

    public static byte[] decode(byte[] base64Data) {
        if (base64Data == null) {
            return null;
        }
        byte[] base64Data2 = removeWhiteSpace(base64Data);
        if (base64Data2.length % 4 != 0) {
            return null;
        }
        int numberQuadruple = base64Data2.length / 4;
        if (numberQuadruple == 0) {
            return new byte[0];
        }
        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        byte[] decodedData = new byte[numberQuadruple * 3];
        while (i < numberQuadruple - 1) {
            int i2 = dataIndex;
            int dataIndex2 = dataIndex + 1;
            byte d1 = base64Data2[i2];
            if (!isData(d1)) {
                return null;
            }
            int dataIndex3 = dataIndex2 + 1;
            byte d2 = base64Data2[dataIndex2];
            if (!isData(d2)) {
                return null;
            }
            int dataIndex4 = dataIndex3 + 1;
            byte d3 = base64Data2[dataIndex3];
            if (!isData(d3)) {
                return null;
            }
            dataIndex = dataIndex4 + 1;
            byte d4 = base64Data2[dataIndex4];
            if (!isData(d4)) {
                return null;
            }
            byte b1 = base64Alphabet[d1];
            byte b2 = base64Alphabet[d2];
            byte b3 = base64Alphabet[d3];
            byte b4 = base64Alphabet[d4];
            int i3 = encodedIndex;
            int encodedIndex2 = encodedIndex + 1;
            decodedData[i3] = (byte) ((b1 << 2) | (b2 >> 4));
            int encodedIndex3 = encodedIndex2 + 1;
            decodedData[encodedIndex2] = (byte) (((b2 & 15) << 4) | ((b3 >> 2) & 15));
            encodedIndex = encodedIndex3 + 1;
            decodedData[encodedIndex3] = (byte) ((b3 << 6) | b4);
            i++;
        }
        int i4 = dataIndex;
        int dataIndex5 = dataIndex + 1;
        byte d12 = base64Data2[i4];
        if (!isData(d12)) {
            return null;
        }
        int dataIndex6 = dataIndex5 + 1;
        byte d22 = base64Data2[dataIndex5];
        if (!isData(d22)) {
            return null;
        }
        byte b12 = base64Alphabet[d12];
        byte b22 = base64Alphabet[d22];
        int dataIndex7 = dataIndex6 + 1;
        byte d32 = base64Data2[dataIndex6];
        int i5 = dataIndex7 + 1;
        byte d42 = base64Data2[dataIndex7];
        if (!isData(d32) || !isData(d42)) {
            if (isPad(d32) && isPad(d42)) {
                if ((b22 & 15) != 0) {
                    return null;
                }
                byte[] tmp = new byte[(i * 3) + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) ((b12 << 2) | (b22 >> 4));
                return tmp;
            }
            if (!isPad(d32) && isPad(d42)) {
                byte b32 = base64Alphabet[d32];
                if ((b32 & 3) != 0) {
                    return null;
                }
                byte[] tmp2 = new byte[(i * 3) + 2];
                System.arraycopy(decodedData, 0, tmp2, 0, i * 3);
                tmp2[encodedIndex] = (byte) ((b12 << 2) | (b22 >> 4));
                tmp2[encodedIndex + 1] = (byte) (((b22 & 15) << 4) | ((b32 >> 2) & 15));
                return tmp2;
            }
            return null;
        }
        byte b33 = base64Alphabet[d32];
        byte b42 = base64Alphabet[d42];
        int i6 = encodedIndex;
        int encodedIndex4 = encodedIndex + 1;
        decodedData[i6] = (byte) ((b12 << 2) | (b22 >> 4));
        int encodedIndex5 = encodedIndex4 + 1;
        decodedData[encodedIndex4] = (byte) (((b22 & 15) << 4) | ((b33 >> 2) & 15));
        int i7 = encodedIndex5 + 1;
        decodedData[encodedIndex5] = (byte) ((b33 << 6) | b42);
        return decodedData;
    }

    protected static byte[] removeWhiteSpace(byte[] data) {
        if (data == null) {
            return null;
        }
        int newSize = 0;
        int len = data.length;
        for (byte b : data) {
            if (!isWhiteSpace(b)) {
                newSize++;
            }
        }
        if (newSize == len) {
            return data;
        }
        byte[] newArray = new byte[newSize];
        int j = 0;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                int i2 = j;
                j++;
                newArray[i2] = data[i];
            }
        }
        return newArray;
    }
}

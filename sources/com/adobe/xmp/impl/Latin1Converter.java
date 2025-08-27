package com.adobe.xmp.impl;

import java.io.UnsupportedEncodingException;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/Latin1Converter.class */
public class Latin1Converter {
    private static final int STATE_START = 0;
    private static final int STATE_UTF8CHAR = 11;

    private Latin1Converter() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static ByteBuffer convert(ByteBuffer byteBuffer) {
        if (!"UTF-8".equals(byteBuffer.getEncoding())) {
            return byteBuffer;
        }
        byte[] bArr = new byte[8];
        int i = 0;
        int i2 = 0;
        ByteBuffer byteBuffer2 = new ByteBuffer((byteBuffer.length() * 4) / 3);
        boolean z = false;
        int i3 = 0;
        while (i3 < byteBuffer.length()) {
            int iCharAt = byteBuffer.charAt(i3);
            switch (z) {
                case false:
                default:
                    if (iCharAt < 127) {
                        byteBuffer2.append((byte) iCharAt);
                        break;
                    } else if (iCharAt >= 192) {
                        i2 = -1;
                        int i4 = iCharAt;
                        while (true) {
                            int i5 = i4;
                            if (i2 < 8 && (i5 & 128) == 128) {
                                i2++;
                                i4 = i5 << 1;
                            }
                        }
                        int i6 = i;
                        i++;
                        bArr[i6] = (byte) iCharAt;
                        z = 11;
                        break;
                    } else {
                        byteBuffer2.append(convertToUTF8((byte) iCharAt));
                        break;
                    }
                case true:
                    if (i2 <= 0 || (iCharAt & 192) != 128) {
                        byteBuffer2.append(convertToUTF8(bArr[0]));
                        i3 -= i;
                        i = 0;
                        z = false;
                        break;
                    } else {
                        int i7 = i;
                        i++;
                        bArr[i7] = (byte) iCharAt;
                        i2--;
                        if (i2 == 0) {
                            byteBuffer2.append(bArr, 0, i);
                            i = 0;
                            z = false;
                            break;
                        } else {
                            break;
                        }
                    }
                    break;
            }
            i3++;
        }
        if (z == 11) {
            for (int i8 = 0; i8 < i; i8++) {
                byteBuffer2.append(convertToUTF8(bArr[i8]));
            }
        }
        return byteBuffer2;
    }

    private static byte[] convertToUTF8(byte b) {
        int i = b & 255;
        if (i >= 128) {
            try {
                return (i == 129 || i == 141 || i == 143 || i == 144 || i == 157) ? new byte[]{32} : new String(new byte[]{b}, "cp1252").getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return new byte[]{b};
    }
}

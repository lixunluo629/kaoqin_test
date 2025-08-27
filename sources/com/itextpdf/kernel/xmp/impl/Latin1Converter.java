package com.itextpdf.kernel.xmp.impl;

import java.io.UnsupportedEncodingException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/Latin1Converter.class */
public class Latin1Converter {
    private static final int STATE_START = 0;
    private static final int STATE_UTF8CHAR = 11;

    private Latin1Converter() {
    }

    public static ByteBuffer convert(ByteBuffer buffer) {
        if ("UTF-8".equals(buffer.getEncoding())) {
            byte[] readAheadBuffer = new byte[8];
            int readAhead = 0;
            int expectedBytes = 0;
            ByteBuffer out = new ByteBuffer((buffer.length() * 4) / 3);
            int state = 0;
            int i = 0;
            while (i < buffer.length()) {
                int b = buffer.charAt(i);
                switch (state) {
                    case 0:
                    default:
                        if (b < 127) {
                            out.append((byte) b);
                            break;
                        } else if (b >= 192) {
                            expectedBytes = -1;
                            int i2 = b;
                            while (true) {
                                int test = i2;
                                if (expectedBytes < 8 && (test & 128) == 128) {
                                    expectedBytes++;
                                    i2 = test << 1;
                                }
                            }
                            int i3 = readAhead;
                            readAhead++;
                            readAheadBuffer[i3] = (byte) b;
                            state = 11;
                            break;
                        } else {
                            byte[] utf8 = convertToUTF8((byte) b);
                            out.append(utf8);
                            break;
                        }
                    case 11:
                        if (expectedBytes > 0 && (b & 192) == 128) {
                            int i4 = readAhead;
                            readAhead++;
                            readAheadBuffer[i4] = (byte) b;
                            expectedBytes--;
                            if (expectedBytes == 0) {
                                out.append(readAheadBuffer, 0, readAhead);
                                readAhead = 0;
                                state = 0;
                                break;
                            } else {
                                break;
                            }
                        } else {
                            byte[] utf82 = convertToUTF8(readAheadBuffer[0]);
                            out.append(utf82);
                            i -= readAhead;
                            readAhead = 0;
                            state = 0;
                            break;
                        }
                        break;
                }
                i++;
            }
            if (state == 11) {
                for (int j = 0; j < readAhead; j++) {
                    byte[] utf83 = convertToUTF8(readAheadBuffer[j]);
                    out.append(utf83);
                }
            }
            return out;
        }
        return buffer;
    }

    private static byte[] convertToUTF8(byte ch2) {
        int c = ch2 & 255;
        if (c >= 128) {
            try {
                if (c == 129 || c == 141 || c == 143 || c == 144 || c == 157) {
                    return new byte[]{32};
                }
                return new String(new byte[]{ch2}, "cp1252").getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return new byte[]{ch2};
    }
}

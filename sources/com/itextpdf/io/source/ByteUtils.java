package com.itextpdf.io.source;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.DecimalFormatUtil;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ByteUtils.class */
public final class ByteUtils {
    static boolean HighPrecision = false;
    private static final byte[] bytes = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final byte[] zero = {48};
    private static final byte[] one = {49};
    private static final byte[] negOne = {45, 49};

    public static byte[] getIsoBytes(String text) {
        if (text == null) {
            return null;
        }
        int len = text.length();
        byte[] b = new byte[len];
        for (int k = 0; k < len; k++) {
            b[k] = (byte) text.charAt(k);
        }
        return b;
    }

    public static byte[] getIsoBytes(byte pre, String text) {
        return getIsoBytes(pre, text, (byte) 0);
    }

    public static byte[] getIsoBytes(byte pre, String text, byte post) {
        if (text == null) {
            return null;
        }
        int len = text.length();
        int start = 0;
        if (pre != 0) {
            len++;
            start = 1;
        }
        if (post != 0) {
            len++;
        }
        byte[] b = new byte[len];
        if (pre != 0) {
            b[0] = pre;
        }
        if (post != 0) {
            b[len - 1] = post;
        }
        for (int k = 0; k < text.length(); k++) {
            b[k + start] = (byte) text.charAt(k);
        }
        return b;
    }

    public static byte[] getIsoBytes(int n) {
        return getIsoBytes(n, (ByteBuffer) null);
    }

    public static byte[] getIsoBytes(double d) {
        return getIsoBytes(d, (ByteBuffer) null);
    }

    static byte[] getIsoBytes(int n, ByteBuffer buffer) {
        boolean negative = false;
        if (n < 0) {
            negative = true;
            n = -n;
        }
        int intLen = intSize(n);
        ByteBuffer buf = buffer == null ? new ByteBuffer(intLen + (negative ? 1 : 0)) : buffer;
        for (int i = 0; i < intLen; i++) {
            buf.prepend(bytes[n % 10]);
            n /= 10;
        }
        if (negative) {
            buf.prepend((byte) 45);
        }
        if (buffer == null) {
            return buf.getInternalBuffer();
        }
        return null;
    }

    static byte[] getIsoBytes(double d, ByteBuffer buffer) {
        return getIsoBytes(d, buffer, HighPrecision);
    }

    static byte[] getIsoBytes(double d, ByteBuffer buffer, boolean highPrecision) {
        long v;
        ByteBuffer buf;
        int intLen;
        byte[] result;
        if (highPrecision) {
            if (Math.abs(d) < 1.0E-6d) {
                if (buffer != null) {
                    buffer.prepend(zero);
                    return null;
                }
                return zero;
            }
            if (Double.isNaN(d)) {
                Logger logger = LoggerFactory.getLogger((Class<?>) ByteUtils.class);
                logger.error(LogMessageConstant.ATTEMPT_PROCESS_NAN);
                d = 0.0d;
            }
            byte[] result2 = DecimalFormatUtil.formatNumber(d, "0.######").getBytes(StandardCharsets.ISO_8859_1);
            if (buffer != null) {
                buffer.prepend(result2);
                return null;
            }
            return result2;
        }
        boolean negative = false;
        if (Math.abs(d) < 1.5E-5d) {
            if (buffer != null) {
                buffer.prepend(zero);
                return null;
            }
            return zero;
        }
        if (d < 0.0d) {
            negative = true;
            d = -d;
        }
        if (d < 1.0d) {
            double d2 = d + 5.0E-6d;
            if (d2 >= 1.0d) {
                if (negative) {
                    result = negOne;
                } else {
                    result = one;
                }
                if (buffer != null) {
                    buffer.prepend(result);
                    return null;
                }
                return result;
            }
            int v2 = (int) (d2 * 100000.0d);
            int len = 5;
            while (len > 0 && v2 % 10 == 0) {
                v2 /= 10;
                len--;
            }
            buf = buffer != null ? buffer : new ByteBuffer(negative ? len + 3 : len + 2);
            for (int i = 0; i < len; i++) {
                buf.prepend(bytes[v2 % 10]);
                v2 /= 10;
            }
            buf.prepend((byte) 46).prepend((byte) 48);
            if (negative) {
                buf.prepend((byte) 45);
            }
        } else if (d <= 32767.0d) {
            int v3 = (int) ((d + 0.005d) * 100.0d);
            if (v3 >= 1000000) {
                intLen = 5;
            } else if (v3 >= 100000) {
                intLen = 4;
            } else if (v3 >= 10000) {
                intLen = 3;
            } else if (v3 >= 1000) {
                intLen = 2;
            } else {
                intLen = 1;
            }
            int fracLen = 0;
            if (v3 % 100 != 0) {
                fracLen = 2;
                if (v3 % 10 != 0) {
                    fracLen = 2 + 1;
                } else {
                    v3 /= 10;
                }
            } else {
                v3 /= 100;
            }
            buf = buffer != null ? buffer : new ByteBuffer(intLen + fracLen + (negative ? 1 : 0));
            for (int i2 = 0; i2 < fracLen - 1; i2++) {
                buf.prepend(bytes[v3 % 10]);
                v3 /= 10;
            }
            if (fracLen > 0) {
                buf.prepend((byte) 46);
            }
            for (int i3 = 0; i3 < intLen; i3++) {
                buf.prepend(bytes[v3 % 10]);
                v3 /= 10;
            }
            if (negative) {
                buf.prepend((byte) 45);
            }
        } else {
            double d3 = d + 0.5d;
            if (d3 > 9.223372036854776E18d) {
                v = Long.MAX_VALUE;
            } else {
                if (Double.isNaN(d3)) {
                    Logger logger2 = LoggerFactory.getLogger((Class<?>) ByteUtils.class);
                    logger2.error(LogMessageConstant.ATTEMPT_PROCESS_NAN);
                    d3 = 0.0d;
                }
                v = (long) d3;
            }
            int intLen2 = longSize(v);
            buf = buffer == null ? new ByteBuffer(intLen2 + (negative ? 1 : 0)) : buffer;
            for (int i4 = 0; i4 < intLen2; i4++) {
                buf.prepend(bytes[(int) (v % 10)]);
                v /= 10;
            }
            if (negative) {
                buf.prepend((byte) 45);
            }
        }
        if (buffer == null) {
            return buf.getInternalBuffer();
        }
        return null;
    }

    private static int longSize(long l) {
        long m = 10;
        for (int i = 1; i < 19; i++) {
            if (l < m) {
                return i;
            }
            m *= 10;
        }
        return 19;
    }

    private static int intSize(int l) {
        long m = 10;
        for (int i = 1; i < 10; i++) {
            if (l < m) {
                return i;
            }
            m *= 10;
        }
        return 10;
    }
}

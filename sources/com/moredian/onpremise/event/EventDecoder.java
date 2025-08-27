package com.moredian.onpremise.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/event/EventDecoder.class */
public class EventDecoder {
    public static int bits2int(int a_num, int a_right_shift_num, int a_bit_num) {
        if (a_bit_num > 32) {
            throw new NumberFormatException("The number of bits[" + a_bit_num + "] should be no more than 32");
        }
        if (a_bit_num == 32) {
            return a_num >>> a_right_shift_num;
        }
        return (a_num >>> a_right_shift_num) & ((1 << a_bit_num) - 1);
    }

    public static int bytes2int(byte[] a_bytes, int a_offset, int a_offset_len) {
        if (a_offset_len > 4) {
            throw new NumberFormatException("Offset length[" + a_offset_len + "] should be no more than 4.");
        }
        if (a_offset + a_offset_len > a_bytes.length) {
            throw new NumberFormatException("The sum of offset[" + a_offset + "] and offset length[" + a_offset_len + "] should be no more than the length of given bytes.");
        }
        int result = 0;
        for (int i = 0; i < a_offset_len; i++) {
            result += (a_bytes[((a_offset + a_offset_len) - 1) - i] & 255) << (i * 8);
        }
        return result;
    }

    public static int bytes2long(byte[] a_bytes, int a_offset, int a_offset_len) {
        if (a_offset_len > 8) {
            throw new NumberFormatException("Offset length[" + a_offset_len + "] should be no more than 8.");
        }
        if (a_offset + a_offset_len > a_bytes.length) {
            throw new NumberFormatException("The sum of offset[" + a_offset + "] and offset length[" + a_offset_len + "] should be no more than the length of given bytes.");
        }
        int result = 0;
        for (int i = 0; i < a_offset_len; i++) {
            result += (a_bytes[((a_offset + a_offset_len) - 1) - i] & 255) << (i * 8);
        }
        return result;
    }

    public static int bytes2int(byte[] a_bytes) {
        return bytes2int(a_bytes, 0, a_bytes.length);
    }

    public static int bytes2long(byte[] a_bytes) {
        return bytes2long(a_bytes, 0, a_bytes.length);
    }

    public static int bytes2int(ByteBuffer a_buf, int a_len) {
        byte[] b_array = new byte[a_len];
        a_buf.get(b_array);
        return bytes2int(b_array);
    }

    public static int bytes2long(ByteBuffer a_buf, int a_len) {
        byte[] b_array = new byte[a_len];
        a_buf.get(b_array);
        return bytes2long(b_array);
    }

    public static String bytes2hex(byte[] bytes, boolean enableHexPrefix) {
        StringBuffer hexBuf = new StringBuffer();
        if (enableHexPrefix) {
            hexBuf.append("0X");
            for (byte b : bytes) {
                if ((b & 255) < 16) {
                    hexBuf.append("0");
                }
                hexBuf.append(Long.toString(b & 255, 16).toUpperCase());
            }
        } else {
            for (byte b2 : bytes) {
                if ((b2 & 255) < 16) {
                    hexBuf.append("0");
                }
                hexBuf.append(Long.toString(b2 & 255, 16).toUpperCase());
            }
        }
        return hexBuf.toString();
    }

    public static String bytes2hex(byte[] bytes) {
        return bytes2hex(bytes, false);
    }

    public static String bytes2hex(ByteBuffer a_buf, int a_len) {
        byte[] b_array = new byte[a_len];
        a_buf.get(b_array);
        return bytes2hex(b_array).toUpperCase();
    }

    public static String bytes2String(ByteBuffer a_buf, int a_len) {
        byte[] b_array = new byte[a_len];
        a_buf.get(b_array);
        try {
            return new String(b_array, "utf-8");
        } catch (UnsupportedEncodingException e) {
            try {
                return new String(b_array, "iso-8859-1");
            } catch (UnsupportedEncodingException e2) {
                throw new RuntimeException("Failed to encode string using iso-8859-1 and utf-8");
            }
        }
    }

    public static String bytes2String(byte[] a_buf) {
        int len = 0;
        while (len < a_buf.length && a_buf[len] != 0) {
            len++;
        }
        try {
            return new String(a_buf, 0, len, "utf-8");
        } catch (UnsupportedEncodingException e) {
            try {
                return new String(a_buf, 0, len, "iso-8859-1");
            } catch (UnsupportedEncodingException e2) {
                throw new RuntimeException("Failed to encode string using utf-8 and iso-8859-1");
            }
        }
    }

    public static String long2Ip(long a_value) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((a_value >>> 24) & 255));
        sb.append(".");
        sb.append(String.valueOf((a_value & 16777215) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((a_value & 65535) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(a_value & 255));
        return sb.toString();
    }

    public static String int2Netmask(int int_netmask) throws NumberFormatException {
        if (int_netmask < 0) {
            throw new NumberFormatException("The argument " + int_netmask + " should not less than 0.");
        }
        if (int_netmask > 32) {
            throw new NumberFormatException("The argument " + int_netmask + " should not greater than 32.");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < int_netmask; i++) {
            sb.append("1");
        }
        for (int i2 = 0; i2 < 32 - int_netmask; i2++) {
            sb.append("0");
        }
        long l = Long.parseLong(sb.toString(), 2);
        return long2Ip(l);
    }

    public static String int2IP(int int_value) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((int_value >>> 24) & 255));
        sb.append(".");
        sb.append(String.valueOf((int_value & 16777215) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((int_value & 65535) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(int_value & 255));
        return sb.toString();
    }

    public static byte[] toByteArray(int number, int byteLen) {
        int temp = number;
        byte[] b = new byte[byteLen];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 255).byteValue();
            temp >>= 8;
        }
        return b;
    }

    public static double long2double(long l) {
        if (l < 0) {
            double d = Math.abs(l) + 9.223372036854776E18d;
            return d;
        }
        return l;
    }

    public static long int2long(int i) {
        if (i < 0) {
            long l = i;
            return (l << 32) >>> 32;
        }
        return i;
    }

    public static int short2int(short s) {
        if (s < 0) {
            int i = s << 16;
            return i >>> 16;
        }
        return s;
    }

    public static int byte2int(byte b) {
        if (b < 0) {
            int i = b << 24;
            return i >>> 24;
        }
        return b;
    }

    public static short byte2short(byte b) {
        return (short) byte2int(b);
    }

    public static byte parseTimeZone(byte b) {
        byte plusTime = (byte) (b >>> 6);
        byte timezone = (byte) (((byte) (b << 2)) >>> 2);
        return (byte) (timezone + plusTime);
    }

    public static String getString(ByteBuffer buf) {
        short len = buf.getShort();
        return bytes2String(buf, len);
    }

    public static String getString(InputStream in) throws IOException {
        byte[] tmp = new byte[2];
        in.read(tmp);
        int iBytes2int = (short) bytes2int(tmp);
        if (iBytes2int > 0) {
            byte[] tmp2 = new byte[iBytes2int];
            in.read(tmp2);
            return bytes2String(ByteBuffer.wrap(tmp2), iBytes2int);
        }
        return "";
    }
}

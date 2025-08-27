package org.apache.poi.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/util/HexDump.class */
public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private HexDump() {
    }

    public static void dump(byte[] data, long offset, OutputStream stream, int index, int length) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (stream == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
        }
        OutputStreamWriter osw = new OutputStreamWriter(stream, UTF8);
        osw.write(dump(data, offset, index, length));
        osw.flush();
    }

    public static synchronized void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        dump(data, offset, stream, index, Integer.MAX_VALUE);
    }

    public static String dump(byte[] data, long offset, int index) {
        return dump(data, offset, index, Integer.MAX_VALUE);
    }

    public static String dump(byte[] data, long offset, int index, int length) {
        if (data == null || data.length == 0) {
            return "No Data" + EOL;
        }
        int data_length = (length == Integer.MAX_VALUE || length < 0 || index + length < 0) ? data.length : Math.min(data.length, index + length);
        if (index < 0 || index >= data.length) {
            String err = "illegal index: " + index + " into array of length " + data.length;
            throw new ArrayIndexOutOfBoundsException(err);
        }
        long display_offset = offset + index;
        StringBuilder buffer = new StringBuilder(74);
        for (int j = index; j < data_length; j += 16) {
            int chars_read = data_length - j;
            if (chars_read > 16) {
                chars_read = 16;
            }
            writeHex(buffer, display_offset, 8, "");
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    writeHex(buffer, data[k + j], 2, SymbolConstants.SPACE_SYMBOL);
                } else {
                    buffer.append("   ");
                }
            }
            buffer.append(' ');
            for (int k2 = 0; k2 < chars_read; k2++) {
                buffer.append(toAscii(data[k2 + j]));
            }
            buffer.append(EOL);
            display_offset += chars_read;
        }
        return buffer.toString();
    }

    public static char toAscii(int dataB) {
        char charB = (char) (dataB & 255);
        if (Character.isISOControl(charB)) {
            return '.';
        }
        switch (charB) {
            case 221:
            case 255:
                charB = '.';
                break;
        }
        return charB;
    }

    public static String toHex(byte[] value) {
        StringBuilder retVal = new StringBuilder();
        retVal.append('[');
        if (value != null && value.length > 0) {
            for (int x = 0; x < value.length; x++) {
                if (x > 0) {
                    retVal.append(", ");
                }
                retVal.append(toHex(value[x]));
            }
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(short[] value) {
        StringBuilder retVal = new StringBuilder();
        retVal.append('[');
        for (int x = 0; x < value.length; x++) {
            if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(byte[] value, int bytesPerLine) {
        if (value.length == 0) {
            return ": 0";
        }
        int digits = (int) Math.round((Math.log(value.length) / Math.log(10.0d)) + 0.5d);
        StringBuilder retVal = new StringBuilder();
        writeHex(retVal, 0L, digits, "");
        retVal.append(": ");
        int i = -1;
        for (int x = 0; x < value.length; x++) {
            i++;
            if (i == bytesPerLine) {
                retVal.append('\n');
                writeHex(retVal, x, digits, "");
                retVal.append(": ");
                i = 0;
            } else if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        return retVal.toString();
    }

    public static String toHex(short value) {
        StringBuilder sb = new StringBuilder(4);
        writeHex(sb, value & 65535, 4, "");
        return sb.toString();
    }

    public static String toHex(byte value) {
        StringBuilder sb = new StringBuilder(2);
        writeHex(sb, value & 255, 2, "");
        return sb.toString();
    }

    public static String toHex(int value) {
        StringBuilder sb = new StringBuilder(8);
        writeHex(sb, value & 4294967295L, 8, "");
        return sb.toString();
    }

    public static String toHex(long value) {
        StringBuilder sb = new StringBuilder(16);
        writeHex(sb, value, 16, "");
        return sb.toString();
    }

    public static String toHex(String value) {
        return (value == null || value.length() == 0) ? "[]" : toHex(value.getBytes(LocaleUtil.CHARSET_1252));
    }

    public static void dump(InputStream in, PrintStream out, int start, int bytesToDump) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int c;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        if (bytesToDump == -1) {
            int i = in.read();
            while (true) {
                int c2 = i;
                if (c2 == -1) {
                    break;
                }
                buf.write(c2);
                i = in.read();
            }
        } else {
            int bytesRemaining = bytesToDump;
            while (true) {
                int i2 = bytesRemaining;
                bytesRemaining--;
                if (i2 <= 0 || (c = in.read()) == -1) {
                    break;
                } else {
                    buf.write(c);
                }
            }
        }
        byte[] data = buf.toByteArray();
        dump(data, 0L, out, start, data.length);
    }

    public static String longToHex(long value) {
        StringBuilder sb = new StringBuilder(18);
        writeHex(sb, value, 16, "0x");
        return sb.toString();
    }

    public static String intToHex(int value) {
        StringBuilder sb = new StringBuilder(10);
        writeHex(sb, value & 4294967295L, 8, "0x");
        return sb.toString();
    }

    public static String shortToHex(int value) {
        StringBuilder sb = new StringBuilder(6);
        writeHex(sb, value & 65535, 4, "0x");
        return sb.toString();
    }

    public static String byteToHex(int value) {
        StringBuilder sb = new StringBuilder(4);
        writeHex(sb, value & 255, 2, "0x");
        return sb.toString();
    }

    private static void writeHex(StringBuilder sb, long value, int nDigits, String prefix) {
        sb.append(prefix);
        char[] buf = new char[nDigits];
        long acc = value;
        for (int i = nDigits - 1; i >= 0; i--) {
            int digit = (int) (acc & 15);
            buf[i] = (char) (digit < 10 ? 48 + digit : (65 + digit) - 10);
            acc >>>= 4;
        }
        sb.append(buf);
    }

    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream(args[0]);
        byte[] b = IOUtils.toByteArray(in);
        in.close();
        System.out.println(dump(b, 0L, 0));
    }
}

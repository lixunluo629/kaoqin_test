package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.commons.io.output.CloseShieldOutputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/HexDump.class */
public class HexDump {

    @Deprecated
    public static final String EOL = System.lineSeparator();
    private static final char[] HEX_CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int[] SHIFTS = {28, 24, 20, 16, 12, 8, 4, 0};

    public static void dump(byte[] data, Appendable appendable) throws IOException, ArrayIndexOutOfBoundsException {
        dump(data, 0L, appendable, 0, data.length);
    }

    public static void dump(byte[] data, long offset, Appendable appendable, int index, int length) throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(appendable, "appendable");
        if (index < 0 || index >= data.length) {
            throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
        }
        long display_offset = offset + index;
        StringBuilder buffer = new StringBuilder(74);
        if (length < 0 || index + length > data.length) {
            throw new ArrayIndexOutOfBoundsException(String.format("Range [%s, %<s + %s) out of bounds for length %s", Integer.valueOf(index), Integer.valueOf(length), Integer.valueOf(data.length)));
        }
        int endIndex = index + length;
        for (int j = index; j < endIndex; j += 16) {
            int chars_read = endIndex - j;
            if (chars_read > 16) {
                chars_read = 16;
            }
            dump(buffer, display_offset).append(' ');
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    dump(buffer, data[k + j]);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (int k2 = 0; k2 < chars_read; k2++) {
                if (data[k2 + j] >= 32 && data[k2 + j] < Byte.MAX_VALUE) {
                    buffer.append((char) data[k2 + j]);
                } else {
                    buffer.append('.');
                }
            }
            buffer.append(System.lineSeparator());
            appendable.append(buffer);
            buffer.setLength(0);
            display_offset += chars_read;
        }
    }

    public static void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(stream, "stream");
        OutputStreamWriter out = new OutputStreamWriter(CloseShieldOutputStream.wrap(stream), Charset.defaultCharset());
        try {
            dump(data, offset, out, index, data.length - index);
            out.close();
        } catch (Throwable th) {
            try {
                out.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static StringBuilder dump(StringBuilder builder, byte value) {
        for (int j = 0; j < 2; j++) {
            builder.append(HEX_CODES[(value >> SHIFTS[j + 6]) & 15]);
        }
        return builder;
    }

    private static StringBuilder dump(StringBuilder builder, long value) {
        for (int j = 0; j < 8; j++) {
            builder.append(HEX_CODES[((int) (value >> SHIFTS[j])) & 15]);
        }
        return builder;
    }
}

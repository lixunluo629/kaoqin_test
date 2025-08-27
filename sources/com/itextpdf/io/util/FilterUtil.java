package com.itextpdf.io.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/FilterUtil.class */
public final class FilterUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) FilterUtil.class);

    private FilterUtil() {
    }

    public static byte[] flateDecode(byte[] input, boolean strict) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        InflaterInputStream zip = new InflaterInputStream(stream);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] b = new byte[strict ? 4092 : 1];
        while (true) {
            try {
                try {
                    int n = zip.read(b);
                    if (n < 0) {
                        break;
                    }
                    output.write(b, 0, n);
                } catch (Exception e) {
                    byte[] byteArray = strict ? null : output.toByteArray();
                    try {
                        zip.close();
                        output.close();
                    } catch (Exception e2) {
                        LOGGER.error(e2.getMessage(), (Throwable) e2);
                    }
                    return byteArray;
                }
            } catch (Throwable th) {
                try {
                    zip.close();
                    output.close();
                } catch (Exception e3) {
                    LOGGER.error(e3.getMessage(), (Throwable) e3);
                }
                throw th;
            }
        }
        zip.close();
        output.close();
        byte[] byteArray2 = output.toByteArray();
        try {
            zip.close();
            output.close();
        } catch (Exception e4) {
            LOGGER.error(e4.getMessage(), (Throwable) e4);
        }
        return byteArray2;
    }

    public static byte[] flateDecode(byte[] input) throws IOException {
        byte[] b = flateDecode(input, true);
        if (b == null) {
            return flateDecode(input, false);
        }
        return b;
    }

    public static void inflateData(byte[] deflated, byte[] inflated) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(deflated);
        try {
            inflater.inflate(inflated);
        } catch (DataFormatException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CannotInflateTiffImage);
        }
    }

    public static InputStream getInflaterInputStream(InputStream input) {
        return new InflaterInputStream(input, new Inflater());
    }
}

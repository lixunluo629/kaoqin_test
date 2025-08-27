package com.drew.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/StreamUtil.class */
public final class StreamUtil {
    public static byte[] readAllBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = stream.read(buffer);
            if (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
            } else {
                return outputStream.toByteArray();
            }
        }
    }
}

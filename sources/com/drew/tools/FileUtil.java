package com.drew.tools;

import com.drew.lang.annotations.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/tools/FileUtil.class */
public class FileUtil {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FileUtil.class.desiredAssertionStatus();
    }

    public static void saveBytes(@NotNull File file, @NotNull byte[] bytes) throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(bytes);
            if (stream != null) {
                stream.close();
            }
        } catch (Throwable th) {
            if (stream != null) {
                stream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static byte[] readBytes(@NotNull File file) throws IOException {
        int length = (int) file.length();
        if (!$assertionsDisabled && length == 0) {
            throw new AssertionError();
        }
        byte[] bytes = new byte[length];
        int totalBytesRead = 0;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            while (totalBytesRead != length) {
                int bytesRead = inputStream.read(bytes, totalBytesRead, length - totalBytesRead);
                if (bytesRead == -1) {
                    break;
                }
                totalBytesRead += bytesRead;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return bytes;
        } catch (Throwable th) {
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static byte[] readBytes(@NotNull String filePath) throws IOException {
        return readBytes(new File(filePath));
    }
}

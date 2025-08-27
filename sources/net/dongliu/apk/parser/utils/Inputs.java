package net.dongliu.apk.parser.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/Inputs.class */
public class Inputs {
    public static byte[] readAll(InputStream in) throws IOException {
        try {
            byte[] buf = new byte[8192];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Throwable th = null;
            while (true) {
                try {
                    try {
                        int len = in.read(buf);
                        if (len == -1) {
                            break;
                        }
                        bos.write(buf, 0, len);
                    } finally {
                    }
                } finally {
                }
            }
            byte[] byteArray = bos.toByteArray();
            if (bos != null) {
                if (0 != 0) {
                    try {
                        bos.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    bos.close();
                }
            }
            return byteArray;
        } finally {
            in.close();
        }
    }
}

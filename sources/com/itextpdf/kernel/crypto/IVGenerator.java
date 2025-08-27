package com.itextpdf.kernel.crypto;

import com.itextpdf.io.util.SystemUtil;
import java.nio.charset.StandardCharsets;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/IVGenerator.class */
public final class IVGenerator {
    private static final ARCFOUREncryption arcfour = new ARCFOUREncryption();

    static {
        long time = SystemUtil.getTimeBasedSeed();
        long mem = SystemUtil.getFreeMemory();
        String s = time + "+" + mem;
        arcfour.prepareARCFOURKey(s.getBytes(StandardCharsets.ISO_8859_1));
    }

    private IVGenerator() {
    }

    public static byte[] getIV() {
        return getIV(16);
    }

    public static byte[] getIV(int len) {
        byte[] b = new byte[len];
        synchronized (arcfour) {
            arcfour.encryptARCFOUR(b);
        }
        return b;
    }
}

package net.dongliu.apk.parser;

import java.io.IOException;
import java.security.cert.CertificateException;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/Main.class */
public class Main {
    public static void main(String[] args) throws IOException, CertificateException {
        ApkFile apkFile = new ApkFile(args[0]);
        Throwable th = null;
        try {
            System.out.println(apkFile.getApkSingers().get(0).getCertificateMetas());
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                apkFile.close();
            }
        } catch (Throwable th3) {
            if (apkFile != null) {
                if (0 != 0) {
                    try {
                        apkFile.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    apkFile.close();
                }
            }
            throw th3;
        }
    }
}

package net.dongliu.apk.parser.parser;

import java.security.cert.CertificateException;
import java.util.List;
import net.dongliu.apk.parser.ApkParsers;
import net.dongliu.apk.parser.bean.CertificateMeta;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/CertificateParser.class */
public abstract class CertificateParser {
    protected final byte[] data;

    public abstract List<CertificateMeta> parse() throws CertificateException;

    public CertificateParser(byte[] data) {
        this.data = data;
    }

    public static CertificateParser getInstance(byte[] data) {
        if (ApkParsers.useBouncyCastle()) {
            return new BCCertificateParser(data);
        }
        return new JSSECertificateParser(data);
    }
}

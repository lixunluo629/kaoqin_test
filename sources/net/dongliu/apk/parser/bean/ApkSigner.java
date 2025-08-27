package net.dongliu.apk.parser.bean;

import java.util.List;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/ApkSigner.class */
public class ApkSigner {
    private String path;
    private List<CertificateMeta> certificateMetas;

    public ApkSigner(String path, List<CertificateMeta> certificateMetas) {
        this.path = path;
        this.certificateMetas = certificateMetas;
    }

    public String getPath() {
        return this.path;
    }

    public List<CertificateMeta> getCertificateMetas() {
        return this.certificateMetas;
    }
}

package net.dongliu.apk.parser.bean;

import java.util.List;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/ApkV2Signer.class */
public class ApkV2Signer {
    private List<CertificateMeta> certificateMetas;

    public ApkV2Signer(List<CertificateMeta> certificateMetas) {
        this.certificateMetas = certificateMetas;
    }

    public List<CertificateMeta> getCertificateMetas() {
        return this.certificateMetas;
    }
}

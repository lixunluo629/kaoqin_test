package org.apache.poi.poifs.crypt;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/ChainingMode.class */
public enum ChainingMode {
    ecb("ECB", 1),
    cbc("CBC", 2),
    cfb("CFB8", 3);

    public final String jceId;
    public final int ecmaId;

    ChainingMode(String jceId, int ecmaId) {
        this.jceId = jceId;
        this.ecmaId = ecmaId;
    }
}

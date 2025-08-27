package org.apache.poi.poifs.crypt.xor;

import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/xor/XOREncryptionHeader.class */
public class XOREncryptionHeader extends EncryptionHeader implements EncryptionRecord, Cloneable {
    protected XOREncryptionHeader() {
    }

    @Override // org.apache.poi.poifs.crypt.standard.EncryptionRecord
    public void write(LittleEndianByteArrayOutputStream leos) {
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionHeader
    /* renamed from: clone */
    public XOREncryptionHeader mo3516clone() throws CloneNotSupportedException {
        return (XOREncryptionHeader) super.mo3516clone();
    }
}

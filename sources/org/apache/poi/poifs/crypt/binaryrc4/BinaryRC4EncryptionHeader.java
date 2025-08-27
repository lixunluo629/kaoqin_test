package org.apache.poi.poifs.crypt.binaryrc4;

import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.CipherProvider;
import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/binaryrc4/BinaryRC4EncryptionHeader.class */
public class BinaryRC4EncryptionHeader extends EncryptionHeader implements EncryptionRecord, Cloneable {
    protected BinaryRC4EncryptionHeader() {
        setCipherAlgorithm(CipherAlgorithm.rc4);
        setKeySize(40);
        setBlockSize(-1);
        setCipherProvider(CipherProvider.rc4);
        setHashAlgorithm(HashAlgorithm.md5);
        setSizeExtra(0);
        setFlags(0);
        setCspName("");
        setChainingMode(null);
    }

    @Override // org.apache.poi.poifs.crypt.standard.EncryptionRecord
    public void write(LittleEndianByteArrayOutputStream littleendianbytearrayoutputstream) {
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionHeader
    /* renamed from: clone */
    public BinaryRC4EncryptionHeader mo3516clone() throws CloneNotSupportedException {
        return (BinaryRC4EncryptionHeader) super.mo3516clone();
    }
}

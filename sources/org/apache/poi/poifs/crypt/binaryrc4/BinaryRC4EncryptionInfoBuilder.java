package org.apache.poi.poifs.crypt.binaryrc4;

import java.io.IOException;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/binaryrc4/BinaryRC4EncryptionInfoBuilder.class */
public class BinaryRC4EncryptionInfoBuilder implements EncryptionInfoBuilder {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BinaryRC4EncryptionInfoBuilder.class.desiredAssertionStatus();
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionInfoBuilder
    public void initialize(EncryptionInfo info, LittleEndianInput dis) throws IOException {
        int vMajor = info.getVersionMajor();
        int vMinor = info.getVersionMinor();
        if (!$assertionsDisabled && (vMajor != 1 || vMinor != 1)) {
            throw new AssertionError();
        }
        info.setHeader(new BinaryRC4EncryptionHeader());
        info.setVerifier(new BinaryRC4EncryptionVerifier(dis));
        Decryptor dec = new BinaryRC4Decryptor();
        dec.setEncryptionInfo(info);
        info.setDecryptor(dec);
        Encryptor enc = new BinaryRC4Encryptor();
        enc.setEncryptionInfo(info);
        info.setEncryptor(enc);
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionInfoBuilder
    public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        info.setHeader(new BinaryRC4EncryptionHeader());
        info.setVerifier(new BinaryRC4EncryptionVerifier());
        Decryptor dec = new BinaryRC4Decryptor();
        dec.setEncryptionInfo(info);
        info.setDecryptor(dec);
        Encryptor enc = new BinaryRC4Encryptor();
        enc.setEncryptionInfo(info);
        info.setEncryptor(enc);
    }
}

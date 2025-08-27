package org.apache.poi.poifs.crypt;

import java.io.IOException;
import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/EncryptionInfoBuilder.class */
public interface EncryptionInfoBuilder {
    void initialize(EncryptionInfo encryptionInfo, LittleEndianInput littleEndianInput) throws IOException;

    void initialize(EncryptionInfo encryptionInfo, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int i, int i2, ChainingMode chainingMode);
}

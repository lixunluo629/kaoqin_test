package org.apache.poi.poifs.crypt.standard;

import org.apache.poi.util.LittleEndianByteArrayOutputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/standard/EncryptionRecord.class */
public interface EncryptionRecord {
    void write(LittleEndianByteArrayOutputStream littleEndianByteArrayOutputStream);
}

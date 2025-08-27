package org.apache.poi.poifs.crypt.dsig;

import java.io.Serializable;
import org.apache.poi.poifs.crypt.HashAlgorithm;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/DigestInfo.class */
public class DigestInfo implements Serializable {
    private static final long serialVersionUID = 1;
    public final byte[] digestValue;
    public final String description;
    public final HashAlgorithm hashAlgo;

    public DigestInfo(byte[] digestValue, HashAlgorithm hashAlgo, String description) {
        this.digestValue = (byte[]) digestValue.clone();
        this.hashAlgo = hashAlgo;
        this.description = description;
    }
}

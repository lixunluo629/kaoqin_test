package org.apache.poi.poifs.crypt.dsig.services;

import org.apache.poi.poifs.crypt.dsig.SignatureConfig;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/services/TimeStampService.class */
public interface TimeStampService extends SignatureConfig.SignatureConfigurable {
    byte[] timeStamp(byte[] bArr, RevocationData revocationData) throws Exception;
}

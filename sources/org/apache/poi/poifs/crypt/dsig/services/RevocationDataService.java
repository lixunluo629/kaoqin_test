package org.apache.poi.poifs.crypt.dsig.services;

import java.security.cert.X509Certificate;
import java.util.List;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/services/RevocationDataService.class */
public interface RevocationDataService {
    RevocationData getRevocationData(List<X509Certificate> list);
}

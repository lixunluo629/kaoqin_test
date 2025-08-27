package org.apache.poi.poifs.crypt.dsig.services;

import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.List;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/services/RevocationData.class */
public class RevocationData {
    private final List<byte[]> crls = new ArrayList();
    private final List<byte[]> ocsps = new ArrayList();

    public void addCRL(byte[] encodedCrl) {
        this.crls.add(encodedCrl);
    }

    public void addCRL(X509CRL crl) throws CRLException {
        try {
            byte[] encodedCrl = crl.getEncoded();
            addCRL(encodedCrl);
        } catch (CRLException e) {
            throw new IllegalArgumentException("CRL coding error: " + e.getMessage(), e);
        }
    }

    public void addOCSP(byte[] encodedOcsp) {
        this.ocsps.add(encodedOcsp);
    }

    public List<byte[]> getCRLs() {
        return this.crls;
    }

    public List<byte[]> getOCSPs() {
        return this.ocsps;
    }

    public boolean hasOCSPs() {
        return false == this.ocsps.isEmpty();
    }

    public boolean hasCRLs() {
        return false == this.crls.isEmpty();
    }

    public boolean hasRevocationDataEntries() {
        return hasOCSPs() || hasCRLs();
    }
}

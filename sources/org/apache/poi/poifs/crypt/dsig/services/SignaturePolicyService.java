package org.apache.poi.poifs.crypt.dsig.services;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/dsig/services/SignaturePolicyService.class */
public interface SignaturePolicyService {
    String getSignaturePolicyIdentifier();

    String getSignaturePolicyDescription();

    String getSignaturePolicyDownloadUrl();

    byte[] getSignaturePolicyDocument();
}

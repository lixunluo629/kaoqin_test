package org.bouncycastle.pqc.crypto.xmss;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.Digest;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/xmss/XMSSParameters.class */
public final class XMSSParameters {
    private final XMSSOid oid;
    private final int height;
    private final int k;
    private final ASN1ObjectIdentifier treeDigestOID;
    private final int winternitzParameter;
    private final String treeDigest;
    private final int treeDigestSize;
    private final WOTSPlusParameters wotsPlusParams;

    public XMSSParameters(int i, Digest digest) {
        if (i < 2) {
            throw new IllegalArgumentException("height must be >= 2");
        }
        if (digest == null) {
            throw new NullPointerException("digest == null");
        }
        this.height = i;
        this.k = determineMinK();
        this.treeDigest = digest.getAlgorithmName();
        this.treeDigestOID = DigestUtil.getDigestOID(digest.getAlgorithmName());
        this.wotsPlusParams = new WOTSPlusParameters(this.treeDigestOID);
        this.treeDigestSize = this.wotsPlusParams.getTreeDigestSize();
        this.winternitzParameter = this.wotsPlusParams.getWinternitzParameter();
        this.oid = DefaultXMSSOid.lookup(this.treeDigest, this.treeDigestSize, this.winternitzParameter, this.wotsPlusParams.getLen(), i);
    }

    private int determineMinK() {
        for (int i = 2; i <= this.height; i++) {
            if ((this.height - i) % 2 == 0) {
                return i;
            }
        }
        throw new IllegalStateException("should never happen...");
    }

    public int getTreeDigestSize() {
        return this.treeDigestSize;
    }

    public int getHeight() {
        return this.height;
    }

    String getTreeDigest() {
        return this.treeDigest;
    }

    ASN1ObjectIdentifier getTreeDigestOID() {
        return this.treeDigestOID;
    }

    int getLen() {
        return this.wotsPlusParams.getLen();
    }

    int getWinternitzParameter() {
        return this.winternitzParameter;
    }

    WOTSPlus getWOTSPlus() {
        return new WOTSPlus(this.wotsPlusParams);
    }

    XMSSOid getOid() {
        return this.oid;
    }

    int getK() {
        return this.k;
    }
}

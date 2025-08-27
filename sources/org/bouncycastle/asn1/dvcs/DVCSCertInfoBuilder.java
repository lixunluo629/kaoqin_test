package org.bouncycastle.asn1.dvcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.PolicyInformation;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/dvcs/DVCSCertInfoBuilder.class */
public class DVCSCertInfoBuilder {
    private int version = 1;
    private DVCSRequestInformation dvReqInfo;
    private DigestInfo messageImprint;
    private ASN1Integer serialNumber;
    private DVCSTime responseTime;
    private PKIStatusInfo dvStatus;
    private PolicyInformation policy;
    private ASN1Set reqSignature;
    private ASN1Sequence certs;
    private Extensions extensions;
    private static final int DEFAULT_VERSION = 1;
    private static final int TAG_DV_STATUS = 0;
    private static final int TAG_POLICY = 1;
    private static final int TAG_REQ_SIGNATURE = 2;
    private static final int TAG_CERTS = 3;

    public DVCSCertInfoBuilder(DVCSRequestInformation dVCSRequestInformation, DigestInfo digestInfo, ASN1Integer aSN1Integer, DVCSTime dVCSTime) {
        this.dvReqInfo = dVCSRequestInformation;
        this.messageImprint = digestInfo;
        this.serialNumber = aSN1Integer;
        this.responseTime = dVCSTime;
    }

    public DVCSCertInfo build() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(10);
        if (this.version != 1) {
            aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.version));
        }
        aSN1EncodableVector.add((ASN1Encodable) this.dvReqInfo);
        aSN1EncodableVector.add((ASN1Encodable) this.messageImprint);
        aSN1EncodableVector.add((ASN1Encodable) this.serialNumber);
        aSN1EncodableVector.add((ASN1Encodable) this.responseTime);
        if (this.dvStatus != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 0, (ASN1Encodable) this.dvStatus));
        }
        if (this.policy != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 1, (ASN1Encodable) this.policy));
        }
        if (this.reqSignature != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 2, (ASN1Encodable) this.reqSignature));
        }
        if (this.certs != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 3, (ASN1Encodable) this.certs));
        }
        if (this.extensions != null) {
            aSN1EncodableVector.add((ASN1Encodable) this.extensions);
        }
        return DVCSCertInfo.getInstance(new DERSequence(aSN1EncodableVector));
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public void setDvReqInfo(DVCSRequestInformation dVCSRequestInformation) {
        this.dvReqInfo = dVCSRequestInformation;
    }

    public void setMessageImprint(DigestInfo digestInfo) {
        this.messageImprint = digestInfo;
    }

    public void setSerialNumber(ASN1Integer aSN1Integer) {
        this.serialNumber = aSN1Integer;
    }

    public void setResponseTime(DVCSTime dVCSTime) {
        this.responseTime = dVCSTime;
    }

    public void setDvStatus(PKIStatusInfo pKIStatusInfo) {
        this.dvStatus = pKIStatusInfo;
    }

    public void setPolicy(PolicyInformation policyInformation) {
        this.policy = policyInformation;
    }

    public void setReqSignature(ASN1Set aSN1Set) {
        this.reqSignature = aSN1Set;
    }

    public void setCerts(TargetEtcChain[] targetEtcChainArr) {
        this.certs = new DERSequence(targetEtcChainArr);
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }
}

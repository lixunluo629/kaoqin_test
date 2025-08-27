package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTCTime;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/TBSCertList.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/TBSCertList.class */
public class TBSCertList extends ASN1Encodable {
    ASN1Sequence seq;
    DERInteger version;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time thisUpdate;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    X509Extensions crlExtensions;

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/TBSCertList$CRLEntry.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/TBSCertList$CRLEntry.class */
    public static class CRLEntry extends ASN1Encodable {
        ASN1Sequence seq;
        DERInteger userCertificate;
        Time revocationDate;
        X509Extensions crlEntryExtensions;

        public CRLEntry(ASN1Sequence aSN1Sequence) {
            if (aSN1Sequence.size() < 2 || aSN1Sequence.size() > 3) {
                throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
            }
            this.seq = aSN1Sequence;
            this.userCertificate = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
            this.revocationDate = Time.getInstance(aSN1Sequence.getObjectAt(1));
        }

        public DERInteger getUserCertificate() {
            return this.userCertificate;
        }

        public Time getRevocationDate() {
            return this.revocationDate;
        }

        public X509Extensions getExtensions() {
            if (this.crlEntryExtensions == null && this.seq.size() == 3) {
                this.crlEntryExtensions = X509Extensions.getInstance(this.seq.getObjectAt(2));
            }
            return this.crlEntryExtensions;
        }

        @Override // org.bouncycastle.asn1.ASN1Encodable
        public DERObject toASN1Object() {
            return this.seq;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/TBSCertList$EmptyEnumeration.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/TBSCertList$EmptyEnumeration.class */
    private class EmptyEnumeration implements Enumeration {
        private EmptyEnumeration() {
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        @Override // java.util.Enumeration
        public Object nextElement() {
            return null;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/TBSCertList$RevokedCertificatesEnumeration.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/TBSCertList$RevokedCertificatesEnumeration.class */
    private class RevokedCertificatesEnumeration implements Enumeration {
        private final Enumeration en;

        RevokedCertificatesEnumeration(Enumeration enumeration) {
            this.en = enumeration;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.en.hasMoreElements();
        }

        @Override // java.util.Enumeration
        public Object nextElement() {
            return new CRLEntry(ASN1Sequence.getInstance(this.en.nextElement()));
        }
    }

    public static TBSCertList getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static TBSCertList getInstance(Object obj) {
        if (obj instanceof TBSCertList) {
            return (TBSCertList) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new TBSCertList((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public TBSCertList(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 3 || aSN1Sequence.size() > 7) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        int i = 0;
        this.seq = aSN1Sequence;
        if (aSN1Sequence.getObjectAt(0) instanceof DERInteger) {
            i = 0 + 1;
            this.version = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            this.version = new DERInteger(0);
        }
        int i2 = i;
        int i3 = i + 1;
        this.signature = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(i2));
        int i4 = i3 + 1;
        this.issuer = X509Name.getInstance(aSN1Sequence.getObjectAt(i3));
        int i5 = i4 + 1;
        this.thisUpdate = Time.getInstance(aSN1Sequence.getObjectAt(i4));
        if (i5 < aSN1Sequence.size() && ((aSN1Sequence.getObjectAt(i5) instanceof DERUTCTime) || (aSN1Sequence.getObjectAt(i5) instanceof DERGeneralizedTime) || (aSN1Sequence.getObjectAt(i5) instanceof Time))) {
            i5++;
            this.nextUpdate = Time.getInstance(aSN1Sequence.getObjectAt(i5));
        }
        if (i5 < aSN1Sequence.size() && !(aSN1Sequence.getObjectAt(i5) instanceof DERTaggedObject)) {
            int i6 = i5;
            i5++;
            this.revokedCertificates = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(i6));
        }
        if (i5 >= aSN1Sequence.size() || !(aSN1Sequence.getObjectAt(i5) instanceof DERTaggedObject)) {
            return;
        }
        this.crlExtensions = X509Extensions.getInstance(aSN1Sequence.getObjectAt(i5));
    }

    public int getVersion() {
        return this.version.getValue().intValue() + 1;
    }

    public DERInteger getVersionNumber() {
        return this.version;
    }

    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }

    public X509Name getIssuer() {
        return this.issuer;
    }

    public Time getThisUpdate() {
        return this.thisUpdate;
    }

    public Time getNextUpdate() {
        return this.nextUpdate;
    }

    public CRLEntry[] getRevokedCertificates() {
        if (this.revokedCertificates == null) {
            return new CRLEntry[0];
        }
        CRLEntry[] cRLEntryArr = new CRLEntry[this.revokedCertificates.size()];
        for (int i = 0; i < cRLEntryArr.length; i++) {
            cRLEntryArr[i] = new CRLEntry(ASN1Sequence.getInstance(this.revokedCertificates.getObjectAt(i)));
        }
        return cRLEntryArr;
    }

    public Enumeration getRevokedCertificateEnumeration() {
        return this.revokedCertificates == null ? new EmptyEnumeration() : new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects());
    }

    public X509Extensions getExtensions() {
        return this.crlExtensions;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.seq;
    }
}

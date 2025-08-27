package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/CrlListID.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/CrlListID.class */
public class CrlListID extends ASN1Encodable {
    private ASN1Sequence crls;

    public static CrlListID getInstance(Object obj) {
        if (obj instanceof CrlListID) {
            return (CrlListID) obj;
        }
        if (obj != null) {
            return new CrlListID(ASN1Sequence.getInstance(obj));
        }
        throw new IllegalArgumentException("null value in getInstance");
    }

    private CrlListID(ASN1Sequence aSN1Sequence) {
        this.crls = (ASN1Sequence) aSN1Sequence.getObjectAt(0);
        Enumeration objects = this.crls.getObjects();
        while (objects.hasMoreElements()) {
            CrlValidatedID.getInstance(objects.nextElement());
        }
    }

    public CrlListID(CrlValidatedID[] crlValidatedIDArr) {
        this.crls = new DERSequence(crlValidatedIDArr);
    }

    public CrlValidatedID[] getCrls() {
        CrlValidatedID[] crlValidatedIDArr = new CrlValidatedID[this.crls.size()];
        for (int i = 0; i < crlValidatedIDArr.length; i++) {
            crlValidatedIDArr[i] = CrlValidatedID.getInstance(this.crls.getObjectAt(i));
        }
        return crlValidatedIDArr;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return new DERSequence(this.crls);
    }
}

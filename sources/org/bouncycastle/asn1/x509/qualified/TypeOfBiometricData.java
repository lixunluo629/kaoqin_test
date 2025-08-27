package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/qualified/TypeOfBiometricData.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/qualified/TypeOfBiometricData.class */
public class TypeOfBiometricData extends ASN1Encodable implements ASN1Choice {
    public static final int PICTURE = 0;
    public static final int HANDWRITTEN_SIGNATURE = 1;
    DEREncodable obj;

    public static TypeOfBiometricData getInstance(Object obj) {
        if (obj == null || (obj instanceof TypeOfBiometricData)) {
            return (TypeOfBiometricData) obj;
        }
        if (obj instanceof DERInteger) {
            return new TypeOfBiometricData(DERInteger.getInstance(obj).getValue().intValue());
        }
        if (obj instanceof DERObjectIdentifier) {
            return new TypeOfBiometricData(DERObjectIdentifier.getInstance(obj));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public TypeOfBiometricData(int i) {
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException("unknow PredefinedBiometricType : " + i);
        }
        this.obj = new DERInteger(i);
    }

    public TypeOfBiometricData(DERObjectIdentifier dERObjectIdentifier) {
        this.obj = dERObjectIdentifier;
    }

    public boolean isPredefined() {
        return this.obj instanceof DERInteger;
    }

    public int getPredefinedBiometricType() {
        return ((DERInteger) this.obj).getValue().intValue();
    }

    public DERObjectIdentifier getBiometricDataOid() {
        return (DERObjectIdentifier) this.obj;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.obj.getDERObject();
    }
}

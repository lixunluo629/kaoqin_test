package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/BasicConstraints.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/BasicConstraints.class */
public class BasicConstraints extends ASN1Encodable {
    DERBoolean cA;
    DERInteger pathLenConstraint;

    public static BasicConstraints getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static BasicConstraints getInstance(Object obj) {
        if (obj == null || (obj instanceof BasicConstraints)) {
            return (BasicConstraints) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new BasicConstraints((ASN1Sequence) obj);
        }
        if (obj instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension) obj));
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public BasicConstraints(ASN1Sequence aSN1Sequence) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (aSN1Sequence.size() == 0) {
            this.cA = null;
            this.pathLenConstraint = null;
            return;
        }
        if (aSN1Sequence.getObjectAt(0) instanceof DERBoolean) {
            this.cA = DERBoolean.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            this.cA = null;
            this.pathLenConstraint = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        }
        if (aSN1Sequence.size() > 1) {
            if (this.cA == null) {
                throw new IllegalArgumentException("wrong sequence in constructor");
            }
            this.pathLenConstraint = DERInteger.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public BasicConstraints(boolean z, int i) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (z) {
            this.cA = new DERBoolean(z);
            this.pathLenConstraint = new DERInteger(i);
        } else {
            this.cA = null;
            this.pathLenConstraint = null;
        }
    }

    public BasicConstraints(boolean z) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        if (z) {
            this.cA = new DERBoolean(true);
        } else {
            this.cA = null;
        }
        this.pathLenConstraint = null;
    }

    public BasicConstraints(int i) {
        this.cA = new DERBoolean(false);
        this.pathLenConstraint = null;
        this.cA = new DERBoolean(true);
        this.pathLenConstraint = new DERInteger(i);
    }

    public boolean isCA() {
        return this.cA != null && this.cA.isTrue();
    }

    public BigInteger getPathLenConstraint() {
        if (this.pathLenConstraint != null) {
            return this.pathLenConstraint.getValue();
        }
        return null;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.cA != null) {
            aSN1EncodableVector.add(this.cA);
        }
        if (this.pathLenConstraint != null) {
            aSN1EncodableVector.add(this.pathLenConstraint);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return this.pathLenConstraint == null ? this.cA == null ? "BasicConstraints: isCa(false)" : "BasicConstraints: isCa(" + isCA() + ")" : "BasicConstraints: isCa(" + isCA() + "), pathLenConstraint = " + this.pathLenConstraint.getValue();
    }
}

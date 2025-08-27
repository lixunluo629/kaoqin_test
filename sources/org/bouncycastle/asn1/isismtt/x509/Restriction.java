package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x500.DirectoryString;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/isismtt/x509/Restriction.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/isismtt/x509/Restriction.class */
public class Restriction extends ASN1Encodable {
    private DirectoryString restriction;

    public static Restriction getInstance(Object obj) {
        if (obj instanceof Restriction) {
            return (Restriction) obj;
        }
        if (obj instanceof ASN1String) {
            return new Restriction(DirectoryString.getInstance(obj));
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    private Restriction(DirectoryString directoryString) {
        this.restriction = directoryString;
    }

    public Restriction(String str) {
        this.restriction = new DirectoryString(str);
    }

    public DirectoryString getRestriction() {
        return this.restriction;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.restriction.toASN1Object();
    }
}

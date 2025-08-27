package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x500.DirectoryString;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/isismtt/x509/AdditionalInformationSyntax.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/isismtt/x509/AdditionalInformationSyntax.class */
public class AdditionalInformationSyntax extends ASN1Encodable {
    private DirectoryString information;

    public static AdditionalInformationSyntax getInstance(Object obj) {
        if (obj instanceof AdditionalInformationSyntax) {
            return (AdditionalInformationSyntax) obj;
        }
        if (obj instanceof ASN1String) {
            return new AdditionalInformationSyntax(DirectoryString.getInstance(obj));
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    private AdditionalInformationSyntax(DirectoryString directoryString) {
        this.information = directoryString;
    }

    public AdditionalInformationSyntax(String str) {
        this(new DirectoryString(str));
    }

    public DirectoryString getInformation() {
        return this.information;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.information.toASN1Object();
    }
}

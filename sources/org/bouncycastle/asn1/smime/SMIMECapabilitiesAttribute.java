package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/smime/SMIMECapabilitiesAttribute.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/smime/SMIMECapabilitiesAttribute.class */
public class SMIMECapabilitiesAttribute extends Attribute {
    public SMIMECapabilitiesAttribute(SMIMECapabilityVector sMIMECapabilityVector) {
        super(SMIMEAttributes.smimeCapabilities, new DERSet(new DERSequence(sMIMECapabilityVector.toASN1EncodableVector())));
    }
}

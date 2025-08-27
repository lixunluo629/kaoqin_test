package org.bouncycastle.cert.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/Control.class */
public interface Control {
    ASN1ObjectIdentifier getType();

    ASN1Encodable getValue();
}

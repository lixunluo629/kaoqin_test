package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/smime/SMIMEAttributes.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/smime/SMIMEAttributes.class */
public interface SMIMEAttributes {
    public static final DERObjectIdentifier smimeCapabilities = PKCSObjectIdentifiers.pkcs_9_at_smimeCapabilities;
    public static final DERObjectIdentifier encrypKeyPref = PKCSObjectIdentifiers.id_aa_encrypKeyPref;
}

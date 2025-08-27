package org.bouncycastle.asn1.eac;

import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/eac/EACObjectIdentifiers.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/eac/EACObjectIdentifiers.class */
public interface EACObjectIdentifiers {
    public static final DERObjectIdentifier bsi_de = new DERObjectIdentifier("0.4.0.127.0.7");
    public static final DERObjectIdentifier id_PK = new DERObjectIdentifier(bsi_de + ".2.2.1");
    public static final DERObjectIdentifier id_PK_DH = new DERObjectIdentifier(id_PK + ".1");
    public static final DERObjectIdentifier id_PK_ECDH = new DERObjectIdentifier(id_PK + ".2");
    public static final DERObjectIdentifier id_CA = new DERObjectIdentifier(bsi_de + ".2.2.3");
    public static final DERObjectIdentifier id_CA_DH = new DERObjectIdentifier(id_CA + ".1");
    public static final DERObjectIdentifier id_CA_DH_3DES_CBC_CBC = new DERObjectIdentifier(id_CA_DH + ".1");
    public static final DERObjectIdentifier id_CA_ECDH = new DERObjectIdentifier(id_CA + ".2");
    public static final DERObjectIdentifier id_CA_ECDH_3DES_CBC_CBC = new DERObjectIdentifier(id_CA_ECDH + ".1");
    public static final DERObjectIdentifier id_TA = new DERObjectIdentifier(bsi_de + ".2.2.2");
    public static final DERObjectIdentifier id_TA_RSA = new DERObjectIdentifier(id_TA + ".1");
    public static final DERObjectIdentifier id_TA_RSA_v1_5_SHA_1 = new DERObjectIdentifier(id_TA_RSA + ".1");
    public static final DERObjectIdentifier id_TA_RSA_v1_5_SHA_256 = new DERObjectIdentifier(id_TA_RSA + ".2");
    public static final DERObjectIdentifier id_TA_RSA_PSS_SHA_1 = new DERObjectIdentifier(id_TA_RSA + ".3");
    public static final DERObjectIdentifier id_TA_RSA_PSS_SHA_256 = new DERObjectIdentifier(id_TA_RSA + ".4");
    public static final DERObjectIdentifier id_TA_ECDSA = new DERObjectIdentifier(id_TA + ".2");
    public static final DERObjectIdentifier id_TA_ECDSA_SHA_1 = new DERObjectIdentifier(id_TA_ECDSA + ".1");
    public static final DERObjectIdentifier id_TA_ECDSA_SHA_224 = new DERObjectIdentifier(id_TA_ECDSA + ".2");
    public static final DERObjectIdentifier id_TA_ECDSA_SHA_256 = new DERObjectIdentifier(id_TA_ECDSA + ".3");
    public static final DERObjectIdentifier id_TA_ECDSA_SHA_384 = new DERObjectIdentifier(id_TA_ECDSA + ".4");
    public static final DERObjectIdentifier id_TA_ECDSA_SHA_512 = new DERObjectIdentifier(id_TA_ECDSA + ".5");
    public static final DERObjectIdentifier id_EAC_ePassport = new DERObjectIdentifier(bsi_de + ".3.1.2.1");
}

package org.bouncycastle.cms;

import com.moredian.onpremise.core.utils.RSAUtils;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.rosstandart.RosstandartObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/CMSSignedHelper.class */
class CMSSignedHelper {
    static final CMSSignedHelper INSTANCE = new CMSSignedHelper();
    private static final Map encryptionAlgs = new HashMap();

    CMSSignedHelper() {
    }

    private static void addEntries(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str) {
        encryptionAlgs.put(aSN1ObjectIdentifier.getId(), str);
    }

    String getEncryptionAlgName(String str) {
        String str2 = (String) encryptionAlgs.get(str);
        return str2 != null ? str2 : str;
    }

    AlgorithmIdentifier fixAlgID(AlgorithmIdentifier algorithmIdentifier) {
        return algorithmIdentifier.getParameters() == null ? new AlgorithmIdentifier(algorithmIdentifier.getAlgorithm(), (ASN1Encodable) DERNull.INSTANCE) : algorithmIdentifier;
    }

    void setSigningEncryptionAlgorithmMapping(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str) {
        addEntries(aSN1ObjectIdentifier, str);
    }

    Store getCertificates(ASN1Set aSN1Set) {
        if (aSN1Set == null) {
            return new CollectionStore(new ArrayList());
        }
        ArrayList arrayList = new ArrayList(aSN1Set.size());
        Enumeration objects = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable) objects.nextElement()).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1Sequence) {
                arrayList.add(new X509CertificateHolder(Certificate.getInstance(aSN1Primitive)));
            }
        }
        return new CollectionStore(arrayList);
    }

    Store getAttributeCertificates(ASN1Set aSN1Set) {
        if (aSN1Set == null) {
            return new CollectionStore(new ArrayList());
        }
        ArrayList arrayList = new ArrayList(aSN1Set.size());
        Enumeration objects = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Encodable aSN1Primitive = ((ASN1Encodable) objects.nextElement()).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1TaggedObject) {
                arrayList.add(new X509AttributeCertificateHolder(AttributeCertificate.getInstance(((ASN1TaggedObject) aSN1Primitive).getObject())));
            }
        }
        return new CollectionStore(arrayList);
    }

    Store getCRLs(ASN1Set aSN1Set) {
        if (aSN1Set == null) {
            return new CollectionStore(new ArrayList());
        }
        ArrayList arrayList = new ArrayList(aSN1Set.size());
        Enumeration objects = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable) objects.nextElement()).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1Sequence) {
                arrayList.add(new X509CRLHolder(CertificateList.getInstance(aSN1Primitive)));
            }
        }
        return new CollectionStore(arrayList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v6, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    Store getOtherRevocationInfo(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Set aSN1Set) {
        if (aSN1Set == null) {
            return new CollectionStore(new ArrayList());
        }
        ArrayList arrayList = new ArrayList(aSN1Set.size());
        Enumeration objects = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable) objects.nextElement()).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Primitive);
                if (aSN1TaggedObject.getTagNo() == 1) {
                    OtherRevocationInfoFormat otherRevocationInfoFormat = OtherRevocationInfoFormat.getInstance(aSN1TaggedObject, false);
                    if (aSN1ObjectIdentifier.equals((ASN1Primitive) otherRevocationInfoFormat.getInfoFormat())) {
                        arrayList.add(otherRevocationInfoFormat.getInfo());
                    }
                }
            }
        }
        return new CollectionStore(arrayList);
    }

    static {
        addEntries(NISTObjectIdentifiers.dsa_with_sha224, "DSA");
        addEntries(NISTObjectIdentifiers.dsa_with_sha256, "DSA");
        addEntries(NISTObjectIdentifiers.dsa_with_sha384, "DSA");
        addEntries(NISTObjectIdentifiers.dsa_with_sha512, "DSA");
        addEntries(NISTObjectIdentifiers.id_dsa_with_sha3_224, "DSA");
        addEntries(NISTObjectIdentifiers.id_dsa_with_sha3_256, "DSA");
        addEntries(NISTObjectIdentifiers.id_dsa_with_sha3_384, "DSA");
        addEntries(NISTObjectIdentifiers.id_dsa_with_sha3_512, "DSA");
        addEntries(OIWObjectIdentifiers.dsaWithSHA1, "DSA");
        addEntries(OIWObjectIdentifiers.md4WithRSA, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(OIWObjectIdentifiers.md4WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(OIWObjectIdentifiers.md5WithRSA, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(OIWObjectIdentifiers.sha1WithRSA, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.md2WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.md4WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.md5WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.sha1WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.sha224WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.sha256WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.sha384WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.sha512WithRSAEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_224, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_256, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_384, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_512, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(X9ObjectIdentifiers.ecdsa_with_SHA1, "ECDSA");
        addEntries(X9ObjectIdentifiers.ecdsa_with_SHA224, "ECDSA");
        addEntries(X9ObjectIdentifiers.ecdsa_with_SHA256, "ECDSA");
        addEntries(X9ObjectIdentifiers.ecdsa_with_SHA384, "ECDSA");
        addEntries(X9ObjectIdentifiers.ecdsa_with_SHA512, "ECDSA");
        addEntries(NISTObjectIdentifiers.id_ecdsa_with_sha3_224, "ECDSA");
        addEntries(NISTObjectIdentifiers.id_ecdsa_with_sha3_256, "ECDSA");
        addEntries(NISTObjectIdentifiers.id_ecdsa_with_sha3_384, "ECDSA");
        addEntries(NISTObjectIdentifiers.id_ecdsa_with_sha3_512, "ECDSA");
        addEntries(X9ObjectIdentifiers.id_dsa_with_sha1, "DSA");
        addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_1, "ECDSA");
        addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_224, "ECDSA");
        addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_256, "ECDSA");
        addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_384, "ECDSA");
        addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_512, "ECDSA");
        addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_1, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_256, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_1, "RSAandMGF1");
        addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_256, "RSAandMGF1");
        addEntries(X9ObjectIdentifiers.id_dsa, "DSA");
        addEntries(PKCSObjectIdentifiers.rsaEncryption, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(X509ObjectIdentifiers.id_ea_rsa, RSAUtils.RSA_KEY_ALGORITHM);
        addEntries(PKCSObjectIdentifiers.id_RSASSA_PSS, "RSAandMGF1");
        addEntries(CryptoProObjectIdentifiers.gostR3410_94, "GOST3410");
        addEntries(CryptoProObjectIdentifiers.gostR3410_2001, "ECGOST3410");
        addEntries(new ASN1ObjectIdentifier("1.3.6.1.4.1.5849.1.6.2"), "ECGOST3410");
        addEntries(new ASN1ObjectIdentifier("1.3.6.1.4.1.5849.1.1.5"), "GOST3410");
        addEntries(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256, "ECGOST3410-2012-256");
        addEntries(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512, "ECGOST3410-2012-512");
        addEntries(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, "ECGOST3410");
        addEntries(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3410");
        addEntries(RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_256, "ECGOST3410-2012-256");
        addEntries(RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_512, "ECGOST3410-2012-512");
    }
}

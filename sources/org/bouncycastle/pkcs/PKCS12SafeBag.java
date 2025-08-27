package org.bouncycastle.pkcs;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.CRLBag;
import org.bouncycastle.asn1.pkcs.CertBag;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.SafeBag;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/pkcs/PKCS12SafeBag.class */
public class PKCS12SafeBag {
    public static final ASN1ObjectIdentifier friendlyNameAttribute = PKCSObjectIdentifiers.pkcs_9_at_friendlyName;
    public static final ASN1ObjectIdentifier localKeyIdAttribute = PKCSObjectIdentifiers.pkcs_9_at_localKeyId;
    private SafeBag safeBag;

    public PKCS12SafeBag(SafeBag safeBag) {
        this.safeBag = safeBag;
    }

    public SafeBag toASN1Structure() {
        return this.safeBag;
    }

    public ASN1ObjectIdentifier getType() {
        return this.safeBag.getBagId();
    }

    public Attribute[] getAttributes() {
        ASN1Set bagAttributes = this.safeBag.getBagAttributes();
        if (bagAttributes == null) {
            return null;
        }
        Attribute[] attributeArr = new Attribute[bagAttributes.size()];
        for (int i = 0; i != bagAttributes.size(); i++) {
            attributeArr[i] = Attribute.getInstance(bagAttributes.getObjectAt(i));
        }
        return attributeArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v1, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v3, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    public Object getBagValue() {
        return getType().equals((ASN1Primitive) PKCSObjectIdentifiers.pkcs8ShroudedKeyBag) ? new PKCS8EncryptedPrivateKeyInfo(EncryptedPrivateKeyInfo.getInstance(this.safeBag.getBagValue())) : getType().equals((ASN1Primitive) PKCSObjectIdentifiers.certBag) ? new X509CertificateHolder(Certificate.getInstance(ASN1OctetString.getInstance(CertBag.getInstance(this.safeBag.getBagValue()).getCertValue()).getOctets())) : getType().equals((ASN1Primitive) PKCSObjectIdentifiers.keyBag) ? PrivateKeyInfo.getInstance(this.safeBag.getBagValue()) : getType().equals((ASN1Primitive) PKCSObjectIdentifiers.crlBag) ? new X509CRLHolder(CertificateList.getInstance(ASN1OctetString.getInstance(CRLBag.getInstance(this.safeBag.getBagValue()).getCrlValue()).getOctets())) : this.safeBag.getBagValue();
    }
}

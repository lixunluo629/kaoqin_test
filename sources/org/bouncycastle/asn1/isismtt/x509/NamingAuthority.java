package org.bouncycastle.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;
import org.bouncycastle.asn1.x500.DirectoryString;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/isismtt/x509/NamingAuthority.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/isismtt/x509/NamingAuthority.class */
public class NamingAuthority extends ASN1Encodable {
    public static final DERObjectIdentifier id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern = new DERObjectIdentifier(ISISMTTObjectIdentifiers.id_isismtt_at_namingAuthorities + ".1");
    private DERObjectIdentifier namingAuthorityId;
    private String namingAuthorityUrl;
    private DirectoryString namingAuthorityText;

    public static NamingAuthority getInstance(Object obj) {
        if (obj == null || (obj instanceof NamingAuthority)) {
            return (NamingAuthority) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new NamingAuthority((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static NamingAuthority getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    private NamingAuthority(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration objects = aSN1Sequence.getObjects();
        if (objects.hasMoreElements()) {
            DEREncodable dEREncodable = (DEREncodable) objects.nextElement();
            if (dEREncodable instanceof DERObjectIdentifier) {
                this.namingAuthorityId = (DERObjectIdentifier) dEREncodable;
            } else if (dEREncodable instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(dEREncodable).getString();
            } else {
                if (!(dEREncodable instanceof ASN1String)) {
                    throw new IllegalArgumentException("Bad object encountered: " + dEREncodable.getClass());
                }
                this.namingAuthorityText = DirectoryString.getInstance(dEREncodable);
            }
        }
        if (objects.hasMoreElements()) {
            DEREncodable dEREncodable2 = (DEREncodable) objects.nextElement();
            if (dEREncodable2 instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(dEREncodable2).getString();
            } else {
                if (!(dEREncodable2 instanceof ASN1String)) {
                    throw new IllegalArgumentException("Bad object encountered: " + dEREncodable2.getClass());
                }
                this.namingAuthorityText = DirectoryString.getInstance(dEREncodable2);
            }
        }
        if (objects.hasMoreElements()) {
            DEREncodable dEREncodable3 = (DEREncodable) objects.nextElement();
            if (!(dEREncodable3 instanceof ASN1String)) {
                throw new IllegalArgumentException("Bad object encountered: " + dEREncodable3.getClass());
            }
            this.namingAuthorityText = DirectoryString.getInstance(dEREncodable3);
        }
    }

    public DERObjectIdentifier getNamingAuthorityId() {
        return this.namingAuthorityId;
    }

    public DirectoryString getNamingAuthorityText() {
        return this.namingAuthorityText;
    }

    public String getNamingAuthorityUrl() {
        return this.namingAuthorityUrl;
    }

    public NamingAuthority(DERObjectIdentifier dERObjectIdentifier, String str, DirectoryString directoryString) {
        this.namingAuthorityId = dERObjectIdentifier;
        this.namingAuthorityUrl = str;
        this.namingAuthorityText = directoryString;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.namingAuthorityId != null) {
            aSN1EncodableVector.add(this.namingAuthorityId);
        }
        if (this.namingAuthorityUrl != null) {
            aSN1EncodableVector.add(new DERIA5String(this.namingAuthorityUrl, true));
        }
        if (this.namingAuthorityText != null) {
            aSN1EncodableVector.add(this.namingAuthorityText);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

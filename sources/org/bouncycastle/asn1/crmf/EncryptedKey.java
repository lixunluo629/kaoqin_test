package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.EnvelopedData;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/crmf/EncryptedKey.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/crmf/EncryptedKey.class */
public class EncryptedKey extends ASN1Encodable implements ASN1Choice {
    private EnvelopedData envelopedData;
    private EncryptedValue encryptedValue;

    public static EncryptedKey getInstance(Object obj) {
        return obj instanceof EncryptedKey ? (EncryptedKey) obj : obj instanceof ASN1TaggedObject ? new EncryptedKey(EnvelopedData.getInstance((ASN1TaggedObject) obj, false)) : obj instanceof EncryptedValue ? new EncryptedKey((EncryptedValue) obj) : new EncryptedKey(EncryptedValue.getInstance(obj));
    }

    public EncryptedKey(EnvelopedData envelopedData) {
        this.envelopedData = envelopedData;
    }

    public EncryptedKey(EncryptedValue encryptedValue) {
        this.encryptedValue = encryptedValue;
    }

    public boolean isEncryptedValue() {
        return this.encryptedValue != null;
    }

    public ASN1Encodable getValue() {
        return this.encryptedValue != null ? this.encryptedValue : this.envelopedData;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.encryptedValue != null ? this.encryptedValue.toASN1Object() : new DERTaggedObject(false, 0, this.envelopedData);
    }
}

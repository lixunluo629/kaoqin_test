package org.bouncycastle.asn1.tsp;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509Extensions;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/TSTInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/tsp/TSTInfo.class */
public class TSTInfo extends ASN1Encodable {
    DERInteger version;
    DERObjectIdentifier tsaPolicyId;
    MessageImprint messageImprint;
    DERInteger serialNumber;
    DERGeneralizedTime genTime;
    Accuracy accuracy;
    DERBoolean ordering;
    DERInteger nonce;
    GeneralName tsa;
    X509Extensions extensions;

    public static TSTInfo getInstance(Object obj) {
        if (obj == null || (obj instanceof TSTInfo)) {
            return (TSTInfo) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new TSTInfo((ASN1Sequence) obj);
        }
        if (!(obj instanceof ASN1OctetString)) {
            throw new IllegalArgumentException("Unknown object in 'TSTInfo' factory : " + obj.getClass().getName() + ".");
        }
        try {
            return getInstance(new ASN1InputStream(((ASN1OctetString) obj).getOctets()).readObject());
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad object format in 'TSTInfo' factory.");
        }
    }

    public TSTInfo(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.version = DERInteger.getInstance(objects.nextElement());
        this.tsaPolicyId = DERObjectIdentifier.getInstance(objects.nextElement());
        this.messageImprint = MessageImprint.getInstance(objects.nextElement());
        this.serialNumber = DERInteger.getInstance(objects.nextElement());
        this.genTime = DERGeneralizedTime.getInstance(objects.nextElement());
        this.ordering = new DERBoolean(false);
        while (objects.hasMoreElements()) {
            DERObject dERObject = (DERObject) objects.nextElement();
            if (dERObject instanceof ASN1TaggedObject) {
                DERTaggedObject dERTaggedObject = (DERTaggedObject) dERObject;
                switch (dERTaggedObject.getTagNo()) {
                    case 0:
                        this.tsa = GeneralName.getInstance(dERTaggedObject, true);
                        break;
                    case 1:
                        this.extensions = X509Extensions.getInstance(dERTaggedObject, false);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown tag value " + dERTaggedObject.getTagNo());
                }
            } else if (dERObject instanceof DERSequence) {
                this.accuracy = Accuracy.getInstance(dERObject);
            } else if (dERObject instanceof DERBoolean) {
                this.ordering = DERBoolean.getInstance(dERObject);
            } else if (dERObject instanceof DERInteger) {
                this.nonce = DERInteger.getInstance(dERObject);
            }
        }
    }

    public TSTInfo(DERObjectIdentifier dERObjectIdentifier, MessageImprint messageImprint, DERInteger dERInteger, DERGeneralizedTime dERGeneralizedTime, Accuracy accuracy, DERBoolean dERBoolean, DERInteger dERInteger2, GeneralName generalName, X509Extensions x509Extensions) {
        this.version = new DERInteger(1);
        this.tsaPolicyId = dERObjectIdentifier;
        this.messageImprint = messageImprint;
        this.serialNumber = dERInteger;
        this.genTime = dERGeneralizedTime;
        this.accuracy = accuracy;
        this.ordering = dERBoolean;
        this.nonce = dERInteger2;
        this.tsa = generalName;
        this.extensions = x509Extensions;
    }

    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }

    public DERObjectIdentifier getPolicy() {
        return this.tsaPolicyId;
    }

    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }

    public Accuracy getAccuracy() {
        return this.accuracy;
    }

    public DERGeneralizedTime getGenTime() {
        return this.genTime;
    }

    public DERBoolean getOrdering() {
        return this.ordering;
    }

    public DERInteger getNonce() {
        return this.nonce;
    }

    public GeneralName getTsa() {
        return this.tsa;
    }

    public X509Extensions getExtensions() {
        return this.extensions;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.tsaPolicyId);
        aSN1EncodableVector.add(this.messageImprint);
        aSN1EncodableVector.add(this.serialNumber);
        aSN1EncodableVector.add(this.genTime);
        if (this.accuracy != null) {
            aSN1EncodableVector.add(this.accuracy);
        }
        if (this.ordering != null && this.ordering.isTrue()) {
            aSN1EncodableVector.add(this.ordering);
        }
        if (this.nonce != null) {
            aSN1EncodableVector.add(this.nonce);
        }
        if (this.tsa != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.tsa));
        }
        if (this.extensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.extensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cms/TimeStampedData.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cms/TimeStampedData.class */
public class TimeStampedData extends ASN1Encodable {
    private DERInteger version;
    private DERIA5String dataUri;
    private MetaData metaData;
    private ASN1OctetString content;
    private Evidence temporalEvidence;

    public TimeStampedData(DERIA5String dERIA5String, MetaData metaData, ASN1OctetString aSN1OctetString, Evidence evidence) {
        this.version = new DERInteger(1);
        this.dataUri = dERIA5String;
        this.metaData = metaData;
        this.content = aSN1OctetString;
        this.temporalEvidence = evidence;
    }

    private TimeStampedData(ASN1Sequence aSN1Sequence) {
        this.version = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        int i = 1;
        if (aSN1Sequence.getObjectAt(1) instanceof DERIA5String) {
            i = 1 + 1;
            this.dataUri = DERIA5String.getInstance(aSN1Sequence.getObjectAt(1));
        }
        if ((aSN1Sequence.getObjectAt(i) instanceof MetaData) || (aSN1Sequence.getObjectAt(i) instanceof ASN1Sequence)) {
            int i2 = i;
            i++;
            this.metaData = MetaData.getInstance(aSN1Sequence.getObjectAt(i2));
        }
        if (aSN1Sequence.getObjectAt(i) instanceof ASN1OctetString) {
            int i3 = i;
            i++;
            this.content = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(i3));
        }
        this.temporalEvidence = Evidence.getInstance(aSN1Sequence.getObjectAt(i));
    }

    public static TimeStampedData getInstance(Object obj) {
        if (obj instanceof TimeStampedData) {
            return (TimeStampedData) obj;
        }
        if (obj != null) {
            return new TimeStampedData(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public DERIA5String getDataUri() {
        return this.dataUri;
    }

    public MetaData getMetaData() {
        return this.metaData;
    }

    public ASN1OctetString getContent() {
        return this.content;
    }

    public Evidence getTemporalEvidence() {
        return this.temporalEvidence;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        if (this.dataUri != null) {
            aSN1EncodableVector.add(this.dataUri);
        }
        if (this.metaData != null) {
            aSN1EncodableVector.add(this.metaData);
        }
        if (this.content != null) {
            aSN1EncodableVector.add(this.content);
        }
        aSN1EncodableVector.add(this.temporalEvidence);
        return new BERSequence(aSN1EncodableVector);
    }
}

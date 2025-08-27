package org.bouncycastle.asn1.dvcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/dvcs/DVCSRequest.class */
public class DVCSRequest extends ASN1Object {
    private DVCSRequestInformation requestInformation;
    private Data data;
    private GeneralName transactionIdentifier;

    public DVCSRequest(DVCSRequestInformation dVCSRequestInformation, Data data) {
        this(dVCSRequestInformation, data, null);
    }

    public DVCSRequest(DVCSRequestInformation dVCSRequestInformation, Data data, GeneralName generalName) {
        this.requestInformation = dVCSRequestInformation;
        this.data = data;
        this.transactionIdentifier = generalName;
    }

    private DVCSRequest(ASN1Sequence aSN1Sequence) {
        this.requestInformation = DVCSRequestInformation.getInstance(aSN1Sequence.getObjectAt(0));
        this.data = Data.getInstance(aSN1Sequence.getObjectAt(1));
        if (aSN1Sequence.size() > 2) {
            this.transactionIdentifier = GeneralName.getInstance(aSN1Sequence.getObjectAt(2));
        }
    }

    public static DVCSRequest getInstance(Object obj) {
        if (obj instanceof DVCSRequest) {
            return (DVCSRequest) obj;
        }
        if (obj != null) {
            return new DVCSRequest(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public static DVCSRequest getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
        aSN1EncodableVector.add((ASN1Encodable) this.requestInformation);
        aSN1EncodableVector.add((ASN1Encodable) this.data);
        if (this.transactionIdentifier != null) {
            aSN1EncodableVector.add((ASN1Encodable) this.transactionIdentifier);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "DVCSRequest {\nrequestInformation: " + this.requestInformation + "\ndata: " + this.data + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + (this.transactionIdentifier != null ? "transactionIdentifier: " + this.transactionIdentifier + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR : "") + "}\n";
    }

    public Data getData() {
        return this.data;
    }

    public DVCSRequestInformation getRequestInformation() {
        return this.requestInformation;
    }

    public GeneralName getTransactionIdentifier() {
        return this.transactionIdentifier;
    }
}

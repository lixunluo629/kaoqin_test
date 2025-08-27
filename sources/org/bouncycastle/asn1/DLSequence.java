package org.bouncycastle.asn1;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DLSequence.class */
public class DLSequence extends ASN1Sequence {
    private int bodyLength;

    public DLSequence() {
        this.bodyLength = -1;
    }

    public DLSequence(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
        this.bodyLength = -1;
    }

    public DLSequence(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector);
        this.bodyLength = -1;
    }

    public DLSequence(ASN1Encodable[] aSN1EncodableArr) {
        super(aSN1EncodableArr);
        this.bodyLength = -1;
    }

    DLSequence(ASN1Encodable[] aSN1EncodableArr, boolean z) {
        super(aSN1EncodableArr, z);
        this.bodyLength = -1;
    }

    private int getBodyLength() throws IOException {
        if (this.bodyLength < 0) {
            int length = this.elements.length;
            int iEncodedLength = 0;
            for (int i = 0; i < length; i++) {
                iEncodedLength += this.elements[i].toASN1Primitive().toDLObject().encodedLength();
            }
            this.bodyLength = iEncodedLength;
        }
        return this.bodyLength;
    }

    int encodedLength() throws IOException {
        int bodyLength = getBodyLength();
        return 1 + StreamUtil.calculateBodyLength(bodyLength) + bodyLength;
    }

    void encode(ASN1OutputStream aSN1OutputStream, boolean z) throws IOException {
        if (z) {
            aSN1OutputStream.write(48);
        }
        ASN1OutputStream dLSubStream = aSN1OutputStream.getDLSubStream();
        int length = this.elements.length;
        if (this.bodyLength >= 0 || length > 16) {
            aSN1OutputStream.writeLength(getBodyLength());
            for (int i = 0; i < length; i++) {
                dLSubStream.writePrimitive(this.elements[i].toASN1Primitive(), true);
            }
            return;
        }
        int iEncodedLength = 0;
        ASN1Primitive[] aSN1PrimitiveArr = new ASN1Primitive[length];
        for (int i2 = 0; i2 < length; i2++) {
            ASN1Primitive dLObject = this.elements[i2].toASN1Primitive().toDLObject();
            aSN1PrimitiveArr[i2] = dLObject;
            iEncodedLength += dLObject.encodedLength();
        }
        this.bodyLength = iEncodedLength;
        aSN1OutputStream.writeLength(iEncodedLength);
        for (int i3 = 0; i3 < length; i3++) {
            dLSubStream.writePrimitive(aSN1PrimitiveArr[i3], true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    ASN1Primitive toDLObject() {
        return this;
    }
}

package org.bouncycastle.asn1;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DLTaggedObject.class */
public class DLTaggedObject extends ASN1TaggedObject {
    public DLTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
    }

    boolean isConstructed() {
        return this.explicit || this.obj.toASN1Primitive().toDLObject().isConstructed();
    }

    int encodedLength() throws IOException {
        int iEncodedLength = this.obj.toASN1Primitive().toDLObject().encodedLength();
        if (this.explicit) {
            return StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(iEncodedLength) + iEncodedLength;
        }
        return StreamUtil.calculateTagLength(this.tagNo) + (iEncodedLength - 1);
    }

    void encode(ASN1OutputStream aSN1OutputStream, boolean z) throws IOException {
        ASN1Primitive dLObject = this.obj.toASN1Primitive().toDLObject();
        int i = 128;
        if (this.explicit || dLObject.isConstructed()) {
            i = 128 | 32;
        }
        aSN1OutputStream.writeTag(z, i, this.tagNo);
        if (this.explicit) {
            aSN1OutputStream.writeLength(dLObject.encodedLength());
        }
        aSN1OutputStream.getDLSubStream().writePrimitive(dLObject, this.explicit);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ASN1Primitive toDLObject() {
        return this;
    }
}

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/LazyEncodedSequence.class */
class LazyEncodedSequence extends ASN1Sequence {
    private byte[] encoded;

    LazyEncodedSequence(byte[] bArr) throws IOException {
        this.encoded = bArr;
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public synchronized ASN1Encodable getObjectAt(int i) {
        force();
        return super.getObjectAt(i);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public synchronized Enumeration getObjects() {
        return null != this.encoded ? new LazyConstructionEnumeration(this.encoded) : super.getObjects();
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public synchronized int hashCode() {
        force();
        return super.hashCode();
    }

    public synchronized Iterator<ASN1Encodable> iterator() {
        force();
        return super.iterator();
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public synchronized int size() {
        force();
        return super.size();
    }

    public synchronized ASN1Encodable[] toArray() {
        force();
        return super.toArray();
    }

    ASN1Encodable[] toArrayInternal() {
        force();
        return super.toArrayInternal();
    }

    synchronized int encodedLength() throws IOException {
        return null != this.encoded ? 1 + StreamUtil.calculateBodyLength(this.encoded.length) + this.encoded.length : super.toDLObject().encodedLength();
    }

    synchronized void encode(ASN1OutputStream aSN1OutputStream, boolean z) throws IOException {
        if (null != this.encoded) {
            aSN1OutputStream.writeEncoded(z, 48, this.encoded);
        } else {
            super.toDLObject().encode(aSN1OutputStream, z);
        }
    }

    synchronized ASN1Primitive toDERObject() {
        force();
        return super.toDERObject();
    }

    synchronized ASN1Primitive toDLObject() {
        force();
        return super.toDLObject();
    }

    private void force() {
        if (null != this.encoded) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            LazyConstructionEnumeration lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
            while (lazyConstructionEnumeration.hasMoreElements()) {
                aSN1EncodableVector.add((ASN1Encodable) lazyConstructionEnumeration.nextElement());
            }
            this.elements = aSN1EncodableVector.takeElements();
            this.encoded = null;
        }
    }
}

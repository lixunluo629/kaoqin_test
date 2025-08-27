package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/LazyDERSequence.class */
public class LazyDERSequence extends DERSequence {
    private byte[] encoded;
    private boolean parsed = false;
    private int size = -1;

    LazyDERSequence(byte[] bArr) throws IOException {
        this.encoded = bArr;
    }

    private void parse() {
        LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
        while (lazyDERConstructionEnumeration.hasMoreElements()) {
            addObject((DEREncodable) lazyDERConstructionEnumeration.nextElement());
        }
        this.parsed = true;
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public synchronized DEREncodable getObjectAt(int i) {
        if (!this.parsed) {
            parse();
        }
        return super.getObjectAt(i);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public synchronized Enumeration getObjects() {
        return this.parsed ? super.getObjects() : new LazyDERConstructionEnumeration(this.encoded);
    }

    @Override // org.bouncycastle.asn1.ASN1Sequence
    public int size() {
        if (this.size < 0) {
            LazyDERConstructionEnumeration lazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
            this.size = 0;
            while (lazyDERConstructionEnumeration.hasMoreElements()) {
                lazyDERConstructionEnumeration.nextElement();
                this.size++;
            }
        }
        return this.size;
    }

    @Override // org.bouncycastle.asn1.DERSequence, org.bouncycastle.asn1.ASN1Sequence, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(48, this.encoded);
    }
}

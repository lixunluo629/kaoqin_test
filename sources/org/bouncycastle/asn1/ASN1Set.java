package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Set.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Set.class */
public abstract class ASN1Set extends ASN1Object {
    protected Vector set = new Vector();

    /* renamed from: org.bouncycastle.asn1.ASN1Set$2, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Set$2.class */
    class AnonymousClass2 implements ASN1SetParser {
        private int pos = 0;
        final /* synthetic */ int val$count;

        AnonymousClass2(int i) {
            this.val$count = i;
        }

        /* JADX WARN: Type inference failed for: r0v13, types: [org.bouncycastle.asn1.ASN1Encodable, org.bouncycastle.asn1.ASN1SetParser] */
        /* JADX WARN: Type inference failed for: r0v16, types: [org.bouncycastle.asn1.ASN1Encodable, org.bouncycastle.asn1.ASN1SequenceParser] */
        @Override // org.bouncycastle.asn1.ASN1SetParser
        public ASN1Encodable readObject() throws IOException {
            if (this.val$count == this.pos) {
                return null;
            }
            ASN1Encodable[] aSN1EncodableArr = ASN1Set.this.elements;
            int i = this.pos;
            this.pos = i + 1;
            ASN1Encodable aSN1Encodable = aSN1EncodableArr[i];
            return aSN1Encodable instanceof ASN1Sequence ? ((ASN1Sequence) aSN1Encodable).parser() : aSN1Encodable instanceof ASN1Set ? ((ASN1Set) aSN1Encodable).parser() : aSN1Encodable;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Set] */
        @Override // org.bouncycastle.asn1.InMemoryRepresentable
        public ASN1Primitive getLoadedObject() {
            return ASN1Set.this;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Set] */
        public ASN1Primitive toASN1Primitive() {
            return ASN1Set.this;
        }
    }

    public static ASN1Set getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Set)) {
            return (ASN1Set) obj;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Set getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return (ASN1Set) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        }
        if (aSN1TaggedObject.isExplicit()) {
            return new DERSet(aSN1TaggedObject.getObject());
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Set) {
            return (ASN1Set) aSN1TaggedObject.getObject();
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (!(aSN1TaggedObject.getObject() instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
        }
        Enumeration objects = ((ASN1Sequence) aSN1TaggedObject.getObject()).getObjects();
        while (objects.hasMoreElements()) {
            aSN1EncodableVector.add((DEREncodable) objects.nextElement());
        }
        return new DERSet(aSN1EncodableVector, false);
    }

    public Enumeration getObjects() {
        return this.set.elements();
    }

    public DEREncodable getObjectAt(int i) {
        return (DEREncodable) this.set.elementAt(i);
    }

    public int size() {
        return this.set.size();
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] aSN1EncodableArr = new ASN1Encodable[size()];
        for (int i = 0; i != size(); i++) {
            aSN1EncodableArr[i] = (ASN1Encodable) getObjectAt(i);
        }
        return aSN1EncodableArr;
    }

    public ASN1SetParser parser() {
        return new ASN1SetParser() { // from class: org.bouncycastle.asn1.ASN1Set.1
            private final int max;
            private int index;

            {
                this.max = ASN1Set.this.size();
            }

            @Override // org.bouncycastle.asn1.ASN1SetParser
            public DEREncodable readObject() throws IOException {
                if (this.index == this.max) {
                    return null;
                }
                ASN1Set aSN1Set = ASN1Set.this;
                int i = this.index;
                this.index = i + 1;
                DEREncodable objectAt = aSN1Set.getObjectAt(i);
                return objectAt instanceof ASN1Sequence ? ((ASN1Sequence) objectAt).parser() : objectAt instanceof ASN1Set ? ((ASN1Set) objectAt).parser() : objectAt;
            }

            @Override // org.bouncycastle.asn1.InMemoryRepresentable
            public DERObject getLoadedObject() {
                return this;
            }

            @Override // org.bouncycastle.asn1.DEREncodable
            public DERObject getDERObject() {
                return this;
            }
        };
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        Enumeration objects = getObjects();
        int size = size();
        while (true) {
            int i = size;
            if (!objects.hasMoreElements()) {
                return i;
            }
            size = (i * 17) ^ getNext(objects).hashCode();
        }
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (!(dERObject instanceof ASN1Set)) {
            return false;
        }
        ASN1Set aSN1Set = (ASN1Set) dERObject;
        if (size() != aSN1Set.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            DEREncodable next = getNext(objects);
            DEREncodable next2 = getNext(objects2);
            DERObject dERObject2 = next.getDERObject();
            DERObject dERObject3 = next2.getDERObject();
            if (dERObject2 != dERObject3 && !dERObject2.equals(dERObject3)) {
                return false;
            }
        }
        return true;
    }

    private DEREncodable getNext(Enumeration enumeration) {
        DEREncodable dEREncodable = (DEREncodable) enumeration.nextElement();
        return dEREncodable == null ? DERNull.INSTANCE : dEREncodable;
    }

    private boolean lessThanOrEqual(byte[] bArr, byte[] bArr2) {
        int iMin = Math.min(bArr.length, bArr2.length);
        for (int i = 0; i != iMin; i++) {
            if (bArr[i] != bArr2[i]) {
                return (bArr[i] & 255) < (bArr2[i] & 255);
            }
        }
        return iMin == bArr.length;
    }

    private byte[] getEncoded(DEREncodable dEREncodable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ASN1OutputStream(byteArrayOutputStream).writeObject(dEREncodable);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }

    protected void sort() {
        if (this.set.size() <= 1) {
            return;
        }
        boolean z = true;
        int size = this.set.size() - 1;
        while (true) {
            int i = size;
            if (!z) {
                return;
            }
            int i2 = 0;
            byte[] encoded = getEncoded((DEREncodable) this.set.elementAt(0));
            z = false;
            for (int i3 = 0; i3 != i; i3++) {
                byte[] encoded2 = getEncoded((DEREncodable) this.set.elementAt(i3 + 1));
                if (lessThanOrEqual(encoded, encoded2)) {
                    encoded = encoded2;
                } else {
                    Object objElementAt = this.set.elementAt(i3);
                    this.set.setElementAt(this.set.elementAt(i3 + 1), i3);
                    this.set.setElementAt(objElementAt, i3 + 1);
                    z = true;
                    i2 = i3;
                }
            }
            size = i2;
        }
    }

    protected void addObject(DEREncodable dEREncodable) {
        this.set.addElement(dEREncodable);
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    public String toString() {
        return this.set.toString();
    }
}

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Sequence.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Sequence.class */
public abstract class ASN1Sequence extends ASN1Object {
    private Vector seq = new Vector();

    /* renamed from: org.bouncycastle.asn1.ASN1Sequence$2, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Sequence$2.class */
    class AnonymousClass2 implements ASN1SequenceParser {
        private int pos = 0;
        final /* synthetic */ int val$count;

        AnonymousClass2(int i) {
            this.val$count = i;
        }

        /* JADX WARN: Type inference failed for: r0v13, types: [org.bouncycastle.asn1.ASN1Encodable, org.bouncycastle.asn1.ASN1SetParser] */
        /* JADX WARN: Type inference failed for: r0v16, types: [org.bouncycastle.asn1.ASN1Encodable, org.bouncycastle.asn1.ASN1SequenceParser] */
        @Override // org.bouncycastle.asn1.ASN1SequenceParser
        public ASN1Encodable readObject() throws IOException {
            if (this.val$count == this.pos) {
                return null;
            }
            ASN1Encodable[] aSN1EncodableArr = ASN1Sequence.this.elements;
            int i = this.pos;
            this.pos = i + 1;
            ASN1Encodable aSN1Encodable = aSN1EncodableArr[i];
            return aSN1Encodable instanceof ASN1Sequence ? ((ASN1Sequence) aSN1Encodable).parser() : aSN1Encodable instanceof ASN1Set ? ((ASN1Set) aSN1Encodable).parser() : aSN1Encodable;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Sequence] */
        @Override // org.bouncycastle.asn1.InMemoryRepresentable
        public ASN1Primitive getLoadedObject() {
            return ASN1Sequence.this;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Sequence] */
        public ASN1Primitive toASN1Primitive() {
            return ASN1Sequence.this;
        }
    }

    public static ASN1Sequence getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Sequence)) {
            return (ASN1Sequence) obj;
        }
        if (!(obj instanceof byte[])) {
            throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
        }
        try {
            return getInstance(ASN1Object.fromByteArray((byte[]) obj));
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to construct sequence from byte[]: " + e.getMessage());
        }
    }

    public static ASN1Sequence getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        if (z) {
            if (aSN1TaggedObject.isExplicit()) {
                return (ASN1Sequence) aSN1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        }
        if (aSN1TaggedObject.isExplicit()) {
            return aSN1TaggedObject instanceof BERTaggedObject ? new BERSequence(aSN1TaggedObject.getObject()) : new DERSequence(aSN1TaggedObject.getObject());
        }
        if (aSN1TaggedObject.getObject() instanceof ASN1Sequence) {
            return (ASN1Sequence) aSN1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + aSN1TaggedObject.getClass().getName());
    }

    public Enumeration getObjects() {
        return this.seq.elements();
    }

    public ASN1SequenceParser parser() {
        return new ASN1SequenceParser() { // from class: org.bouncycastle.asn1.ASN1Sequence.1
            private final int max;
            private int index;

            {
                this.max = ASN1Sequence.this.size();
            }

            @Override // org.bouncycastle.asn1.ASN1SequenceParser
            public DEREncodable readObject() throws IOException {
                if (this.index == this.max) {
                    return null;
                }
                ASN1Sequence aSN1Sequence = ASN1Sequence.this;
                int i = this.index;
                this.index = i + 1;
                DEREncodable objectAt = aSN1Sequence.getObjectAt(i);
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

    public DEREncodable getObjectAt(int i) {
        return (DEREncodable) this.seq.elementAt(i);
    }

    public int size() {
        return this.seq.size();
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
        if (!(dERObject instanceof ASN1Sequence)) {
            return false;
        }
        ASN1Sequence aSN1Sequence = (ASN1Sequence) dERObject;
        if (size() != aSN1Sequence.size()) {
            return false;
        }
        Enumeration objects = getObjects();
        Enumeration objects2 = aSN1Sequence.getObjects();
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

    protected void addObject(DEREncodable dEREncodable) {
        this.seq.addElement(dEREncodable);
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    public String toString() {
        return this.seq.toString();
    }
}

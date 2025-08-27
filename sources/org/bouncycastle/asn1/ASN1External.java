package org.bouncycastle.asn1;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1External.class */
public abstract class ASN1External extends ASN1Primitive {
    protected ASN1ObjectIdentifier directReference;
    protected ASN1Integer indirectReference;
    protected ASN1Primitive dataValueDescriptor;
    protected int encoding;
    protected ASN1Primitive externalContent;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6 */
    public ASN1External(ASN1EncodableVector aSN1EncodableVector) {
        int i = 0;
        Object objFromVector = getObjFromVector(aSN1EncodableVector, 0);
        boolean z = objFromVector instanceof ASN1ObjectIdentifier;
        ?? objFromVector2 = objFromVector;
        if (z) {
            this.directReference = (ASN1ObjectIdentifier) objFromVector;
            i = 0 + 1;
            objFromVector2 = getObjFromVector(aSN1EncodableVector, i);
        }
        boolean z2 = (objFromVector2 == true ? 1 : 0) instanceof ASN1Integer;
        ?? objFromVector3 = objFromVector2;
        if (z2) {
            this.indirectReference = objFromVector2 == true ? 1 : 0;
            i++;
            objFromVector3 = getObjFromVector(aSN1EncodableVector, i);
        }
        boolean z3 = (objFromVector3 == true ? 1 : 0) instanceof ASN1TaggedObject;
        ASN1Primitive objFromVector4 = objFromVector3;
        if (!z3) {
            this.dataValueDescriptor = objFromVector3 == true ? 1 : 0;
            i++;
            objFromVector4 = getObjFromVector(aSN1EncodableVector, i);
        }
        if (aSN1EncodableVector.size() != i + 1) {
            throw new IllegalArgumentException("input vector too large");
        }
        if (!(objFromVector4 instanceof ASN1TaggedObject)) {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        }
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objFromVector4;
        setEncoding(aSN1TaggedObject.getTagNo());
        this.externalContent = aSN1TaggedObject.getObject();
    }

    private ASN1Primitive getObjFromVector(ASN1EncodableVector aSN1EncodableVector, int i) {
        if (aSN1EncodableVector.size() <= i) {
            throw new IllegalArgumentException("too few objects in input vector");
        }
        return aSN1EncodableVector.get(i).toASN1Primitive();
    }

    public ASN1External(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, DERTaggedObject dERTaggedObject) {
        this(aSN1ObjectIdentifier, aSN1Integer, aSN1Primitive, dERTaggedObject.getTagNo(), dERTaggedObject.toASN1Primitive());
    }

    public ASN1External(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, int i, ASN1Primitive aSN1Primitive2) {
        setDirectReference(aSN1ObjectIdentifier);
        setIndirectReference(aSN1Integer);
        setDataValueDescriptor(aSN1Primitive);
        setEncoding(i);
        setExternalContent(aSN1Primitive2.toASN1Primitive());
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERExternal] */
    @Override // org.bouncycastle.asn1.ASN1Primitive
    ASN1Primitive toDERObject() {
        return new DERExternal(this.directReference, this.indirectReference, this.dataValueDescriptor, this.encoding, this.externalContent);
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    ASN1Primitive toDLObject() {
        return new DLExternal(this.directReference, this.indirectReference, this.dataValueDescriptor, this.encoding, this.externalContent);
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        int iHashCode = 0;
        if (this.directReference != null) {
            iHashCode = this.directReference.hashCode();
        }
        if (this.indirectReference != null) {
            iHashCode ^= this.indirectReference.hashCode();
        }
        if (this.dataValueDescriptor != null) {
            iHashCode ^= this.dataValueDescriptor.hashCode();
        }
        return iHashCode ^ this.externalContent.hashCode();
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    boolean isConstructed() {
        return true;
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    int encodedLength() throws IOException {
        return getEncoded().length;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v6, types: [org.bouncycastle.asn1.ASN1Integer, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v8, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    @Override // org.bouncycastle.asn1.ASN1Primitive
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1External)) {
            return false;
        }
        if (this == aSN1Primitive) {
            return true;
        }
        ASN1External aSN1External = (ASN1External) aSN1Primitive;
        if (this.directReference != null && (aSN1External.directReference == null || !aSN1External.directReference.equals((ASN1Primitive) this.directReference))) {
            return false;
        }
        if (this.indirectReference != null && (aSN1External.indirectReference == null || !aSN1External.indirectReference.equals((ASN1Primitive) this.indirectReference))) {
            return false;
        }
        if (this.dataValueDescriptor == null || (aSN1External.dataValueDescriptor != null && aSN1External.dataValueDescriptor.equals(this.dataValueDescriptor))) {
            return this.externalContent.equals(aSN1External.externalContent);
        }
        return false;
    }

    public ASN1Primitive getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }

    public ASN1ObjectIdentifier getDirectReference() {
        return this.directReference;
    }

    public int getEncoding() {
        return this.encoding;
    }

    public ASN1Primitive getExternalContent() {
        return this.externalContent;
    }

    public ASN1Integer getIndirectReference() {
        return this.indirectReference;
    }

    private void setDataValueDescriptor(ASN1Primitive aSN1Primitive) {
        this.dataValueDescriptor = aSN1Primitive;
    }

    private void setDirectReference(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.directReference = aSN1ObjectIdentifier;
    }

    private void setEncoding(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("invalid encoding value: " + i);
        }
        this.encoding = i;
    }

    private void setExternalContent(ASN1Primitive aSN1Primitive) {
        this.externalContent = aSN1Primitive;
    }

    private void setIndirectReference(ASN1Integer aSN1Integer) {
        this.indirectReference = aSN1Integer;
    }
}

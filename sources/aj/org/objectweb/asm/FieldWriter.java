package aj.org.objectweb.asm;

import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstantAttribute;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/FieldWriter.class */
final class FieldWriter extends FieldVisitor {
    private final ClassWriter b;
    private final int c;
    private final int d;
    private final int e;
    private int f;
    private int g;
    private AnnotationWriter h;
    private AnnotationWriter i;
    private AnnotationWriter k;
    private AnnotationWriter l;
    private Attribute j;

    FieldWriter(ClassWriter classWriter, int i, String str, String str2, String str3, Object obj) {
        super(327680);
        if (classWriter.B == null) {
            classWriter.B = this;
        } else {
            classWriter.C.fv = this;
        }
        classWriter.C = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.f = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.g = classWriter.a(obj).a;
        }
    }

    @Override // aj.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.h;
            this.h = annotationWriter;
        } else {
            annotationWriter.g = this.i;
            this.i = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.a(i, typePath, byteVector);
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.k;
            this.k = annotationWriter;
        } else {
            annotationWriter.g = this.l;
            this.l = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        attribute.a = this.j;
        this.j = attribute;
    }

    @Override // aj.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
    }

    int a() {
        int iA = 8;
        if (this.g != 0) {
            this.b.newUTF8(ConstantAttribute.tag);
            iA = 8 + 8;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8(SyntheticAttribute.tag);
            iA += 6;
        }
        if ((this.c & 131072) != 0) {
            this.b.newUTF8(DeprecatedAttribute.tag);
            iA += 6;
        }
        if (this.f != 0) {
            this.b.newUTF8(SignatureAttribute.tag);
            iA += 8;
        }
        if (this.h != null) {
            this.b.newUTF8(AnnotationsAttribute.visibleTag);
            iA += 8 + this.h.a();
        }
        if (this.i != null) {
            this.b.newUTF8(AnnotationsAttribute.invisibleTag);
            iA += 8 + this.i.a();
        }
        if (this.k != null) {
            this.b.newUTF8(TypeAnnotationsAttribute.visibleTag);
            iA += 8 + this.k.a();
        }
        if (this.l != null) {
            this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag);
            iA += 8 + this.l.a();
        }
        if (this.j != null) {
            iA += this.j.a(this.b, null, 0, -1, -1);
        }
        return iA;
    }

    void a(ByteVector byteVector) {
        byteVector.putShort(this.c & ((393216 | ((this.c & 262144) / 64)) ^ (-1))).putShort(this.d).putShort(this.e);
        int iA = 0;
        if (this.g != 0) {
            iA = 0 + 1;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            iA++;
        }
        if ((this.c & 131072) != 0) {
            iA++;
        }
        if (this.f != 0) {
            iA++;
        }
        if (this.h != null) {
            iA++;
        }
        if (this.i != null) {
            iA++;
        }
        if (this.k != null) {
            iA++;
        }
        if (this.l != null) {
            iA++;
        }
        if (this.j != null) {
            iA += this.j.a();
        }
        byteVector.putShort(iA);
        if (this.g != 0) {
            byteVector.putShort(this.b.newUTF8(ConstantAttribute.tag));
            byteVector.putInt(2).putShort(this.g);
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            byteVector.putShort(this.b.newUTF8(SyntheticAttribute.tag)).putInt(0);
        }
        if ((this.c & 131072) != 0) {
            byteVector.putShort(this.b.newUTF8(DeprecatedAttribute.tag)).putInt(0);
        }
        if (this.f != 0) {
            byteVector.putShort(this.b.newUTF8(SignatureAttribute.tag));
            byteVector.putInt(2).putShort(this.f);
        }
        if (this.h != null) {
            byteVector.putShort(this.b.newUTF8(AnnotationsAttribute.visibleTag));
            this.h.a(byteVector);
        }
        if (this.i != null) {
            byteVector.putShort(this.b.newUTF8(AnnotationsAttribute.invisibleTag));
            this.i.a(byteVector);
        }
        if (this.k != null) {
            byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.visibleTag));
            this.k.a(byteVector);
        }
        if (this.l != null) {
            byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag));
            this.l.a(byteVector);
        }
        if (this.j != null) {
            this.j.a(this.b, null, 0, -1, -1, byteVector);
        }
    }
}

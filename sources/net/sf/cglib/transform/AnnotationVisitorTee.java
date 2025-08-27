package net.sf.cglib.transform;

import org.objectweb.asm.AnnotationVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/AnnotationVisitorTee.class */
public class AnnotationVisitorTee extends AnnotationVisitor {
    private AnnotationVisitor av1;
    private AnnotationVisitor av2;

    public static AnnotationVisitor getInstance(AnnotationVisitor av1, AnnotationVisitor av2) {
        if (av1 == null) {
            return av2;
        }
        if (av2 == null) {
            return av1;
        }
        return new AnnotationVisitorTee(av1, av2);
    }

    public AnnotationVisitorTee(AnnotationVisitor av1, AnnotationVisitor av2) {
        super(262144);
        this.av1 = av1;
        this.av2 = av2;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        this.av2.visit(name, value);
        this.av2.visit(name, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String desc, String value) {
        this.av1.visitEnum(name, desc, value);
        this.av2.visitEnum(name, desc, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return getInstance(this.av1.visitAnnotation(name, desc), this.av2.visitAnnotation(name, desc));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        return getInstance(this.av1.visitArray(name), this.av2.visitArray(name));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        this.av1.visitEnd();
        this.av2.visitEnd();
    }
}

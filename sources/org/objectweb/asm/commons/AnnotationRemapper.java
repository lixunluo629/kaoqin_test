package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;

/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/AnnotationRemapper.SCL.lombok */
public class AnnotationRemapper extends AnnotationVisitor {
    protected final Remapper remapper;

    public AnnotationRemapper(AnnotationVisitor annotationVisitor, Remapper remapper) {
        this(393216, annotationVisitor, remapper);
    }

    protected AnnotationRemapper(int api, AnnotationVisitor annotationVisitor, Remapper remapper) {
        super(api, annotationVisitor);
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        super.visit(name, this.remapper.mapValue(value));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(name, this.remapper.mapDesc(descriptor), value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(name, this.remapper.mapDesc(descriptor));
        if (annotationVisitor == null) {
            return null;
        }
        return annotationVisitor == this.av ? this : new AnnotationRemapper(this.api, annotationVisitor, this.remapper);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor annotationVisitor = super.visitArray(name);
        if (annotationVisitor == null) {
            return null;
        }
        return annotationVisitor == this.av ? this : new AnnotationRemapper(this.api, annotationVisitor, this.remapper);
    }
}

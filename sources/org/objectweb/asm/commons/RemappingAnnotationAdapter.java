package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;

@Deprecated
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/RemappingAnnotationAdapter.SCL.lombok */
public class RemappingAnnotationAdapter extends AnnotationVisitor {
    protected final Remapper remapper;

    public RemappingAnnotationAdapter(AnnotationVisitor av, Remapper remapper) {
        this(393216, av, remapper);
    }

    protected RemappingAnnotationAdapter(int api, AnnotationVisitor av, Remapper remapper) {
        super(api, av);
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        this.av.visit(name, this.remapper.mapValue(value));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String desc, String value) {
        this.av.visitEnum(name, this.remapper.mapDesc(desc), value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationVisitor v = this.av.visitAnnotation(name, this.remapper.mapDesc(desc));
        if (v == null) {
            return null;
        }
        return v == this.av ? this : new RemappingAnnotationAdapter(v, this.remapper);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor v = this.av.visitArray(name);
        if (v == null) {
            return null;
        }
        return v == this.av ? this : new RemappingAnnotationAdapter(v, this.remapper);
    }
}

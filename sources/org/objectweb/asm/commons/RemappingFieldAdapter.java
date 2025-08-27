package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

@Deprecated
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/RemappingFieldAdapter.SCL.lombok */
public class RemappingFieldAdapter extends FieldVisitor {
    private final Remapper remapper;

    public RemappingFieldAdapter(FieldVisitor fv, Remapper remapper) {
        this(393216, fv, remapper);
    }

    protected RemappingFieldAdapter(int api, FieldVisitor fv, Remapper remapper) {
        super(api, fv);
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = this.fv.visitAnnotation(this.remapper.mapDesc(desc), visible);
        if (av == null) {
            return null;
        }
        return new RemappingAnnotationAdapter(av, this.remapper);
    }

    @Override // org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(desc), visible);
        if (av == null) {
            return null;
        }
        return new RemappingAnnotationAdapter(av, this.remapper);
    }
}

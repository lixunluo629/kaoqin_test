package net.sf.cglib.transform;

import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/ClassTransformerTee.class */
public class ClassTransformerTee extends ClassTransformer {
    private ClassVisitor branch;

    public ClassTransformerTee(ClassVisitor branch) {
        super(262144);
        this.branch = branch;
    }

    @Override // net.sf.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = new ClassVisitorTee(this.branch, target);
    }
}

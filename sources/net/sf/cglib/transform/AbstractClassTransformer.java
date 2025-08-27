package net.sf.cglib.transform;

import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/AbstractClassTransformer.class */
public abstract class AbstractClassTransformer extends ClassTransformer {
    protected AbstractClassTransformer() {
        super(262144);
    }

    @Override // net.sf.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = target;
    }
}

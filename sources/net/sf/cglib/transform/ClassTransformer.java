package net.sf.cglib.transform;

import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/transform/ClassTransformer.class */
public abstract class ClassTransformer extends ClassVisitor {
    public abstract void setTarget(ClassVisitor classVisitor);

    public ClassTransformer() {
        super(262144);
    }

    public ClassTransformer(int opcode) {
        super(opcode);
    }
}

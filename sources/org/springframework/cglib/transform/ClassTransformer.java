package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/ClassTransformer.class */
public abstract class ClassTransformer extends ClassVisitor {
    public abstract void setTarget(ClassVisitor classVisitor);

    public ClassTransformer() {
        super(393216);
    }

    public ClassTransformer(int opcode) {
        super(opcode);
    }
}

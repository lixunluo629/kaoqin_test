package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/ClassTransformerTee.class */
public class ClassTransformerTee extends ClassTransformer {
    private ClassVisitor branch;

    public ClassTransformerTee(ClassVisitor branch) {
        super(393216);
        this.branch = branch;
    }

    @Override // org.springframework.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = new ClassVisitorTee(this.branch, target);
    }
}

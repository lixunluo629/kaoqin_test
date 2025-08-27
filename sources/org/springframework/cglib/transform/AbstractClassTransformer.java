package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/AbstractClassTransformer.class */
public abstract class AbstractClassTransformer extends ClassTransformer {
    protected AbstractClassTransformer() {
        super(393216);
    }

    @Override // org.springframework.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = target;
    }
}

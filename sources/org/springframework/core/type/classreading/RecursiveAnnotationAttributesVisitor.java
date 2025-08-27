package org.springframework.core.type.classreading;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/RecursiveAnnotationAttributesVisitor.class */
class RecursiveAnnotationAttributesVisitor extends AbstractRecursiveAnnotationVisitor {
    protected final String annotationType;

    public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, ClassLoader classLoader) {
        super(classLoader, attributes);
        this.annotationType = annotationType;
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnd() {
        AnnotationUtils.registerDefaultValues(this.attributes);
    }
}

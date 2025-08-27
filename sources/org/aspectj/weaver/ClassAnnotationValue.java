package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ClassAnnotationValue.class */
public class ClassAnnotationValue extends AnnotationValue {
    private String signature;

    public ClassAnnotationValue(String sig) {
        super(99);
        this.signature = sig;
    }

    @Override // org.aspectj.weaver.AnnotationValue
    public String stringify() {
        return this.signature;
    }

    public String toString() {
        return this.signature;
    }
}

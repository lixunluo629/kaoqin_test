package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AnnotationAnnotationValue.class */
public class AnnotationAnnotationValue extends AnnotationValue {
    private AnnotationAJ value;

    public AnnotationAnnotationValue(AnnotationAJ value) {
        super(64);
        this.value = value;
    }

    public AnnotationAJ getAnnotation() {
        return this.value;
    }

    @Override // org.aspectj.weaver.AnnotationValue
    public String stringify() {
        return this.value.stringify();
    }

    public String toString() {
        return this.value.toString();
    }
}

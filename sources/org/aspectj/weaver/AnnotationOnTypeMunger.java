package org.aspectj.weaver;

import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AnnotationOnTypeMunger.class */
public class AnnotationOnTypeMunger extends ResolvedTypeMunger {
    AnnotationAJ newAnnotation;
    private volatile int hashCode;

    public AnnotationOnTypeMunger(AnnotationAJ anno) {
        super(AnnotationOnType, null);
        this.hashCode = 0;
        this.newAnnotation = anno;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("unimplemented");
    }

    public AnnotationAJ getNewAnnotation() {
        return this.newAnnotation;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AnnotationOnTypeMunger)) {
            return false;
        }
        AnnotationOnTypeMunger o = (AnnotationOnTypeMunger) other;
        return this.newAnnotation.getTypeSignature().equals(o.newAnnotation.getTypeSignature());
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + this.newAnnotation.getTypeSignature().hashCode();
            this.hashCode = result;
        }
        return this.hashCode;
    }
}

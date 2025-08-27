package org.aspectj.weaver;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AbstractAnnotationAJ.class */
public abstract class AbstractAnnotationAJ implements AnnotationAJ {
    protected final ResolvedType type;
    private Set<String> supportedTargets = null;

    @Override // org.aspectj.weaver.AnnotationAJ
    public abstract boolean isRuntimeVisible();

    @Override // org.aspectj.weaver.AnnotationAJ
    public abstract Set<String> getTargets();

    @Override // org.aspectj.weaver.AnnotationAJ
    public abstract boolean hasNameValuePair(String str, String str2);

    @Override // org.aspectj.weaver.AnnotationAJ
    public abstract boolean hasNamedValue(String str);

    @Override // org.aspectj.weaver.AnnotationAJ
    public abstract String stringify();

    public AbstractAnnotationAJ(ResolvedType type) {
        this.type = type;
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final ResolvedType getType() {
        return this.type;
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final String getTypeSignature() {
        return this.type.getSignature();
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final String getTypeName() {
        return this.type.getName();
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final boolean allowedOnAnnotationType() {
        ensureAtTargetInitialized();
        if (this.supportedTargets.isEmpty()) {
            return true;
        }
        return this.supportedTargets.contains("ANNOTATION_TYPE");
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final boolean allowedOnField() {
        ensureAtTargetInitialized();
        if (this.supportedTargets.isEmpty()) {
            return true;
        }
        return this.supportedTargets.contains("FIELD");
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final boolean allowedOnRegularType() {
        ensureAtTargetInitialized();
        if (this.supportedTargets.isEmpty()) {
            return true;
        }
        return this.supportedTargets.contains("TYPE");
    }

    public final void ensureAtTargetInitialized() {
        if (this.supportedTargets == null) {
            AnnotationAJ atTargetAnnotation = retrieveAnnotationOnAnnotation(UnresolvedType.AT_TARGET);
            if (atTargetAnnotation == null) {
                this.supportedTargets = Collections.emptySet();
            } else {
                this.supportedTargets = atTargetAnnotation.getTargets();
            }
        }
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final String getValidTargets() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        Iterator<String> iter = this.supportedTargets.iterator();
        while (iter.hasNext()) {
            String evalue = iter.next();
            sb.append(evalue);
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override // org.aspectj.weaver.AnnotationAJ
    public final boolean specifiesTarget() {
        ensureAtTargetInitialized();
        return !this.supportedTargets.isEmpty();
    }

    private final AnnotationAJ retrieveAnnotationOnAnnotation(UnresolvedType requiredAnnotationSignature) {
        AnnotationAJ[] annos = this.type.getAnnotations();
        for (int i = 0; i < annos.length; i++) {
            AnnotationAJ a = annos[i];
            if (a.getTypeSignature().equals(requiredAnnotationSignature.getSignature())) {
                return annos[i];
            }
        }
        return null;
    }
}

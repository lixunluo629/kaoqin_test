package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AnnotatedElement.class */
public interface AnnotatedElement {
    boolean hasAnnotation(UnresolvedType unresolvedType);

    ResolvedType[] getAnnotationTypes();

    AnnotationAJ getAnnotationOfType(UnresolvedType unresolvedType);
}

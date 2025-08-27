package org.aspectj.lang.reflect;

import java.lang.annotation.Annotation;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclareAnnotation.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclareAnnotation.class */
public interface DeclareAnnotation {

    /* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclareAnnotation$Kind.class
 */
    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclareAnnotation$Kind.class */
    public enum Kind {
        Field,
        Method,
        Constructor,
        Type
    }

    AjType<?> getDeclaringType();

    Kind getKind();

    SignaturePattern getSignaturePattern();

    TypePattern getTypePattern();

    Annotation getAnnotation();

    String getAnnotationAsText();
}

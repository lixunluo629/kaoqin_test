package org.aspectj.weaver;

import java.util.Set;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/AnnotationAJ.class */
public interface AnnotationAJ {
    public static final AnnotationAJ[] EMPTY_ARRAY = new AnnotationAJ[0];

    String getTypeSignature();

    String getTypeName();

    ResolvedType getType();

    boolean allowedOnAnnotationType();

    boolean allowedOnField();

    boolean allowedOnRegularType();

    Set<String> getTargets();

    boolean hasNamedValue(String str);

    boolean hasNameValuePair(String str, String str2);

    String getValidTargets();

    String stringify();

    boolean specifiesTarget();

    boolean isRuntimeVisible();

    String getStringFormOfValue(String str);
}

package org.aspectj.weaver;

import java.util.Collection;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ReferenceTypeDelegate.class */
public interface ReferenceTypeDelegate {
    boolean isAspect();

    boolean isAnnotationStyleAspect();

    boolean isInterface();

    boolean isEnum();

    boolean isAnnotation();

    String getRetentionPolicy();

    boolean canAnnotationTargetType();

    AnnotationTargetKind[] getAnnotationTargetKinds();

    boolean isAnnotationWithRuntimeRetention();

    boolean isClass();

    boolean isGeneric();

    boolean isAnonymous();

    boolean isNested();

    boolean hasAnnotation(UnresolvedType unresolvedType);

    AnnotationAJ[] getAnnotations();

    ResolvedType[] getAnnotationTypes();

    ResolvedMember[] getDeclaredFields();

    ResolvedType[] getDeclaredInterfaces();

    ResolvedMember[] getDeclaredMethods();

    ResolvedMember[] getDeclaredPointcuts();

    TypeVariable[] getTypeVariables();

    int getModifiers();

    PerClause getPerClause();

    Collection<Declare> getDeclares();

    Collection<ConcreteTypeMunger> getTypeMungers();

    Collection<ResolvedMember> getPrivilegedAccesses();

    ResolvedType getSuperclass();

    WeaverStateInfo getWeaverState();

    ReferenceType getResolvedTypeX();

    boolean isExposedToWeaver();

    boolean doesNotExposeShadowMungers();

    ISourceContext getSourceContext();

    String getSourcefilename();

    String getDeclaredGenericSignature();

    ResolvedType getOuterClass();

    boolean copySourceContext();

    boolean isCacheable();

    int getCompilerVersion();

    void ensureConsistent();

    boolean isWeavable();

    boolean hasBeenWoven();

    boolean hasAnnotations();
}

package org.aspectj.weaver;

import java.util.Collection;
import java.util.Collections;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/BoundedReferenceTypeDelegate.class */
class BoundedReferenceTypeDelegate extends AbstractReferenceTypeDelegate {
    public BoundedReferenceTypeDelegate(ReferenceType backing) {
        super(backing, false);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAspect() {
        return this.resolvedTypeX.isAspect();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationStyleAspect() {
        return this.resolvedTypeX.isAnnotationStyleAspect();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isInterface() {
        return this.resolvedTypeX.isInterface();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isEnum() {
        return this.resolvedTypeX.isEnum();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotation() {
        return this.resolvedTypeX.isAnnotation();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationWithRuntimeRetention() {
        return this.resolvedTypeX.isAnnotationWithRuntimeRetention();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnonymous() {
        return this.resolvedTypeX.isAnonymous();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isNested() {
        return this.resolvedTypeX.isNested();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getOuterClass() {
        return this.resolvedTypeX.getOuterClass();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getRetentionPolicy() {
        return this.resolvedTypeX.getRetentionPolicy();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean canAnnotationTargetType() {
        return this.resolvedTypeX.canAnnotationTargetType();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        return this.resolvedTypeX.getAnnotationTargetKinds();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isGeneric() {
        return this.resolvedTypeX.isGenericType();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getDeclaredGenericSignature() {
        return this.resolvedTypeX.getDeclaredGenericSignature();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotation(UnresolvedType ofType) {
        return this.resolvedTypeX.hasAnnotation(ofType);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationAJ[] getAnnotations() {
        return this.resolvedTypeX.getAnnotations();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotations() {
        return this.resolvedTypeX.hasAnnotations();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getAnnotationTypes() {
        return this.resolvedTypeX.getAnnotationTypes();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredFields() {
        return this.resolvedTypeX.getDeclaredFields();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getDeclaredInterfaces() {
        return this.resolvedTypeX.getDeclaredInterfaces();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredMethods() {
        return this.resolvedTypeX.getDeclaredMethods();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredPointcuts() {
        return this.resolvedTypeX.getDeclaredPointcuts();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public PerClause getPerClause() {
        return this.resolvedTypeX.getPerClause();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<Declare> getDeclares() {
        return this.resolvedTypeX.getDeclares();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ConcreteTypeMunger> getTypeMungers() {
        return this.resolvedTypeX.getTypeMungers();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ResolvedMember> getPrivilegedAccesses() {
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getModifiers() {
        return this.resolvedTypeX.getModifiers();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getSuperclass() {
        return this.resolvedTypeX.getSuperclass();
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public WeaverStateInfo getWeaverState() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public TypeVariable[] getTypeVariables() {
        return this.resolvedTypeX.getTypeVariables();
    }
}

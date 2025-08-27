package org.aspectj.weaver;

import java.util.Collection;
import org.aspectj.weaver.patterns.Declare;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/GeneratedReferenceTypeDelegate.class */
public class GeneratedReferenceTypeDelegate extends AbstractReferenceTypeDelegate {
    private ResolvedType superclass;

    public GeneratedReferenceTypeDelegate(ReferenceType backing) {
        super(backing, false);
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAspect() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationStyleAspect() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isInterface() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isEnum() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotation() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnnotationWithRuntimeRetention() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isAnonymous() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isNested() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getOuterClass() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getRetentionPolicy() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean canAnnotationTargetType() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean isGeneric() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public String getDeclaredGenericSignature() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotation(UnresolvedType ofType) {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public AnnotationAJ[] getAnnotations() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public boolean hasAnnotations() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getAnnotationTypes() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredFields() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType[] getDeclaredInterfaces() {
        return ResolvedType.NONE;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredMethods() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedMember[] getDeclaredPointcuts() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public PerClause getPerClause() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<Declare> getDeclares() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ConcreteTypeMunger> getTypeMungers() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public Collection<ResolvedMember> getPrivilegedAccesses() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public int getModifiers() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    public void setSuperclass(ResolvedType superclass) {
        this.superclass = superclass;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public ResolvedType getSuperclass() {
        return this.superclass;
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public WeaverStateInfo getWeaverState() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }

    @Override // org.aspectj.weaver.ReferenceTypeDelegate
    public TypeVariable[] getTypeVariables() {
        throw new UnsupportedOperationException("Not supported for GeneratedReferenceTypeDelegate");
    }
}

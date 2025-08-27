package org.aspectj.weaver.reflect;

import java.lang.reflect.Member;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.MemberKind;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedMemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ReflectionBasedResolvedMemberImpl.class */
public class ReflectionBasedResolvedMemberImpl extends ResolvedMemberImpl {
    private AnnotationFinder annotationFinder;
    private GenericSignatureInformationProvider gsigInfoProvider;
    private boolean onlyRuntimeAnnotationsCached;
    private Member reflectMember;

    public ReflectionBasedResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes, Member reflectMember) {
        super(kind, declaringType, modifiers, returnType, name, parameterTypes);
        this.annotationFinder = null;
        this.gsigInfoProvider = new Java14GenericSignatureInformationProvider();
        this.reflectMember = reflectMember;
    }

    public ReflectionBasedResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes, UnresolvedType[] checkedExceptions, Member reflectMember) {
        super(kind, declaringType, modifiers, returnType, name, parameterTypes, checkedExceptions);
        this.annotationFinder = null;
        this.gsigInfoProvider = new Java14GenericSignatureInformationProvider();
        this.reflectMember = reflectMember;
    }

    public ReflectionBasedResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes, UnresolvedType[] checkedExceptions, ResolvedMember backingGenericMember, Member reflectMember) {
        super(kind, declaringType, modifiers, returnType, name, parameterTypes, checkedExceptions, backingGenericMember);
        this.annotationFinder = null;
        this.gsigInfoProvider = new Java14GenericSignatureInformationProvider();
        this.reflectMember = reflectMember;
    }

    public ReflectionBasedResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, String name, String signature, Member reflectMember) {
        super(kind, declaringType, modifiers, name, signature);
        this.annotationFinder = null;
        this.gsigInfoProvider = new Java14GenericSignatureInformationProvider();
        this.reflectMember = reflectMember;
    }

    public Member getMember() {
        return this.reflectMember;
    }

    public void setGenericSignatureInformationProvider(GenericSignatureInformationProvider gsigProvider) {
        this.gsigInfoProvider = gsigProvider;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType[] getGenericParameterTypes() {
        return this.gsigInfoProvider.getGenericParameterTypes(this);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        return this.gsigInfoProvider.getGenericReturnType(this);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isSynthetic() {
        return this.gsigInfoProvider.isSynthetic(this);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isVarargsMethod() {
        return this.gsigInfoProvider.isVarArgs(this);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isBridgeMethod() {
        return this.gsigInfoProvider.isBridge(this);
    }

    public void setAnnotationFinder(AnnotationFinder finder) {
        this.annotationFinder = finder;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        boolean areRuntimeRetentionAnnotationsSufficient = false;
        if (ofType instanceof ResolvedType) {
            areRuntimeRetentionAnnotationsSufficient = ((ResolvedType) ofType).isAnnotationWithRuntimeRetention();
        }
        unpackAnnotations(areRuntimeRetentionAnnotationsSufficient);
        return super.hasAnnotation(ofType);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl
    public boolean hasAnnotations() {
        unpackAnnotations(false);
        return super.hasAnnotations();
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        unpackAnnotations(false);
        return super.getAnnotationTypes();
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        unpackAnnotations(false);
        if (this.annotationFinder == null || this.annotationTypes == null) {
            return null;
        }
        for (ResolvedType type : this.annotationTypes) {
            if (type.getSignature().equals(ofType.getSignature())) {
                return this.annotationFinder.getAnnotationOfType(ofType, this.reflectMember);
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public String getAnnotationDefaultValue() {
        if (this.annotationFinder == null) {
            return null;
        }
        return this.annotationFinder.getAnnotationDefaultValue(this.reflectMember);
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public ResolvedType[][] getParameterAnnotationTypes() {
        if (this.parameterAnnotationTypes == null && this.annotationFinder != null) {
            this.parameterAnnotationTypes = this.annotationFinder.getParameterAnnotationTypes(this.reflectMember);
        }
        return this.parameterAnnotationTypes;
    }

    private void unpackAnnotations(boolean areRuntimeRetentionAnnotationsSufficient) {
        if (this.annotationFinder != null) {
            if (this.annotationTypes == null || (!areRuntimeRetentionAnnotationsSufficient && this.onlyRuntimeAnnotationsCached)) {
                this.annotationTypes = this.annotationFinder.getAnnotations(this.reflectMember, areRuntimeRetentionAnnotationsSufficient);
                this.onlyRuntimeAnnotationsCached = areRuntimeRetentionAnnotationsSufficient;
            }
        }
    }
}

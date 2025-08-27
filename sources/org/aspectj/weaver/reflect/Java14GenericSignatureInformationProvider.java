package org.aspectj.weaver.reflect;

import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/Java14GenericSignatureInformationProvider.class */
public class Java14GenericSignatureInformationProvider implements GenericSignatureInformationProvider {
    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public UnresolvedType[] getGenericParameterTypes(ReflectionBasedResolvedMemberImpl resolvedMember) {
        return resolvedMember.getParameterTypes();
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public UnresolvedType getGenericReturnType(ReflectionBasedResolvedMemberImpl resolvedMember) {
        return resolvedMember.getReturnType();
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isBridge(ReflectionBasedResolvedMemberImpl resolvedMember) {
        return false;
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isVarArgs(ReflectionBasedResolvedMemberImpl resolvedMember) {
        return false;
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isSynthetic(ReflectionBasedResolvedMemberImpl resolvedMember) {
        return false;
    }
}

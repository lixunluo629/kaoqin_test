package org.aspectj.weaver.reflect;

import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/GenericSignatureInformationProvider.class */
public interface GenericSignatureInformationProvider {
    UnresolvedType[] getGenericParameterTypes(ReflectionBasedResolvedMemberImpl reflectionBasedResolvedMemberImpl);

    UnresolvedType getGenericReturnType(ReflectionBasedResolvedMemberImpl reflectionBasedResolvedMemberImpl);

    boolean isBridge(ReflectionBasedResolvedMemberImpl reflectionBasedResolvedMemberImpl);

    boolean isVarArgs(ReflectionBasedResolvedMemberImpl reflectionBasedResolvedMemberImpl);

    boolean isSynthetic(ReflectionBasedResolvedMemberImpl reflectionBasedResolvedMemberImpl);
}

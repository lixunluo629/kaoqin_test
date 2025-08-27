package org.aspectj.weaver.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/Java15GenericSignatureInformationProvider.class */
public class Java15GenericSignatureInformationProvider implements GenericSignatureInformationProvider {
    private final World world;

    public Java15GenericSignatureInformationProvider(World forWorld) {
        this.world = forWorld;
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public UnresolvedType[] getGenericParameterTypes(ReflectionBasedResolvedMemberImpl resolvedMember) {
        JavaLangTypeToResolvedTypeConverter typeConverter = new JavaLangTypeToResolvedTypeConverter(this.world);
        Type[] pTypes = new Type[0];
        Member member = resolvedMember.getMember();
        if (member instanceof Method) {
            pTypes = ((Method) member).getGenericParameterTypes();
        } else if (member instanceof Constructor) {
            pTypes = ((Constructor) member).getGenericParameterTypes();
        }
        return typeConverter.fromTypes(pTypes);
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public UnresolvedType getGenericReturnType(ReflectionBasedResolvedMemberImpl resolvedMember) {
        JavaLangTypeToResolvedTypeConverter typeConverter = new JavaLangTypeToResolvedTypeConverter(this.world);
        Member member = resolvedMember.getMember();
        if (member instanceof Field) {
            return typeConverter.fromType(((Field) member).getGenericType());
        }
        if (member instanceof Method) {
            return typeConverter.fromType(((Method) member).getGenericReturnType());
        }
        if (member instanceof Constructor) {
            return typeConverter.fromType(((Constructor) member).getDeclaringClass());
        }
        throw new IllegalStateException("unexpected member type: " + member);
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isBridge(ReflectionBasedResolvedMemberImpl resolvedMember) {
        Member member = resolvedMember.getMember();
        if (member instanceof Method) {
            return ((Method) member).isBridge();
        }
        return false;
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isVarArgs(ReflectionBasedResolvedMemberImpl resolvedMember) {
        Member member = resolvedMember.getMember();
        if (member instanceof Method) {
            return ((Method) member).isVarArgs();
        }
        if (member instanceof Constructor) {
            return ((Constructor) member).isVarArgs();
        }
        return false;
    }

    @Override // org.aspectj.weaver.reflect.GenericSignatureInformationProvider
    public boolean isSynthetic(ReflectionBasedResolvedMemberImpl resolvedMember) {
        Member member = resolvedMember.getMember();
        return member.isSynthetic();
    }
}

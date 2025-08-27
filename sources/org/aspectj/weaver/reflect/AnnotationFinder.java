package org.aspectj.weaver.reflect;

import java.lang.reflect.Member;
import org.aspectj.weaver.AnnotationAJ;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/AnnotationFinder.class */
public interface AnnotationFinder {
    void setClassLoader(ClassLoader classLoader);

    void setWorld(World world);

    Object getAnnotation(ResolvedType resolvedType, Object obj);

    Object getAnnotationFromMember(ResolvedType resolvedType, Member member);

    AnnotationAJ getAnnotationOfType(UnresolvedType unresolvedType, Member member);

    String getAnnotationDefaultValue(Member member);

    Object getAnnotationFromClass(ResolvedType resolvedType, Class<?> cls);

    ResolvedType[] getAnnotations(Member member, boolean z);

    ResolvedType[][] getParameterAnnotationTypes(Member member);
}

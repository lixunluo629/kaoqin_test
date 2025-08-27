package org.aspectj.weaver.reflect;

import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/IReflectionWorld.class */
public interface IReflectionWorld {
    AnnotationFinder getAnnotationFinder();

    ResolvedType resolve(Class cls);
}

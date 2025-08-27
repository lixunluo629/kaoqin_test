package org.aspectj.weaver.reflect;

import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/DeferredResolvedPointcutDefinition.class */
public class DeferredResolvedPointcutDefinition extends ResolvedPointcutDefinition {
    public DeferredResolvedPointcutDefinition(UnresolvedType declaringType, int modifiers, String name, UnresolvedType[] parameterTypes) {
        super(declaringType, modifiers, name, parameterTypes, UnresolvedType.VOID, null);
    }
}

package org.aspectj.weaver.internal.tools;

import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.TypePattern;
import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegateFactory;
import org.aspectj.weaver.tools.TypePatternMatcher;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/TypePatternMatcherImpl.class */
public class TypePatternMatcherImpl implements TypePatternMatcher {
    private final TypePattern pattern;
    private final World world;

    public TypePatternMatcherImpl(TypePattern pattern, World world) {
        this.pattern = pattern;
        this.world = world;
    }

    @Override // org.aspectj.weaver.tools.TypePatternMatcher
    public boolean matches(Class aClass) {
        ResolvedType rt = ReflectionBasedReferenceTypeDelegateFactory.resolveTypeInWorld(aClass, this.world);
        return this.pattern.matchesStatically(rt);
    }
}

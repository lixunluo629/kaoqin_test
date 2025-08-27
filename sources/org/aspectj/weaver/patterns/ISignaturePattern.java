package org.aspectj.weaver.patterns;

import java.util.List;
import java.util.Map;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ISignaturePattern.class */
public interface ISignaturePattern {
    public static final byte PATTERN = 1;
    public static final byte NOT = 2;
    public static final byte OR = 3;
    public static final byte AND = 4;

    boolean matches(Member member, World world, boolean z);

    ISignaturePattern parameterizeWith(Map<String, UnresolvedType> map, World world);

    ISignaturePattern resolveBindings(IScope iScope, Bindings bindings);

    List<ExactTypePattern> getExactDeclaringTypes();

    boolean isMatchOnAnyName();

    boolean couldEverMatch(ResolvedType resolvedType);

    boolean isStarAnnotation();
}

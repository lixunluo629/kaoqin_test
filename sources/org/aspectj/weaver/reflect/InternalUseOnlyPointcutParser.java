package org.aspectj.weaver.reflect;

import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/InternalUseOnlyPointcutParser.class */
public class InternalUseOnlyPointcutParser extends PointcutParser {
    public InternalUseOnlyPointcutParser(ClassLoader classLoader, ReflectionWorld world) {
        setClassLoader(classLoader);
        setWorld(world);
    }

    public InternalUseOnlyPointcutParser(ClassLoader classLoader) {
        setClassLoader(classLoader);
    }

    @Override // org.aspectj.weaver.tools.PointcutParser
    public Pointcut resolvePointcutExpression(String expression, Class inScope, PointcutParameter[] formalParameters) {
        return super.resolvePointcutExpression(expression, inScope, formalParameters);
    }

    @Override // org.aspectj.weaver.tools.PointcutParser
    public Pointcut concretizePointcutExpression(Pointcut pc, Class inScope, PointcutParameter[] formalParameters) {
        return super.concretizePointcutExpression(pc, inScope, formalParameters);
    }
}

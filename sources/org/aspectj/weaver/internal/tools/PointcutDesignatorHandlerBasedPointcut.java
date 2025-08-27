package org.aspectj.weaver.internal.tools;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.Bindings;
import org.aspectj.weaver.patterns.ExposedState;
import org.aspectj.weaver.patterns.FastMatchInfo;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.PatternNodeVisitor;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.reflect.ReflectionBasedReferenceTypeDelegate;
import org.aspectj.weaver.reflect.ReflectionFastMatchInfo;
import org.aspectj.weaver.reflect.ReflectionShadow;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.MatchingContext;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/PointcutDesignatorHandlerBasedPointcut.class */
public class PointcutDesignatorHandlerBasedPointcut extends Pointcut {
    private final ContextBasedMatcher matcher;
    private final World world;

    public PointcutDesignatorHandlerBasedPointcut(ContextBasedMatcher expr, World world) {
        this.matcher = expr;
        this.world = world;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public byte getPointcutKind() {
        return (byte) 22;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) throws ClassNotFoundException {
        if (info instanceof ReflectionFastMatchInfo) {
            if (!(this.world instanceof ReflectionWorld)) {
                throw new IllegalStateException("Can only match user-extension pcds with a ReflectionWorld");
            }
            Class<?> clazz = null;
            try {
                clazz = Class.forName(info.getType().getName(), false, ((ReflectionWorld) this.world).getClassLoader());
            } catch (ClassNotFoundException e) {
                if (info.getType() instanceof ReferenceType) {
                    ReferenceTypeDelegate rtd = ((ReferenceType) info.getType()).getDelegate();
                    if (rtd instanceof ReflectionBasedReferenceTypeDelegate) {
                        clazz = ((ReflectionBasedReferenceTypeDelegate) rtd).getClazz();
                    }
                }
            }
            if (clazz == null) {
                return FuzzyBoolean.MAYBE;
            }
            return FuzzyBoolean.fromBoolean(this.matcher.couldMatchJoinPointsInType(clazz, ((ReflectionFastMatchInfo) info).getMatchingContext()));
        }
        throw new IllegalStateException("Can only match user-extension pcds against Reflection FastMatchInfo objects");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if (shadow instanceof ReflectionShadow) {
            MatchingContext context = ((ReflectionShadow) shadow).getMatchingContext();
            org.aspectj.weaver.tools.FuzzyBoolean match = this.matcher.matchesStatically(context);
            if (match == org.aspectj.weaver.tools.FuzzyBoolean.MAYBE) {
                return FuzzyBoolean.MAYBE;
            }
            if (match == org.aspectj.weaver.tools.FuzzyBoolean.YES) {
                return FuzzyBoolean.YES;
            }
            if (match == org.aspectj.weaver.tools.FuzzyBoolean.NO) {
                return FuzzyBoolean.NO;
            }
        }
        throw new IllegalStateException("Can only match user-extension pcds against Reflection shadows (not BCEL)");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected void resolveBindings(IScope scope, Bindings bindings) {
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (!this.matcher.mayNeedDynamicTest()) {
            return Literal.TRUE;
        }
        matchInternal(shadow);
        return new MatchingContextBasedTest(this.matcher);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new UnsupportedOperationException("can't write custom pointcut designator expressions to stream");
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return data;
    }
}

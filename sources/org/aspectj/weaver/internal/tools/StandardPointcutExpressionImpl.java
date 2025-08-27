package org.aspectj.weaver.internal.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.AbstractPatternNodeVisitor;
import org.aspectj.weaver.patterns.AnnotationPointcut;
import org.aspectj.weaver.patterns.ArgsAnnotationPointcut;
import org.aspectj.weaver.patterns.ArgsPointcut;
import org.aspectj.weaver.patterns.CflowPointcut;
import org.aspectj.weaver.patterns.ExposedState;
import org.aspectj.weaver.patterns.IfPointcut;
import org.aspectj.weaver.patterns.NotAnnotationTypePattern;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.aspectj.weaver.patterns.ThisOrTargetPointcut;
import org.aspectj.weaver.patterns.WithinAnnotationPointcut;
import org.aspectj.weaver.patterns.WithinCodeAnnotationPointcut;
import org.aspectj.weaver.reflect.ReflectionFastMatchInfo;
import org.aspectj.weaver.reflect.StandardShadow;
import org.aspectj.weaver.reflect.StandardShadowMatchImpl;
import org.aspectj.weaver.tools.DefaultMatchingContext;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.ShadowMatch;
import org.aspectj.weaver.tools.StandardPointcutExpression;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/StandardPointcutExpressionImpl.class */
public class StandardPointcutExpressionImpl implements StandardPointcutExpression {
    private World world;
    private Pointcut pointcut;
    private String expression;
    private PointcutParameter[] parameters;
    private MatchingContext matchContext = new DefaultMatchingContext();

    public StandardPointcutExpressionImpl(Pointcut pointcut, String expression, PointcutParameter[] params, World inWorld) {
        this.pointcut = pointcut;
        this.expression = expression;
        this.world = inWorld;
        this.parameters = params;
        if (this.parameters == null) {
            this.parameters = new PointcutParameter[0];
        }
    }

    public Pointcut getUnderlyingPointcut() {
        return this.pointcut;
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public void setMatchingContext(MatchingContext aMatchContext) {
        this.matchContext = aMatchContext;
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public boolean couldMatchJoinPointsInType(Class aClass) {
        ResolvedType matchType = this.world.resolve(aClass.getName());
        ReflectionFastMatchInfo info = new ReflectionFastMatchInfo(matchType, null, this.matchContext, this.world);
        return this.pointcut.fastMatch(info).maybeTrue();
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public boolean mayNeedDynamicTest() {
        HasPossibleDynamicContentVisitor visitor = new HasPossibleDynamicContentVisitor();
        this.pointcut.traverse(visitor, null);
        return visitor.hasDynamicContent();
    }

    private ExposedState getExposedState() {
        return new ExposedState(this.parameters.length);
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public ShadowMatch matchesMethodExecution(ResolvedMember aMethod) {
        return matchesExecution(aMethod);
    }

    public ShadowMatch matchesConstructorExecution(Constructor aConstructor) {
        return null;
    }

    private ShadowMatch matchesExecution(ResolvedMember aMember) {
        Shadow s = StandardShadow.makeExecutionShadow(this.world, aMember, this.matchContext);
        StandardShadowMatchImpl sm = getShadowMatch(s);
        sm.setSubject(aMember);
        sm.setWithinCode(null);
        sm.setWithinType((ResolvedType) aMember.getDeclaringType());
        return sm;
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public ShadowMatch matchesStaticInitialization(ResolvedType aType) {
        Shadow s = StandardShadow.makeStaticInitializationShadow(this.world, aType, this.matchContext);
        StandardShadowMatchImpl sm = getShadowMatch(s);
        sm.setSubject(null);
        sm.setWithinCode(null);
        sm.setWithinType(aType);
        return sm;
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public ShadowMatch matchesMethodCall(ResolvedMember aMethod, ResolvedMember withinCode) {
        Shadow s = StandardShadow.makeCallShadow(this.world, aMethod, withinCode, this.matchContext);
        StandardShadowMatchImpl sm = getShadowMatch(s);
        sm.setSubject(aMethod);
        sm.setWithinCode(withinCode);
        sm.setWithinType((ResolvedType) withinCode.getDeclaringType());
        return sm;
    }

    private StandardShadowMatchImpl getShadowMatch(Shadow forShadow) {
        FuzzyBoolean match = this.pointcut.match(forShadow);
        Test residueTest = Literal.TRUE;
        ExposedState state = getExposedState();
        if (match.maybeTrue()) {
            residueTest = this.pointcut.findResidue(forShadow, state);
        }
        StandardShadowMatchImpl sm = new StandardShadowMatchImpl(match, residueTest, state, this.parameters);
        sm.setMatchingContext(this.matchContext);
        return sm;
    }

    @Override // org.aspectj.weaver.tools.StandardPointcutExpression
    public String getPointcutExpression() {
        return this.expression;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/StandardPointcutExpressionImpl$HasPossibleDynamicContentVisitor.class */
    private static class HasPossibleDynamicContentVisitor extends AbstractPatternNodeVisitor {
        private boolean hasDynamicContent;

        private HasPossibleDynamicContentVisitor() {
            this.hasDynamicContent = false;
        }

        public boolean hasDynamicContent() {
            return this.hasDynamicContent;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(WithinAnnotationPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(WithinCodeAnnotationPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(AnnotationPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ArgsAnnotationPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ArgsPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(CflowPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(IfPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(NotAnnotationTypePattern node, Object data) {
            return node.getNegatedPattern().accept(this, data);
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(NotPointcut node, Object data) {
            return node.getNegatedPointcut().accept(this, data);
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ThisOrTargetAnnotationPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }

        @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
        public Object visit(ThisOrTargetPointcut node, Object data) {
            this.hasDynamicContent = true;
            return null;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/internal/tools/StandardPointcutExpressionImpl$Handler.class */
    public static class Handler implements Member {
        private Class decClass;
        private Class exType;

        public Handler(Class decClass, Class exType) {
            this.decClass = decClass;
            this.exType = exType;
        }

        @Override // java.lang.reflect.Member
        public int getModifiers() {
            return 0;
        }

        @Override // java.lang.reflect.Member
        public Class getDeclaringClass() {
            return this.decClass;
        }

        @Override // java.lang.reflect.Member
        public String getName() {
            return null;
        }

        public Class getHandledExceptionType() {
            return this.exType;
        }

        @Override // java.lang.reflect.Member
        public boolean isSynthetic() {
            return false;
        }
    }
}

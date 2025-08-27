package org.aspectj.weaver.reflect;

import java.lang.reflect.Member;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.And;
import org.aspectj.weaver.ast.Call;
import org.aspectj.weaver.ast.FieldGetCall;
import org.aspectj.weaver.ast.HasAnnotation;
import org.aspectj.weaver.ast.ITestVisitor;
import org.aspectj.weaver.ast.Instanceof;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Not;
import org.aspectj.weaver.ast.Or;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.internal.tools.MatchingContextBasedTest;
import org.aspectj.weaver.patterns.ExposedState;
import org.aspectj.weaver.tools.DefaultMatchingContext;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.ShadowMatch;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ShadowMatchImpl.class */
public class ShadowMatchImpl implements ShadowMatch {
    private FuzzyBoolean match;
    private ExposedState state;
    private Test residualTest;
    private PointcutParameter[] params;
    private Member withinCode;
    private Member subject;
    private Class<?> withinType;
    private MatchingContext matchContext = new DefaultMatchingContext();

    public ShadowMatchImpl(FuzzyBoolean match, Test test, ExposedState state, PointcutParameter[] params) {
        this.match = match;
        this.residualTest = test;
        this.state = state;
        this.params = params;
    }

    public void setWithinCode(Member aMember) {
        this.withinCode = aMember;
    }

    public void setSubject(Member aMember) {
        this.subject = aMember;
    }

    public void setWithinType(Class<?> aClass) {
        this.withinType = aClass;
    }

    @Override // org.aspectj.weaver.tools.ShadowMatch
    public boolean alwaysMatches() {
        return this.match.alwaysTrue();
    }

    @Override // org.aspectj.weaver.tools.ShadowMatch
    public boolean maybeMatches() {
        return this.match.maybeTrue();
    }

    @Override // org.aspectj.weaver.tools.ShadowMatch
    public boolean neverMatches() {
        return this.match.alwaysFalse();
    }

    @Override // org.aspectj.weaver.tools.ShadowMatch
    public JoinPointMatch matchesJoinPoint(Object thisObject, Object targetObject, Object[] args) {
        if (neverMatches()) {
            return JoinPointMatchImpl.NO_MATCH;
        }
        if (new RuntimeTestEvaluator(this.residualTest, thisObject, targetObject, args, this.matchContext).matches()) {
            return new JoinPointMatchImpl(getPointcutParameters(thisObject, targetObject, args));
        }
        return JoinPointMatchImpl.NO_MATCH;
    }

    @Override // org.aspectj.weaver.tools.ShadowMatch
    public void setMatchingContext(MatchingContext aMatchContext) {
        this.matchContext = aMatchContext;
    }

    private PointcutParameter[] getPointcutParameters(Object thisObject, Object targetObject, Object[] args) {
        Var[] vars = this.state.vars;
        PointcutParameterImpl[] bindings = new PointcutParameterImpl[this.params.length];
        for (int i = 0; i < bindings.length; i++) {
            bindings[i] = new PointcutParameterImpl(this.params[i].getName(), this.params[i].getType());
            bindings[i].setBinding(((ReflectionVar) vars[i]).getBindingAtJoinPoint(thisObject, targetObject, args, this.subject, this.withinCode, this.withinType));
        }
        return bindings;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/ShadowMatchImpl$RuntimeTestEvaluator.class */
    private static class RuntimeTestEvaluator implements ITestVisitor {
        private boolean matches = true;
        private final Test test;
        private final Object thisObject;
        private final Object targetObject;
        private final Object[] args;
        private final MatchingContext matchContext;

        public RuntimeTestEvaluator(Test aTest, Object thisObject, Object targetObject, Object[] args, MatchingContext context) {
            this.test = aTest;
            this.thisObject = thisObject;
            this.targetObject = targetObject;
            this.args = args;
            this.matchContext = context;
        }

        public boolean matches() {
            this.test.accept(this);
            return this.matches;
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(And e) {
            boolean leftMatches = new RuntimeTestEvaluator(e.getLeft(), this.thisObject, this.targetObject, this.args, this.matchContext).matches();
            if (!leftMatches) {
                this.matches = false;
            } else {
                this.matches = new RuntimeTestEvaluator(e.getRight(), this.thisObject, this.targetObject, this.args, this.matchContext).matches();
            }
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(Instanceof instanceofTest) {
            ReflectionVar v = (ReflectionVar) instanceofTest.getVar();
            Object value = v.getBindingAtJoinPoint(this.thisObject, this.targetObject, this.args);
            World world = v.getType().getWorld();
            ResolvedType desiredType = instanceofTest.getType().resolve(world);
            if (value == null) {
                this.matches = false;
            } else {
                ResolvedType actualType = world.resolve(value.getClass().getName());
                this.matches = desiredType.isAssignableFrom(actualType);
            }
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(MatchingContextBasedTest matchingContextTest) {
            this.matches = matchingContextTest.matches(this.matchContext);
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(Not not) {
            this.matches = !new RuntimeTestEvaluator(not.getBody(), this.thisObject, this.targetObject, this.args, this.matchContext).matches();
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(Or or) {
            boolean leftMatches = new RuntimeTestEvaluator(or.getLeft(), this.thisObject, this.targetObject, this.args, this.matchContext).matches();
            if (leftMatches) {
                this.matches = true;
            } else {
                this.matches = new RuntimeTestEvaluator(or.getRight(), this.thisObject, this.targetObject, this.args, this.matchContext).matches();
            }
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(Literal literal) {
            if (literal == Literal.FALSE) {
                this.matches = false;
            } else {
                this.matches = true;
            }
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(Call call) {
            throw new UnsupportedOperationException("Can't evaluate call test at runtime");
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(FieldGetCall fieldGetCall) {
            throw new UnsupportedOperationException("Can't evaluate fieldGetCall test at runtime");
        }

        @Override // org.aspectj.weaver.ast.ITestVisitor
        public void visit(HasAnnotation hasAnnotation) {
            ReflectionVar v = (ReflectionVar) hasAnnotation.getVar();
            Object value = v.getBindingAtJoinPoint(this.thisObject, this.targetObject, this.args);
            World world = v.getType().getWorld();
            ResolvedType actualVarType = world.resolve(value.getClass().getName());
            ResolvedType requiredAnnotationType = hasAnnotation.getAnnotationType().resolve(world);
            this.matches = actualVarType.hasAnnotation(requiredAnnotationType);
        }
    }
}

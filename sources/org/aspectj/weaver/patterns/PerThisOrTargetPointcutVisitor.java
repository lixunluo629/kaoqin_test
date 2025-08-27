package org.aspectj.weaver.patterns;

import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerThisOrTargetPointcutVisitor.class */
public class PerThisOrTargetPointcutVisitor extends AbstractPatternNodeVisitor {
    private static final TypePattern MAYBE = new TypePatternMayBe();
    private final boolean m_isTarget;
    private final ResolvedType m_fromAspectType;

    public PerThisOrTargetPointcutVisitor(boolean isTarget, ResolvedType fromAspectType) {
        this.m_isTarget = isTarget;
        this.m_fromAspectType = fromAspectType;
    }

    public TypePattern getPerTypePointcut(Pointcut perClausePointcut) {
        Object o = perClausePointcut.accept(this, perClausePointcut);
        if (o instanceof TypePattern) {
            return (TypePattern) o;
        }
        throw new BCException("perClausePointcut visitor did not return a typepattern, it returned " + o + (o == null ? "" : " of type " + o.getClass()));
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinPointcut node, Object data) {
        if (this.m_isTarget) {
            return MAYBE;
        }
        return node.getTypePattern();
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithincodePointcut node, Object data) {
        if (this.m_isTarget) {
            return MAYBE;
        }
        return node.getSignature().getDeclaringType();
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinAnnotationPointcut node, Object data) {
        if (this.m_isTarget) {
            return MAYBE;
        }
        return new AnyWithAnnotationTypePattern(node.getAnnotationTypePattern());
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinCodeAnnotationPointcut node, Object data) {
        if (this.m_isTarget) {
            return MAYBE;
        }
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(KindedPointcut node, Object data) {
        if (node.getKind().equals(Shadow.AdviceExecution)) {
            return MAYBE;
        }
        if (node.getKind().equals(Shadow.ConstructorExecution) || node.getKind().equals(Shadow.Initialization) || node.getKind().equals(Shadow.MethodExecution) || node.getKind().equals(Shadow.PreInitialization) || node.getKind().equals(Shadow.StaticInitialization)) {
            SignaturePattern signaturePattern = node.getSignature();
            boolean isStarAnnotation = signaturePattern.isStarAnnotation();
            if (!this.m_isTarget && node.getKind().equals(Shadow.MethodExecution) && !isStarAnnotation) {
                return new HasMemberTypePatternForPerThisMatching(signaturePattern);
            }
            return signaturePattern.getDeclaringType();
        }
        if (node.getKind().equals(Shadow.ConstructorCall) || node.getKind().equals(Shadow.FieldGet) || node.getKind().equals(Shadow.FieldSet) || node.getKind().equals(Shadow.MethodCall)) {
            if (this.m_isTarget) {
                return node.getSignature().getDeclaringType();
            }
            return MAYBE;
        }
        if (node.getKind().equals(Shadow.ExceptionHandler)) {
            return MAYBE;
        }
        throw new ParserException("Undetermined - should not happen: " + node.getKind().getSimpleName(), null);
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndPointcut node, Object data) {
        return new AndTypePattern(getPerTypePointcut(node.left), getPerTypePointcut(node.right));
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrPointcut node, Object data) {
        return new OrTypePattern(getPerTypePointcut(node.left), getPerTypePointcut(node.right));
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ThisOrTargetAnnotationPointcut node, Object data) {
        if (this.m_isTarget && !node.isThis()) {
            return new AnyWithAnnotationTypePattern(node.getAnnotationTypePattern());
        }
        if (!this.m_isTarget && node.isThis()) {
            return new AnyWithAnnotationTypePattern(node.getAnnotationTypePattern());
        }
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ThisOrTargetPointcut node, Object data) {
        if ((this.m_isTarget && !node.isThis()) || (!this.m_isTarget && node.isThis())) {
            String pointcutString = node.getType().toString();
            if (pointcutString.equals("<nothing>")) {
                return new NoTypePattern();
            }
            TypePattern copy = new PatternParser(pointcutString.replace('$', '.')).parseTypePattern();
            copy.includeSubtypes = true;
            return copy;
        }
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ReferencePointcut node, Object data) {
        ResolvedType searchStart = this.m_fromAspectType;
        if (node.onType != null) {
            searchStart = node.onType.resolve(this.m_fromAspectType.getWorld());
            if (searchStart.isMissing()) {
                return MAYBE;
            }
        }
        ResolvedPointcutDefinition pointcutDec = searchStart.findPointcut(node.name);
        return getPerTypePointcut(pointcutDec.getPointcut());
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(IfPointcut node, Object data) {
        return TypePattern.ANY;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(HandlerPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(CflowPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ConcreteCflowPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ArgsPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ArgsAnnotationPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnnotationPointcut node, Object data) {
        return MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(Pointcut.MatchesNothingPointcut node, Object data) {
        return new NoTypePattern() { // from class: org.aspectj.weaver.patterns.PerThisOrTargetPointcutVisitor.1
            @Override // org.aspectj.weaver.patterns.NoTypePattern
            public String toString() {
                return "false";
            }
        };
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerThisOrTargetPointcutVisitor$TypePatternMayBe.class */
    private static class TypePatternMayBe extends AnyTypePattern {
        private TypePatternMayBe() {
        }
    }
}

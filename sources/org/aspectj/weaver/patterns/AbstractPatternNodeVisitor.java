package org.aspectj.weaver.patterns;

import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AbstractPatternNodeVisitor.class */
public abstract class AbstractPatternNodeVisitor implements PatternNodeVisitor {
    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnyTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NoTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(EllipsisTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnyWithAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnyAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(EllipsisAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnnotationPatternList node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AnnotationPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ArgsAnnotationPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ArgsPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(BindingAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(BindingTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(CflowPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ConcreteCflowPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(DeclareAnnotation node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(DeclareErrorOrWarning node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(DeclareParents node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(DeclarePrecedence node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(DeclareSoft node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ExactAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ExactTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(HandlerPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(IfPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(KindedPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ModifiersPattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NamePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PerCflow node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PerFromSuper node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PerObject node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PerSingleton node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PerTypeWithin node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(PatternNode node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ReferencePointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(SignaturePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ThisOrTargetAnnotationPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ThisOrTargetPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ThrowsPattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(TypePatternList node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WildAnnotationTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WildTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinAnnotationPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinCodeAnnotationPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithinPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WithincodePointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(Pointcut.MatchesNothingPointcut node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(TypeVariablePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(TypeVariablePatternList node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(HasMemberTypePattern node, Object data) {
        return node;
    }

    @Override // org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(TypeCategoryTypePattern node, Object data) {
        return node;
    }
}

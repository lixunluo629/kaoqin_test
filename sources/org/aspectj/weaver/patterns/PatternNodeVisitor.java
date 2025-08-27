package org.aspectj.weaver.patterns;

import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PatternNodeVisitor.class */
public interface PatternNodeVisitor {
    Object visit(AndAnnotationTypePattern andAnnotationTypePattern, Object obj);

    Object visit(AnyAnnotationTypePattern anyAnnotationTypePattern, Object obj);

    Object visit(EllipsisAnnotationTypePattern ellipsisAnnotationTypePattern, Object obj);

    Object visit(ExactAnnotationTypePattern exactAnnotationTypePattern, Object obj);

    Object visit(BindingAnnotationTypePattern bindingAnnotationTypePattern, Object obj);

    Object visit(NotAnnotationTypePattern notAnnotationTypePattern, Object obj);

    Object visit(OrAnnotationTypePattern orAnnotationTypePattern, Object obj);

    Object visit(WildAnnotationTypePattern wildAnnotationTypePattern, Object obj);

    Object visit(AnnotationPatternList annotationPatternList, Object obj);

    Object visit(AndTypePattern andTypePattern, Object obj);

    Object visit(AnyTypePattern anyTypePattern, Object obj);

    Object visit(AnyWithAnnotationTypePattern anyWithAnnotationTypePattern, Object obj);

    Object visit(EllipsisTypePattern ellipsisTypePattern, Object obj);

    Object visit(ExactTypePattern exactTypePattern, Object obj);

    Object visit(BindingTypePattern bindingTypePattern, Object obj);

    Object visit(NotTypePattern notTypePattern, Object obj);

    Object visit(NoTypePattern noTypePattern, Object obj);

    Object visit(OrTypePattern orTypePattern, Object obj);

    Object visit(WildTypePattern wildTypePattern, Object obj);

    Object visit(TypePatternList typePatternList, Object obj);

    Object visit(HasMemberTypePattern hasMemberTypePattern, Object obj);

    Object visit(TypeCategoryTypePattern typeCategoryTypePattern, Object obj);

    Object visit(AndPointcut andPointcut, Object obj);

    Object visit(CflowPointcut cflowPointcut, Object obj);

    Object visit(ConcreteCflowPointcut concreteCflowPointcut, Object obj);

    Object visit(HandlerPointcut handlerPointcut, Object obj);

    Object visit(IfPointcut ifPointcut, Object obj);

    Object visit(KindedPointcut kindedPointcut, Object obj);

    Object visit(Pointcut.MatchesNothingPointcut matchesNothingPointcut, Object obj);

    Object visit(AnnotationPointcut annotationPointcut, Object obj);

    Object visit(ArgsAnnotationPointcut argsAnnotationPointcut, Object obj);

    Object visit(ArgsPointcut argsPointcut, Object obj);

    Object visit(ThisOrTargetAnnotationPointcut thisOrTargetAnnotationPointcut, Object obj);

    Object visit(ThisOrTargetPointcut thisOrTargetPointcut, Object obj);

    Object visit(WithinAnnotationPointcut withinAnnotationPointcut, Object obj);

    Object visit(WithinCodeAnnotationPointcut withinCodeAnnotationPointcut, Object obj);

    Object visit(NotPointcut notPointcut, Object obj);

    Object visit(OrPointcut orPointcut, Object obj);

    Object visit(ReferencePointcut referencePointcut, Object obj);

    Object visit(WithinPointcut withinPointcut, Object obj);

    Object visit(WithincodePointcut withincodePointcut, Object obj);

    Object visit(PerCflow perCflow, Object obj);

    Object visit(PerFromSuper perFromSuper, Object obj);

    Object visit(PerObject perObject, Object obj);

    Object visit(PerSingleton perSingleton, Object obj);

    Object visit(PerTypeWithin perTypeWithin, Object obj);

    Object visit(DeclareAnnotation declareAnnotation, Object obj);

    Object visit(DeclareErrorOrWarning declareErrorOrWarning, Object obj);

    Object visit(DeclareParents declareParents, Object obj);

    Object visit(DeclarePrecedence declarePrecedence, Object obj);

    Object visit(DeclareSoft declareSoft, Object obj);

    Object visit(ModifiersPattern modifiersPattern, Object obj);

    Object visit(NamePattern namePattern, Object obj);

    Object visit(SignaturePattern signaturePattern, Object obj);

    Object visit(ThrowsPattern throwsPattern, Object obj);

    Object visit(TypeVariablePattern typeVariablePattern, Object obj);

    Object visit(TypeVariablePatternList typeVariablePatternList, Object obj);

    Object visit(PatternNode patternNode, Object obj);
}

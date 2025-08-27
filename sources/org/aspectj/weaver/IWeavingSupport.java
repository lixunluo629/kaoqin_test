package org.aspectj.weaver;

import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/IWeavingSupport.class */
public interface IWeavingSupport {
    Advice createAdviceMunger(AjAttribute.AdviceAttribute adviceAttribute, Pointcut pointcut, Member member, ResolvedType resolvedType);

    ConcreteTypeMunger makeCflowStackFieldAdder(ResolvedMember resolvedMember);

    ConcreteTypeMunger makeCflowCounterFieldAdder(ResolvedMember resolvedMember);

    ConcreteTypeMunger makePerClauseAspect(ResolvedType resolvedType, PerClause.Kind kind);

    ConcreteTypeMunger concreteTypeMunger(ResolvedTypeMunger resolvedTypeMunger, ResolvedType resolvedType);

    ConcreteTypeMunger createAccessForInlineMunger(ResolvedType resolvedType);

    Var makeCflowAccessVar(ResolvedType resolvedType, Member member, int i);
}

package org.aspectj.weaver.tools;

import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/StandardPointcutExpression.class */
public interface StandardPointcutExpression {
    void setMatchingContext(MatchingContext matchingContext);

    boolean couldMatchJoinPointsInType(Class cls);

    boolean mayNeedDynamicTest();

    ShadowMatch matchesMethodExecution(ResolvedMember resolvedMember);

    ShadowMatch matchesStaticInitialization(ResolvedType resolvedType);

    ShadowMatch matchesMethodCall(ResolvedMember resolvedMember, ResolvedMember resolvedMember2);

    String getPointcutExpression();
}

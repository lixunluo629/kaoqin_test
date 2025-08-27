package org.aspectj.weaver.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/PointcutExpression.class */
public interface PointcutExpression {
    void setMatchingContext(MatchingContext matchingContext);

    boolean couldMatchJoinPointsInType(Class cls);

    boolean mayNeedDynamicTest();

    ShadowMatch matchesMethodExecution(Method method);

    ShadowMatch matchesConstructorExecution(Constructor constructor);

    ShadowMatch matchesStaticInitialization(Class cls);

    ShadowMatch matchesAdviceExecution(Method method);

    ShadowMatch matchesInitialization(Constructor constructor);

    ShadowMatch matchesPreInitialization(Constructor constructor);

    ShadowMatch matchesMethodCall(Method method, Member member);

    ShadowMatch matchesMethodCall(Method method, Class cls);

    ShadowMatch matchesConstructorCall(Constructor constructor, Member member);

    ShadowMatch matchesConstructorCall(Constructor constructor, Class cls);

    ShadowMatch matchesHandler(Class cls, Member member);

    ShadowMatch matchesHandler(Class cls, Class cls2);

    ShadowMatch matchesFieldSet(Field field, Member member);

    ShadowMatch matchesFieldSet(Field field, Class cls);

    ShadowMatch matchesFieldGet(Field field, Member member);

    ShadowMatch matchesFieldGet(Field field, Class cls);

    String getPointcutExpression();
}

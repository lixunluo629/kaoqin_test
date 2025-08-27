package org.aspectj.lang.reflect;

import java.lang.reflect.Type;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/Advice.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/Advice.class */
public interface Advice {
    AjType getDeclaringType();

    AdviceKind getKind();

    String getName();

    AjType<?>[] getParameterTypes();

    Type[] getGenericParameterTypes();

    AjType<?>[] getExceptionTypes();

    PointcutExpression getPointcutExpression();
}

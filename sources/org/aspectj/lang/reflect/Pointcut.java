package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/Pointcut.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/Pointcut.class */
public interface Pointcut {
    String getName();

    int getModifiers();

    AjType<?>[] getParameterTypes();

    String[] getParameterNames();

    AjType getDeclaringType();

    PointcutExpression getPointcutExpression();
}

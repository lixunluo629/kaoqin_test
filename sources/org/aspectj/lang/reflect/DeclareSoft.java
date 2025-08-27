package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclareSoft.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclareSoft.class */
public interface DeclareSoft {
    AjType getDeclaringType();

    AjType getSoftenedExceptionType() throws ClassNotFoundException;

    PointcutExpression getPointcutExpression();
}

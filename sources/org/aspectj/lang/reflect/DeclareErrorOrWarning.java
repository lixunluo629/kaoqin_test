package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclareErrorOrWarning.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclareErrorOrWarning.class */
public interface DeclareErrorOrWarning {
    AjType getDeclaringType();

    PointcutExpression getPointcutExpression();

    String getMessage();

    boolean isError();
}

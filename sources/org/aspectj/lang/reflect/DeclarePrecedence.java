package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclarePrecedence.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclarePrecedence.class */
public interface DeclarePrecedence {
    AjType getDeclaringType();

    TypePattern[] getPrecedenceOrder();
}

package org.aspectj.lang;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/Signature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/Signature.class */
public interface Signature {
    String toString();

    String toShortString();

    String toLongString();

    String getName();

    int getModifiers();

    Class getDeclaringType();

    String getDeclaringTypeName();
}

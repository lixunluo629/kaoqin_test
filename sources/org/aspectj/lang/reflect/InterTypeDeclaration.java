package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/InterTypeDeclaration.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/InterTypeDeclaration.class */
public interface InterTypeDeclaration {
    AjType<?> getDeclaringType();

    AjType<?> getTargetType() throws ClassNotFoundException;

    int getModifiers();
}

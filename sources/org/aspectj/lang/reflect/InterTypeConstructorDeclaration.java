package org.aspectj.lang.reflect;

import java.lang.reflect.Type;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/InterTypeConstructorDeclaration.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/InterTypeConstructorDeclaration.class */
public interface InterTypeConstructorDeclaration extends InterTypeDeclaration {
    AjType<?>[] getParameterTypes();

    Type[] getGenericParameterTypes();

    AjType<?>[] getExceptionTypes();
}

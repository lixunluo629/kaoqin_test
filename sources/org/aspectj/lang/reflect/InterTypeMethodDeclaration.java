package org.aspectj.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/InterTypeMethodDeclaration.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/InterTypeMethodDeclaration.class */
public interface InterTypeMethodDeclaration extends InterTypeDeclaration {
    String getName();

    AjType<?> getReturnType();

    Type getGenericReturnType();

    AjType<?>[] getParameterTypes();

    Type[] getGenericParameterTypes();

    TypeVariable<Method>[] getTypeParameters();

    AjType<?>[] getExceptionTypes();
}

package org.aspectj.lang.reflect;

import java.lang.reflect.Type;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/InterTypeFieldDeclaration.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/InterTypeFieldDeclaration.class */
public interface InterTypeFieldDeclaration extends InterTypeDeclaration {
    String getName();

    AjType<?> getType();

    Type getGenericType();
}

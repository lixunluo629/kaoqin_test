package org.aspectj.lang.reflect;

import java.lang.reflect.Type;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/DeclareParents.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/DeclareParents.class */
public interface DeclareParents {
    AjType getDeclaringType();

    TypePattern getTargetTypesPattern();

    boolean isExtends();

    boolean isImplements();

    Type[] getParentTypes() throws ClassNotFoundException;
}

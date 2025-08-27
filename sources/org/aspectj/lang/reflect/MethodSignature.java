package org.aspectj.lang.reflect;

import java.lang.reflect.Method;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/MethodSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/MethodSignature.class */
public interface MethodSignature extends CodeSignature {
    Class getReturnType();

    Method getMethod();
}

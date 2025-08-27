package org.aspectj.lang.reflect;

import java.lang.reflect.Method;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/AdviceSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/AdviceSignature.class */
public interface AdviceSignature extends CodeSignature {
    Class getReturnType();

    Method getAdvice();
}

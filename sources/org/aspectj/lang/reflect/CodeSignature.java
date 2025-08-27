package org.aspectj.lang.reflect;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/CodeSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/CodeSignature.class */
public interface CodeSignature extends MemberSignature {
    Class[] getParameterTypes();

    String[] getParameterNames();

    Class[] getExceptionTypes();
}

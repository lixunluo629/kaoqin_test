package org.aspectj.lang.reflect;

import org.aspectj.lang.Signature;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/CatchClauseSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/CatchClauseSignature.class */
public interface CatchClauseSignature extends Signature {
    Class getParameterType();

    String getParameterName();
}

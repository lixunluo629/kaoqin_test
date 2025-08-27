package org.aspectj.lang.reflect;

import java.lang.reflect.Constructor;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/InitializerSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/InitializerSignature.class */
public interface InitializerSignature extends CodeSignature {
    Constructor getInitializer();
}

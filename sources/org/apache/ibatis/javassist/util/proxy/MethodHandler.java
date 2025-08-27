package org.apache.ibatis.javassist.util.proxy;

import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/MethodHandler.class */
public interface MethodHandler {
    Object invoke(Object obj, Method method, Method method2, Object[] objArr) throws Throwable;
}

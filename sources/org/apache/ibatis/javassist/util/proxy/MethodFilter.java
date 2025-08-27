package org.apache.ibatis.javassist.util.proxy;

import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/MethodFilter.class */
public interface MethodFilter {
    boolean isHandled(Method method);
}
